package io.github.furstenheim;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.lang.UUID;
import cn.hutool.http.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main class of the package
 */
@Slf4j
public class CopyDown {
    public CopyDown () {
        this.options = OptionsBuilder.anOptions().build();
        setUp();
    }

    public CopyDown (Options options) {
        this.options = options;
        setUp();
    }

    /**
     * Accepts a HTML string and converts it to markdown
     *
     * Note, if LinkStyle is chosen to be REFERENCED the method is not thread safe.
     * @param input html to be converted
     * @return markdown text
     */
    public String convert(String input) {
        references = new ArrayList<>();
        CopyNode copyRootNode = new CopyNode(input);
        String result = process(copyRootNode);
        return postProcess(result);
    }

    public void convert(HtmlSourceOption htmlSourceOption) throws IOException {
        Document doc = Jsoup.connect(htmlSourceOption.getUrl()).get();
        HtmlSourceResult result = findImgAndTitleAndNickname(doc, htmlSourceOption);

        String pinyin = Pinyin4jUtil.getFirstSpellPinYin(result.getMdTitle(), true);
        log.info("pinyin{}", pinyin);

        Elements content = doc.select(htmlSourceOption.getContentSelector());
        String input = content.html();

        // 指定 md 名字
        // 下载题图
        String markdown = convert(input);
        log.info("markdown\n{}", markdown);

        // 下载封面图
        long size = HttpUtil.downloadFile(result.getCoverImageUrl(),
                FileUtil.file(htmlSourceOption.getImgdest() + pinyin + ".jpg"));
        log.info("cover image size{}", size);

        FileWriter writer = new FileWriter(htmlSourceOption.getMddest()+ pinyin + ".md");

        // 作者名写到文件中
        String author = findAuthor(doc, htmlSourceOption);
        if (StringUtils.isBlank(author)) {
            author = result.getNickName();
        }

        // 标题写入到文件中
        writer.append("---\n");
        writer.append("author: " + author + "\n");
        writer.append("title: " + result.getMdTitle() + "\n");
        writer.append("category:\n");
        writer.append("  - 优质文章\n");
        writer.append("---\n\n");
        writer.append(markdown);
        log.info("all done");
    }

    public String findAuthor(Document doc, HtmlSourceOption option) {
        for (Element metaTag : doc.getElementsByTag("meta")) {
            String content = metaTag.attr("content");
            String name = metaTag.attr("name");
            if (option.getAuthorKey().equals(name)) {
                return content;
            }
        }
        return null;
    }

    public HtmlSourceResult findImgAndTitleAndNickname(Document doc, HtmlSourceOption option) {
        // get <script>
        for (Element scripts : doc.getElementsByTag("script")) {
            // get data from <script>
            for (DataNode dataNode : scripts.dataNodes()) {
                // find data which contains
                if (dataNode.getWholeData().contains(option.getCoverImageKey())) {
                    log.info("contains");
                    HtmlSourceResult result = HtmlSourceResult.builder().build();

                    // 昵称
                    Pattern nikeNamePattern = Pattern.compile("var\\s+"+option.getNicknameKey()+"\\s+=\\s+\"(.*)\";");
                    Matcher nikeNameMatcher = nikeNamePattern.matcher(dataNode.getWholeData());
                    if (nikeNameMatcher.find()) {
                        String nickName = nikeNameMatcher.group(1);
                        log.info("find nickName success{}", nickName);
                        result.setNickName(nickName);
                    }

                    // 文件名
                    Pattern titlePattern = Pattern.compile("var\\s+"+option.getTitleKey()+"\\s+=\\s+'(.*)'\\.html\\(false\\);");
                    Matcher titleMatcher = titlePattern.matcher(dataNode.getWholeData());
                    if (titleMatcher.find()) {
                        String title = titleMatcher.group(1);
                        log.info("find title success{}", title);
                        result.setMdTitle(title);
                    }

                    // 封面图
                    Pattern pattern = Pattern.compile("var\\s+"+option.getCoverImageKey()+"\\s+=\\s+\"(.*)\";");
                    Matcher matcher = pattern.matcher(dataNode.getWholeData());
                    if (matcher.find()) {
                        String msg_cdn_url = matcher.group(1);
                        log.info("find msg_cdn_url success{}", msg_cdn_url);
                        result.setCoverImageUrl(msg_cdn_url);
                    }
                    return result;
                }
            }
        }
        return null;
    }

    private Rules rules;
    private final Options options;
    private List<String> references = null;

    private void setUp () {
        rules = new Rules();
    }
    private static class Escape {
        String pattern;
        String replace;

        public Escape(String pattern, String replace) {
            this.pattern = pattern;
            this.replace = replace;
        }
    }
    private final List<Escape> escapes = Arrays.asList(
            new Escape("\\\\", "\\\\\\\\"),
            new Escape("\\*", "\\\\*"),
            new Escape("^-", "\\\\-"),
            new Escape("^\\+ ", "\\\\+ "),
            new Escape("^(=+)", "\\\\$1"),
            new Escape("^(#{1,6}) ", "\\\\$1 "),
            new Escape("`", "\\\\`"),
            new Escape("^~~~", "\\\\~~~"),
            new Escape("\\[", "\\\\["),
            new Escape("\\]", "\\\\]"),
            new Escape("^>", "\\\\>"),
            new Escape("_", "\\\\_"),
            new Escape("^(\\d+)\\. ", "$1\\\\. ")
    );

    private String postProcess (String output) {
        for (Rule rule: rules.rules) {
            if (rule.getAppend() != null) {
                output = join(output, rule.getAppend().get());
            }
        }
        return output.replaceAll("^[\\t\\n\\r]+", "").replaceAll("[\\t\\r\\n\\s]+$", "");
    }
    private String process (CopyNode node) {
        String result = "";
        for (Node child : node.element.childNodes()) {
            CopyNode copyNodeChild = new CopyNode(child, node);
            String replacement = "";
            if (NodeUtils.isNodeType3(child)) {
                // TODO it should be child.nodeValue
                TextNode textNode = (TextNode)child;
                String nodeCode = textNode.getWholeText();
                // 不要改变代码的格式
                replacement = copyNodeChild.isCode() ? nodeCode : escape(nodeCode);
            } else if (NodeUtils.isNodeType1(child)) {
                replacement = replacementForNode(copyNodeChild);
            }
            result = join(result, replacement);
        }
        return result;
    }
    private String replacementForNode (CopyNode node) {
        Rule rule = rules.findRule(node.element);
        String content = process(node);
        CopyNode.FlankingWhiteSpaces flankingWhiteSpaces = node.flankingWhitespace();
        if (flankingWhiteSpaces.getLeading().length() > 0 || flankingWhiteSpaces.getTrailing().length() > 0) {
            content = content.trim();
        }
        return flankingWhiteSpaces.getLeading() + rule.getReplacement().apply(content, node.element)
         + flankingWhiteSpaces.getTrailing();
    }
    private static final Pattern leadingNewLinePattern = Pattern.compile("^(\n*)");
    private static final Pattern trailingNewLinePattern = Pattern.compile("(\n*)$");
    private String join (String string1, String string2) {
        Matcher trailingMatcher = trailingNewLinePattern.matcher(string1);
        trailingMatcher.find();
        Matcher leadingMatcher = leadingNewLinePattern.matcher(string2);
        leadingMatcher.find();
        int nNewLines = Integer.min(2, Integer.max(leadingMatcher.group().length(), trailingMatcher.group().length()));
        String newLineJoin = String.join("", Collections.nCopies(nNewLines, "\n"));
        return trailingMatcher.replaceAll("")
                + newLineJoin
                + leadingMatcher.replaceAll("");
    }

    private String escape (String string) {
        for (Escape escape : escapes) {
            string = string.replaceAll(escape.pattern, escape.replace);
        }
        return string;
    }

    class Rules {
        private List<Rule> rules;

        public Rules () {
            this.rules = new ArrayList<>();

            addRule("blankReplacement", new Rule((element) -> CopyNode.isBlank(element), (content, element) ->
                    CopyNode.isBlock(element) ? "\n\n" : ""));
            addRule("paragraph", new Rule("p", (content, element) -> {return "\n\n" + content + "\n\n";}));
            addRule("br", new Rule("br", (content, element) -> {return options.br + "\n";}));
            addRule("heading", new Rule(new String[]{"h1", "h2", "h3", "h4", "h5", "h6" }, (content, element) -> {
                Integer hLevel = Integer.parseInt(element.nodeName().substring(1, 2));
                if (options.headingStyle == HeadingStyle.SETEXT && hLevel < 3) {
                    String underline = String.join("", Collections.nCopies(content.length(), hLevel == 1 ? "=" : "-"));
                    return "\n\n" + content + "\n" + underline + "\n\n";
                } else {
                    return "\n\n" + String.join("", Collections.nCopies(hLevel, "#")) + " " + content + "\n\n";
                }
            }));
            addRule("blockquote", new Rule("blockquote", (content, element) -> {
                content = content.replaceAll("^\n+|\n+$", "");
                content = content.replaceAll("(?m)^", "> ");
                return "\n\n" + content + "\n\n";
            }));
            addRule("list", new Rule(new String[] { "ul", "ol" }, (content, element) -> {
                Element parent = (Element) element.parentNode();
                if (parent.nodeName().equals("li") && parent.child(parent.childrenSize() - 1) == element) {
                    return "\n" + content;
                } else {
                    return "\n\n" + content + "\n\n";
                }
            }));
            addRule("listItem", new Rule("li", (content, element) -> {
                content = content.replaceAll("^\n+", "") // remove leading new lines
                        .replaceAll("\n+$", "\n") // remove trailing new lines with just a single one
                        .replaceAll("(?m)\n", "\n"); // indent
                String prefix = options.bulletListMaker + "   ";
                Element parent = (Element)element.parentNode();
                if (parent.nodeName().equals("ol")) {
                    String start = parent.attr("start");
                    int index = parent.children().indexOf(element);
                    int parsedStart = 1;
                    if (start.length() != 0) {
                        try {
                            parsedStart = Integer.valueOf(start);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                    prefix = String.valueOf(parsedStart + index) + ".  ";
                }
                return prefix + content + (element.nextSibling() != null && !Pattern.compile("\n$").matcher(content).find() ? "\n": "");
            }));
            addRule("indentedCodeBlock", new Rule((element) -> {

                return options.codeBlockStyle == CodeBlockStyle.INDENTED
                       && element.parentNode().nodeName().equals("pre")
                       && element.childNodeSize() > 0
                       && element.childNode(0).nodeName().equals("code");
            }, (content, element) -> {
                // TODO check textContent
                return "\n\n    " + ((Element)element.childNode(0)).wholeText().replaceAll("\n", "\n    ");
            }));
            // 行内代码
            addRule("code", new Rule((element) -> {
                boolean hasSiblings = element.previousSibling() != null || element.nextSibling() != null;
                boolean isNotCodeBlock = !element.parentNode().nodeName().equals("pre") && hasSiblings;
                return element.nodeName().equals("code") && isNotCodeBlock;
            }, (content, element) -> {
                if (content.trim().length() == 0) {
                    return "";
                }
                String delimiter = "`";
                String leadingSpace = "";
                String trailingSpace = "";
                Pattern pattern = Pattern.compile("(?m)(`)+");
                Matcher matcher = pattern.matcher(content);
                if (matcher.find()) {
                    if (Pattern.compile("^`").matcher(content).find()) {
                        leadingSpace = " ";
                    }
                    if (Pattern.compile("`$").matcher(content).find()) {
                        trailingSpace = " ";
                    }
                    int counter = 1;
                    if (delimiter.equals(matcher.group())) {
                        counter++;
                    }
                    while (matcher.find()) {
                        if (delimiter.equals(matcher.group())) {
                            counter++;
                        }
                    }
                    delimiter = String.join("", Collections.nCopies(counter, "`"));
                }
                return delimiter + leadingSpace + content + trailingSpace + delimiter;
            }));
            // 代码块
            addRule("fencedCodeBock", new Rule((element) -> {
                boolean isCodeBlock = element.parentNode().nodeName().equals("pre");
                return element.nodeName().equals("code") && isCodeBlock
                        && options.codeBlockStyle == CodeBlockStyle.FENCED;
            }, (content, element) -> {
                String leadingSpace = "\n";
                String trailingSpace = "";
                String childClass = element.childNode(0).attr("class");
                if (childClass == null) {
                    childClass = "";
                }
                Matcher languageMatcher = Pattern.compile("language-(\\S+)").matcher(childClass);
                String language = "";
                if (languageMatcher.find()) {
                    language = languageMatcher.group(1);
                }

                String code;
                if (element.childNode(0) instanceof Element) {
                    code = ((Element)element.childNode(0)).wholeText();
                } else {
                    code = element.childNode(0).outerHtml();
                }

                String fenceChar = options.fence.substring(0, 1);
                int fenceSize = 3;
                Matcher fenceMatcher = Pattern.compile("(?m)^(" + fenceChar + "{3,})").matcher(content);
                while (fenceMatcher.find()) {
                    String group = fenceMatcher.group(1);
                    fenceSize = Math.max(group.length() + 1, fenceSize);
                }
                String fence = String.join("", Collections.nCopies(fenceSize, fenceChar));
                if (code.length() > 0 && code.charAt(code.length() - 1) == '\n') {
                    code = code.substring(0, code.length() - 1);
                }
                return (
                        "\n\n" + fence + language + "\n" + content
                          + fence + "\n\n"
                        );
            }));

            addRule("horizontalRule", new Rule("hr", (content, element) -> {
                return "\n\n" + options.hr + "\n\n";
            }));
            addRule("inlineLink", new Rule((element) -> {
                return options.linkStyle == LinkStyle.INLINED
                       && element.nodeName().equals("a")
                       && element.attr("href").length() != 0;
            }, (content, element) -> {
                String href = element.attr("href");
                String title = cleanAttribute(element.attr("title"));
                if (title.length() != 0) {
                    title = " \"" + title + "\"";
                }
                return "["+ content + "](" + href + title + ")";
            }));
            addRule("referenceLink", new Rule((element) -> {
                return options.linkStyle == LinkStyle.REFERENCED
                       && element.nodeName().equals("a")
                       && element.attr("href").length() != 0;
            }, (content, element) -> {
                String href = element.attr("href");
                String title = cleanAttribute(element.attr("title"));
                if (title.length() != 0) {
                    title = " \"" + title + "\"";
                }
                String replacement;
                String reference;
                switch (options.linkReferenceStyle) {
                    case COLLAPSED:
                        replacement = "[" + content + "][]";
                        reference = "[" + content + "]: " + href + title;
                        break;
                    case SHORTCUT:
                        replacement = "[" + content + "]";
                        reference = "[" + content + "]: " + href + title;
                        break;
                    case DEFAULT:
                    default:
                        int id = references.size() + 1;
                        replacement = "[" + content + "][" + id + "]";
                        reference = "[" + id + "]: " + href + title;
                }
                references.add(reference);
                return replacement;
            }, () -> {
                String referenceString = "";
                if (references.size() > 0) {
                    referenceString = "\n\n" + String.join("\n", references) + "\n\n";
                }
                return referenceString;
            }));
            addRule("emphasis", new Rule(new String[]{"em", "i"}, (content, element) -> {
                if (content.trim().length() == 0) {
                    return "";
                }
                return options.emDelimiter + content + options.emDelimiter;
            }));
            addRule("strong", new Rule(new String[]{"strong", "b"}, (content, element) -> {
                if (content.trim().length() == 0) {
                    return "";
                }
                return options.strongDelimiter + content + options.strongDelimiter;
            }));

            addRule("img", new Rule("img", (content, element) -> {
                String alt = cleanAttribute(element.attr("alt"));
                String src = element.hasAttr("src") ? element.attr("src"): element.attr("data-src");

                if (src.length() == 0) {
                    return "";
                }
                String title = cleanAttribute(element.attr("title"));
                String titlePart = "";
                if (title.length() != 0) {
                    titlePart = " \"" + title + "\"";
                }
                return "![" + alt + "]" + "(" + src + titlePart + ")";
            }));
            addRule("default", new Rule((element -> true), (content, element) -> CopyNode.isBlock(element) ? "\n\n" + content + "\n\n" : content));
        }

        public Rule findRule (Node node) {
            for (Rule rule : rules) {
                if (rule.getFilter().test(node)) {
                    return rule;
                }
            }
            return null;
        }

        private void addRule (String name, Rule rule) {
            rule.setName(name);
            rules.add(rule);
        }
        private String cleanAttribute (String attribute) {
            return attribute.replaceAll("(\n+\\s*)+", "\n");
        }
    }
}

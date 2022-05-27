package top.copydown;

import io.github.furstenheim.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 微信搜索「沉默王二」，回复 Java
 *
 * @author 沉默王二
 * @date 5/27/22
 */
@Slf4j
public class Demo {
    public static final String urls [] = {
            "https://mp.weixin.qq.com/s?__biz=MzAwMTk4NjM1MA==&mid=2247505620&idx=1&sn=b084ced6625abec1a3577e7415d0addc&chksm=9ad3cc95ada44583e2aa6bec4430d320dfaf0e964186a95fde59dc95e2b212ba16a8e8c3e7b8#rd",
            "https://www.mdnice.com/writing/6a250d838af14f72b609387725358d36"
    };
    public static final String mddest = System.getProperty("user.home")+"/Documents/GitHub/toBeBetterJavaer/docs/nicearticle/";
    public static final String imagedest = System.getProperty("user.home")+"/Documents/GitHub/toBeBetterJavaer/images/nicearticle/";

    public static void main(String[] args) throws IOException {
        OptionsBuilder optionsBuilder = OptionsBuilder.anOptions();
        Options options = optionsBuilder
                .withEmDelimiter("*")
                .withCodeBlockStyle(CodeBlockStyle.FENCED)
                .build();

        CopyDown copyDown = new CopyDown(options);
        copyDown.convert(HtmlSourceOption.builder()
                .url(urls[0])
                .contentSelector(HtmlSourceSelector.WEIXIN.getSelector())
                .coverImageKey("msg_cdn_url")
                .titleKey("msg_title")
                .imgdest(imagedest)
                .mddest(mddest)
                .nicknameKey("nickname")
                .authorKey("author").build());
    }
}

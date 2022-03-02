$(() => {

  let totalHtml = ''
  for (let i = 0; i < 27; i++) {
    const isHide = i >= 7 ? true: false
    const tempHtml = getFakeArticleHtml(isHide)
    totalHtml += tempHtml
  }
  $('.article-list').append(totalHtml)
})


function getFakeArticleHtml(isHide) {
  let finallHtml =
    '<div class="article-item'+ (isHide ? ' noshow': '') +'">'
    +'  <div class="article-basic-info">'
    +'    <a class="tab-link gray" href="javascript:void(0)">yonghu1</a>'
    +'    <span class="wide-border small-margin"></span>'
    +'    <span class="tab-link light-gray">10天前</span>'
    +'    <span class="wide-border small-margin"></span>'
    +'    <a class="tab-link light-gray" href="javascript:void(0)">文章标签1</a>'
    +'  </div>'
    +'  <div class="flex-row">'
    +'    <div class="flex-item-grow1">'
    +'      <!-- 文章标题 -->'
    +'      <a target="_blank" href="http://www.baidu.com" class="article-title">'
    +'        Vite+Vue3.2+TS 相关API归纳总结'
    +'      </a>'
    +'      <!-- 文章摘要 -->'
    +'      <p class="article-summary tab-link light-gray keep-1row">'
    +'        我们基于 IntelliJ 技术栈，通过设计新语言、编写 IDE 插件、Gradle / Dokka 插件，形成一套完整的文档辅助解。我们基于 IntelliJ 技术栈，通过设计新语言、编写 IDE 插件、Gradle / Dokka 插件，形成一套完整的文档辅助解'
    +'      </p>'
    +'      <!-- 文章阅读量、点赞、评论数 -->'
    +'      <ul class="article-statistics-info">'
    +'        <li>'
    +'          <img src="images/read-count.png" />'
    +'          <span>1.6w</span>'
    +'        </li>'
    +'        <li>'
    +'          <img src="images/zan.png" />'
    +'          <span>2300</span>'
    +'        </li>'
    +'        <li>'
    +'          <img src="images/comment.png" />'
    +'          <span>56</span>'
    +'        </li>'
    +'      </ul>'
    +'    </div>'
    +'    <img src="images/demo-pic3.png" class="article-img" />'
    +'  </div>'
    + '</div>'
  return finallHtml;
}
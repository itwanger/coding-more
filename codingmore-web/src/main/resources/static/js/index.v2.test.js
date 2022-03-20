$(() => {

  let totalHtml = ''
  for (let i = 0; i < 27; i++) {
    const isHide = i >= 7 ? true: false
    const tempHtml = getFakeArticleHtml(isHide)
    totalHtml += tempHtml
  }
  // $('.article-list').append(totalHtml)
})


function getFakeArticleHtml(isHide) {

  let finallHtml = $('.article-list > .article-item').eq(0)[0].outerHTML
  return finallHtml;
}
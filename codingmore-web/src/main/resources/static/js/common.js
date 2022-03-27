// 页面公共ready事件
$(function() {
  NProgress.start()
  $(window).load(function () {
    NProgress.done()
  })
})

function OpenNewPage(url) {
  window.open(url)
}

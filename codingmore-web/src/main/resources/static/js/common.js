// 页面公共ready事件
$(function() {
  // NProgress.start()
  window.addEventListener('load',() => {
    // setTimeout(()=>{
    //   NProgress.done()
    // })
  })
  bindCommonEvents()
})

// 页面通用事件
const bindCommonEvents = () => {
  // 绑定搜索按钮点击事件
  $('#searchBtn').on('click', () => {
    setSearchLayerVisible(true)
  })

  // 绑定菜单点击事件
  $('.menu-icon').one('click', () => { setSearchLayerVisible(true) })
}

const setSearchLayerVisible = val => {
  console.log('setSearchLayerVisible=', val)
  if (val) {
    // 展开搜索层
    let bottomStart = document.body.clientHeight - 50
    $('.search-layer').css('bottom', bottomStart + 'px')
    $('.search-layer').show().animate({ 'bottom': '0'}, 300)
    $('.menu-icon').unbind().one('click', () => { setSearchLayerVisible(false) })
  } else {
    // 关闭搜索层
    $('.search-layer').hide()
    $('.menu-icon').unbind().one('click', () => { setSearchLayerVisible(true) })
  }
  // 展开、关闭通用代码
  $('.menu-icon').toggleClass('opened')
  $('.menu-icon > img, .main-area, #searchBtn').toggleClass('noshow') //.removeClass('noshow').siblings().addClass('none')
}

// 打开新页面方法
function openNewPage(url) {
  window.open(url)
}

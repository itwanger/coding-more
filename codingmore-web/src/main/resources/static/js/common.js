// 页面公共ready事件
$(function() {
  // NProgress.start()
  window.addEventListener('load',() => {
    // setTimeout(()=>{
    //   NProgress.done()
    // })
  })
  bindCommonEvents()
  bindSiteInfo()
  // NProgress.done()
})

const bindSiteInfo = () => {
  let siteAttr = $('#site_attr_info').val()
  let siteAttrObject = JSON.parse(siteAttr);
  for (let key in siteAttrObject) {
    $("#" + key).text(siteAttrObject[key]);
  }
}

// 页面通用事件
const bindCommonEvents = () => {
  // 绑定搜索按钮点击事件
  $('#searchBtn').on('click', () => {
    setSearchLayerVisible(true)
  })

  // 绑定菜单点击事件
  $('.menu-icon').one('click', () => { setSearchLayerVisible(true) })
}

// 搜索层展开与隐藏的效果
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

// 简单封装ajax get请求
const ajax_get = (url, data, fnSuccess, fnFailed, otherOptions) => {
  if(!otherOptions) {
    otherOptions = {}
  }
  otherOptions['type'] = 'GET'
  ajax_fn(url, data, fnSuccess, fnFailed, otherOptions)
}

// 简单封装ajax post请求
const ajax_post = (url, data, fnSuccess, fnFailed, otherOptions) => {
  if(!otherOptions) {
    otherOptions = {}
  }
  otherOptions['type'] = 'POST'
  ajax_fn(url, data, fnSuccess, fnFailed, otherOptions)
}
// 实际执行的ajax方法
const ajax_fn = (url, data, fnSuccess, fnFailed, otherOptions) => {
  let ajaxOptions = {
    url,
    data,
    cache: false,
    success: res => {
      if(typeof res === 'string') {
        res = JSON.parse(res)
      }
      if($.isFunction(fnSuccess)) {
        fnSuccess(res)
      }
    },
    error: (e1, e2, e3) => {
      console.log('ajax_post::handled exception')
      console.log('e1=', e1)
      console.log('e2=', e2)
      console.log('e3=', e3)
      if($.isFunction(fnFailed)) {
        fnFailed({e1, e2, e3})
      }
    }
  }

  ajaxOptions = $.extend(ajaxOptions, otherOptions)

  $.ajax(ajaxOptions)
}

// 打开新页面方法
const openNewPage = (url) => {
  window.open(url)
}

// 返回顶部方法
const backToTop = () => {
  $('body').scrollTop(0);
}


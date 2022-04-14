$(() => {
  bindEvents()
})
// 绑定页面事件
const bindEvents = () => {
  // 监听滚动条位置是否到最下面了
  $('body').scroll(function () {
    let scrollTop = $(this).scrollTop();
    let scrollHeight = $(document).height();
    let windowHeight = $(this).height();
    // 滚动条到最底部判断
    if (scrollTop + windowHeight == scrollHeight) {
      loadingMoreArticles(null, 0)
    }
  });
}

let pageIndex = 1
let pageSize = 10

const loadingMoreArticles = (postTitleKeyword, tabIndex) => {
  pageIndex++
  let orderBy = 'menu_order'
  if(tabIndex === 1) {
    orderBy = 'post_date'
  } else if(tabIndex === 2) {
    orderBy = 'page_view'
  }
  let reqData = {
    asc: false,
    orderBy,
    page:pageIndex,
    pageSize
  }
  if(postTitleKeyword) {
    reqData['postTitleKeyword'] = postTitleKeyword
  }
  ajax_get('/rest/postPageQuery', reqData, function(retData) {
    console.log('异步查询文章列表返回结果', retData)
  }, null, loadingArticlesEffect)
}

const loadingArticlesEffect = {
  beforeSend: (xhr) => {
    bottomLoadingFn(true)
  },
  complete: (xhr,status) => {
    bottomLoadingFn(false)
  }
}

const bottomLoadingFn = (showLoading) => {
  if(showLoading) {
    $('.loading-over').addClass('noshow')
    $('.loading-more').removeClass('noshow')
  } else {
    $('.loading-over').removeClass('noshow')
    $('.loading-more').addClass('noshow')
  }
}


$(() => {
    bindEvents()
})
// 绑定页面事件
const bindEvents = () => {
    bindBodyEndScroll(true)
}

let pageIndex = 1
let pageSize = 10

const bindBodyEndScroll = (isBind) => {
    if (isBind) {
        // 监听滚动条位置是否到最下面了
        $('body').scroll(function () {
            let scrollTop = $(this).scrollTop();
            let scrollHeight = $(document).height();
            let windowHeight = $(this).height();
            // 滚动条到最底部判断
            if (scrollTop + windowHeight === scrollHeight) {
                loadingMoreArticles(null, 0)
            }
        });
    } else {
        $('body').unbind('scroll')
    }
}

const loadingMoreArticles = (postTitleKeyword, tabIndex) => {
    pageIndex++
    let orderBy = 'menu_order'
    if (tabIndex === 1) {
        orderBy = 'post_date'
    } else if (tabIndex === 2) {
        orderBy = 'page_view'
    }
    let reqData = {
        asc: false,
        orderBy,
        page: pageIndex,
        pageSize
    }
    if (postTitleKeyword) {
        reqData['postTitleKeyword'] = postTitleKeyword
    }
    ajax_get('/rest/postPageQuery', reqData, function (retData) {
        console.log('异步查询文章列表返回结果', retData)
        if (retData.code === 0) {
            if (retData.result.items.length > 0) {
                const dataArr = retData.result.items
                let totalHtml = ''
                dataArr.forEach(pageVo => {
                    let articleCoverSrc = pageVo.attribute ? (pageVo.attribute.articleCoverUrl ? pageVo.attribute.articleCoverUrl : 'images/demo-pic1.png') : 'images/demo-pic1.png'
                    let articleItemHtml =
                        '<div class="article-item" onclick="javascript:openNewPage(\'' + pageVo.postsId + '.html\')">'
                        + '<div class="article-basic-info">'
                        + '<a class="tab-link light-gray" href="javascript:void(0)">' + pageVo.userNiceName + '</a>'
                        + '<span class="wide-border small-margin"></span>'
                        + '<span class="tab-link light-gray">' + pageVo.postModified + '</span>'
                        + (pageVo.tagsName ? '<span class="wide-border small-margin"></span>' : '')
                        + (pageVo.tagsName ? '<a class="tab-link light-gray" href="javascript:void(0)">' + pageVo.tagsName + '</a>' : '')
                        + '</div>'
                        + '<a target="_blank" href="javascript:void(0)" class="article-title-cellphone">' + pageVo.postTitle + '</a>'
                        + '<div class="flex-row">'
                        + '<div class="flex-auto-item">'
                        + '<a target="_blank" href="javascript:void(0)" class="article-title">' + pageVo.postTitle + '</a>'
                        + '<p class="article-summary tab-link light-gray keep-rows">' + pageVo.postExcerpt + '</p>'
                        + '<div class="clear"></div>'
                        + '<ul class="article-statistics-info">'
                        + '<li>'
                        + '<img src="images/read-count.png"/>'
                        + '<span>' + pageVo.pageView + '</span>'
                        + '</li>'
                        + '<li>'
                        + '<img src="images/zan.png"/>'
                        + '<span>' + pageVo.likeCount + '</span>'
                        + '</li>'
                        + '</ul>'
                        + '</div>'
                        + '<img class="article-img" src="' + articleCoverSrc + '"/>'
                        + '</div>'
                        + '</div>'
                    totalHtml += articleItemHtml
                })
                $('#article_list').append(totalHtml)
                bindBodyEndScroll(true)
            } else {
                $('#loading_over').text('没有更多内容了')
                bindBodyEndScroll(false)
            }
        }
    }, null, loadingArticlesEffect)
}

const loadingArticlesEffect = {
    beforeSend: (xhr) => {
        bottomLoadingFn(true)
    },
    complete: (xhr, status) => {
        bottomLoadingFn(false)
    }
}

const bottomLoadingFn = (showLoading) => {
    if (showLoading) {
        bindBodyEndScroll(false)
        $('.loading-over').addClass('noshow')
        $('.loading-more').removeClass('noshow')
    } else {
        setTimeout(() => {
            $('.loading-over').removeClass('noshow')
            $('.loading-more').addClass('noshow')
        }, 2000)
    }
}


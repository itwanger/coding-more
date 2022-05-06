// 页面变量定义
let pageIndex = 1
let pageSize = 10
let currentTabIndex = 0
let searchText = ''

// document ready 事件
$(() => {
    // 显示骨架屏
    reloadArticleListEffect()
    bindEvents()
})

// 绑定页面事件
const bindEvents = () => {
    bindBodyEndScroll(true)
}

// 重新加载列表效果方法
const reloadArticleListEffect = () => {
    showSkeleton(true)
    setTimeout(() => {
        showSkeleton(false)
    }, 2000)
}

// 切换tab列表页方法
const tabChanged = (tabIndex) => {
    if(currentTabIndex === tabIndex) {
        return
    }
    // 切换效果
    $('.article-list-head > a.active').removeClass('active')
    $('.article-list-head > a').eq(tabIndex).addClass('active')

    // 设置数据
    currentTabIndex = tabIndex
    pageIndex = 0
    // 加载效果
    reloadArticleListEffect()
    // 清空列表
    $('#article_list').html('')
    // 重新请求数据
    loadingMoreArticles(searchText, currentTabIndex, {})
}

// 重新加载列表的骨架屏效果方法
const showSkeleton = (isToShow) => {
    let skeletonContainer = $('#skeleton_container')
    let articleList = $('#article_list')
    if(isToShow) {
        if(articleList.is(':visible')) {
            articleList.addClass('noshow')
        }
        if(!skeletonContainer.is(':visible')) {
            skeletonContainer.removeClass('noshow')
        }
    } else {
        if(!articleList.is(':visible')) {
            articleList.removeClass('noshow')
        }
        if(skeletonContainer.is(':visible')) {
            skeletonContainer.addClass('noshow')
        }
    }
}

// 绑定滚动条事件
const bindBodyEndScroll = (isBind) => {
    if (isBind) {
        // 监听滚动条位置是否到最下面了
        $('body').scroll(function () {
            let scrollTop = $(this).scrollTop();
            let scrollHeight = $(document).height();
            let windowHeight = $(this).height();
            // 滚动条到最底部判断
            if (scrollTop + windowHeight === scrollHeight) {
                loadingMoreArticles(searchText, currentTabIndex)
            }
        });
    } else {
        $('body').unbind('scroll')
    }
}

// 加载更多文章的方法（搜索关键词，当前tab页签索引）
const loadingMoreArticles = (postTitleKeyword, tabIndex, otherAjaxOptions) => {
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
    if (otherAjaxOptions === undefined) {
        otherAjaxOptions = loadingArticlesEffect
    }
    ajax_get('/rest/postPageQuery', reqData, function (retData) {
        console.log('异步查询文章列表返回结果', retData)
        if (retData.code === 0) {
            if (retData.result.items.length > 0) {
                const dataArr = retData.result.items
                let totalHtml = ''
                dataArr.forEach(pageVo => {
                    let articleCoverSrc = pageVo.attribute ? (pageVo.attribute.articleCoverUrl ? pageVo.attribute.articleCoverUrl : 'images/demo-pic1.png') : 'images/demo-pic1.png'
                    let tagsHtml = ''
                    if(pageVo.tags && pageVo.tags.length > 0) {
                        for (let i = 0; i < pageVo.tags.length; i++) {
                            var item = pageVo.tags[i]
                            if(i != 0) {
                                tagsHtml += '<span class="tab-link light-gray">·</span>'
                            }
                            tagsHtml += '<a class="tab-link light-gray" href="javascript:void(0)" onclick="javascript:openNewPageByTag(\'${tagItem.postTagId}\')">'+ item.description +'</a>'
                        }
                    }
                    let articleItemHtml =
                        '<div class="article-item" onclick="javascript:openNewPage(\'' + pageVo.postsId + '.html\')">'
                        + '<div class="article-basic-info">'
                        + '<a class="tab-link light-gray" href="javascript:void(0)">' + pageVo.userNiceName + '</a>'
                        + '<span class="wide-border small-margin"></span>'
                        + '<span class="tab-link light-gray">' + pageVo.postModifiedShortTime + '</span>'
                        + (pageVo.tags && pageVo.tags.length > 0 ? '<span class="wide-border small-margin"></span>' : '')
                        + tagsHtml
                        //+ (pageVo.tagsName ? '<a class="tab-link light-gray" href="javascript:void(0)">' + pageVo.tagsName + '</a>' : '')
                        + '</div>'
                        + '<a target="_blank" href="javascript:void(0)" class="article-title-cellphone">' + pageVo.postTitle + '</a>'
                        + '<div class="flex-row">'
                        + '<div class="flex-auto-item">'
                        + '<a target="_blank" href="javascript:void(0)" class="article-title">' + pageVo.postTitle + '</a>'
                        + '<p class="article-summary tab-link light-gray keep-rows">' + pageVo.postExcerpt + '</p>'
                        + '<div class="clear"></div>'
                        + '<ul class="article-statistics-info">'
                        + '<li>'
                        + '<img src="images/read-count.png"/> '
                        + '<span>' + (pageVo.pageView ? pageVo.pageView : 0) + '</span>'
                        + '</li>'
                        + '<li>'
                        + '<img src="images/zan.png"/> '
                        + '<span>' + (pageVo.likeCount ? pageVo.likeCount : 0) + '</span>'
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
    }, null, otherAjaxOptions)
}

// 加载文章效果，用于ajax
const loadingArticlesEffect = {
    beforeSend: (xhr) => {
        bottomLoadingFn(true)
    },
    complete: (xhr, status) => {
        bottomLoadingFn(false)
    }
}

// 底部加载更多文章的效果控制
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


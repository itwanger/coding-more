// 页面变量定义
let pageIndex = 1
let pageSize = 10
let currentTabIndex = 0
let searchText = ''
let searchTagId = null
let searchTagName = null
let dataLoadingStatus = 0

// document ready 事件
$(() => {
    initPage()
    // 加载手机端console，调试代码
    // new VConsole()
})

const initPage = () => {
    let transTagId = localStorage.getItem('search_tag_id')
    let transTagName = localStorage.getItem('search_tag_name')
    if(!transTagId) {
        // 显示骨架屏
        reloadArticleListEffect()
        bindEvents()
    } else {
        searchTagId = transTagId
        searchTagName = transTagName
        localStorage.removeItem('search_tag_id')
        localStorage.removeItem('search_tag_name')
        tabChanged(0)
    }

    let transText = localStorage.getItem('search_text')
    if(!transText) {
        // 显示骨架屏
        reloadArticleListEffect()
        bindEvents()
    } else {
        searchText = transText
        localStorage.removeItem('search_text')
        tabChanged(0)
    }
}

// 绑定页面事件
const bindEvents = () => {
    bindBodyEndScroll(true)

    $('#refleshBtn').click(function () {
        window.location.reload()
    })
}

// 重新加载列表效果方法
const reloadArticleListEffect = () => {
    showSkeleton(true)
    setTimeout(() => {
        showSkeleton(false)
    }, 0)
}

// 切换tab列表页方法
const tabChanged = (tabIndex) => {

    // 初始化加载中和加载完毕的元素状态
    $('#loading_over').text('').removeClass('noshow')
    $('#loading-more').addClass('noshow')
    $('.btn-back-top').addClass('noshow')
    dataLoadingStatus = 1

    // 切换效果
    $('.article-list-head > a.active').removeClass('active')
    $('.article-list-head > a').eq(tabIndex).addClass('active')

    // 设置数据
    currentTabIndex = tabIndex
    pageIndex = 0
    // 清空列表
    $('#article_list').html('')
    // 重新请求数据
    loadData(firstPageLoadingEffect)
}

// 重新加载列表的骨架屏效果方法
const showSkeleton = (isToShow) => {
    let skeletonContainer = $('#skeleton_container')
    let articleList = $('#article_list, .bottom-loading')
    if(isToShow) {
        articleList.addClass('noshow')
        skeletonContainer.removeClass('noshow')
    } else {
        articleList.removeClass('noshow')
        skeletonContainer.addClass('noshow')
    }
}

// 绑定滚动条事件
const bindBodyEndScroll = (isBind) => {
    let bodyElement = $('body')
    if (isBind) {
        bodyElement.unbind('scroll')
        // 监听滚动条位置是否到最下面了
        bodyElement.scroll(function () {
            let scrollTop = $(this).scrollTop();
            let scrollHeight = $(document).height();
            let windowHeight = $(this).height();

            // 滚动条到最底部判断
            if (scrollTop + windowHeight + 70 >= scrollHeight && dataLoadingStatus === 0) {
                dataLoadingStatus = 1
                loadData()
            }
            if (scrollTop > 900) {
                $('.btn-back-top').removeClass('noshow')
            } else {
                $('.btn-back-top').addClass('noshow')
            }
        });
    } else {
        bodyElement.unbind('scroll')
    }
}

// 展示某标签的列表搜索方法
const searchByTag = (tagId, tagName) => {
    let e = window.event || arguments.callee.caller.arguments[0];
    e.stopPropagation()

    if(searchTagId === tagId) {
        return
    }

    searchTagId = tagId
    searchTagName = tagName
    tabChanged(0)
}

const searchByText = () => {
    let inputText = $('#txtSearch').val().trim()
    if(!inputText) {
        alert('请输入搜索内容再搜索')
        return
    }
    if(searchText === inputText) {
        return
    }

    setSearchLayerVisible(false)
    searchText = inputText
    tabChanged(0)
}

// 关闭查询条件
const closeCondition = (conditionType) => {
    if(conditionType === 'tag') {
        searchTagId = null
        searchTagName = null
        tabChanged(0)
    } else if(conditionType === 'text') {
        searchText = ''
        tabChanged(0)
    }
}

// 修改顶部条件布局
const makeTopConditionChange = () => {
    let conditionHtml = ''
    if(searchText) {
        const tagConditionHtml =
            '<div class="condition-item flex-row h-center v-center flex-fixed-item">' +
            '搜索：' + searchText +
            '<i class="close-icon" onclick="closeCondition(\'text\')">' +
            '<img src="images/close-white.png" />' +
            '</i>' +
            '</div>';
        conditionHtml += tagConditionHtml
    }
    if(searchTagId && searchTagName) {
        const tagConditionHtml =
            '<div class="condition-item flex-row h-center v-center flex-fixed-item">' +
            '标签：' + searchTagName +
            '<i class="close-icon" onclick="closeCondition(\'tag\')">' +
            '<img src="images/close-white.png" />' +
            '</i>' +
            '</div>';
        conditionHtml += tagConditionHtml
    }
    $('.condition-container').html(conditionHtml)
}

// 加载更多文章的方法（搜索关键词，当前tab页签索引）
const loadData = (otherAjaxOptions) => {
    // 更新页面查询条件的效果
    makeTopConditionChange();
    // 生成查询条件
    pageIndex++
    let orderBy = 'menu_order,post_modified'
    if (currentTabIndex === 1) {
        orderBy = 'post_modified'
    } else if (currentTabIndex === 2) {
        orderBy = 'page_view,post_modified'
    }
    let reqData = {
        asc: false,
        orderBy,
        page: pageIndex,
        pageSize
    }
    // 搜索条件：标题
    if (searchText) {
        reqData['postTitleKeyword'] = searchText // encodeURIComponent(searchText)
    }
    // 搜索条件：标签
    if (searchTagId){
        reqData['searchTagId'] = searchTagId
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
                            let item = pageVo.tags[i]
                            if(i !== 0) {
                                tagsHtml += '<span class="tab-link light-gray">·</span>'
                            }
                            tagsHtml += '<a class="tab-link light-gray" href="javascript:void(0)" onclick="searchByTag('+ item.postTagId +',\''+ item.description +'\');cancelBubble=true;">'+ item.description +'</a>'
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
                        + '<a href="javascript:void(0)" class="article-title-cellphone">' + pageVo.postTitle + '</a>'
                        + '<div class="flex-row">'
                        + '<div class="flex-auto-item article-item-left">'
                        + '<a href="javascript:void(0)" class="article-title">' + pageVo.postTitle + '</a>'
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
            }
            if (retData.result.items.length < pageSize) {
                $('#loading_over').text('没有更多内容了')
                dataLoadingStatus = -1
            } else {
                $('#loading_over').text('')
                dataLoadingStatus = 0
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

// 异步加载列表首页时候的效果
const firstPageLoadingEffect = {
    beforeSend: (xhr) => {
        showSkeleton(true)
    },
    complete: (xhr, status) => {
        setTimeout(() => {
            showSkeleton(false)
        }, 1200)
    }
}

// 底部加载更多文章的效果控制
const bottomLoadingFn = (showLoading) => {
    if (showLoading) {
        $('.loading-over').addClass('noshow')
        $('.loading-more').removeClass('noshow')
    } else {
        setTimeout(() => {
            $('.loading-over').removeClass('noshow')
            $('.loading-more').addClass('noshow')
        }, 0)
    }
}


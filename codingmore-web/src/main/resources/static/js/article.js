$(() => {
    bindEvents()
})

// 绑定页面事件
const bindEvents = () => {
    // 根据情况判断是否绑定点赞方法
    let btnLike = $('.btn-like')
    if(!btnLike.hasClass('clicked')) {
        btnLike.one('click', () => {
            likeArticleClick()
        })
    }

    // 绑定滚动条事件，以合理显示返回顶部按钮
    $('body').scroll(function () {
        let scrollTop = $(this).scrollTop();

        if (scrollTop > 900) {
            $('.btn-back-top').removeClass('noshow')
        } else {
            $('.btn-back-top').addClass('noshow')
        }
    });
}
// 文章点赞方法
const likeArticleClick = () => {
    let postsId = $('#article_id').val()
    let reqData = { postsId }
    ajax_post('/rest/clickLike', reqData, (retData) => {
        if(retData.code === 0) {
            $('.btn-like').addClass('clicked')
            $('.thanks-note').removeClass('noshow')
        }
    })
}
// 文章点击标签跳转标签文章列表方法
const jumpToSearchTag = (tagId, tagName) => {
    localStorage.setItem('search_tag_id', tagId)
    localStorage.setItem('search_tag_name', tagName)
    openNewPage('/')
}

// 文章点击标签跳转标签文章列表方法
const searchByText = () => {
    let paramText = $('#txtSearch').val().trim()
    if(!paramText) {
        alert('请输入搜索内容再搜索')
        return
    }
    localStorage.setItem('search_text', paramText)
    openNewPage('/')
}


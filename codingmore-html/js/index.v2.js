$(() => {
  // 监听滚动条位置是否到最下面了
  $('body').scroll(function () {
    var scrollTop = $(this).scrollTop();
    var scrollHeight = $(document).height();
    var windowHeight = $(this).height();
    // 滚动条到最底部判断
    if (scrollTop + windowHeight == scrollHeight) {
      // 显示正在加载
      
      // 判断是否已经把预加载的数据都展示了

    }
  });
})
$(function () {
  initPage();
});

function initPage() { 
  console.log('window.innerHeight=', window.innerHeight, '顶部高度=', $('.top-fixed').height(), '底部高度=', $('.bottom-info').height());
  let mainMinHeight = window.innerHeight - $('.top-fixed').height() - $('.bottom-info').height();
  let mainHeightNow = $('.main-content').height();
  if (mainHeightNow < mainMinHeight) { 
    $('.main-content').css('min-height', mainMinHeight);
  }
  else {
    $('.main-content').height(mainHeightNow);
  }
}
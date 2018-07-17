document.addEventListener("turbolinks:load", function (event) {
  

  $('#sidebarCollapse').on('click', function() {
    $('#sidebar').toggleClass('show');
    $('.overlay').toggleClass('show');
    if ($('.icon-collapse').hasClass("fa-bars")) {
      $('.icon-collapse').removeClass("fa-bars");
      $('.icon-collapse').addClass("fa-times");
      $('.wrap-admin-page').css("overflowY", "hidden");
      $('.wrap-admin-page').css("height", "100vh");
    } else {
      $('.icon-collapse').addClass("fa-bars");
      $('.icon-collapse').removeClass("fa-times");
      $('.wrap-admin-page').css("overflowY", "auto");
      $('.wrap-admin-page').css("height", "auto");
    }
  });

  $('.overlay').on('click', function() {
    // hide sidebar
    $('#sidebar').removeClass('show');
    // hide overlay
    $('.overlay').removeClass('show');
    if ($('.icon-collapse').hasClass("fa-bars")) {
      $('.icon-collapse').removeClass("fa-bars");
      $('.icon-collapse').addClass("fa-times");
      $('.wrap-admin-page').css("overflowY", "hidden");
      $('.wrap-admin-page').css("height", "100vh");
    } else {
      $('.icon-collapse').addClass("fa-bars");
      $('.icon-collapse').removeClass("fa-times");
      $('.wrap-admin-page').css("overflowY", "auto");
      $('.wrap-admin-page').css("height", "auto");
    }
  });
});
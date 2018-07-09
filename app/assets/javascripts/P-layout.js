document.addEventListener("turbolinks:load", function (event) {
  

  $('#sidebarCollapse').on('click', function() {
    $('#sidebar').toggleClass('show');
    $('.overlay').toggleClass('show');
    if ($('.icon-collapse').hasClass("fa-bars")) {
      $('.icon-collapse').removeClass("fa-bars");
      $('.icon-collapse').addClass("fa-times");
    } else {
      $('.icon-collapse').addClass("fa-bars");
      $('.icon-collapse').removeClass("fa-times");
    }
  });

  $('.overlay').on('click', function() {
    // hide sidebar
    $('#sidebar').removeClass('show');
    // hide overlay
    $('.overlay').removeClass('show');
  });
});
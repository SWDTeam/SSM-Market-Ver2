function snackbarShow() {
  // Get the snackbar DIV
  var x = document.getElementById("notice");
  

  // Add the "show" class to DIV
  if (x !== null) {
    x.className = "show";
    var width = $("#notice").css("width");
    var screenWidth = $(window).width();

    width = parseInt(width);
    width = width/2;

    if (screenWidth <= 400) {
      $("#notice").css("width", screenWidth+"px");
      $("#notice").css("marginLeft", "-" +screenWidth/2+ "px");
    } else {
      var flag = $("#sidebar").css("marginLeft");

      if (flag === undefined || flag === "-250px") {
        $("#notice").css("marginLeft", "-" +width+ "px");
      }
    }
    // After 3 seconds, remove the show class from DIV
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
  }
}

document.addEventListener("turbolinks:load", function (event) {
  var notice = $("#notice").text();
  if (notice !== "" && notice !== " ") {
    snackbarShow();
  }
});
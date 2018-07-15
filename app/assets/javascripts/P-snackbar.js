function snackbarShow() {
  // Get the snackbar DIV
  var x = document.getElementById("notice");

  // Add the "show" class to DIV
  if (x !== null) {
    x.className = "show";

    // After 3 seconds, remove the show class from DIV
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
  }
}

document.addEventListener("turbolinks:load", function (event) {
  var notice = $("#notice").text();
  if (notice !== "") {
    snackbarShow();
  }
});
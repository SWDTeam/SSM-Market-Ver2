//to change odd element's background
$(document).ready(function() {
  var element = document.getElementsByClassName("wrap-content-index-category");

  var i;
  for (i = 0; i < element.length; i++) {
    if (i % 2 != 0) {
      $(element[i]).css("background-color", "#F4F3EF");
    }
  }
})
function dropDownAdPage() {
  document.getElementById("dropdown-admin-page").classList.toggle("show-adpage");
  var ele = document.getElementsByClassName("change-icon");

  if (ele[0].classList.contains("fa-angle-down")) {
    ele[0].classList.remove("fa-angle-down");
    ele[0].classList.add("fa-angle-up");
  } else {
    ele[0].classList.remove("fa-angle-up");
    ele[0].classList.add("fa-angle-down");
  }
}
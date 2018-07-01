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

function dropContentMenu(id, index) {
  var field = document.getElementById(id);
  field.classList.toggle("show-adpage");

  /*if (field.classList.contains("show-adpage")) {
    field.style.height = "auto";
  } else {
    field.style.height = "0";
  }*/

  var ele = document.getElementsByClassName("change-icon")[index];

  if (ele.classList.contains("fa-angle-down")) {
    ele.classList.remove("fa-angle-down");
    ele.classList.add("fa-angle-up");
  } else {
    ele.classList.remove("fa-angle-up");
    ele.classList.add("fa-angle-down");
  }
}
function changeActiveClass() {
  var ele = document.getElementsByClassName("active")[0];
  $(ele).removeClass("active");

  ele = document.getElementsByClassName("nav-li-admin-page");

  var pgurl = window.location.href;

  if (pgurl.includes("home")) {
    $(ele[0]).addClass("active");
  } else if (pgurl.includes("accounts")) {
    $(ele[1]).addClass("active");
  } else if (pgurl.includes("products")) {
    $(ele[2]).addClass("active");
  } else if (pgurl.includes("orders")) {
    $(ele[3]).addClass("active");
  } else {
    $(ele[4]).addClass("active");
  }
}
//-------------login page-------------
var flag = true;
$("#form--login").submit(function () {
    flag = checkLogin();
    return flag;
});

function checkLogin() {
  resetTextErrors();
  var email = $("#loginEmail").val();
  var pass = $("#loginPassword").val();
  var t = true;

  if (email === "" || email === null) {
      $("#error--email").text("Email can't be blank!").css("color", "red");
      t = false;
  }
  if (pass === "" || pass === null) {
      $("#error--pass").text("Password can't be blank!").css("color", "red");
      t = false;
  }
  return t;
}

//-------------login page-------------
document.addEventListener("turbolinks:load", function (event) {
var flag;
    $("#M--form--login").submit(function () {
        flag = checkLogin();
        return flag;
    });
});

function checkLogin() {
  resetTextErrors();
  var email = $("#account_email").val();
  console.log(email);
  var pass = $("#account_password").val();
  var t = true;

  if (email == "" || email == null || email == undefined) {
      $("#error--email").text("Email can't be blank!");
      t = false;
  }
  if (pass == "" || pass == null || pass == undefined) {
      $("#error--pass").text("Password can't be blank!");
      t = false;
  }
  return t;
}


//---------reset text---------
function resetTextErrors() {
    $("#error--email").text("").css("color", "red");
    $("#error--name").text("").css("color", "red");
    $("#error--phone").text("").css("color", "red");
    $("#error--gender").text("").css("color", "red");
    $("#error--role").text("").css("color", "red");
    $("#error--status").text("").css("color", "red");
    $("#error--pass").text("").css("color", "red");
    $("#error--confirm--pass").text("").css("color", "red");
    $("#error--old--pass").text("").css("color", "red");
    $("#error--manufacturer").text("").css("color", "red");
    $("#error--price").text("").css("color", "red");
    $("#error--quantity").text("").css("color", "red");
    $("#error--date--manufacture").text("").css("color", "red");
    $("#error--date--expiration").text("").css("color", "red");
    $("#error--pic").text("").css("color", "red");
    $("#error--editor").text("").css("color", "red");
    $("#error--description").text("").css("color", "red");
}

document.addEventListener("turbolinks:load", function (event) {

//-------------login page-------------
    var flag = true;
    $(".form--login").submit(function () {
        flag = checkLogin();
        return flag;
    });

//-------------forgot pass page-------------
    $(".form--forget--pass").submit(function () {
        flag = checkNewPass();
        return flag;
    });

//-------------new product page-------------
    flag = true;
    $("#form--new--product").submit(function () {
        flag = checkNewProduct();
        console.log("flag " + flag);
        return flag;
    });

//-------------new cate-------------
    flag = true;
    $("#form--add--cate").submit(function () {
        flag = checkNewCate();
        return flag;
    });

//-------------change password page-------------
    flag = true;
    $("#form--change--password").submit(function () {
        flag = checkChangePassword();
        return flag;
    });
});

function resetTextErrors() {
    $("#error--email").text("");
    $("#error--email--newpass").text("");
    $("#error--name").text("");
    $("#error--phone").text("");
    $("#error--gender").text("");
    $("#error--role").text("");
    $("#error--status").text("");
    $("#error--pass").text("");
    $("#error--pass--change").text("");
    $("#error--confirm--pass").text("");
    $("#error--old--pass").text("");
    $("#error--manufacturer").text("").css("color", "red");
    $("#error--price").text("").css("color", "red");
    $("#error--quantity").text("").css("color", "red");
    $("#error--date--manufacture").text("").css("color", "red");
    $("#error--date--expiration").text("").css("color", "red");
    $("#error--pic").text("").css("color", "red");
    $("#error--editor").text("").css("color", "red");
    $("#error--product--key").text("").css("color", "red");
    $("#error--product--cate").text("").css("color", "red");
    $("#error--description").text("").css("color", "red");
}

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

function checkNewPass() { 
    resetTextErrors();
    var email = $("#loginEmailNewPass").val();
    var t = true;

    if (email === "" || email === null) {
        $("#error--email--newpass").text("Email can't be blank!").css("color", "red");
        t = false;
    }

    return t;
}

function checkNewProduct() {
    resetTextErrors();
    var t = true;
    console.log("show 1");
    var name = $("#product_name").val();
    var price = $("#product_price").val();
    var amount = $("#product_quantity").val();
    var manu = $("#product_manufacturer").val();
    var dateManu = $("#product_manu_date").val();
    var endManu = $("#product_expired_date").val();
    var img = $("#product_images_attributes_0_url").val();
    var editor = $("#product_description").val();
    var productKey = $("#product_product_key").val();
    var cate = $("#product_product_category :selected").val();

    console.log(editor);
    var numberExp = /^[0-9]*$/;

    if (productKey === "" || productKey === null) {
        $("#error--product--key").text("Product key can't be blank");
    }

    if (cate === "" || cate === null) {
        $("#error--product--cate").text("Category key can't be blank")
    }

    if (editor === "<p>&nbsp;</p>" || editor === "" || editor === null) {
        $("#error--editor").text("Description can't be blank");
        t = false;
    } else if (editor.length > 1000) {
        $("#error--editor").text("Description length maxium 1000 characters");
        t = false;
    }
    if (name === "" || name === null) {
        $("#error--name").text("Name can't be blank!").css("color", "red");
        t = false;
    } else if (name.length > 100) {
        $("#error--name").text("Name maximum is 100 characters!").css("color", "red");
        t = false;
    }
    if (price === "" || price === null) {
        $("#error--price").text("Price can't be blank");
        t = false;
    } else if (!price.match(numberExp)) {
        $("#error--price").text("Price is invaild");
        t = false;
    } else if (price.length > 6) {
        $("#error--price").text("Max price is $999.999");
        t = false;
    }
    if (manu === null || manu === "") {
        $("#error--manufacturer").text("Manufacturer can't be blank");
        t = false;
    } else if (manu.length > 50) {
        $("#error--manufacturer").text("Manufacturer max length is 50 characters");
        t = false;
    }
    if (amount === null || amount === "") {
        $("#error--quantity").text("Quantity can't be blank");
        t = false;
    } else if (!amount.match(numberExp)) {
        $("#error--quantity").text("Quantity is vaild");
        t = false;
    } else if (amount.length > 4) {
        $("#error--quantity").text("Max quantity is 9999 item");
        t = false;
    }
    if (dateManu === null || dateManu === "") {
        $("#error--date--manufacture").text("Date of manufacture can't be blank");
        t = false;
    }
    if (endManu === null || endManu === "") {
        $("#error--date--expiration").text("Expiration date can't be blank");
        t = false;
    }
    dateManu = new Date(dateManu);
    endManu = new Date(endManu);
    if (endManu < dateManu) {
        $("#error--date--expiration").text("Expiration date invaild!");
        t = false;
    }
    if (img === null || img === "") {
        $("#error--pic").text("At least having one image!");
        t = false;
    }
    console.log("Check t " + t);
    return t;
}

function checkNewCate() {
    $("#error--name--cate").text("").css("color", "red");
    $("#error--pic--cate").text("").css("color", "red");

    var t = true;
    var name = $("#category_name").val();
//  console.log(name);
    var img = $("#category_images_url").val();
    if (name === "" || name === null) {
        $("#error--name--cate").text("Name can't be blank!");
        t = false;
    } else if (name.length > 50) {
        $("#error--name").text("Name maximum is 50 characters!");
        t = false;
    }
    if (img === null || img === "") {
        $("#error--pic--cate").text("Image can't empty!");
        t = false;
    }
    return t;
}

function checkChangePassword() {
    resetTextErrors();
    var t = true;
    var oldPass = $("#old_password").val();

    t = checkPassword();
    if (oldPass === "" || oldPass === null) {
        $("#error--old--pass").text("Current password is invaild!").css("color", "red");
        t = false;
    }
    return t;
}

function checkPassword() {
    resetTextErrors();
    var oldPass = $("#old_password").val();
    var pass = $("#new_password").val();
    var confirmPass = $("#confirm_password").val();
    var t = true;
    if (pass === "" || pass === null) {
        $("#error--pass--change").text("Password can't be blank!").css("color", "red");
        t = false;
    } else if (pass.length < 6 || pass.length > 30) {
        $("#error--pass--change").text("Password is length in range [6,30]!").css("color", "red");
        t = false;
    } else if (pass == oldPass) {
        $("#error--pass--change").text("New Password can't be the same as Old Password").css("color", "red");
        t = false;
    }
    if (confirmPass === null || confirmPass === "") {
        $("#error--confirm--pass").text("Confirm password can't be blank!").css("color", "red");
        t = false;
    }
    if (confirmPass !== pass) {
        $("#error--confirm--pass").text("Confirm password not match!").css("color", "red");
        t = false;
    }
    return t;
}
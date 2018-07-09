document.addEventListener("turbolinks:load", function (event) {
  $("#select_status").change(function () {
    $("#select_category").val('');
    getListProductByStatus();
  });

  $("#select_category").change(function () {
    $("#select_status").val('');
    getListProductByCategory();
  });

  $("#M-btn-search").click(function () {
    getListProductByName();
  });

  $("#M-btn-search-cates").click(function () {
    getListCategoriesByName();
  });

  //accounts
  $("#account_role").change(function () {
    getListUsersByRole();
  });

  $("#M-btn-user-search").click(function () {
    getListUsersByName();
  });


})

//reset list
function resetListProducts() {
  var count = 1;
  var row_product = $(".M-test");
  $.each(row_product, function (key, value) {
    var id = $(value).attr('id');
    $("#" + id).css("display", "");
    $("#" + id + "-index").text(count);
    count++;
    if (count % 2 != 0) $('#' + id).css("background-color", "#F4F3EF");
    else $('#' + id).css("background-color", "white");
  })
}
//================Product index====================
// SORT BY STATUS OF PRODUCT
function getListProductByStatus() {
  var value = $("#select_status").val();
  //reset
  if (value == "") {
    resetListProducts();
  }

  //call ajax
  if (value != "") {
    $.ajax({
      url: "/products_by_status",
      method: "get",
      data: {
        status: value
      },
      success: function (data) {
        if (data.length < 1) alert("Not found");
        else {
          var row_product = $(".M-test");
          var count = 1;
          $.each(row_product, function (key, value) {
            data.some(function (item) {
              var id = $(value).attr('id');
              // console.log("item "+ item.id + "key" + id);
              if (item.id == id) {
                $("#" + id).css("display", "");
                // console.log(id + "block"); 
                $("#" + id + "-index").text(count);
                count++;
                // console.log("count " + count);

                //color
                if (count % 2 != 0) $('#' + id).css("background-color", "#F4F3EF");
                else $('#' + id).css("background-color", "white");
                return item.id == id;
              } else {
                $("#" + id).css("display", "none");
                // console.log(id + "none");    
              }
            });
          })
        }
      }
    })
  }
}
// SORT BY STATUS OF PRODUCT

// SORT BY CATEGORY ID OF PRODUCT
function getListProductByCategory() {
  var value = $("#select_category").val();
  // alert(value);
  //reset
  if (value == "") {
    resetListProducts();
  }
  //call ajax value != ''
  else {
    $.ajax({
      url: "/products_by_category",
      method: "get",
      data: {
        category_id: value
      },
      success: function (data) {
        if (data.length < 1) alert("Not found");
        else {
          var row_product = $(".M-test");
          var count = 1;
          $.each(row_product, function (key, value) {
            data.some(function (item) {
              var id = $(value).attr('id');
              if (item.id == id) {
                $("#" + id).css("display", "");
                $("#" + id + "-index").text(count);
                count++;
                //color
                if (count % 2 != 0) $('#' + id).css("background-color", "#F4F3EF");
                else $('#' + id).css("background-color", "white");
                return item.id == id;
              } else $("#" + id).css("display", "none");
            });
          })
        }
      }
    })
  }
}
// SORT BY CATEGORY ID OF PRODUCT

// SORT BY PRODUCT NAME OF PRODUCT
function getListProductByName() {
  var value = $("#txt_search_name").val();
  // alert(value);
  // reset
  if (value == "") {
    resetListProducts();
  }
  // call ajax value != ''
  else {
    $.ajax({
      url: "/products_by_name",
      method: "get",
      data: {
        name: value
      },
      success: function (data) {
        if (data.length < 1) alert("Not found");
        else {
          var row_product = $(".M-test");
          var count = 1;
          $.each(row_product, function (key, value) {
            data.some(function (item) {
              var id = $(value).attr('id');
              if (item.id == id) {
                $("#" + id).css("display", "");
                $("#" + id + "-index").text(count);
                count++;
                //color
                if (count % 2 != 0) $('#' + id).css("background-color", "#F4F3EF");
                else $('#' + id).css("background-color", "white");
                return item.id == id;
              } else $("#" + id).css("display", "none");
            });
          })
        }
      }
    })
  }
}
// SORT BY PRODUCT NAME OF PRODUCT
//================Product index====================
//SORT BY CATEGORY NAME OF CATEGORY
function getListCategoriesByName() {
  var value = $("#txt_search_cate").val();
  // alert(value);
  // reset
  if (value == "") {
    resetListProducts();
  }
  // call ajax value != ''
  else {
    $.ajax({
      url: "/categories_by_name",
      method: "get",
      data: {
        name: value
      },
      success: function (data) {
        if (data.length < 1) alert("Not found");
        else {
          var row_product = $(".M-test");
          var count = 1;
          $.each(row_product, function (key, value) {
            data.some(function (item) {
              var id = $(value).attr('id');
              if (item.id == id) {
                $("#" + id).css("display", "");
                $("#" + id + "-index").text(count);
                count++;
                //color
                if (count % 2 != 0) $('#' + id).css("background-color", "#F4F3EF");
                else $('#' + id).css("background-color", "white");
                return item.id == id;
              } else $("#" + id).css("display", "none");
            });
          })
        }
      }
    })
  }
}

//================User index====================
// SORT BY USER ROLE OF USER
function getListUsersByRole() {
  var value = $("#account_role").val();
  // alert(value);
  // reset
  if (value == "") {
    resetListProducts();
  }
  // call ajax value != ''
  else {
    $.ajax({
      url: "/account_by_role",
      method: "get",
      data: {
        role_id: value
      },
      success: function (data) {
        if (data.length < 1) alert("Not found");
        else {
          var row_product = $(".M-test");
          var count = 1;
          $.each(row_product, function (key, value) {
            data.some(function (item) {
              var id = $(value).attr('id');
              if (item.id == id) {
                $("#" + id).css("display", "");
                $("#" + id + "-index").text(count);
                count++;
                //color
                if (count % 2 != 0) $('#' + id).css("background-color", "#F4F3EF");
                else $('#' + id).css("background-color", "white");
                return item.id == id;
              } else $("#" + id).css("display", "none");
            });
          })
        }
      }
    })
  }
}

// SORT BY USER ROLE OF USER

// SORT BY USER NAME OF USER
function getListUsersByName() {
  var value = $("#txt_search_name").val();
  // alert(value);
  // reset
  if (value == "") {
    resetListProducts();
  }
  // call ajax value != ''
  else {
    $.ajax({
      url: "/account_by_name",
      method: "get",
      data: {
        name: value
      },
      success: function (data) {
        if (data.length < 1) alert("Not found");
        else {
          var row_product = $(".M-test");
          var count = 1;
          $.each(row_product, function (key, value) {
            data.some(function (item) {
              var id = $(value).attr('id');
              if (item.id == id) {
                $("#" + id).css("display", "");
                $("#" + id + "-index").text(count);
                count++;
                //color
                if (count % 2 != 0) $('#' + id).css("background-color", "#F4F3EF");
                else $('#' + id).css("background-color", "white");
                return item.id == id;
              } else $("#" + id).css("display", "none");
            });
          })
        }
      }
    })
  }
}

// SORT BY USER NAME OF USER
document.addEventListener("turbolinks:load", function (event) {
  //products
  $("#select_status").change(function () {
    $("#select_category").val('');
    getListProductByStatus();
  });

  $("#select_category").change(function () {
    $("#select_status").val('');
    getListProductByCategory();
  });

  $("#M-btn-search").click(function () {
    $("#select_status").val('');
    $("#select_category").val('');
    getListProductByName();
  });

  //categories
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

  //order
  $("#select_order_status").change(function () {
    getListOrdersByStatus();
  });

  $("#M-btn-order-search").click(function () {
    getListOrdersByCode();
  });
})

//================Product index====================
// SORT BY STATUS OF PRODUCT
function getListProductByStatus() {
  var value = $("#select_status").val();
  //reset
  if (value == "") resetList();
  //call ajax
  if (value != "") {
    $.ajax({
      url: "/products_by_status",
      method: "get",
      data: {
        status: value
      },
      success: function (data) {
        getSuccessData(data);
      }
    })
  }
}
// SORT BY STATUS OF PRODUCT

// SORT BY CATEGORY ID OF PRODUCT
function getListProductByCategory() {
  var value = $("#select_category").val();
  //reset
  if (value == "") resetList();
  //call ajax value != ''
  else {
    $.ajax({
      url: "/products_by_category",
      method: "get",
      data: {
        category_id: value
      },
      success: function (data) {
        getSuccessData(data);
      }
    })
  }
}
// SORT BY CATEGORY ID OF PRODUCT

// SORT BY PRODUCT NAME OF PRODUCT
function getListProductByName() {
  var value = $("#txt_search_name").val();
  // reset
  if (value == "") resetList();
  // call ajax value != ''
  else {
    $.ajax({
      url: "/products_by_name",
      method: "get",
      data: {
        name: value
      },
      success: function (data) {
        getSuccessData(data);
      }
    })
  }
}
// SORT BY PRODUCT NAME OF PRODUCT
//================Product index====================
//SORT BY CATEGORY NAME OF CATEGORY
function getListCategoriesByName() {
  var value = $("#txt_search_cate").val();
  // reset
  if (value == "") resetList();
  // call ajax value != ''
  else {
    $.ajax({
      url: "/categories_by_name",
      method: "get",
      data: {
        name: value
      },
      success: function (data) {
        getSuccessData(data);
      }
    })
  }
}

//================User index====================
// SORT BY USER ROLE OF USER
function getListUsersByRole() {
  var value = $("#account_role").val();
  // reset
  if (value == "") resetList();
  // call ajax value != ''
  else {
    $.ajax({
      url: "/account_by_role",
      method: "get",
      data: {
        role_id: value
      },
      success: function (data) {
        getSuccessData(data);
      }
    })
  }
}

// SORT BY USER ROLE OF USER

// SORT BY USER NAME OF USER
function getListUsersByName() {
  var value = $("#txt_search_name").val();
  // reset
  if (value == "") resetList();
  // call ajax value != ''
  else {
    $.ajax({
      url: "/account_by_name",
      method: "get",
      data: {
        name: value
      },
      success: function (data) {
        getSuccessData(data);
      }
    })
  }
}

// SORT BY USER NAME OF USER

//================Order index====================
// SORT BY ORDER STATUS OF ORDER
function getListOrdersByStatus() {
  var value = $("#select_order_status").val();
  // reset
  if (value == "") resetList();
  // call ajax value != ''
  else {
    $.ajax({
      url: "/orders_by_status",
      method: "get",
      data: {
        status: value
      },
      success: function (data) {
        getSuccessData(data);
      }
    })
  }
}

// SORT BY ORDER STATUS OF ORDER

// SORT BY ORDER CODE OF ORDER
function getListOrdersByCode() {
  var value = $("#txt_search_name").val();
  // reset
  if (value == "") resetList();
  // call ajax value != ''
  else {
    $.ajax({
      url: "/orders_by_code",
      method: "get",
      data: {
        code: value
      },
      success: function (data) {
        getSuccessData(data);
      }
    })
  }
}
// SORT BY ORDER CODE OF ORDER

//--------sub function----------
function getSuccessData(data) {
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

//reset list
function resetList() {
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
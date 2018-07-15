document.addEventListener("turbolinks:load", function (event) {
  //products
  $("#select_status").change(function () {
    $("#select_category").val('');
    $("#M-result").html('');
    getListProductByStatus();
  });

  $("#select_category").change(function () {
    $("#select_status").val('');
    $("#M-result").html('');
    getListProductByCategory();
  });

  $("#M-btn-search").click(function () {
    $("#select_status").val('');
    $("#select_category").val('');
    $("#M-result").html('');
    getListProductByName();
  });

  //categories
  $("#M-btn-search-cates").click(function () {
    $("#M-result").html('');
    getListCategoriesByName();
  });

  //accounts
  $("#account_role").change(function () {
    $("#M-result").html('');
    getListUsersByRole();
  });

  $("#M-btn-user-search").click(function () {
    $("#M-result").html('');
    getListUsersByName();
  });

  //order
  $("#select_order_status").change(function () {
    $("#M-result").html('');
    getListOrdersByStatus();
  });

  $("#M-btn-order-search").click(function () {
    $("#M-result").html('');
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
        getSuccessDataProducts(data);
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
        getSuccessDataProducts(data);
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
        getSuccessDataProducts(data);
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
        getSuccessDataCategories(data);
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
        getSuccessDataUsers(data);
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
        getSuccessDataUsers(data);
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
        getSuccessDataOrders(data);
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
        getSuccessDataOrders(data);
      }
    })
  }
}
// SORT BY ORDER CODE OF ORDER

//--------Sub function Products----------
function getSuccessDataProducts(data) {
  var account_id = $("#txt_current").val();

  if (data.length < 1) alert("Not found");
  else {
    var row_product = $(".M-test");
    var row_result = $("#M-result");
    var html = '';

    row_product.css("display", "none");
    row_result.css("display", "");

    var count = 1;
    data.some(function (item) {
      html += '<div class="row py-3 wrap-content-index-product content-index-product M--result--row">';
      html += '<div class="col-1 text-center">' + count + '</div>';

      html += '<div class="col-3">' + item.name + '</div>';
      html += '<div class="col-2">' + item.category_name + '</div>';
      html += '<div class="col-1 text-center">' + item.quantity + '</div>';
      html += '<div class="col-2 text-center" style="text-transform: capitalize;">' + item.status + '</div>';
      html += '<div class="col-1 text-center"><center><a href="/products/' + item.id +
        '"><i class="fa fa-search-plus show-bnt-index-account align-items-center"></i></a></center></div>';
      if (item.status == 'active') {
        html += '<div class="col-2"><center><a rel="nofollow" data-method="put" href="/accounts/' +
          account_id + '/products/' + item.id + '?product%5Bstatus%5D=hidden"><i class="fa fa-unlock-alt active-bnt-index-account align-items-center" style="width: 85%;"><span class="px-1 align-items-center" style="font-size: 13px;">Active</span></i></a></center></div>';
      } else {
        html += '<div class="col-2"><center><a rel="nofollow" data-method="put" href="/accounts/' +
          account_id + '/products/' + item.id + '?product%5Bstatus%5D=active"><i class="fa fa-lock ban-bnt-index-account align-items-center" style="width: 85%;"><span class="px-1 align-items-center" style="font-size: 13px;">Ban</span></i></a></center></div>';
      }
      html += '</div>';
      count++;
      //check color
      var checkQuantity = $(".M--result--row");
      if (item.quantity < 1) {
        checkQuantity.css({
          "color": "red",
          "font-weight": "bold"
        });
      }
      row_result.append(html);
      html = '';
    })
    changeColor();
  }
}

//--------Sub function Categories----------
function getSuccessDataCategories(data) {
  var account_id = $("#txt_current").val();

  if (data.length < 1) alert("Not found");
  else {
    var row_product = $(".M-test");
    var row_result = $("#M-result");
    var html = '';

    row_product.css("display", "none");
    row_result.css("display", "");

    var count = 1;
    data.some(function (item) {
      html += '<div class="row py-3 wrap-content-index-category content-index-category M--result--row">';
      html += '<div class="col-2 text-center">' + count + '</div>';
      html += '<div class="col-2"><img src="' + item.image + '" alt="" class="img-index-categories"></div>';
      html += '<div class="col-4">' + item.name + '</div>';
      html += '<div class="col-2 text-center">' + item.count_products + '</div>';

      if (item.status == 'active') {
        html += '<div class="col-1"><center><a href="/accounts/' +
          account_id + '/categories/' + item.id + '/edit"><button class="edit-bnt-index-category text-center"><i class="fa fa-edit"></i></button></a></center></div>';
      } else {
        html += '<div class="col-1"><center><a href="/accounts/' +
          account_id + '/categories/' + item.id + '/edit"><button class="edit-bnt-index-category"><i class="fa fa-lock"></i></button></a></center></div>';
      }
      html += '</div>';
      count++;
      row_result.append(html);
      html = '';
    })
    changeColor();
  }
}

//--------Sub function Users----------
function getSuccessDataUsers(data) {
  var account_id = $("#txt_current").val();

  if (data.length < 1) alert("Not found");
  else {
    var row_product = $(".M-test");
    var row_result = $("#M-result");
    var html = '';

    row_product.css("display", "none");
    row_result.css("display", "");

    var count = 1;
    data.some(function (item) {
      html += '<div class="row py-3 wrap-content-index-product content-index-account M--result--row">';
      html += '<div class="col-1 px-1 text-center">' + count + '</div>';
      html += '<div class="col-2 pl-0">' + item.name + '</div>';
      html += '<div class="col-4 pl-0">' + item.email + '</div>';
      html += '<div class="col-1 px-1">' + item.role + '</div>';
      html += '<div class="col-1 pr-0 text-center">' + item.status + '</div>';
      html += '<div class="col-1 pr-0"><center><a href="/accounts/' +
        item.id + '"><i class="fa fa-search-plus show-bnt-index-account align-items-center"></i></a></center></div>';
      html += '<div class="col-2">';
      if (item.role.toLowerCase() != 'admin') {
        if (item.status.toLowerCase() == 'active') {
          html += '<center><a rel="nofollow" data-method="put" href="/accounts/' +
            item.id + '?account%5Bstatus%5D=hidden"><i class="fa fa-lock ban-bnt-index-account align-items-center" style="width: 65%;"><span class="px-1 align-items-center" style="font-size: 13px;">Ban</span></i></a></center>';
        } else {
          html += '<center><a rel="nofollow" data-method="put" href="/accounts/' +
            item.id + '?account%5Bstatus%5D=active"><i class="fa fa-unlock-alt active-bnt-index-account align-items-center" style="width: 65%;"><span class="px-1 align-items-center" style="font-size: 13px;">Active</span></i></a></center>';
        }
      }
      html += '</div></div>';
      count++;
      row_result.append(html);
      html = '';
    })
    changeColor();
  }
}

//--------Sub function Orders----------
function getSuccessDataOrders(data) {
  var account_id = $("#txt_current").val();

  if (data.length < 1) alert("Not found");
  else {
    var row_product = $(".M-test");
    var row_result = $("#M-result");
    var html = '';

    row_product.css("display", "none");
    row_result.css("display", "");

    var count = 1;
    data.some(function (item) {
      html += '<div class="row py-3 wrap-content-index-category content-index-order M--result--row">';
      html += '<div class="col-1 px-2 text-center">' + count + '</div>';
      html += '<div class="col-1 pl-1 text-center">' + item.code + '</div>';
      html += '<div class="col-3 pl-4">' + item.cus_name + '</div>';
      html += '<div class="col-1 px-1 text-center">' + item.total_price + '</div>';
      html += '<div class="col-2 text-center">';

      if (item.status == 'pending') html += '<span class="order-status" style="background-color: #F6AC43;">Pending</span>';
      else html += '<span class="order-status" style="background-color: #008AFF;">Payment</span>';

      html += '</div><div class="col-2 px-1 text-center" style="font-size: 15px;">';
      html += '<a href="/orders/' + item.id + '"><i class="fa fa-search-plus show-bnt-index-account align-items-center" style="width: 70%;"></i></a></div>';
      html += '<div class="col-2 pl-0 text-center pr-3">';
      if (item.status == "pending") {
        html += '<a rel="nofollow" data-method="put" href="/accounts/' +
          account_id + '/orders/' + item.id + '?order%5Bcashier_id%5D=' + account_id + '&amp;order%5Bstatus%5D=payment"><button class="bnt-payment-order" style="width: 70%;">Payment</button></a>';
      }
      html += '</div></div>';
      count++;
      row_result.append(html);
      html = '';
    })
    changeColor();
  }
}

//reset list
function resetList() {
  var row_product = $(".M-test");
  row_product.css("display", "");
}

//change color
function changeColor() {
  var element = document.getElementsByClassName("M--result--row");
  var i;
  for (i = 0; i < element.length; i++) {
    if (i % 2 != 0) {
      $(element[i]).css("background-color", "#F4F3EF");
    }
  }
}
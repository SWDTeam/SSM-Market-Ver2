
document.addEventListener("turbolinks:load", function (event) {
  $("#select_status").change(function(){
    getlistProductByStatus();
  });
})

// SORT BY STATUS OF PRODUCT
function getlistProductByStatus() {
  var value = $("#select_status").val();
  var count = 1;
  //reset
  if(value == ""){
    var row_product = $(".M-test");
    $.each(row_product, function(key, value) {
      var id = $(value).attr('id');
      $("#" + id).css("display", "");
      $("#" + id+ "-index").text(count);
      count++;
      if (count % 2 != 0) $('#'+ id).css("background-color", "#F4F3EF");
      else $('#'+ id).css("background-color", "white");
    })
  }

  //call ajax
  if (value != ""){
  $.ajax({
    url: "/products_by_status",
    method: "get",
    data: {
      status: value
    }, 
    success: function (data) { 
      if (data.length < 1) console.log("Not found");
      else {
        var row_product = $(".M-test"); 
        var count = 1;
        $.each(row_product, function(key, value) {
          data.some(function (item) { 
            var id = $(value).attr('id');
            // console.log("item "+ item.id + "key" + id);
            if(item.id == id){
              $("#" + id).css("display", "");
              // console.log(id + "block"); 
              $("#" + id+ "-index").text(count);
              count++;
              // console.log("count " + count);
              
              //color
              if (count % 2 != 0) $('#'+ id).css("background-color", "#F4F3EF");
              else $('#'+ id).css("background-color", "white");
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
package test.kietpt.smartmarket.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import test.kietpt.smartmarket.R;

import test.kietpt.smartmarket.adapter.OrderDetailAdapter;
import test.kietpt.smartmarket.model.OrderDetailDTO;

import test.kietpt.smartmarket.ulti.IpConfig;

public class OrderedDetailActi extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    Button btnDelete;
    OrderDetailAdapter orderDetailAdapter;
    static TextView txtTotalOrderDetail;
    static ArrayList<OrderDetailDTO> listOrderDetail;
    int orderId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_detail);
        reflect();
        actionToolBar();
        getTotalPriceInOrderDetail();
        getOrderId();


    }

    private void actionToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getOrderId() {
        Intent intent = getIntent();
        orderId = intent.getIntExtra("orderId", 1);
        Log.e("ORDERID ", orderId + "");
        getOrderDetail("https://ssm-market.herokuapp.com/api/v1/order_details/" + orderId);
    }

    private void getOrderDetail(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("OrderDetail List", response.toString());
                if (response.toString() != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int id = jsonObject.getInt("order_products_id");

                            int productId = jsonObject.getInt("product_id");
                            String name = jsonObject.getString("product_name");
                            //float price = (float) jsonObject.getDouble("price");


                            int quantity = jsonObject.getInt("quantity");
                            float total = (float) jsonObject.getDouble("total");
                            Log.e("PPICE = ", total + "");
                            String status = jsonObject.getString("status");

                            String urlPic = jsonObject.getString("url");
                            String urlTest = "https://ssm-market.herokuapp.com" + urlPic;
                            OrderDetailDTO dto = new OrderDetailDTO();
                            dto.setId(id);
                            dto.setProductId(productId);
                            dto.setProductName(name);
                            dto.setPrice(total);
                            dto.setQuantity(quantity);
                            dto.setStatus(status);
                            dto.setImgKey(urlTest);
                            listOrderDetail.add(dto);
                            getTotalPriceInOrderDetail();

                            orderDetailAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void reflect() {
        toolbar = (Toolbar) findViewById(R.id.toolbarOrderDetail);
        listView = (ListView) findViewById(R.id.listViewOrderDetail);
        txtTotalOrderDetail = (TextView) findViewById(R.id.txtTotalPriceOrderDetail);


        btnDelete = (Button) findViewById(R.id.btnDeleteOrderDetail);
        listOrderDetail = new ArrayList<>();
        orderDetailAdapter = new OrderDetailAdapter(OrderedDetailActi.this, listOrderDetail);
        listView.setAdapter(orderDetailAdapter);
    }

    public static void getTotalPriceInOrderDetail() {
        float totalOfPrice = 0;
        for (int i = 0; i < listOrderDetail.size(); i++) {
            Log.e("TESTKIET", listOrderDetail.get(i).getId() + " - " + listOrderDetail.get(i).getPrice());
            totalOfPrice += listOrderDetail.get(i).getPrice();
        }
        Log.e("PRICE TEST ", totalOfPrice + "");
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTotalOrderDetail.setText(decimalFormat.format(totalOfPrice) + " $ ");
    }


    public void deleteOrdered(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm your ordered !!! ");
        builder.setMessage("Do you want to confirm your ordered ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletedOrder("https://ssm-market.herokuapp.com/api/v1/orders/" + orderId);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });
        builder.show();
    }

    private void deletedOrder(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Deleted Ordered ", response.toString());
                try {
                    String message = response.getString("message");
                    if (message.equals("Canceled order successfully")) {
                        Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                        Toast.makeText(OrderedDetailActi.this, "Deleted Ordered Successfully!!!!", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(OrderedDetailActi.this, "Delete fail , Something wrong!!! ", Toast.LENGTH_SHORT).show();
                        Log.e("Delete Ordered ", " Delete fail");


                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}

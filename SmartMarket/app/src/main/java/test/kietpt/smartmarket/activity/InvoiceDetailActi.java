package test.kietpt.smartmarket.activity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import android.widget.ListView;



import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.adapter.OrderDetailAdapter;
import test.kietpt.smartmarket.model.OrderDetailDTO;

public class InvoiceDetailActi extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;

    OrderDetailAdapter orderDetailAdapter;

    ArrayList<OrderDetailDTO> listOrderDetail;
    int orderId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail);
        reflect();
        actionToolBar();

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
                Log.e("OrderDetail Invoice List ", response.toString());
                if (response.toString() != null) {

                    try {
                        for (int i = 0; i < response.length(); i++) {
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
                        }
                        orderDetailAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("reponse order detail invoice ",error.toString());
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void reflect() {
        toolbar = (Toolbar) findViewById(R.id.toolbarOrderDetailInvoice);
        listView = (ListView) findViewById(R.id.listViewInvoiceDetail);

        listOrderDetail = new ArrayList<>();
        orderDetailAdapter = new OrderDetailAdapter(InvoiceDetailActi.this, listOrderDetail);
        listView.setAdapter(orderDetailAdapter);
    }


}


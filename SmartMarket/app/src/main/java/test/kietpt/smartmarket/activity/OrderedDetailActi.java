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
    Button btnConfirm, btnDelete;
    OrderDetailAdapter orderDetailAdapter;
    static TextView txtTotalOrderDetail;
    static ArrayList<OrderDetailDTO> listOrderDetail;
    int orderId;
    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_detail);
        reflect();
        actionToolBar();
        //getTotalPriceInOrderDetail();
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
        getOrderDetail("http://" + IpConfig.ipConfig + ":8084/SSM_Project/GetOrderDetailCus?orderId=" + orderId);
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
                            int id = jsonObject.getInt("orderDetailId");
                            int orderId = jsonObject.getInt("orderId");
                            int productId = jsonObject.getInt("productId");
                            String name = jsonObject.getString("productName");
                            String productKey = jsonObject.getString("productKey");
                            float price = (float) jsonObject.getDouble("price");
                            Log.e("PPICE = ", price + "");
                            int quantity = jsonObject.getInt("quantity");
                            String status = jsonObject.getString("status");

                            String urlPic = jsonObject.getString("imgKey");
                            String urlTest = "http://" + IpConfig.ipConfig + ":8084/SSM_Project/img/" + urlPic;

                            listOrderDetail.add(new OrderDetailDTO(id, orderId, productId, productKey, name,
                                    quantity, price, status, urlTest));
                            getTotalPriceInOrderDetail();

                            orderDetailAdapter.notifyDataSetChanged();
                            flag = 1;
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
        btnConfirm = (Button) findViewById(R.id.btnConfirmOrderDetail);
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

    public void confirmOrdered(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Ordered Confirmed ", response.toString());
                if (response.toString().contains("/")) {
                    String[] errorQuantity = response.toString().split("/");
                    for (int i = 0; i < errorQuantity.length; i++) {
                        Toast.makeText(OrderedDetailActi.this, errorQuantity[i].toString(), Toast.LENGTH_LONG).show();
                    }
                } else if (response.equals("successfully")) {
                    Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                    startActivity(intent);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error ordered confirm ", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < listOrderDetail.size(); i++) {
                    Log.e("Testdasdasd", i + "");
                }
                for (int i = 0; i < listOrderDetail.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("orderDetailId", listOrderDetail.get(i).getId());
                        jsonObject.put("productId", listOrderDetail.get(i).getProductId());
                        jsonObject.put("productName", listOrderDetail.get(i).getProductName());
                        jsonObject.put("productPrice", listOrderDetail.get(i).getPrice());
                        jsonObject.put("productQuantity", listOrderDetail.get(i).getQuantity());
                        jsonObject.put("productKey", listOrderDetail.get(i).getProductKey());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("listConfirmOrdered", jsonArray.toString());
                params.put("orderId", String.valueOf(orderId));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void confirmOrdered(View view) {
        if (flag == 1) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm your ordered !!! ");
            builder.setMessage("Do you want to confirm your ordered ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    confirmOrdered("http://" + IpConfig.ipConfig + ":8084/SSM_Project/ConfirmOrderedCus");
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
    }

    public void deleteOrdered(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm your ordered !!! ");
        builder.setMessage("Do you want to confirm your ordered ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deletedOrder("http://" + IpConfig.ipConfig + ":8084/SSM_Project/DeleteOrderedCus");
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Deleted Ordered ", response.toString());
                if (response.toString().equals("successfully")) {
                    Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                    Toast.makeText(OrderedDetailActi.this, "Deleted Ordered Successfully!!!!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                } else {
                    Toast.makeText(OrderedDetailActi.this, "Delete fail , Something wrong!!! ", Toast.LENGTH_SHORT).show();
                    Log.e("Delete Ordered ", " Delete fail");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Delete Ordered ", error.getMessage());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < listOrderDetail.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("orderDetailId", listOrderDetail.get(i).getId());
                        jsonObject.put("productId", listOrderDetail.get(i).getProductId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(jsonObject);
                }
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("listDeleteOrdered", jsonArray.toString());
                params.put("orderId", String.valueOf(orderId));
                return params;

            }
        };
        requestQueue.add(stringRequest);
    }
}

package test.kietpt.smartmarket.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.adapter.CartConfirmAdapter;
import test.kietpt.smartmarket.model.OrderDTO;
import test.kietpt.smartmarket.model.OrderDetailDTO;
import test.kietpt.smartmarket.ulti.CheckConnection;
import test.kietpt.smartmarket.ulti.Database;
import test.kietpt.smartmarket.ulti.IpConfig;


public class ConfirmCartActi extends AppCompatActivity {

    ListView listProductInCart;
    TextView txtCartIsEmpty, emailConfirmCart, phoneConfirmCart;
    static TextView txtTotalPrice;
    Button btnConfirm, btnBackToHome;
    Toolbar toolbar;
    CartConfirmAdapter cartAdapterConfirm;
    EditText addressShip;
    Database database;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_cart);
        reflect();
        if (CheckConnection.haveNetworkConnection(this)) {
            showInforAccount();
            viewDetailProduct();
            checkCart();
            getDataInCart();
            confirmCart();
            backToHome();
        } else {
            CheckConnection.showConnection(this, "Please check your wifi!!!");
        }

    }

    private void viewDetailProduct() {
        listProductInCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent = new Intent(ProductListActi.this, ProductDetailActi.class);
//                intent.putExtra("ProductInfo", arrayList.get(i));
//                startActivity(intent);
                return false;
            }
        });
    }

    private void showInforAccount() {
        emailConfirmCart.setText(MainActivity.account.getEmail());
        phoneConfirmCart.setText(MainActivity.account.getPhone());
        addressShip.setText(MainActivity.account.getAddress());
    }

    private void backToHome() {
        btnBackToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public static void getDataInCart() {
        float totalOfPrice = 0;
        for (int i = 0; i < MainActivity.listCart.size(); i++) {
            totalOfPrice += MainActivity.listCart.get(i).getProductPrice();
        }
        Log.e("TOTLAPRICE ", totalOfPrice + "");
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTotalPrice.setText(decimalFormat.format(totalOfPrice) + " $ ");
    }

    private void checkCart() {
        if (MainActivity.listCart.size() <= 0) {
            cartAdapterConfirm.notifyDataSetChanged();
            txtCartIsEmpty.setVisibility(View.VISIBLE);
            listProductInCart.setVisibility(View.INVISIBLE);
        } else {
            cartAdapterConfirm.notifyDataSetChanged();
            txtCartIsEmpty.setVisibility(View.INVISIBLE);
            listProductInCart.setVisibility(View.VISIBLE);
        }
    }

    private void reflect() {

        listProductInCart = (ListView) findViewById(R.id.listViewCartItemConfirm);
        txtCartIsEmpty = (TextView) findViewById(R.id.txtCartIsEmptyConfirm);
        emailConfirmCart = (TextView) findViewById(R.id.emailConfirmCart);
        phoneConfirmCart = (TextView) findViewById(R.id.phoneConfirmCart);
        addressShip = (EditText) findViewById(R.id.addressConfirmCart);
        txtTotalPrice = (TextView) findViewById(R.id.txtTotalPriceConfirm);
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnBackToHome = (Button) findViewById(R.id.btnBackToHome);
        toolbar = (Toolbar) findViewById(R.id.toolbarCartConfirm);
        progressBar = (ProgressBar) findViewById(R.id.progressBarMyCartConfirm);
        database = new Database(this);
        cartAdapterConfirm = new CartConfirmAdapter(ConfirmCartActi.this, MainActivity.listCart);
        listProductInCart.setAdapter(cartAdapterConfirm);

    }

    public void confirmCart() {
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.listCart.size() <= 0) {
                    Toast.makeText(getApplicationContext(), "Please to buy something before confirm ", Toast.LENGTH_SHORT).show();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmCartActi.this);
                    builder.setTitle("Confirm cart!!!");
                    builder.setMessage("Do you want to confirm your cart?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            insertCart("https://ssm-market.herokuapp.com/api/v1/orders");

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
        });


    }

    private void insertCart(String url) {

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < MainActivity.listCart.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("product_id", MainActivity.listCart.get(i).getProductId());
                jsonObject.put("quantity", MainActivity.listCart.get(i).getProductQuantity());
                jsonObject.put("price", MainActivity.listCart.get(i).getPriceChekced());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }

        JSONObject jsonOrder = new JSONObject();
        JSONObject jsOrder = new JSONObject();
        try {
            jsonOrder.put("address_ship", addressShip.getText().toString());
            jsonOrder.put("account_id", MainActivity.account.getUserId());
            jsOrder.put("order", jsonOrder);
            jsOrder.put("order_product", jsonArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("Json request insert to cart ", jsOrder + "");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsOrder,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(final JSONObject response) {

                        Log.e("OrderCode and Price ", response.toString());

                        try {
//                            boolean checked = response.getBoolean("success");
//                            Log.e("Checked ", checked + "");
//                            if (!checked) {
//
//                            } else {
                                JSONObject jsOrder = response.getJSONObject("order");
                                int orderId = jsOrder.getInt("id");
                                final String orderCode = jsOrder.getString("code");
                                final float totalPrice = (float) jsOrder.getDouble("total_price");
                                String statusOrder = jsOrder.getString("status");
                                int totalQuantity = jsOrder.getInt("total_quantity");
                                int accountId = jsOrder.getInt("account_id");

                                Log.e("json reponse order ", orderId + " - " + " - " + orderCode + " - " + totalPrice + " - " + statusOrder +
                                        " - " + totalQuantity + " - " + accountId);

                                final Thread thread = new Thread() {
                                    @Override
                                    public void run() {
                                        try {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressBar.setVisibility(View.VISIBLE);
                                                }
                                            });
                                            synchronized (this) {
                                                wait(3000);

                                            }
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        database.deleteCart(MainActivity.listCart);
                                        MainActivity.listCart = null;
                                        Intent intent = new Intent(getApplicationContext(), OrderedNotiActi.class);
                                        intent.putExtra("OrderCodeAndPrice", orderCode+"-"+totalPrice);
                                        startActivity(intent);
                                    }
                                };
                                thread.start();
                            //}
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("fail insert order ", error.toString());
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Please click to Back to home to return ", Toast.LENGTH_SHORT).show();

    }
}

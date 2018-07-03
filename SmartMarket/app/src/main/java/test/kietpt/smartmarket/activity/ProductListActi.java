package test.kietpt.smartmarket.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

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
import test.kietpt.smartmarket.adapter.ProductListAdapter;
import test.kietpt.smartmarket.model.ProductDTO;
import test.kietpt.smartmarket.ulti.CheckConnection;
import test.kietpt.smartmarket.ulti.IpConfig;

public class ProductListActi extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    ProductListAdapter productListAdapter;
    public static ArrayList<ProductDTO> listProduct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        reflect();
        if (CheckConnection.haveNetworkConnection(this)) {
            getListProductById();
            backByToolBar();
            catchProductItem();
        } else {
            CheckConnection.showConnection(this, "Please check your wifi ");
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuHome:
                Intent intentHome = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intentHome);
                break;
//            case R.id.menuCart:
//                Intent intentCart = new Intent(getApplicationContext(),MyCartActi.class);
//                startActivity(intentCart);
//                break;
            case R.id.menuSearch:
                Intent intentSearch = new Intent(getApplicationContext(),SearchViewActi.class);
                startActivity(intentSearch);
                break;
            case R.id.menuAccount:
                if(MainActivity.account != null){
                    Intent intentAccount = new Intent(getApplicationContext(),AccountActivity.class);
                    startActivity(intentAccount);
                }else{
                    Intent intentAccount = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intentAccount);
                }
                break;
            case R.id.menuCall:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:01676243500"));
                startActivity(intent);
                break;
            case R.id.menuMessage:
                Intent intentasd = new Intent();
                intentasd.setAction(Intent.ACTION_SENDTO);
                intentasd.putExtra("sms_body","");
                intentasd.setData(Uri.parse("sms:01676243500"));
                startActivity(intentasd);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void catchProductItem() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ProductListActi.this, ProductDetailActi.class);
                Log.e("i == ",i+"");
                intent.putExtra("ProductInfo", listProduct.get(i));
                startActivity(intent);
            }
        });
    }

    private void backByToolBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getListProductById() {
        Intent intent = getIntent();
        String checked = "";
        checked = intent.getStringExtra("cateId");
        Log.e("CHECKED ",checked);
        String[] cate = checked.split("-");
        //int position = 0;
        //position = Integer.parseInt(checked1[0]);
        toolbar.setTitle(cate[1]);
        Log.e("CATEID PRODUCT LIST ", Integer.parseInt(cate[0]) + "");
        getData("http://" + IpConfig.ipConfig + ":8084/SSM_Project/GetProductListByCateId?txtCateId=" + Integer.parseInt(cate[0]));
    }


    private void getData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("RESPONSE PRODUCT LIST ", response.toString());
                if (response.toString() != null) {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int id = jsonObject.getInt("productId");
                            String name = jsonObject.getString("productName");
                            String des = jsonObject.getString("description");
                            String urlPic = jsonObject.getString("urlPic");
                            String key = jsonObject.getString("productKey");
                            int cateId = jsonObject.getInt("categoryId");
                            float price = (float) jsonObject.getDouble("price");
                            String manufacture = jsonObject.getString("manufacturer");
                            String manuDate = jsonObject.getString("manuDate");
                            String expiredDate = jsonObject.getString("expiredDate");
                            int quantity = jsonObject.getInt("quantity");
                            String urlTest = "http://"+IpConfig.ipConfig+":8084/SSM_Project/img/"+urlPic;

                            listProduct.add(new ProductDTO(name, des, urlTest, key, cateId, id, price,manufacture,manuDate,expiredDate,quantity));
                            productListAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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

    public void reflect() {
        toolbar = (Toolbar) findViewById(R.id.toolbarProductListItem);
        listView = (ListView) findViewById(R.id.listViewProductListItem);
        listProduct = new ArrayList<>();
        productListAdapter = new ProductListAdapter(this, listProduct);
        listView.setAdapter(productListAdapter);
    }
}

package test.kietpt.smartmarket.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
    ArrayList<ProductDTO> listProduct;
    EditText searchName;
    ArrayList<ProductDTO> listSearch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        reflect();

        if (CheckConnection.haveNetworkConnection(this)) {
            getListProductById();
            searchProductName();
            backByToolBar();
            catchProductItem();

        } else {
            CheckConnection.showConnection(this, "Please check your wifi ");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuHome:
                Intent intentHome = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentHome);
                break;
            case R.id.menuSearch:
                Intent intentSearch = new Intent(getApplicationContext(), SearchViewActi.class);
                startActivity(intentSearch);
                break;
            case R.id.menuAccount:
                if (MainActivity.account != null) {
                    Intent intentAccount = new Intent(getApplicationContext(), AccountActivity.class);
                    startActivity(intentAccount);
                } else {
                    Intent intentAccount = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intentAccount);
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void catchProductItem() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ProductListActi.this, ProductDetailActi.class);
                Log.e("i == ", i + "");
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
        Log.e("cateId = ", checked);
        String[] cate = checked.split("-");
        toolbar.setTitle(cate[1]);
        Log.e("category id product list ", Integer.parseInt(cate[0]) + "");
        getData("https://ssm-market.herokuapp.com/api/v1/list_products/" + Integer.parseInt(cate[0]));
    }

    private void getData(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("reponse json product", response + "");
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("products");
                            listProduct = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonProduct = jsonArray.getJSONObject(i);
                                int id = jsonProduct.getInt("id");
                                String name = jsonProduct.getString("name");
                                String des = jsonProduct.getString("description");
                                String urlPic = jsonProduct.getString("url");
                                String productKey = jsonProduct.getString("product_key");
                                int quantity = jsonProduct.getInt("quantity");
                                int cateId = jsonProduct.getInt("category_id");
                                String manufacture = jsonProduct.getString("manufacturer");
                                String manuDate = jsonProduct.getString("manu_date");
                                String expiredDate = jsonProduct.getString("expired_date");
                                float price = (float) jsonProduct.getDouble("price");
                                float priceChecked = (float) jsonProduct.getDouble("price");
                                String urlTest = "https://ssm-market.herokuapp.com" + urlPic;
                                listProduct.add(new ProductDTO(name, des, urlTest, productKey, cateId, id, price, manufacture, manuDate, expiredDate, quantity, priceChecked));
                            }
                            listSearch.clear();
                            listSearch.addAll(listProduct);
                            productListAdapter = new ProductListAdapter(getApplicationContext(), listProduct);
                            listView.setAdapter(productListAdapter);

                            productListAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error product list json ", error.toString());
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    public void searchProductName(){
        searchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.e("Start + before + count ", start + " - " + before + " - " + count);
                if (s.toString().equals("")) {
                    getListProductById();
                } else {
                    searchItemsProductName(s.toString().toLowerCase().trim());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void searchItemsProductName(String s) {
        listProduct.clear();
        for (int i = 0; i < listSearch.size(); i++) {
            if (listSearch.get(i).getProductName().toLowerCase().contains(s.toLowerCase()) || listSearch.get(i).getProductName()
                    .toUpperCase().contains(s.toUpperCase())){
                listProduct.add(listSearch.get(i));
            }
        }
        productListAdapter.notifyDataSetChanged();
    }

    public void reflect() {
        toolbar = (Toolbar) findViewById(R.id.toolbarProductListItem);
        searchName = (EditText) findViewById(R.id.txtSearchByProductName);
        listView = (ListView) findViewById(R.id.listViewProductListItem);
        listSearch = new ArrayList<>();
    }
}

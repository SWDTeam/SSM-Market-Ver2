package test.kietpt.smartmarket.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.adapter.CategoryAdapter;
import test.kietpt.smartmarket.adapter.HotProductAdapter;
import test.kietpt.smartmarket.model.Account;
import test.kietpt.smartmarket.model.Cart;
import test.kietpt.smartmarket.model.CategoryDTO;
import test.kietpt.smartmarket.model.ProductDTO;
import test.kietpt.smartmarket.ulti.CheckConnection;
import test.kietpt.smartmarket.ulti.Database;
import test.kietpt.smartmarket.ulti.IpConfig;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listViewMenu;
    DrawerLayout drawerLayout;
    List<CategoryDTO> listCategory;
    CategoryAdapter categoryAdapter;
    ArrayList<ProductDTO> listHotProduct;
    HotProductAdapter hotProductAdapter;
    ImageView imgCartMainActi;
    TextView txtCountMainActi;
    int id = 0;
    String cateName = "";
    String urlPic = "";
    public static List<Cart> listCart;
    public static Account account;
    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reflect();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            actionBar();
            actionViewFlipper();
            //getListCategory("http://" + IpConfig.ipConfig + ":8084/SSM_Project/GetListCategory?btnAction="+"view");
            getListCategory("https://ssm-market.herokuapp.com/api/v1/categories");
            //getListHotProduct("http://" + IpConfig.ipConfig + ":8084/SSM_Project/GetListHotProduct");
            getListHotProduct("https://ssm-market.herokuapp.com/api/v1/products");
            //catchOnMenuItem();
            getProductCount();
        } else {
            CheckConnection.showConnection(getApplicationContext(), "Check your connection with wifi");
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
                if (account != null) {
                    Intent intentAccount = new Intent(getApplicationContext(), AccountActivity.class);
                    startActivity(intentAccount);
                } else {
                    Intent intentAccount = new Intent(getApplicationContext(), LoginActivity.class);
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
                intentasd.putExtra("sms_body", "");
                intentasd.setData(Uri.parse("sms:01676243500"));
                startActivity(intentasd);
                break;
            case R.id.menuScan:
                Intent intentScan = new Intent(getApplicationContext(), ScanBarcode.class);
                startActivity(intentScan);

        }
        return super.onOptionsItemSelected(item);
    }

    private void catchOnMenuItem() {
        listViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    Intent intent = new Intent(MainActivity.this, ProductListActi.class);
                    intent.putExtra("cateId", listCategory.get(i).getCateId() + "-" + listCategory.get(i).getCateName());
                    startActivity(intent);
                }

            }
        });
    }

    private void getListHotProduct(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("reponse json product", response + "");
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = jsonObject.getJSONArray("products");
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
                            String urlTest = "https://ssm-market.herokuapp.com" + urlPic;

                            listHotProduct.add(new ProductDTO(name, des, urlTest, productKey, cateId, id, price, manufacture, manuDate, expiredDate, quantity));
                            hotProductAdapter.notifyDataSetChanged();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.e("reponse json product error", error.getMessage());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    private void getListCategory(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("reponse json category ", response + "");
                        try {
                            JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("categories");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonCategory = jsonArray.getJSONObject(i);
                                    id = jsonCategory.getInt("id");
                                    cateName = jsonCategory.getString("name");
                                    urlPic = jsonCategory.getString("url");
                                    Log.e("TEST123456",id+" - "+cateName+" - "+urlPic);
                                    String urlTest = "https://ssm-market.herokuapp.com"+urlPic;
                                    listCategory.add(new CategoryDTO(id, cateName, urlTest));
                                    categoryAdapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("CATELSIT ERROR + ", error.getMessage());
                    }
                });
        requestQueue.add(jsonArrayRequest);

    }

    private void actionViewFlipper() {
        ArrayList<String> listQuangcao = new ArrayList<>();
        listQuangcao.add("http://khanpakan.com/image/catalog/slider/banner-1.jpg");
        listQuangcao.add("http://www.discountmantra.in/wp-content/uploads/Come-Shop-At-The-Grocery-Sale-At-Amazon-From-15-18-March.jpg");
        listQuangcao.add("http://www.andersonsglenarbor.com/wp-content/uploads/2018/03/March-Madness-Canned-Food-Sale.jpg");
        listQuangcao.add("http://wildforwags.com/wp-content/uploads/2015/04/chobani-deal.jpg");
        for (int i = 0; i < listQuangcao.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Picasso.get().load(listQuangcao.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(5000);
        viewFlipper.setAutoStart(true);
        Animation animation_slide_in = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_in_right);
        Animation animation_slide_out = AnimationUtils.loadAnimation(MainActivity.this, R.anim.slide_out_right);
        viewFlipper.setInAnimation(animation_slide_in);
        viewFlipper.setOutAnimation(animation_slide_out);
    }

    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    public void getProductCount() {
        Log.e("count = ", database.getProductCount() + "");
        if (database.getProductCount() <= 0) {
            txtCountMainActi.setVisibility(View.INVISIBLE);
        } else {
            txtCountMainActi.setVisibility(View.VISIBLE);
            txtCountMainActi.setText(String.valueOf(database.getProductCount()));
        }
    }

    public void reflect() {
        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipperMain);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerMain);
        navigationView = (NavigationView) findViewById(R.id.navigaView);
        listViewMenu = (ListView) findViewById(R.id.listViewNavigation);
        imgCartMainActi = (ImageView) findViewById(R.id.imageViewCartMainActi);
        txtCountMainActi = (TextView) findViewById(R.id.txtCountMainActi);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        listCategory = new ArrayList<>();
        listCategory.add(0, new CategoryDTO(0, "Home", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4vmBj6G_kAJeoOsKTEF-woPgV7XLWn-6ydO5hqeqDiiH4wlq4"));
        //listCategory.add(1, new CategoryDTO(1, "cocaTest", "http://southeasternbeers.co.uk/291-thickbox_default/330ml-coke-icon.jpg"));
        //listCategory.add(2, new CategoryDTO(2, "7upTest", "http://muaban247.top/image/cache/catalog/7up-500x539.jpg"));
        //listCategory.add(3, new CategoryDTO(3, "milkTest", "https://image.freepik.com/free-icon/milk-box-package_318-50886.jpg"));
        categoryAdapter = new CategoryAdapter(MainActivity.this, listCategory);
        listViewMenu.setAdapter(categoryAdapter);

        listHotProduct = new ArrayList<>();
        hotProductAdapter = new HotProductAdapter(MainActivity.this, listHotProduct);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(hotProductAdapter);

        database = new Database(this);
        if (listCart != null) {
            listCart = database.getAllProductInCart();
        } else {
            listCart = new ArrayList<>();
        }

        imgCartMainActi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyCartActi.class);
                startActivity(intent);
            }
        });

    }
}

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
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.adapter.CategoryAdapter;
import test.kietpt.smartmarket.adapter.HotProductAdapter;
import test.kietpt.smartmarket.model.Account;
import test.kietpt.smartmarket.model.Cart;
import test.kietpt.smartmarket.model.CategoryDTO;
import test.kietpt.smartmarket.model.ProductDTO;
import test.kietpt.smartmarket.ulti.CheckConnection;
import test.kietpt.smartmarket.ulti.IpConfig;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView;
    NavigationView navigationView;
    ListView listViewMenu;
    DrawerLayout drawerLayout;
    ArrayList<CategoryDTO> listCategory;
    CategoryAdapter categoryAdapter;
    ArrayList<ProductDTO> listHotProduct;
    HotProductAdapter hotProductAdapter;

    int id = 0;
    String cateName = "";
    String urlPic = "";
    public static ArrayList<Cart> listCart;
    public static Account account;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reflect();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            actionBar();
            actionViewFlipper();
            getListCategory("http://" + IpConfig.ipConfig + ":8084/SSM_Project/GetListCategory?btnAction="+"view");
            getListHotProduct("http://" + IpConfig.ipConfig + ":8084/SSM_Project/GetListHotProduct");
            catchOnMenuItem();

        } else {
            CheckConnection.showConnection(getApplicationContext(), "Check your connection with wifi");
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
            case R.id.menuCart:
                Intent intentCart = new Intent(getApplicationContext(),MyCartActi.class);
                startActivity(intentCart);
                break;
            case R.id.menuSearch:
                Intent intentSearch = new Intent(getApplicationContext(),SearchViewActi.class);
                startActivity(intentSearch);
                break;
            case R.id.menuAccount:
                if(account != null){
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

    private void getListHotProduct(String s) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(s, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("REPOSNSE JSON PRODUCT", response.toString());
                if (response.toString() != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int id = jsonObject.getInt("productId");
                            String name = jsonObject.getString("productName");
                            String des = jsonObject.getString("description");
                            String urlPic = jsonObject.getString("urlPic");
                            String productKey = jsonObject.getString("productKey");
                            int quantity = jsonObject.getInt("quantity");
                            int cateId = jsonObject.getInt("categoryId");
                            String manufacture = jsonObject.getString("manufacturer");
                            String manuDate = jsonObject.getString("manuDate");
                            String expiredDate = jsonObject.getString("expiredDate");
                            float price = (float) jsonObject.getDouble("price");
                            String urlTest = "http://"+IpConfig.ipConfig+":8084/SSM_Project/img/"+urlPic;

                            listHotProduct.add(new ProductDTO(name, des, urlTest, productKey, cateId, id, price, manufacture, manuDate, expiredDate,quantity));
                            hotProductAdapter.notifyDataSetChanged();

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

    private void getListCategory(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("RESPONSE JSON CATE ", response.toString());
                if (response.toString() != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("cateId");
                            cateName = jsonObject.getString("cateName");
                            urlPic = jsonObject.getString("imgPic");
                            String urlTest = "http://"+IpConfig.ipConfig+":8084/SSM_Project/img/"+urlPic;
                            listCategory.add(new CategoryDTO(id, cateName, urlTest));
                            categoryAdapter.notifyDataSetChanged();
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
                        Log.e("CATELSIT ERROR + ", error.getMessage());
                    }
                });
        requestQueue.add(jsonArrayRequest);

    }

    private void actionViewFlipper() {
        ArrayList<String> listQuangcao = new ArrayList<>();
        listQuangcao.add("https://vn-live-02.slatic.net/original/b6d9d092090c60d1291e390a47a568d4.jpg");
        listQuangcao.add("https://vn-live-02.slatic.net/original/2df899a695fd0069dabead783c717fbd.jpg");
        listQuangcao.add("https://vn-live-02.slatic.net/original/fc2730140372e1342fe2b65fcce2b89e.jpg");
        listQuangcao.add("https://vn-test-11.slatic.net/p/7/dam-cat-han-kieu-moi-thoi-trang-tt037-9872-93033304-c2b553c3b6a84f204a34b1b2de0af080-catalog_233.jpg");
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

//    public void moveToScanBarcode(View view) {
//        Intent intent = new Intent(this, ScanBarcode.class);
//        startActivity(intent);
//    }

    public void reflect() {
        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipperMain);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerMain);
        navigationView = (NavigationView) findViewById(R.id.navigaView);
        listViewMenu = (ListView) findViewById(R.id.listViewNavigation);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        listCategory = new ArrayList<>();
        listCategory.add(0, new CategoryDTO(0, "Home", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4vmBj6G_kAJeoOsKTEF-woPgV7XLWn-6ydO5hqeqDiiH4wlq4"));
        categoryAdapter = new CategoryAdapter(MainActivity.this, listCategory);
        listViewMenu.setAdapter(categoryAdapter);

        listHotProduct = new ArrayList<>();
        hotProductAdapter = new HotProductAdapter(MainActivity.this, listHotProduct);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(hotProductAdapter);

        if (listCart != null) {

        } else {
            listCart = new ArrayList<>();
        }
//
    }
}

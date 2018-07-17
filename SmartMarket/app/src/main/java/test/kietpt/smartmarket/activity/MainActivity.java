package test.kietpt.smartmarket.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.darwindeveloper.horizontalscrollmenulibrary.custom_views.HorizontalScrollMenuView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.adapter.CategoryAdapter;
import test.kietpt.smartmarket.adapter.CheapProductAdapter;
import test.kietpt.smartmarket.adapter.HotProductAdapter;
import test.kietpt.smartmarket.model.Account;
import test.kietpt.smartmarket.model.Cart;
import test.kietpt.smartmarket.model.CategoryDTO;
import test.kietpt.smartmarket.model.ProductDTO;
import test.kietpt.smartmarket.ulti.CheckConnection;
import test.kietpt.smartmarket.ulti.Database;


public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recyclerHotProductMain,recyclerCheapProductMain;
    ListView listViewMenu;
    DrawerLayout drawerLayout;
    ArrayList<CategoryDTO> listCategory;
    CategoryAdapter categoryAdapter;
    ArrayList<ProductDTO> listHotProduct,listCheapProduct;
    HotProductAdapter hotProductAdapter;
    CheapProductAdapter cheapProductAdapter;
    TextView txtCountMainActi;
    int id = 0;
    String cateName = "";
    String urlPic = "";
    public static List<Cart> listCart;
    public static Account account;
    Database database;
    HorizontalScrollMenuView menu;
    ImageView imgScan,imgSearch;
    TextView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reflect();
        createMenu();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            actionBar();
            actionViewFlipper();

            getListCategory("https://ssm-market.herokuapp.com/api/v1/categories");
            getListHotProduct("https://ssm-market.herokuapp.com/api/v1/products");
            getListCheapProduct("https://ssm-market.herokuapp.com/api/v1/list_products_low_price");
            catchOnMenuItem();
            getProductCount();
            goToSearchView();
            goToScanQrcode();
        } else {
            CheckConnection.showConnection(getApplicationContext(), "Check your connection with wifi");
            finish();
        }
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

    private void getListCheapProduct(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("reponse json product", response + "");
                try {
                    //JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = response.getJSONArray("products");
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
                        Log.e("Price checked Main = ",priceChecked+"");
                        String urlTest = "https://ssm-market.herokuapp.com" + urlPic;

                        listCheapProduct.add(new ProductDTO(name, des, urlTest, productKey, cateId, id,
                                price, manufacture, manuDate, expiredDate, quantity,priceChecked));
                    }
                    cheapProductAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("reponse json product error", error.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }



    private void getListHotProduct(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,url,null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("reponse json product", response + "");
                try {
                    //JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray jsonArray = response.getJSONArray("products");
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
                            Log.e("Price checked Main = ",priceChecked+"");
                            String urlTest = "https://ssm-market.herokuapp.com" + urlPic;

                            listHotProduct.add(new ProductDTO(name, des, urlTest, productKey, cateId, id,
                                    price, manufacture, manuDate, expiredDate, quantity,priceChecked));
                    }
                    hotProductAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.e("reponse json product error", error.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public void getListCategory(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("reponse json category ", response + "");
                        try {

                            JSONArray jsonArray = response.getJSONArray("categories");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonCategory = jsonArray.getJSONObject(i);
                                    id = jsonCategory.getInt("id");
                                    cateName = jsonCategory.getString("name");
                                    urlPic = jsonCategory.getString("url");

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
        toolbar.setNavigationIcon(R.drawable.menuicon);
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
        menu = (HorizontalScrollMenuView) findViewById(R.id.menuMainActivity);
        imgScan = (ImageView)findViewById(R.id.imgScanQrcode);
        searchView = (TextView) findViewById(R.id.textSearch);
        imgSearch = (ImageView) findViewById(R.id.imgSearch);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipperMain);
        recyclerHotProductMain = (RecyclerView) findViewById(R.id.recyclerHotProductMain);
        recyclerCheapProductMain = (RecyclerView) findViewById(R.id.recyclerCheapProductMain);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listViewMenu = (ListView) findViewById(R.id.listViewNavigation);
        txtCountMainActi = (TextView) findViewById(R.id.txtCountMainActi);


        //get list category
        listCategory = new ArrayList<>();
        listCategory.add(0, new CategoryDTO(0, "Home",String.valueOf(R.drawable.menuhome)));
        categoryAdapter = new CategoryAdapter(MainActivity.this, listCategory);
        listViewMenu.setAdapter(categoryAdapter);


        //get list cheap product
        listCheapProduct = new ArrayList<>();
        cheapProductAdapter = new CheapProductAdapter(MainActivity.this, listCheapProduct);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        recyclerCheapProductMain.setLayoutManager(linearLayoutManager);
        recyclerCheapProductMain.setAdapter(cheapProductAdapter);

        // get list new product
        listHotProduct = new ArrayList<>();
        hotProductAdapter = new HotProductAdapter(MainActivity.this, listHotProduct);
        recyclerHotProductMain.setHasFixedSize(true);
        recyclerHotProductMain.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerHotProductMain.setAdapter(hotProductAdapter);

        database = new Database(this);
        if (listCart != null) {
            listCart = database.getAllProductInCart();
        } else {
            listCart = new ArrayList<>();
        }

    }
    private void goToSearchView(){
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch = new Intent(getApplicationContext(), SearchViewActi.class);
                startActivity(intentSearch);
            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSearch = new Intent(getApplicationContext(), SearchViewActi.class);
                startActivity(intentSearch);
            }
        });
    }
    private void goToScanQrcode(){
        imgScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentScan = new Intent(getApplicationContext(), ScanBarcode.class);
                startActivity(intentScan);
            }
        });
    }
    private void createMenu(){
        menu.addItem("Home",R.drawable.ic_home);
        menu.addItem("Category",R.drawable.ic_category);
        menu.addItem("Cart",R.drawable.ic_shoppingcart);
        menu.addItem("Account",R.drawable.ic_account);
        menu.setOnHSMenuClickListener(new HorizontalScrollMenuView.OnHSMenuClickListener() {
            @Override
            public void onHSMClick(com.darwindeveloper.horizontalscrollmenulibrary.extras.MenuItem menuItem, int position) {


                switch (position){
                    case 0:
                        break;
                    case 1:
                        drawerLayout.openDrawer(GravityCompat.START);
                        break;
                    case 2:
                        Intent intent = new Intent(getApplicationContext(), MyCartActi.class);
                        startActivity(intent);
                        break;
                    case 3:
                        if (account != null) {
                            Intent intentAccount = new Intent(getApplicationContext(), AccountActivity.class);
                            startActivity(intentAccount);
                        } else {
                            Intent intentAccount = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intentAccount);
                        }
                        break;
                    case 4:
                        default:
                }

            }
        });
    }
}

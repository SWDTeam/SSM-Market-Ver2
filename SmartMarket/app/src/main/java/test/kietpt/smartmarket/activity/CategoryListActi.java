package test.kietpt.smartmarket.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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
import test.kietpt.smartmarket.adapter.CategoryAdapter;
import test.kietpt.smartmarket.model.CategoryDTO;
import test.kietpt.smartmarket.ulti.CheckConnection;
import test.kietpt.smartmarket.ulti.Database;
import test.kietpt.smartmarket.ulti.IpConfig;

public class CategoryListActi extends AppCompatActivity {

    ListView listView;
    Toolbar toolbar;
    ArrayList<CategoryDTO> arrayList;
    CategoryAdapter adapter;
    ImageView imgCart;
    TextView txtCount;
    Database database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        reflect();
        if (CheckConnection.haveNetworkConnection(this)) {
            getDataCategory();
            catchListViewCate();
            actionToolbar();
            getProductCount();
        } else {
            CheckConnection.showConnection(this, "Please check your wifi!!");
            finish();
        }
    }

    private void actionToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
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

    private void catchListViewCate() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ProductListActi.class);
                intent.putExtra("cateId", arrayList.get(i).getCateId() + "-" + arrayList.get(i).getCateName());
                startActivity(intent);
            }
        });
    }

    public void getDataCategory() {
        Intent intent = getIntent();
        String txtSearch = intent.getStringExtra("txtSearchView");
        if(txtSearch.equals("1")){
            getListCateBySearchName("https://ssm-market.herokuapp.com/api/v1/categories");
        }else {
            Log.e("txtSearch = ", txtSearch);
            getListCateBySearchName("https://ssm-market.herokuapp.com/api/v1/search_category_name/" + txtSearch);
        }
    }

    private void getListCateBySearchName(String s) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, s, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("reponse json category ", response + "");

                        // dùng để kiểm tra chuỗi json trả về có header là error hay ko, nếu có thì Toast ra
//                        try {
//                            String error = response.getString("errors");
//                            if (error.equals("Not found")) {
//                                Toast.makeText(CategoryListActi.this, "Not found", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }

                        // dùng để get value từ chuỗi json trả về

                        try {
                            //JSONObject jsonObject = new JSONObject(response.toString());
                            JSONArray jsonArray = response.getJSONArray("categories");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonCategory = jsonArray.getJSONObject(i);
                                    int id = jsonCategory.getInt("id");
                                    String cateName = jsonCategory.getString("name");
                                    String urlPic = jsonCategory.getString("url");
                                    String urlTest = "https://ssm-market.herokuapp.com" + urlPic;
                                    Log.e("json object category ", id + " - " + cateName + " - " + urlPic);
                                    arrayList.add(new CategoryDTO(id, cateName, urlTest));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error category list reponse ",error.toString());
                        Toast.makeText(CategoryListActi.this, "Not found", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
    public void getProductCount() {
        Log.e("count = ", database.getProductCount() + "");
        if (database.getProductCount() <= 0) {
            txtCount.setVisibility(View.INVISIBLE);
        } else {
            txtCount.setVisibility(View.VISIBLE);
            txtCount.setText(String.valueOf(database.getProductCount()));
        }
    }
    private void reflect() {
        listView = (ListView) findViewById(R.id.listViewCateList);
        toolbar = (Toolbar) findViewById(R.id.toolbarCateList);
        arrayList = new ArrayList<>();
        adapter = new CategoryAdapter(this, arrayList);
        listView.setAdapter(adapter);
        database = new Database(this);

        imgCart = (ImageView)findViewById(R.id.imageViewCartCategoryList);
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyCartActi.class);
                startActivity(intent);
            }
        });
        txtCount = (TextView)findViewById(R.id.txtCountCategoryListActi);
    }

}

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
import android.widget.ListView;

import com.android.volley.Request;
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
import test.kietpt.smartmarket.adapter.CategoryAdapter;
import test.kietpt.smartmarket.model.CategoryDTO;
import test.kietpt.smartmarket.ulti.CheckConnection;
import test.kietpt.smartmarket.ulti.IpConfig;

public class CategoryListActi extends AppCompatActivity {

    ListView listView;
    Toolbar toolbar;
    ArrayList<CategoryDTO> arrayList;
    CategoryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);
        reflect();
        if(CheckConnection.haveNetworkConnection(this)){
            getDataCategory();
            catchListViewCate();
            actionToolbar();
        }else{
           CheckConnection.showConnection(this,"Please check your wifi!!");
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

    private void getDataCategory() {
        Intent intent = getIntent();
        String txtSearch = intent.getStringExtra("txtSearchView");
        getListCateName("http://" + IpConfig.ipConfig + ":8084/SSM_Project/GetListCategory?btnAction=searchName&txtSearch="+txtSearch);
    }

    private void getListCateName(String s) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, s, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("GETCATELISTBYNAME ",response.toString());
                        if(response.toString() != null){
                            for(int i = 0; i< response.length(); i++){
                                try {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    int id = jsonObject.getInt("cateId");
                                    String name = jsonObject.getString("cateName");
                                    String urlPic = jsonObject.getString("imgPic");
                                    String urlTest = "http://"+IpConfig.ipConfig+":8084/SSM_Project/img/"+urlPic;

                                    CategoryDTO dto = new CategoryDTO();
                                    dto.setCateId(id);
                                    dto.setCateName(name);
                                    dto.setImgPic(urlTest);
                                    arrayList.add(dto);
                                    adapter.notifyDataSetChanged();
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
        listView = (ListView)findViewById(R.id.listViewCateList);
        toolbar = (Toolbar)findViewById(R.id.toolbarCateList);
        arrayList = new ArrayList<>();
        adapter = new CategoryAdapter(this,arrayList);
        listView.setAdapter(adapter);
    }

}

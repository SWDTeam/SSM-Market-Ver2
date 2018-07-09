package test.kietpt.smartmarket.activity;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.adapter.MyOrderedAdapter;
import test.kietpt.smartmarket.model.OrderDTO;
import test.kietpt.smartmarket.ulti.IpConfig;

public class MyOrderedActi extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    MyOrderedAdapter orderedAdapter;
    ArrayList<OrderDTO> listOrderDto;
    TextView orderedIsEmpty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ordered);
        reflect();
        actionBar();
        getListOrdered("https://ssm-market.herokuapp.com/api/v1/lists_orders/" + MainActivity.account.getUserId());
        catchListOrdered();
    }

    private void catchListOrdered() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),OrderedDetailActi.class);
                Toast.makeText(MyOrderedActi.this, "orderId = "+listOrderDto.get(i).getOrderId(), Toast.LENGTH_SHORT).show();
                intent.putExtra("orderId",listOrderDto.get(i).getOrderId());
                startActivity(intent);
            }
        });
    }

    private void getListOrdered(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("My Ordered :", response.toString());

                if (response.toString() != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int orderId = jsonObject.getInt("id");
                            String orderCode = jsonObject.getString("code");
                            String date = jsonObject.getString("created_at");
                            int quantity = jsonObject.getInt("total_quantity");
                            float price = (float) jsonObject.getDouble("total_price");
                            String status = jsonObject.getString("status");
                            Toast.makeText(MyOrderedActi.this, orderId+" - "+orderCode+" - "+date+" - "+
                                    quantity+" - "+price+" - "+status, Toast.LENGTH_SHORT).show();

                            listOrderDto.add(new OrderDTO(orderId, orderCode, price, quantity, date,status));
                            orderedAdapter.notifyDataSetChanged();


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
    private void actionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void reflect() {
        toolbar = (Toolbar) findViewById(R.id.toolbarMyOrdered);
        listView = (ListView) findViewById(R.id.listviewOrdered);
        orderedIsEmpty = (TextView)findViewById(R.id.txtOrderedIsEmpty);
        listOrderDto = new ArrayList<>();
        orderedAdapter = new MyOrderedAdapter(MyOrderedActi.this, listOrderDto);
        listView.setAdapter(orderedAdapter);
    }

}

package test.kietpt.smartmarket.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.adapter.MyOrderedAdapter;
import test.kietpt.smartmarket.model.OrderDTO;
import test.kietpt.smartmarket.ulti.IpConfig;

public class MyCanceledOrdersActi extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    MyOrderedAdapter orderedAdapter;
    ArrayList<OrderDTO> listOrderDto;
    TextView orderedIsEmpty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_canceled_order);
        reflect();
        actionBar();
        getListCanceledOrdered("http://" + IpConfig.ipConfig + ":8084/SSM_Project/GetListCanceledOrderCus?userId=" + MainActivity.account.getUserId());
    }
    private void getListCanceledOrdered(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("My Ordered :", response.toString());
                if(response.toString().equals("[]")){
                    orderedIsEmpty.setVisibility(View.VISIBLE);
                }
                if (response.toString() != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int orderId = jsonObject.getInt("orderId");
                            String orderCode = jsonObject.getString("orderCode");
                            String date = jsonObject.getString("startTime");
                            int quantity = jsonObject.getInt("totalQuantity");
                            float price = (float) jsonObject.getDouble("totalPrice");
                            String status = jsonObject.getString("status");
                            Toast.makeText(MyCanceledOrdersActi.this, orderId+" - "+orderCode+" - "+date+" - "+
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
        toolbar = (Toolbar) findViewById(R.id.toolbarMyCanceledOrder);
        listView = (ListView) findViewById(R.id.listviewCanceledOrdered);
        orderedIsEmpty = (TextView)findViewById(R.id.txtOrderedIsEmptyCanceled);
        listOrderDto = new ArrayList<>();
        orderedAdapter = new MyOrderedAdapter(MyCanceledOrdersActi.this, listOrderDto);
        listView.setAdapter(orderedAdapter);
    }
}

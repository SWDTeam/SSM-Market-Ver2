package test.kietpt.smartmarket.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import test.kietpt.smartmarket.adapter.MyOrderedAdapter;
import test.kietpt.smartmarket.model.OrderDTO;

public class MyInvoiceActi extends AppCompatActivity {

    Toolbar toolbar;
    ListView listView;
    MyOrderedAdapter orderedAdapter;
    ArrayList<OrderDTO> listOrderDto;
    TextView invoiceIsEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invoice);
        reflect();
        actionBar();

        getListOrdered("https://ssm-market.herokuapp.com/api/v1/lists_orders/" + MainActivity.account.getUserId() + "/payment");
        catchListOrdered();

    }

    private void catchListOrdered() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), InvoiceDetailActi.class);
                intent.putExtra("orderId", listOrderDto.get(i).getOrderId());
                startActivity(intent);
            }
        });
    }

    private void getListOrdered(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("My Invoice :", response.toString());

                        if (response.toString() != null) {

                                try {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject jsonObject = response.getJSONObject(i);
                                        int orderId = jsonObject.getInt("id");
                                        String orderCode = jsonObject.getString("code");
                                        String date = jsonObject.getString("created_at");
                                        int quantity = jsonObject.getInt("total_quantity");
                                        float price = (float) jsonObject.getDouble("total_price");
                                        String status = jsonObject.getString("status");


                                        listOrderDto.add(new OrderDTO(orderId, orderCode, price, quantity, date, status));

                                    }
                                    showMessageIsEmpty();
                                    orderedAdapter.notifyDataSetChanged();
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
    private void showMessageIsEmpty(){
        if(listOrderDto.size() <= 0){
            orderedAdapter.notifyDataSetChanged();
            invoiceIsEmpty.setVisibility(View.VISIBLE);
            listView.setVisibility(View.INVISIBLE);

        }else{
            orderedAdapter.notifyDataSetChanged();
            invoiceIsEmpty.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);

        }
    }

    private void reflect() {
        toolbar = (Toolbar) findViewById(R.id.toolbarMyInvoice);
        listView = (ListView) findViewById(R.id.listviewInvoice);
        invoiceIsEmpty = (TextView) findViewById(R.id.txtInvoiceIsEmpty);
        listOrderDto = new ArrayList<>();
        orderedAdapter = new MyOrderedAdapter(MyInvoiceActi.this, listOrderDto);
        listView.setAdapter(orderedAdapter);
    }

}



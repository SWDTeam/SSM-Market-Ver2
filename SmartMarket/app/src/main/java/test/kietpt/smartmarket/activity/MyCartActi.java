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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.adapter.CartAdapter;
import test.kietpt.smartmarket.model.Account;
import test.kietpt.smartmarket.model.ProductDTO;
import test.kietpt.smartmarket.ulti.Database;

public class MyCartActi extends AppCompatActivity {


    ListView listProductInCart;
    TextView txtCartIsEmpty;
    static TextView txtTotal;
    Button btnBuy, btnContinue;
    Toolbar toolbar;
    Database database;
    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        reflect();
        actionToolBar();
        checkCart();
        getDataInCart();
        continueToShop();
    }

    private void continueToShop() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }



    public static void getDataInCart() {
        float totalOfPrice = 0;
        for (int i = 0; i < MainActivity.listCart.size(); i++) {
            totalOfPrice += MainActivity.listCart.get(i).getProductPrice();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTotal.setText(decimalFormat.format(totalOfPrice)+" $ ");
    }

    private void checkCart() {
        if (MainActivity.listCart.size() <= 0) {
            cartAdapter.notifyDataSetChanged();
            txtCartIsEmpty.setVisibility(View.VISIBLE);
            listProductInCart.setVisibility(View.INVISIBLE);
        } else {
            cartAdapter.notifyDataSetChanged();
            txtCartIsEmpty.setVisibility(View.INVISIBLE);
            listProductInCart.setVisibility(View.VISIBLE);
        }
    }

    private void actionToolBar() {
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
        listProductInCart = (ListView) findViewById(R.id.listViewCartItem);
        txtCartIsEmpty = (TextView) findViewById(R.id.txtCartIsEmpty);
        txtTotal = (TextView) findViewById(R.id.txtTotalPrice);
        btnBuy = (Button) findViewById(R.id.btnBuy);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        toolbar = (Toolbar) findViewById(R.id.toolbarCart);
        cartAdapter = new CartAdapter(MyCartActi.this, MainActivity.listCart);
        listProductInCart.setAdapter(cartAdapter);
        database = new Database(this);
    }

    public void buyProduct(View view) {
            if(MainActivity.account == null ){
                MainActivity.account = new Account();
                Toast.makeText(MyCartActi.this, "da vao day ", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }else if(database.checkEmail(MainActivity.account.getEmail())){
                Intent intent = new Intent(getApplicationContext(),ConfirmCartActi.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
    }

}

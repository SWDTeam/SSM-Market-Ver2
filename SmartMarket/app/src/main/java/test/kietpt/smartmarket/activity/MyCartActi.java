package test.kietpt.smartmarket.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.darwindeveloper.horizontalscrollmenulibrary.custom_views.HorizontalScrollMenuView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.adapter.CartAdapter;
import test.kietpt.smartmarket.model.Account;
import test.kietpt.smartmarket.model.Cart;
import test.kietpt.smartmarket.model.ProductDTO;
import test.kietpt.smartmarket.ulti.CheckConnection;
import test.kietpt.smartmarket.ulti.Database;

public class MyCartActi extends AppCompatActivity {


    ListView listProductInCart;
    public static TextView txtCartIsEmpty,txtCountMyCartActi;
    static TextView txtTotal;
    Button btnBuy;
    Toolbar toolbar;
    Database database;
    CartAdapter cartAdapter;
    ProgressBar progressBar;
    HorizontalScrollMenuView menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        reflect();
        createMenu();
        if(CheckConnection.haveNetworkConnection(this)){
            actionToolBar();
            checkCart();
            getDataInCart();
            buyProduct();
            getProductCount();
        }else{
            CheckConnection.showConnection(this,"Please check you wifi!!!");
        }


    }
    public static void getDataInCart() {
        float totalOfPrice = 0;
        for (int i = 0; i < MainActivity.listCart.size(); i++) {
            totalOfPrice += MainActivity.listCart.get(i).getProductPrice();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        txtTotal.setText(decimalFormat.format(totalOfPrice) + " $ ");
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
        menu = (HorizontalScrollMenuView)findViewById(R.id.menuMyCartActi);
        txtCountMyCartActi = (TextView)findViewById(R.id.txtCountMyCartActi);
        txtTotal = (TextView) findViewById(R.id.txtTotalPrice);
        btnBuy = (Button) findViewById(R.id.btnBuy);

        toolbar = (Toolbar) findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBarMyCart);

        database = new Database(this);
        MainActivity.listCart = database.getAllProductInCart();
        cartAdapter = new CartAdapter(MyCartActi.this, MainActivity.listCart);
        listProductInCart.setAdapter(cartAdapter);

    }
    public void getProductCount() {
        Log.e("count = ", database.getProductCount() + "");
        if (database.getProductCount() <= 0) {
            txtCountMyCartActi.setVisibility(View.INVISIBLE);
        } else {
            txtCountMyCartActi.setVisibility(View.VISIBLE);
            txtCountMyCartActi.setText(String.valueOf(database.getProductCount()));
        }
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
                        Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intentMain);
                        break;
                    case 1:
                        Intent intentCategory = new Intent(getApplicationContext(), CategoryListActi.class);
                        intentCategory.putExtra("txtSearchView","1");
                        startActivity(intentCategory);
                        break;
                    case 2:

                        break;
                    case 3:
                        if (MainActivity.account != null) {
                            Intent intentAccount = new Intent(getApplicationContext(), AccountActivity.class);
                            startActivity(intentAccount);
                        } else {
                            Intent intentAccount = new Intent(getApplicationContext(), LoginActivity.class);
                            startActivity(intentAccount);
                        }
                    break;

                }

            }
        });
    }

    public void buyProduct() {
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MainActivity.listCart.size() == 0){
                    Toast.makeText(getApplicationContext(), "Please buy some thing!!!", Toast.LENGTH_SHORT).show();
                }
                if (MainActivity.account == null) {
                    MainActivity.account = new Account();
                    Toast.makeText(MyCartActi.this, "da vao day ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else if (database.checkEmail(MainActivity.account.getEmail())) {
                    final Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (MainActivity.listCart.size() > 0) {
                                            progressBar.setVisibility(View.VISIBLE);
                                        } else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
                                synchronized (this) {
                                    wait(3000);
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(getApplicationContext(), ConfirmCartActi.class);
                            startActivity(intent);
                            finish();
                        }
                    };
                    thread.start();

                } else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

}

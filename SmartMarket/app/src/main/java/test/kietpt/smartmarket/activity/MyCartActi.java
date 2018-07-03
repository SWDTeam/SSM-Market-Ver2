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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView txtCartIsEmpty;
    static TextView txtTotal;
    Button btnBuy, btnContinue;
    Toolbar toolbar;
    Database database;
    CartAdapter cartAdapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        reflect();
        if(CheckConnection.haveNetworkConnection(this)){
            //actionToolBar();
            checkCart();
            getDataInCart();
            buyProduct();
            continueToShop();
        }else{
            CheckConnection.showConnection(this,"Please check you wifi!!!");
        }


    }

    private void continueToShop() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
        txtTotal = (TextView) findViewById(R.id.txtTotalPrice);
        btnBuy = (Button) findViewById(R.id.btnBuy);
        btnContinue = (Button) findViewById(R.id.btnContinue);
        toolbar = (Toolbar) findViewById(R.id.toolbarCart);
        setSupportActionBar(toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBarMyCart);

        database = new Database(this);
        MainActivity.listCart = database.getAllProductInCart();
        cartAdapter = new CartAdapter(MyCartActi.this, MainActivity.listCart);
        listProductInCart.setAdapter(cartAdapter);

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

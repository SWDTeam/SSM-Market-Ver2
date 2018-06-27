package test.kietpt.smartmarket.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.model.Cart;
import test.kietpt.smartmarket.model.ProductDTO;
import test.kietpt.smartmarket.ulti.CheckConnection;
import test.kietpt.smartmarket.ulti.Database;

public class ProductDetailActi extends AppCompatActivity {

    Toolbar toolbarDetail;
    ImageView imgDetail;
    TextView txtNameDetail,txtPriceDetail,txtManuDetail,txtManuTimeDetail,txtExpiredDetail,txtDesDetail;
    Spinner spinner;
    Button btnAddToCart;
    String selected="";

    int productId = 0;
    String productName = "";
    String productKey = "";
    String urlPic = "";
    float price;
    String manu="";
    String manuTime="";
    String expiredTime="";
    String des = "";
    int quantityTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        reflect();
        if(CheckConnection.haveNetworkConnection(getApplicationContext())){
            showProductDetail();
            actionToolbar();
            catchSpinner();


        }else{
            CheckConnection.showConnection(getApplicationContext(),"Plesae check you wifi");
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

    public void reflect(){
        toolbarDetail = (Toolbar)findViewById(R.id.productDetailToolbar);
        imgDetail =(ImageView)findViewById(R.id.imgViewProductDetail);
        txtNameDetail = (TextView)findViewById(R.id.txtNameProductDetail);
        txtPriceDetail = (TextView)findViewById(R.id.txtPriceProductDetail);
        txtManuDetail = (TextView)findViewById(R.id.txtManuProductDetail);
        txtManuTimeDetail = (TextView)findViewById(R.id.txtManuTimeProductDetail);
        txtExpiredDetail = (TextView)findViewById(R.id.txtExpiredProductDetail);
        txtDesDetail = (TextView)findViewById(R.id.txtDesProductDetail);
        spinner = (Spinner)findViewById(R.id.spinnerQuantity);
        btnAddToCart =(Button)findViewById(R.id.btnAddToCartProductDetail);


    }

    public void showProductDetail(){
        Intent intent = getIntent();
        ProductDTO dto = (ProductDTO) intent.getSerializableExtra("ProductInfo");
        productId = dto.getProductId();
        productName = dto.getProductName();
        Log.e("TEST1234",dto.getProductId()+" - "+dto.getProductName());
        productKey = dto.getProductKey();
        urlPic = dto.getUrlPic();
        price = dto.getPrice();
        manu = dto.getManufacturer();
        manuTime = dto.getManuDate();
        expiredTime = dto.getExpiredDate();
        des = dto.getDescription();
        quantityTemp = dto.getQuantity();

        toolbarDetail.setTitle(productName);
        txtNameDetail.setText(productName);
        DecimalFormat format = new DecimalFormat("###,###,###");
        txtPriceDetail.setText("Price: "+format.format(price)+" $ ");
        Picasso.get().load(urlPic).placeholder(R.drawable.error).error(R.drawable.errors).into(imgDetail);
        txtManuDetail.setText(manu);
        txtManuTimeDetail.setText(manuTime);
        txtExpiredDetail.setText(expiredTime);
        txtDesDetail.setText(Html.fromHtml(des,Html.FROM_HTML_MODE_COMPACT));

    }

    private void catchSpinner() {
        Integer[] quantity = new Integer[]{1,2,3,4,5};
        ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item,quantity);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected = parent.getItemAtPosition(position).toString();
                Log.e("SELECTED ",selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void actionToolbar() {
        setSupportActionBar(toolbarDetail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarDetail.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    public void addToCart(View view) {
        if(MainActivity.listCart.size() > 0){
            int quan = Integer.parseInt(selected);
            boolean exist = false;
            for(int i = 0; i<MainActivity.listCart.size(); i++){
                if(MainActivity.listCart.get(i).getProductId() == productId){
                    MainActivity.listCart.get(i).setProductQuantity(MainActivity.listCart.get(i).getProductQuantity()+quan);
                    MainActivity.listCart.get(i).setProductPrice(price*MainActivity.listCart.get(i).getProductQuantity());
                    exist = true;
                }
            }
            if(exist == false){
                int quantity = Integer.parseInt(selected);
                float priceNew = quantity * price;
                MainActivity.listCart.add(new Cart(productId,productName,productKey,urlPic,priceNew,quantity));
            }
        }else{
            int quantity = Integer.parseInt(selected);
            float priceNew = quantity * price;
            MainActivity.listCart.add(new Cart(productId,productName,productKey,urlPic,priceNew,quantity));
        }
        Intent intent = new Intent(ProductDetailActi.this,MyCartActi.class);
        startActivity(intent);
    }
}

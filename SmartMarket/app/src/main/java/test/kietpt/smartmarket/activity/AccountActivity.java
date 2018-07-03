package test.kietpt.smartmarket.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.model.OrderDetailDTO;
import test.kietpt.smartmarket.ulti.CheckConnection;
import test.kietpt.smartmarket.ulti.Database;

public class AccountActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayList;
    TextView textViewUsername,txtCount;
    ImageView imgPicAccount,imgCart;

    Database database;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        reflect();
        if(CheckConnection.haveNetworkConnection(this)){
            getDataInfo();
            getProductCount();
        }else{
            CheckConnection.showConnection(this,"Please check your wifi!!");
        }
    }

    private void getDataInfo() {
        try{
            textViewUsername.setText(MainActivity.account.getUsername());
            if (MainActivity.account.getGender().equals("male")) {
                imgPicAccount.setBackground(getDrawable(R.drawable.maleicon));
            }
            arrayList = new ArrayList<String>();
            arrayList.add("My Profile");
            arrayList.add("My Ordered");
            arrayList.add("My Canceled Orders");
            arrayList.add("Change Password");
            arrayList.add("Logout");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AccountActivity.this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(arrayAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    if (i == 0) {
                        Log.e("IIIIII  ", i + " - - - --");
                        Intent intent = new Intent(AccountActivity.this, ProfileActi.class);
                        startActivity(intent);
                    } else if (i == 1) {
                        Log.e("IIIIII  ", i + " - - - --");

                        Intent intent = new Intent(AccountActivity.this, MyOrderedActi.class);
                        startActivity(intent);
                    } else if (i == 2) {
                        Log.e("IIIIII  ", i + " - - - --");

                        Intent intent = new Intent(AccountActivity.this, MyCanceledOrdersActi.class);
                        startActivity(intent);
                    } else if (i == 3) {
                        Log.e("IIIIII  ", i + " - - - --");
                        Intent intent = new Intent(AccountActivity.this, ChangePassActi.class);
                        startActivity(intent);
                    } else {
                        confirmLogout();
                    }
                }
            });
        }catch (Exception e){
            Log.e("TEST123456789",e.getMessage());
        }
    }
    private void confirmLogout(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Logout");
        dialog.setMessage("Do you want to logout ? ");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.deleteCustomer(MainActivity.account.getEmail());
                MainActivity.account = null;
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        dialog.show();
    }

    private void reflect() {
        listView = (ListView) findViewById(R.id.listViewAccountActi);
        textViewUsername = (TextView) findViewById(R.id.txtUsernameAccount);
        imgPicAccount = (ImageView) findViewById(R.id.imgPicAccount);
        toolbar = (Toolbar)findViewById(R.id.toolbarAccount);
        setSupportActionBar(toolbar);
        imgCart = (ImageView)findViewById(R.id.imageViewCartAccount);
        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MyCartActi.class);
                startActivity(intent);
            }
        });
        txtCount = (TextView)findViewById(R.id.txtCountAccount);
        database = new Database(this);
    }

    public void getProductCount() {
        Log.e("count = ",database.getProductCount()+"");
        if(database.getProductCount() <= 0){
            txtCount.setVisibility(View.INVISIBLE);
        } else {
            txtCount.setVisibility(View.VISIBLE);
            txtCount.setText(String.valueOf(database.getProductCount()));
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

}

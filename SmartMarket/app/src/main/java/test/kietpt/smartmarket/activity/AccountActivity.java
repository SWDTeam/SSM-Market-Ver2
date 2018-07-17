package test.kietpt.smartmarket.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.GravityCompat;
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
import android.widget.Toast;

import com.darwindeveloper.horizontalscrollmenulibrary.custom_views.HorizontalScrollMenuView;

import java.util.ArrayList;
import java.util.List;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.adapter.AccountAdapter;
import test.kietpt.smartmarket.model.AccountMenuItemDTO;
import test.kietpt.smartmarket.model.OrderDetailDTO;
import test.kietpt.smartmarket.ulti.CheckConnection;
import test.kietpt.smartmarket.ulti.Database;

public class AccountActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<AccountMenuItemDTO> list;
    TextView textViewUsername, txtCount;
    ImageView imgPicAccount;
    AccountAdapter adapter;
    Database database;
    Toolbar toolbar;
    HorizontalScrollMenuView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        reflect();
        createMenu();
        if (CheckConnection.haveNetworkConnection(this)) {
            getDataInfo();
            getProductCount();
        } else {
            CheckConnection.showConnection(this, "Please check your wifi!!");
        }
    }

    private void getDataInfo() {
        try {
            textViewUsername.setText(MainActivity.account.getUsername());
            if (MainActivity.account.getGender().equals("male")) {
                imgPicAccount.setBackground(getDrawable(R.drawable.maleicon));
            }
            list = new ArrayList<>();
            list.add(new AccountMenuItemDTO(R.drawable.profile, "Profile"));
            list.add(new AccountMenuItemDTO(R.drawable.pendingicon, "My Orders"));
            list.add(new AccountMenuItemDTO(R.drawable.invoiceicon, "My Invoice"));
            list.add(new AccountMenuItemDTO(R.drawable.pass, "Change Password"));
            list.add(new AccountMenuItemDTO(R.drawable.ic_sms, "Send SMS to office customer service"));
            list.add(new AccountMenuItemDTO(R.drawable.phone, "Phone to office customer service"));
            list.add(new AccountMenuItemDTO(R.drawable.logout, "Logout"));

            adapter = new AccountAdapter(this, list);
            listView.setAdapter(adapter);
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

                        Intent intent = new Intent(AccountActivity.this, MyInvoiceActi.class);
                        startActivity(intent);
                    } else if (i == 3) {
                        Log.e("IIIIII  ", i + " - - - --");
                        Intent intent = new Intent(AccountActivity.this, ChangePassActi.class);
                        startActivity(intent);
                    } else if (i == 4) {
                        Log.e("IIIIII  ", i + " - - - --");

                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SENDTO);
                        intent.putExtra("sms_body", "");
                        intent.setData(Uri.parse("sms:01676243500"));
                        startActivity(intent);
                    } else if (i == 5) {
                        Log.e("IIIIII  ", i + " - - - --");
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:01676243500"));
                        startActivity(intent);
                    } else {
                        confirmLogout();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void confirmLogout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Logout");
        dialog.setMessage("Do you want to logout ? ");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.deleteCustomer(MainActivity.account.getEmail());
                MainActivity.account = null;
                Toast.makeText(AccountActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
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
        menu = (HorizontalScrollMenuView)findViewById(R.id.menuAccountActivity);
        textViewUsername = (TextView) findViewById(R.id.txtUsernameAccount);
        imgPicAccount = (ImageView) findViewById(R.id.imgPicAccount);
        toolbar = (Toolbar) findViewById(R.id.toolbarAccount);
        setSupportActionBar(toolbar);

        txtCount = (TextView) findViewById(R.id.txtCountAccountActi);
        database = new Database(this);


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
                        Intent intent = new Intent(getApplicationContext(), MyCartActi.class);
                        startActivity(intent);
                        break;
                    case 3:

                        break;
                    case 4:
                    default:
                }

            }
        });
    }

}

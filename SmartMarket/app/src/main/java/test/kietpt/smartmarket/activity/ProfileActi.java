package test.kietpt.smartmarket.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.model.Account;
import test.kietpt.smartmarket.ulti.CheckConnection;
import test.kietpt.smartmarket.ulti.Database;
import test.kietpt.smartmarket.ulti.IpConfig;

public class ProfileActi extends AppCompatActivity {

    TextInputEditText email, username, address, phone;
    Spinner gender;
    String selectdSpinner;
    Database database = new Database(this);
    TextView checkUsername, checkAddress,checkPhone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        reflect();
        if(CheckConnection.haveNetworkConnection(this)){
            getDataCusInfo();
        }else{
            CheckConnection.showConnection(this,"Please check your wifi!!!");
        }



    }

    private void getDataCusInfo() {
        email.setText(MainActivity.account.getEmail().toString());
        username.setText(MainActivity.account.getUsername().toString());
        address.setText(MainActivity.account.getAddress().toString());
        phone.setText(MainActivity.account.getPhone().toString());


        List<String> dataSrc = new ArrayList<String>();
        if (MainActivity.account.getGender().toString().equals("male")) {
            dataSrc.add("male");
            dataSrc.add("female");
        } else {
            dataSrc.add("female");
            dataSrc.add("male");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataSrc);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(arrayAdapter);
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectdSpinner = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void reflect() {
        email = (TextInputEditText) findViewById(R.id.txtEmailProfile);
        email.setEnabled(false);
        username = (TextInputEditText) findViewById(R.id.txtUsernameProfile);
        gender = (Spinner) findViewById(R.id.spGenderProfile);
        address = (TextInputEditText) findViewById(R.id.txtAddressProfile);
        phone = (TextInputEditText) findViewById(R.id.txtPhoneProfile);
        checkUsername = (TextView) findViewById(R.id.checkUsernamePrifle);
        checkAddress = (TextView) findViewById(R.id.checkAddressProfile);
        checkPhone = (TextView) findViewById(R.id.checkPhoneProfile);
    }


    public boolean checkValidate() {

        String txtUsername = username.getText().toString();
        String txtAddress = address.getText().toString();
        String txtPhone = phone.getText().toString();

        boolean checked = false;
        if (!txtUsername.matches("^[\\p{L}\\s'.-]+$")) {
            checkUsername.setVisibility(View.VISIBLE);
            checkUsername.setText("Please input name");
            checked = true;
        }
        if (txtAddress.length() == 0) {
            checkAddress.setVisibility(View.VISIBLE);
            checkAddress.setText("Please not empty");
            checked = true;
        }
        if (!txtPhone.matches("0[1-9]{10,11}")) {
            if (txtPhone.length() == 0) {
                checkPhone.setVisibility(View.VISIBLE);
                checkPhone.setText("Please input phone");
                checked = true;
            }
        }
        return checked;
    }

    public void updateProfile(View view) {
        if (!checkValidate()) {
            confirmUpdateProfile();
        } else {
            Toast.makeText(ProfileActi.this, "Please to check your profile!!!!!!", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateProfileCustomer(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("UPDATE JSON + ", response.toString());
                        if (response.toString() != null) {
                            try {
                                String email = response.getString("email");
                                String username = response.getString("username");
                                String gender = response.getString("gender");
                                String phone = response.getString("phone");
                                String address = response.getString("address");

                                MainActivity.account = new Account();
                                MainActivity.account.setEmail(email);
                                MainActivity.account.setUsername(username);
                                MainActivity.account.setGender(gender);
                                MainActivity.account.setPhone(phone);
                                MainActivity.account.setAddress(address);
                                if (MainActivity.account != null) {
                                    database.updateCustomer(MainActivity.account);
                                    Toast.makeText(ProfileActi.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                                }
                                Intent intent = new Intent(ProfileActi.this, AccountActivity.class);
                                startActivity(intent);

                            } catch (Exception e) {
                                Log.e("ERROR UPDATE ", e.getMessage());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileActi.this, "Something wrong: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("ERROR UPDATE ", error.getMessage());

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public void confirmUpdateProfile() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirm your profile!!!!!!!");
        alertDialog.setIcon(R.drawable.username);

        alertDialog.setMessage("Do you want to change you profile ? ");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateProfileCustomer("http://" + IpConfig.ipConfig + ":8084/SSM_Project/UpdateCustomerController?txtEmail=" + email.getText().toString() +
                        "&txtUsername=" + username.getText().toString() +
                        "&txtAddress=" + address.getText().toString() + "&txtPhone=" + phone.getText().toString() +
                        "&txtGender=" + selectdSpinner.toString());
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dialog.dismiss();
                    }
                });
            }
        });
        alertDialog.show();
    }
}

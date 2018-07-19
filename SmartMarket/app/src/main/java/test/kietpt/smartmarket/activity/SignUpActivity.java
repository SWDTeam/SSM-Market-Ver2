package test.kietpt.smartmarket.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;

import java.util.List;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.model.Account;
import test.kietpt.smartmarket.ulti.Database;
import test.kietpt.smartmarket.ulti.IpConfig;


public class SignUpActivity extends AppCompatActivity {

    TextInputEditText email, pass, username, address, phone, confirmPass;
    private Spinner spGender;
    private String selectedGender;
    private Database database;
    TextView checkEmail;
    TextView checkPass;
    TextView checkConfirmPass;
    TextView checkUsername;
    TextView checkAddress;
    TextView checkPhone;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        reflect();
        catchSpinner();


    }

    // check gender
    private void catchSpinner() {
        List<String> dataSrc = new ArrayList<>();
        dataSrc.add("male");
        dataSrc.add("female");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataSrc);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(arrayAdapter);
        spGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void reflect() {
        //các edit text
        email = (TextInputEditText) findViewById(R.id.txtEmailSignUp);
        pass = (TextInputEditText) findViewById(R.id.txtPasswordSignUp);
        confirmPass = (TextInputEditText) findViewById(R.id.txtConfirmPasswordSignUp);
        username = (TextInputEditText) findViewById(R.id.txtUsernameSignUp);
        address = (TextInputEditText) findViewById(R.id.txtAddressSignUp);
        phone = (TextInputEditText) findViewById(R.id.txtPhoneSignUp);
        spGender = (Spinner) findViewById(R.id.spGenderSignUp);

        progressBar = (ProgressBar)findViewById(R.id.progressBarSignUp);


        //các textview dùng để checkvalidation
        checkEmail = (TextView) findViewById(R.id.checkEmail);
        checkPass = (TextView) findViewById(R.id.checkPassword);
        checkConfirmPass = (TextView) findViewById(R.id.checkConfirmPass);
        checkUsername = (TextView) findViewById(R.id.checkUsername);
        checkAddress = (TextView) findViewById(R.id.checkAddress);
        checkPhone = (TextView) findViewById(R.id.checkPhone);

        database = new Database(this);
    }

    public void onClickBack(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public boolean checkValidate() {

        String txtEmail = email.getText().toString();
        String txtPass = pass.getText().toString();
        String txtConfirmPass = confirmPass.getText().toString();
        String txtUsername = username.getText().toString();
        String txtAddress = address.getText().toString();
        String txtPhone = phone.getText().toString();

        boolean checked = false;
        if(!txtEmail.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            checkEmail.setVisibility(View.VISIBLE);
            checkEmail.setText("Format abc123@gmail.com");
            checked = true;
        }
        if (txtPass.length() < 6 || txtConfirmPass.length() < 6 || !txtPass.equalsIgnoreCase(txtConfirmPass)) {
            if (txtPass.length() < 6) {
                checkPass.setVisibility(View.VISIBLE);
                checkPass.setText("Password have 6 characters");
            }
            if (txtConfirmPass.length() < 6) {
                checkConfirmPass.setVisibility(View.VISIBLE);
                checkConfirmPass.setText("Password have 6 characters");
            }
            checkConfirmPass.setVisibility(View.VISIBLE);
            checkConfirmPass.setText("Please input same password");
            checked = true;
        }
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
        if (!txtPhone.matches("0[0-9]{10,11}")) {

            checkPhone.setVisibility(View.VISIBLE);
            checkPhone.setText("Please input phone");
            checked = true;

        }
        return checked;
    }

    //nut sign up ben activity_sign_up
    public void onClickSignUp(View view) {

        if (checkValidate()) {
            Toast.makeText(SignUpActivity.this, "Something wrong!!! please sign up again ", Toast.LENGTH_SHORT).show();
        } else {
            final Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.VISIBLE);
                            }
                        });
                        synchronized (this) {
                            wait(3000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            };
            thread.start();
            callApiSignUpCustomer("https://ssm-market.herokuapp.com/api/v1/sign_up");
        }

    }

    public void callApiSignUpCustomer(String url) {

        JSONObject jsonObject = new JSONObject();
        JSONObject js = new JSONObject();
        try {
            jsonObject.put("email", email.getText().toString());
            jsonObject.put("password", pass.getText().toString());
            jsonObject.put("name", username.getText().toString());
            jsonObject.put("phone", phone.getText().toString());
            jsonObject.put("address", address.getText().toString());
            jsonObject.put("gender", selectedGender);
            jsonObject.put("role_id", 2);
            js.put("registration", jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("test json sign up ", js + "");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reponse sign up ", response.toString());
                        try {

                            JSONArray jsonArray = response.getJSONArray("email");
                            if (jsonArray.length() == 1) {
                                Toast.makeText(SignUpActivity.this, "Email has already, please input another email", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            int userId = response.getInt("id");
                            String email = response.getString("email");
                            String username = response.getString("name");
                            String gender = response.getString("gender");
                            String phone = response.getString("phone");
                            String address = response.getString("address");
                            String status = response.getString("status");

                            MainActivity.account = new Account(userId, email, username, gender, phone, pass.getText().toString(), address, status);
                            if (!database.checkEmail(MainActivity.account.getEmail())) {
                                database.insertCustomer(MainActivity.account);
                            }
                            Toast.makeText(SignUpActivity.this, "Create Successfully", Toast.LENGTH_SHORT).show();
                            //database.getAllCustomer();
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

                        } catch (Exception e) {
                            Log.e("ERROR SIGNUP ", e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("ERRROR SIGNUP", "ERROR---- " + error.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}

package test.kietpt.smartmarket.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.model.Account;
import test.kietpt.smartmarket.ulti.CheckConnection;
import test.kietpt.smartmarket.ulti.Database;
import test.kietpt.smartmarket.ulti.IpConfig;


public class LoginActivity extends AppCompatActivity {


    TextInputEditText email, pass;
    Database database;
    Button login;
    ImageView imgTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        reflect();
        if (CheckConnection.haveNetworkConnection(this)) {
            loginInto();
        } else {
            CheckConnection.showConnection(this, "Please check your wifi!!!!");
        }
    }

    private void loginInto() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("LOGIN BUTTON", email.getText().toString() + " - " + pass.getText().toString());

                if (email.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please Input Email or Password", Toast.LENGTH_SHORT).show();
                } else {
                    //loginCustomer("http://" + IpConfig.ipConfig + ":3000/api/v1/sign_in");
                    loginCustomer("https://ssm-market.herokuapp.com/api/v1/sign_in");
//                    loginCustomer("http://" + IpConfig.ipConfig + ":8084/SSM_Project/LoginCusMobileController?txtEmail=" + email.getText().toString() +
//                            "&txtPassword=" + pass.getText().toString());
                }
            }
        });
    }

    private void reflect() {
        email = (TextInputEditText) findViewById(R.id.txtEmail);
        pass = (TextInputEditText) findViewById(R.id.txtPassword);
        imgTest = (ImageView) findViewById(R.id.imgTest);
        login = (Button) findViewById(R.id.btnLogin);
        database = new Database(this);
    }


    public void signUp(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void loginCustomer(String url) {
        final RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonObject = new JSONObject();
        JSONObject js = new JSONObject();
        try {
            jsonObject.put("email", email.getText().toString());
            jsonObject.put("password", pass.getText().toString());
            js.put("session", jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("Json request login ", js + "");

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, js
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("REPONSE LOGIN : ", response.toString());
                if (response.toString() != null || !response.toString().equals("")) {
                    Log.e("LOGIN ", "da vao Login ");
                    try {
                        boolean checked = response.getBoolean("success");
                        Log.e("Checked ", checked + "");
                        if (checked) {
                            int userReponse = response.getInt("id");
                            String emailReponse = response.getString("email");
                            Log.e("EMAILREPONSE + ", emailReponse);
                            String usernameReponse = response.getString("name");
                            String genderReponse = response.getString("gender");
                            String phoneReponse = response.getString("phone");
                            String addressReponse = response.getString("address");
                            String statusReponse = response.getString("status");

//                        int userReponse = response.getInt("userId");
//                        String emailReponse = response.getString("email");
//                        Log.e("EMAILREPONSE + ", emailReponse);
//                        String usernameReponse = response.getString("username");
//                        String genderReponse = response.getString("gender");
//                        String phoneReponse = response.getString("phone");
//                        String addressReponse = response.getString("address");
//                        String statusReponse = response.getString("status");

                            MainActivity.account = new Account(userReponse, emailReponse, usernameReponse, genderReponse, phoneReponse, pass.getText().toString(),
                                    addressReponse, statusReponse);

                            // dùng để kiểm tra xem account có trùng mail hay không
                            if (!database.checkEmail(emailReponse)) {
                                Log.e("ACCOUNT KO TON TAI  ", " khong  ton tai");
                                database.insertCustomer(MainActivity.account);
                            } else {
                                Log.e("ACCOUNT TON TAI ", " da ton tai");
                            }

                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Json reponse ", "chuỗi json trả về login bị null");
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_LONG).show();
                        Log.e("error login ", error.toString());
                    }
                }
        );
        requestQueue.add(stringRequest);
    }

}

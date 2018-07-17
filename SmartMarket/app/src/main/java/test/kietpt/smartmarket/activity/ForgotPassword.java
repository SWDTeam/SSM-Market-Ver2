package test.kietpt.smartmarket.activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

import test.kietpt.smartmarket.R;

public class ForgotPassword extends AppCompatActivity {
    TextInputEditText email;
    Button btnSend;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        reflect();
        sendMail();

    }

    private void reflect() {
        email = (TextInputEditText) findViewById(R.id.txtEmailForgot);
        btnSend = (Button) findViewById(R.id.btnSendMail);
        progressBar = (ProgressBar) findViewById(R.id.progressBarForgot);
    }

    private void sendMail() {

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().equals("")) {
                    Toast.makeText(ForgotPassword.this, "Please input your email!!!!", Toast.LENGTH_SHORT).show();
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
                    callApiforgotPassword("https://ssm-market.herokuapp.com/api/v1/forgot_password");
                }
            }
        });

    }

    private void callApiforgotPassword(final String url) {
        JSONObject js = new JSONObject();
        try {
            js.put("email", email.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("json object request  = ", js + "");
        RequestQueue requestQueue = Volley.newRequestQueue(ForgotPassword.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("json response send mail : ", response.toString());

                        try {
                            String status = response.getString("status");
                            if (status.equals("ok")) {
                                Toast.makeText(ForgotPassword.this, "Send Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), CodeForgotPassActi.class);
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("response error forgot pass : ", error.toString());
                        Toast.makeText(ForgotPassword.this, "Invalid Email, Please input correct email!!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),ForgotPassword.class);
                        startActivity(intent);
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

}

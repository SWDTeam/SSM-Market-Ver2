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

public class CodeForgotPassActi extends AppCompatActivity {
    TextInputEditText code, newPass, confirmPass;
    Button btnReset;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_forgot_pass);
        reflect();
        resetPassword();
    }

    private void reflect() {
        code = (TextInputEditText) findViewById(R.id.txtCodeForgot);
        newPass = (TextInputEditText) findViewById(R.id.txtNewPassForgot);
        confirmPass = (TextInputEditText) findViewById(R.id.txtConfirmPassForgot);
        btnReset = (Button) findViewById(R.id.btnResetPass);
        progressBar = (ProgressBar) findViewById(R.id.progressBarCode);
    }

    public boolean validatePass() {
        String codeChecked = code.getText().toString();
        String newPassword = newPass.getText().toString();
        String confirmPassword = confirmPass.getText().toString();
        boolean checked = false;

        if (newPassword.equals("") || confirmPassword.equals("") || codeChecked.equals("")) {
            checked = true;
            Toast.makeText(this, "Please input password and code ", Toast.LENGTH_SHORT).show();
        }
        if (!newPassword.equals(confirmPassword)) {
            checked = true;
            Toast.makeText(CodeForgotPassActi.this, "New Pass and Confirm Pass have to be same", Toast.LENGTH_SHORT).show();
        }
        return checked;
    }

    private void resetPassword() {
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validatePass()) {
                    Toast.makeText(CodeForgotPassActi.this, "Something wrong!!!!", Toast.LENGTH_SHORT).show();
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
                    callApiResetPassword("https://ssm-market.herokuapp.com/api/v1/reset_password");
                }
            }
        });

    }

    private void callApiResetPassword(final String url) {

        JSONObject js = new JSONObject();
        try {
            js.put("password", newPass.getText().toString());
            js.put("token", code.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("json object request  = ", js + "");
        RequestQueue requestQueue = Volley.newRequestQueue(CodeForgotPassActi.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("json response reset password : ", response.toString());

                        try {
                            String status = response.getString("status");
                            if (status.equals("ok")) {
                                Toast.makeText(CodeForgotPassActi.this, "Change Password Successfully ", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    Log.e("error response json code ",error.toString());
                        Toast.makeText(CodeForgotPassActi.this, "Invalid Code, Please input correct code!!!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),CodeForgotPassActi.class);
                        startActivity(intent);
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

}

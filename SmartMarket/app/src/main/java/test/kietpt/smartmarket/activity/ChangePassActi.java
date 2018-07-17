package test.kietpt.smartmarket.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.model.Account;
import test.kietpt.smartmarket.ulti.Database;
import test.kietpt.smartmarket.ulti.IpConfig;

public class ChangePassActi extends AppCompatActivity {

    TextInputEditText oldPass, newPass, confirmPass;
    Button btnChange;
    Database database = new Database(this);
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        reflect();

    }

    private void reflect() {
        oldPass = (TextInputEditText) findViewById(R.id.txtOldPass);
        newPass = (TextInputEditText) findViewById(R.id.txtNewPass);
        confirmPass = (TextInputEditText) findViewById(R.id.txtConfirmPass);
        progressBar = (ProgressBar)findViewById(R.id.progressBarChangePass);
    }


    public boolean validatePass() {
        String oldPassword = oldPass.getText().toString();
        String newPassword = newPass.getText().toString();
        String confirmPassword = confirmPass.getText().toString();
        boolean checked = false;
        if (oldPassword.equals("") || newPassword.equals("") || confirmPassword.equals("")) {
            checked = true;
            Toast.makeText(ChangePassActi.this, "Please input password", Toast.LENGTH_SHORT).show();
        }
        if (oldPassword.equals(newPassword) || oldPassword.equals(confirmPassword)) {
            checked = true;
            Toast.makeText(ChangePassActi.this, "Old Pass have to be differnent New Pass or Confirm pass", Toast.LENGTH_SHORT).show();
        }
        if (!newPassword.equals(confirmPassword)) {
            checked = true;
            Toast.makeText(ChangePassActi.this, "New Pass and Confirm Pass have to be same", Toast.LENGTH_SHORT).show();
        }
        return checked;
    }

    public void callApiChangePassword(String url) {
        JSONObject jsonObject = new JSONObject();
        JSONObject js = new JSONObject();
        try {
            jsonObject.put("id", MainActivity.account.getUserId());
            jsonObject.put("old_password",oldPass.getText().toString());
            jsonObject.put("new_password", newPass.getText().toString());
            js.put("account", jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e("test json change pass ", js + "");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, js,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("CHANGE PASS +", response.toString());

                        if (response.toString() != null) {
                            try {
                                boolean checked = response.getBoolean("success");
                                if(checked){
                                    int id = response.getInt("id");
                                    MainActivity.account.setUserId(id);
                                    MainActivity.account.setPassword(newPass.getText().toString());
                                    if (MainActivity.account != null) {
                                        database.updatePassword(MainActivity.account);
                                        Toast.makeText(ChangePassActi.this, "Update Password Successfully", Toast.LENGTH_SHORT).show();
                                    }
                                    Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                                    startActivity(intent);
                                }else if(!checked){
                                    Toast.makeText(ChangePassActi.this, "Please correct old password", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }else{
                            Log.e("Wrong Change Pass ","Some thing wrong!!! ");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error change pass + ", error.getMessage());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public void changePasword(View view) {
        if (!validatePass()) {
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
            confirmUpdatePass();
        } else {
            Toast.makeText(ChangePassActi.this, "Please to check your password!!!!!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void confirmUpdatePass() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirm your password!");
        alertDialog.setIcon(R.drawable.pass);

        alertDialog.setMessage("Do you want to change password ? ");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callApiChangePassword("https://ssm-market.herokuapp.com/api/v1/change_password");
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

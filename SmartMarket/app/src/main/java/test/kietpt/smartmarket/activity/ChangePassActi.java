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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import test.kietpt.smartmarket.R;
import test.kietpt.smartmarket.model.Account;
import test.kietpt.smartmarket.ulti.Database;
import test.kietpt.smartmarket.ulti.IpConfig;

public class ChangePassActi extends AppCompatActivity {

    TextInputEditText oldPass, newPass, confirmPass;
    Button btnChange;
    Database database = new Database(this);

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

    public void changePassword(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("CHANGE PASS +", response.toString());
                        if (response.toString().equals("{}")) {
                            Toast.makeText(ChangePassActi.this, "Please correct old password", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                String email = response.getString("email");
                                String newPassword = response.getString("password");
                                MainActivity.account.setEmail(email);
                                MainActivity.account.setPassword(newPassword);
                                if (MainActivity.account != null) {
                                    database.updatePassword(MainActivity.account);
                                    Toast.makeText(ChangePassActi.this, "Update Password Successfully", Toast.LENGTH_SHORT).show();
                                }
                                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                Log.e("ERROR PASSWORD + ", e.getMessage());
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR PASSWORD + ", error.getMessage());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }

    public void changePasword(View view) {
        if (!validatePass()) {
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
                changePassword("http://" + IpConfig.ipConfig + ":8084/SSM_Project/ChangePassCustomer?txtEmail=" + MainActivity.account.getEmail() + "&txtOldPassword="
                        + oldPass.getText().toString() + "&txtNewPassword=" + newPass.getText().toString());
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

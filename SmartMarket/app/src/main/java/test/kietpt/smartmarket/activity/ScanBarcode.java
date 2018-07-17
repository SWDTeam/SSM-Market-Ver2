package test.kietpt.smartmarket.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import test.kietpt.smartmarket.model.ProductDTO;
import test.kietpt.smartmarket.ulti.IpConfig;

import static android.Manifest.permission.CAMERA;

public class ScanBarcode extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final int REQUEST_CAMERA = 1;
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                Toast.makeText(this, "Permission is granted! ", Toast.LENGTH_SHORT).show();

            } else {
                requestPermission();
            }
        }

    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(ScanBarcode.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int grantResults[]) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted) {
                        Toast.makeText(this, "Permission Granted ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Permission Denied ", Toast.LENGTH_SHORT).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CAMERA)) {
                                displayAlertMessage("you need to allow access for both permissions ",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkPermission()){
                if(scannerView == null){
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            }else{
                requestPermission();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener) {
        new AlertDialog.Builder(ScanBarcode.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) {
        String scanResult = result.getText();
        Toast.makeText(this, scanResult, Toast.LENGTH_LONG).show();
        getBarcode("https://ssm-market.herokuapp.com/api/v1/products_barcode/"+scanResult);
    }
    public void getBarcode(String url){
        Log.e("da vao day", " da vao day r ne");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("da vao day", " da vao day r ne ahihi do ngoc");
                        Log.e("Json Product Barcode ", response.toString());
                        if(response.toString() != null) {
                            try {

                                int id = response.getInt("id");
                                String name = response.getString("name");
                                String des = response.getString("description");
                                String urlPic = response.getString("url");
                                String key = response.getString("product_key");
                                float price = (float) response.getDouble("price");
                                float priceChecked = (float) response.getDouble("price");
                                String manufacture = response.getString("manufacturer");
                                String manuDate = response.getString("manu_date");
                                String expiredDate = response.getString("expired_date");

                                String urlTest = "https://ssm-market.herokuapp.com" + urlPic;

                                ProductDTO dto = new ProductDTO();
                                dto.setProductId(id);
                                dto.setProductName(name);
                                dto.setDescription(des);
                                dto.setProductKey(key);
                                dto.setPrice(price);
                                dto.setManufacturer(manufacture);
                                dto.setManuDate(manuDate);
                                dto.setExpiredDate(expiredDate);
                                dto.setUrlPic(urlTest);
                                dto.setPriceChecked(priceChecked);
                                Intent intent = new Intent(getApplicationContext(),ProductDetailActi.class);
                                intent.putExtra("ProductInfo",dto);
                                startActivity(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}

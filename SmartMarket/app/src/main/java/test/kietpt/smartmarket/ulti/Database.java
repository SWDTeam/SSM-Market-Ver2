package test.kietpt.smartmarket.ulti;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import test.kietpt.smartmarket.model.Account;
import test.kietpt.smartmarket.model.ProductDTO;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ssmarket";
    public static final String TABLE_NAME_ACCOUNT = "Account";
    public static final String TABLE_NAME_ORDER_DETAIL = "Order_Detail";
    public static final int DATABASE_VERSION = 2;

    // CursorFactory la con tro dung de duyet database
    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_NAME_ACCOUNT + "(" +
                "userId INTEGER PRIMARY KEY ," +
                "email VARCHAR(50) UNIQUE," +
                "userName VARCHAR (50), " +
                "gender VARCHAR (10)," +
                "phone VARCHAR (12), " +
                "password VARCHAR (20)," +
                "address VARCHAR (500)," +
                "status VARCHAR (20) ) "
        );
        db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_NAME_ORDER_DETAIL + "(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "productId INTEGER UNIQUE," +
                "productKey VARCHAR (50) UNIQUE, " +
                "productName VARCHAR (500), " +
                "urlPic VARCHAR (3000), " +
                "quantity INTEGER, " +
                "price REAL ," +
                "categoryId INTEGER ," +
                "status VARCHAR (50) ) "
        );
        Log.e("CREATE TABLE ","tao table thanh cong");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertProduct(ProductDTO dto) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("productId", dto.getProductId());
        values.put("productKey", dto.getProductKey());
        values.put("productName", dto.getProductName());
        values.put("urlPic", dto.getUrlPic());
        values.put("quantity", dto.getQuantity());
        values.put("price", dto.getPrice());
        values.put("categoryId", dto.getCategoryId());
        values.put("status", dto.getStatus());
        db.insert(TABLE_NAME_ORDER_DETAIL, null, values);
        Log.e("DB INSERT + ", "them thanh cong product trong Database ");
        db.close();
    }
    public void insertCustomer(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId",account.getUserId());
        values.put("email", account.getEmail());
        values.put("userName", account.getUsername());
        values.put("gender", account.getGender());
        values.put("phone", account.getPhone());
        values.put("password", account.getPassword());
        values.put("address", account.getAddress());
        values.put("status", account.getStatus());
        db.insert(TABLE_NAME_ACCOUNT, null, values);
        Log.e("DB INSERT + ", "them thanh cong customer trong Database ");
        db.close();
    }

    public Account getCustomerInfo(String email) {
        Account account = null;
        SQLiteDatabase db = this.getReadableDatabase();
        System.out.println(email + " da vao database ");
        Cursor cursor = db.rawQuery("Select * from Account where email = '" + email + "' ", null);
        if (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String mail = cursor.getString(1);
            String username = cursor.getString(2);
            String gender = cursor.getString(3);
            String phone = cursor.getString(4);
            String pass = cursor.getString(5);
            String address = cursor.getString(6);
            String status = cursor.getString(7);
            account = new Account(id, mail, username, gender, phone, pass, address, status);
        }
        return account;
    }

    public void getAllCustomer() {
        Log.e("123456", "da vao getCustomer() ");
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor dataCustomer = sqlDB.rawQuery("Select * From Account", null);
        while (dataCustomer.moveToNext()) {
            int userId = dataCustomer.getInt(0);
            String email = dataCustomer.getString(1);
            String username = dataCustomer.getString(2);
            String gender = dataCustomer.getString(3);
            String phone = dataCustomer.getString(4);
            String passsword = dataCustomer.getString(5);
            String address = dataCustomer.getString(6);
            String status = dataCustomer.getString(7);
        }
    }

    public ArrayList<ProductDTO> getAllProductInCart() {
        Log.e("IN CART", "da vao de show product cart trong db");
        ArrayList<ProductDTO> list= new ArrayList<>();
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor dataProduct = sqlDB.rawQuery("Select * From Order_Detail", null);
        while (dataProduct.moveToNext()) {
            int id = dataProduct.getInt(0);
            int productId = dataProduct.getInt(1);
            String productKey = dataProduct.getString(2);
            String productName = dataProduct.getString(3);
            String urlPic = dataProduct.getString(4);
            int quantity = dataProduct.getInt(5);
            Float price = dataProduct.getFloat(6);
            int categoryId = dataProduct.getInt(7);
            String status = dataProduct.getString(8);
            ProductDTO dto = new ProductDTO();
            dto.setId(id);
            dto.setProductId(productId);
            dto.setProductKey(productKey);
            dto.setProductName(productName);
            dto.setUrlPic(urlPic);
            dto.setQuantity(quantity);
            dto.setPrice(price);
            dto.setCategoryId(categoryId);
            dto.setStatus(status);
            list.add(dto);
        }
        Log.e("RETURN LIST PRODUCT ","Da return list product");
        return list;
    }

    public boolean checkEmail(String mail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From Account Where email = '" + mail + "'", null);
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }
    public boolean checkProductId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From Order_Detail Where productId = '" + id + "'", null);
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }
    public void deleteCustomer(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put("email",email);
        db.delete(TABLE_NAME_ACCOUNT, "email = '" + email + "'", null);
        Log.e("DB DELETE + ", "DELETE thanh cong trong Database ");
    }
    public void updateCustomer(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", account.getEmail());
        values.put("userName", account.getUsername());
        values.put("gender", account.getGender());
        values.put("phone", account.getPhone());
        values.put("address", account.getAddress());
        db.update(TABLE_NAME_ACCOUNT,values,"email = '"+account.getEmail().toString()+"'",null);

        Log.e("DB UPDATE + ", "UPDATE thanh cong trong Database ");
        db.close();
    }
    public void updatePassword(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email",account.getEmail());
        values.put("password",account.getPassword());
        db.update(TABLE_NAME_ACCOUNT,values,"email = '"+account.getEmail().toString()+"'",null);
        Log.e("UPDATE PASSOWORD + ","Update password thanh cong");
        db.close();
    }
}

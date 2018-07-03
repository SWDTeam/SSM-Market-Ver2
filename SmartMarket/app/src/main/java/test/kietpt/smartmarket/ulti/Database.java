package test.kietpt.smartmarket.ulti;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import test.kietpt.smartmarket.model.Account;
import test.kietpt.smartmarket.model.Cart;
import test.kietpt.smartmarket.model.ProductDTO;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ssmarket";
    public static final String TABLE_NAME_ACCOUNT = "Account";
    public static final String TABLE_NAME_CART = "CartProduct";
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
        db.execSQL(" CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CART + "(" +
                "productId INTEGER PRIMARY KEY ," +
                "productKey VARCHAR (50) UNIQUE, " +
                "productName VARCHAR (500), " +
                "urlPic VARCHAR (3000), " +
                "quantity INTEGER, " +
                "price REAL ," +
                "userId INTEGER )"
        );
        Log.e("CREATE TABLE ", "tao table thanh cong");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertToCart(Cart dto, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("productId", dto.getProductId());
        values.put("productKey", dto.getProductKey());
        values.put("productName", dto.getProductName());
        values.put("urlPic", dto.getUrlPic());
        values.put("quantity", dto.getProductQuantity());
        values.put("price", dto.getProductPrice());
        values.put("userId", userId);
        db.insert(TABLE_NAME_CART, null, values);
        Log.e("add to cart + ", "them thanh cong product vao cart trong Database ");
        db.close();
    }

    public void updateQuantityInCart(int productId, int quantity, float price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("productId", productId);
        values.put("quantity", quantity);
        values.put("price", price);
        db.update(TABLE_NAME_CART, values, "productId = '" + productId + "'", null);
        Log.e("update quantity in cart + ", "Update quanity thanh cong");
        db.close();
    }

    public int getProductCount(){
        //int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.rawQuery("select count(*) from CartProduct " , null);
        //count = cursor.getCount();
        //Log.e("Count in product = ",count+"");
        return (int) DatabaseUtils.longForQuery(db,"select count(*) from CartProduct ",null);
        //return count;
    }
    public void deleteProductInCart(int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_CART, "productId = '" + productId + "'", null);
        Log.e("delete product in cart + ", "DELETE product " + productId + "thanh cong trong Database ");
    }

    public void deleteCart(List<Cart> listCart) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < listCart.size(); i++) {
            db.delete(TABLE_NAME_CART, "productId = '" + listCart.get(i).getProductId() + "'", null);
        }
        Log.e("delete cart + ", "DELETE cart thanh cong trong Database ");
    }

    public String getProductQuanAndPrice(int id) {
        Log.e("get product quantity + ", "da vao duoc cho nay");
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select quantity,price from CartProduct where productId = '" + id + "'", null);
        int quantity = 0;
        float price = 0;
        if (cursor.moveToNext()) {
            quantity = cursor.getInt(0);
            price = cursor.getFloat(1);
        }
        Log.e("get product quantity and price+ ", "da lay ra duoc quantity cua product");
        return quantity + "-" + price;

    }

    public List<Cart> getAllProductInCart() {
        Log.e("IN CART", "da vao de show product cart trong db");
        List<Cart> list = new ArrayList<>();
        SQLiteDatabase sqlDB = this.getReadableDatabase();
        Cursor dataProduct = sqlDB.rawQuery("Select * From CartProduct", null);
        while (dataProduct.moveToNext()) {
            int productId = dataProduct.getInt(0);
            String productKey = dataProduct.getString(1);
            String productName = dataProduct.getString(2);
            String urlPic = dataProduct.getString(3);
            int quantity = dataProduct.getInt(4);
            float price = dataProduct.getFloat(5);
            Cart dto = new Cart();
            dto.setProductId(productId);
            dto.setProductKey(productKey);
            dto.setProductName(productName);
            dto.setUrlPic(urlPic);
            dto.setProductQuantity(quantity);
            dto.setProductPrice(price);
            list.add(dto);
        }
        Log.e("RETURN LIST PRODUCT ", "Da return list product");
        return list;
    }

    public boolean checkProductIdInCart(String key) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From CartProduct Where productKey = '" + key + "'", null);
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    public void insertCustomer(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", account.getUserId());
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


    public boolean checkEmail(String mail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From Account Where email = '" + mail + "'", null);
        if (cursor.moveToNext()) {
            return true;
        }
        return false;
    }


    public void deleteCustomer(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME_ACCOUNT, "email = '" + email + "'", null);
        Log.e("DB DELETE + ", "DELETE thanh cong trong Database ");
    }

    public void updateCustomer(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", account.getEmail());
        values.put("userName", account.getUsername());
        values.put("gender", account.getGender());
        values.put("phone", account.getPhone());
        values.put("address", account.getAddress());
        db.update(TABLE_NAME_ACCOUNT, values, "email = '" + account.getEmail().toString() + "'", null);

        Log.e("DB UPDATE + ", "UPDATE thanh cong trong Database ");
        db.close();
    }

    public void updatePassword(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", account.getUserId());
        values.put("password", account.getPassword());
        db.update(TABLE_NAME_ACCOUNT, values, "userId = '" + account.getUserId() + "'", null);
        Log.e("UPDATE PASSOWORD + ", "Update password thanh cong");
        db.close();
    }

    public int getCustomerId(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * From Account Where email = '" + email + "'", null);
        if (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            return id;
        }
        return 0;
    }
}

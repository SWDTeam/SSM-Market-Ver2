package test.kietpt.smartmarket.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Cart implements Serializable{

   private int productId;
   private String productName;
   private String productKey;
   private String urlPic;
   private Float productPrice;
   private int productQuantity;
   private int quantityTemp;
   private float priceChekced;

    public float getPriceChekced() {
        return priceChekced;
    }

    public void setPriceChekced(float priceChekced) {
        this.priceChekced = priceChekced;
    }

    public int getQuantityTemp() {
        return quantityTemp;
    }

    public void setQuantityTemp(int quantityTemp) {
        this.quantityTemp = quantityTemp;
    }

    public Cart(){}
    public Cart(int productId, String productName, String productKey, String urlPic, Float productPrice, int productQuantity) {
        this.productId = productId;
        this.productName = productName;
        this.productKey = productKey;
        this.urlPic = urlPic;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getUrlPic() {
        return urlPic;
    }

    public void setUrlPic(String urlPic) {
        this.urlPic = urlPic;
    }

    public Float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Float productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }
}

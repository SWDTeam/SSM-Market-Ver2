package test.kietpt.smartmarket.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProductDTO implements Serializable {
    public static final String STATUS_ACTIVE = "active";

    private String productName, manufacturer, description, status, urlPic, productKey, manuDate, expiredDate;
    private int quantity, categoryId, productId, id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ProductDTO() {
    }

    private float price;

    public ProductDTO(String productName, String description, String urlPic, String productKey, int categoryId,
                      int productId, float price, String manufacturer, String manuDate, String expiredDate,int quantity) {
        this.productName = productName;
        this.description = description;
        this.urlPic = urlPic;
        this.productKey = productKey;
        this.categoryId = categoryId;
        this.productId = productId;
        this.price = price;
        this.manufacturer = manufacturer;
        this.manuDate = manuDate;
        this.expiredDate = expiredDate;
        this.quantity = quantity;
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

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getManuDate() {
        return manuDate;
    }

    public void setManuDate(String manuDate) {
        this.manuDate = manuDate;
    }

    public String getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(String expiredDate) {
        this.expiredDate = expiredDate;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}

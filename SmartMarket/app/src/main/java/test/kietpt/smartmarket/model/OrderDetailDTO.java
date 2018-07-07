package test.kietpt.smartmarket.model;

import java.io.Serializable;

public class OrderDetailDTO implements Serializable {
    private int id;
    private int orderId;
    private int productId;
    private String productName;
    private int quantity;
    private float price;
    private String status;
    private String productKey;
    private String imgKey;




    public OrderDetailDTO() {
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public OrderDetailDTO(int id, int orderId, int productId,String productKey, String productName, int quantity, float price, String status, String imgKey) {
        this.id = id;
        this.orderId = orderId;
        this.productId = productId;
        this.productKey = productKey;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.status = status;
        this.imgKey = imgKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }
}

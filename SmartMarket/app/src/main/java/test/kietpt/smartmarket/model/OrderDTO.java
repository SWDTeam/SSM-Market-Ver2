package test.kietpt.smartmarket.model;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.Date;

public class OrderDTO implements Serializable {
    private int orderId;
    private String orderCode;
    private float totalPrice;
    private int totalQuantity;
    private String orderedDate;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderDTO() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getOrderedDate() {
        return orderedDate;
    }

    public void setOrderedDate(String orderedDate) {
        this.orderedDate = orderedDate;
    }

    public OrderDTO(int orderId, String orderCode, float totalPrice, int totalQuantity, String orderedDate, String status) {
        this.orderId = orderId;
        this.orderCode = orderCode;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
        this.orderedDate = orderedDate;
        this.status = status;
    }
}

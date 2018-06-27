package test.kietpt.smartmarket.model;

import java.io.Serializable;
import java.util.Date;

public class Account implements Serializable {
    private int userId;
    private String email;
    private String username;
    private String gender;
    private String phone;
    private String password;
    private String address;
    private String status;



    public Account() {
    }

    public Account(int userId, String email, String username, String gender, String phone, String password, String address,String status) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.gender = gender;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

package test.kietpt.smartmarket.model;

import java.io.Serializable;

public class AccountMenuItemDTO implements Serializable {
    private int imgIcon;
    private String name;

    public AccountMenuItemDTO() {
    }

    public AccountMenuItemDTO(int imgIcon, String name) {
        this.imgIcon = imgIcon;
        this.name = name;
    }

    public int getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(int imgIcon) {
        this.imgIcon = imgIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

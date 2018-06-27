package test.kietpt.smartmarket.model;

import java.io.Serializable;

public class CategoryDTO implements Serializable{
    private int cateId;
    private String cateName;
    private String status;
    private String imgPic;

    public CategoryDTO() {
    }

    public CategoryDTO(int cateId, String cateName, String imgPic) {
        this.cateId = cateId;
        this.cateName = cateName;
        this.imgPic = imgPic;
    }

    public CategoryDTO(int cateId, String cateName, String status, String imgPic) {
        this.cateId = cateId;
        this.cateName = cateName;
        this.status = status;
        this.imgPic = imgPic;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImgPic() {
        return imgPic;
    }

    public void setImgPic(String imgPic) {
        this.imgPic = imgPic;
    }

}

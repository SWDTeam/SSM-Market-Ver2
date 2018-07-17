package test.kietpt.smartmarket.model;

import java.io.Serializable;

public class SearchHistoryDTO implements Serializable{
    private int id;
    private String searchName;

    public SearchHistoryDTO(){}
    public SearchHistoryDTO(int id, String searchName) {
        this.id = id;
        this.searchName = searchName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
}

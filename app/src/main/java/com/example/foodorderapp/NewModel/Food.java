package com.example.foodorderapp.NewModel;

import java.io.Serializable;

public class Food implements Serializable {
    private String id;
    private String categoryid ;
    private String detail;
    private String image;
    private String name;
    private String price;

    public Food() {

    }

    public Food(String id, String categoryid, String detail, String image, String name, String price) {
        this.id = id;
        this.categoryid = categoryid;
        this.detail = detail;
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}

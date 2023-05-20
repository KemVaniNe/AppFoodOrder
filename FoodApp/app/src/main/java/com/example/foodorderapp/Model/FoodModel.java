package com.example.foodorderapp.Model;
import java.io.Serializable;
public class FoodModel implements Serializable{
    private String id;
    private  String category_id;
    private String name;
    private String image;
    private String price;
    private String detail;
    public FoodModel(String id, String categoryid, String detail, String image, String name, String price) {
        this.id = id;
        this.category_id = categoryid;
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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}

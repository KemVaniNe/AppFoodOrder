package com.example.foodorderapp.Model;
public class CategoryModel {
    private String id_category;
    private String Name_category;
    private String image_category;

    public CategoryModel(String id_category, String name_category) {
        this.id_category = id_category;
        Name_category = name_category;
    }

    public String getId_category() {
        return id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }

    public String getName_category() {
        return Name_category;
    }

    public void setName_category(String name_category) {
        Name_category = name_category;
    }

    public String getImage_category() {
        return image_category;
    }

    public void setImage_category(String image_category) {
        this.image_category = image_category;
    }
}

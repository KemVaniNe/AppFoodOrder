package com.example.foodorderapp.Model;

public class FoodOrderModel {
    private String id_foodorder ;
    private String id;
    public String getNameFood() {
        return NameFood;
    }

    public void setNameFood(String nameFood) {
        NameFood = nameFood;
    }

    public String getImageFood() {
        return ImageFood;
    }

    public void setImageFood(String imageFood) {
        ImageFood = imageFood;
    }

    private String NameFood ;
    private int Number ;
    private int price ;
    private String ImageFood ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_foodorder() {
        return id_foodorder;
    }

    public void setId_foodorder(String id_foodorder) {
        this.id_foodorder = id_foodorder;
    }

    public int getNumber() {
        return Number;
    }

    public void setNumber(int number) {
        Number = number;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}

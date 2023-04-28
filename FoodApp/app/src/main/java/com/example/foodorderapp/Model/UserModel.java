package com.example.foodorderapp.Model;
public class UserModel {
    private String id;
    private String name;
    private String pass;

    private String email;
    private String phone;
    private String avatar ;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserModel(){

    }

    public UserModel(String name, String pass) {
        this.name = name;
        this.pass = pass;
    }

    public UserModel(String name, String pass, String email, String phone) {
        this.name = name;
        this.pass = pass;
        this.email = email;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

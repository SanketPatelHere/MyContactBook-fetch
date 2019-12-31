package com.example.mycontactbook;

public class Contacts {
    String name, phone;
    int image;
    String img;
    public Contacts()
    {

    }
    public Contacts(String name, String phone, String image) {
        this.name = name;
        this.phone = phone;
        this.img = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}

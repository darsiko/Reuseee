package com.example.reuse.models;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    private int imageResId; // Resource ID for the product image
    private String name;    // Product name
    private String price;   // Product price
    private String userName; // User name
    private String userStatus; // User status
    private int userAvatarResId; // Resource ID for the user avatar
    private List<String> tags;
    public Product(int imageResId, String name, String price, String userName, String userStatus, int userAvatarResId, List<String> tags ) {
        this.imageResId = imageResId;
        this.name = name;
        this.price = price;
        this.userName = userName;
        this.userStatus = userStatus;
        this.userAvatarResId = userAvatarResId;
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public int getUserAvatarResId() {
        return userAvatarResId;
    }
}

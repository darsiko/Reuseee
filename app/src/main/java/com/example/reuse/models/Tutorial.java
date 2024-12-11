package com.example.reuse.models;

public class Tutorial {

    private int imageResId; // Resource ID for the product image
    private String name;    // Product name
    private String author;
    private String description;
    private int userAvatarResId;

    public Tutorial(int imageResId, String name, int userAvatarResId, String description, String author) {
        this.imageResId = imageResId;
        this.name = name;
        this.author = author;
        this.userAvatarResId = userAvatarResId;
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getUserAvatarResId() {
        return userAvatarResId;
    }

    public void setUserAvatarResId(int userAvatarResId) {
        this.userAvatarResId = userAvatarResId;
    }
}

package com.example.reuse.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Tutorial implements Parcelable {

    private int imageResId; // Resource ID for the product image
    private String name;    // Product name
    private String author;
    private String description;
    private int userAvatarResId;
    private String urlTutorial;

    public String getUrlTutorial() {
        return urlTutorial;
    }

    public void setUrlTutorial(String urlTutorial) {
        this.urlTutorial = urlTutorial;
    }

    public Tutorial(int imageResId, String name, int userAvatarResId, String description, String author, String urlTutorial) {
        this.imageResId = imageResId;
        this.name = name;
        this.author = author;
        this.userAvatarResId = userAvatarResId;
        this.description = description;
        this.urlTutorial = urlTutorial;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int i) {
        dest.writeInt(imageResId);
        dest.writeString(name);
        dest.writeInt(userAvatarResId);
        dest.writeString(description);
        dest.writeString(author);
        dest.writeString(urlTutorial);
    }
}

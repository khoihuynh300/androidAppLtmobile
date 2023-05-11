package com.example.ltmobile.Model;

import java.io.Serializable;

public class ImageInn implements Serializable {
    private int imageInnId;
    private String image;

    public ImageInn(int imageInnId, String image) {
        this.imageInnId = imageInnId;
        this.image = image;
    }

    public int getImageInnId() {
        return imageInnId;
    }

    public void setImageInnId(int imageInnId) {
        this.imageInnId = imageInnId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

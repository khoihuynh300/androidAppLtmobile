package com.example.ltmobile.Model;

import java.util.Date;
import java.util.List;

public class Inn {
    private int innId;
    private String address, phoneNumber, describe;
    private Double price;
    private Date createdAt;
    private Date updatedAt;
    private String proposed;
    private ImageInn mainImage;
    private List<ImageInn> images;

    public Inn(int innId, String address, String phoneNumber, String describe, Double price, Date createdAt, Date updatedAt, String proposed, ImageInn mainImage, List<ImageInn> images) {
        this.innId = innId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.describe = describe;
        this.price = price;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.proposed = proposed;
        this.mainImage = mainImage;
        this.images = images;
    }

    public int getInnId() {
        return innId;
    }

    public void setInnId(int innId) {
        this.innId = innId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getProposed() {
        return proposed;
    }

    public void setProposed(String proposed) {
        this.proposed = proposed;
    }

    public ImageInn getMainImage() {
        return mainImage;
    }

    public void setMainImage(ImageInn mainImage) {
        this.mainImage = mainImage;
    }

    public List<ImageInn> getImages() {
        return images;
    }

    public void setImages(List<ImageInn> images) {
        this.images = images;
    }
}


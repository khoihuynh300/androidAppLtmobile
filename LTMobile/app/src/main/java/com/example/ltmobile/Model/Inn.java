package com.example.ltmobile.Model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Inn implements Serializable {
    private int innId;
    private String address, phoneNumber, describe;
    private Double price;
    private Double priceWater;
    private Double priceELec;
    private Date createdAt;
    private Date updatedAt;
    private String proposed;
    private int proposedId;
    private int size;
    private ImageInn mainImage;
    private List<ImageInn> images;
    private boolean isConfirmed;
    private long confirmedById;

    public Inn(int innId, String address, String phoneNumber, String describe, Double price, Double priceWater, Double priceELec, Date createdAt, Date updatedAt, String proposed, int proposedId, ImageInn mainImage, List<ImageInn> images) {
        this.innId = innId;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.describe = describe;
        this.price = price;
        this.priceWater = priceWater;
        this.priceELec = priceELec;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.proposed = proposed;
        this.proposedId = proposedId;
        this.mainImage = mainImage;
        this.images = images;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Double getPriceWater() {
        return priceWater;
    }

    public void setPriceWater(Double priceWater) {
        this.priceWater = priceWater;
    }

    public Double getPriceELec() {
        return priceELec;
    }

    public void setPriceELec(Double priceELec) {
        this.priceELec = priceELec;
    }

    public int getProposedId() {
        return proposedId;
    }

    public void setProposedId(int proposedId) {
        this.proposedId = proposedId;
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

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }

    public long getConfirmedById() {
        return confirmedById;
    }

    public void setConfirmedById(long confirmedById) {
        this.confirmedById = confirmedById;
    }
}


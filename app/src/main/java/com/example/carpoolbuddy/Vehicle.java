package com.example.carpoolbuddy;

import java.io.Serializable;

public class Vehicle implements Serializable {

    private String owner;
    private String carModel;
    private String capacity;
    private String price;
    private String UID;

    private String URI;

    public Vehicle(){

    }

    public Vehicle(String owner, String carModel, String capacity, String price, String UID, String URI){
        this.owner = owner;
        this.carModel = carModel;
        this.capacity = capacity;
        this.price = price;
        this.UID = UID;
        this.URI = URI;
    }
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public void setUUID(String UUID) {
        this.UID = UUID;
    }
    public String getUUID() {
        return UID;
    }
    public String getURL() {
        return URI;
    }

    public void setURL(String URL) {
        this.URI = URI;
    }

}

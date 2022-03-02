package com.example.carpoolbuddy;

import java.util.ArrayList;

public class User {
    private String userName;
    private String role;
    private String UID;
    private String balance;
    private ArrayList<String> bookedRideList;
    private ArrayList<String> ownedVehicleList;
    private String phoneNumber;

    public User(){

    }

    public User(String userName, String role, String balance, ArrayList<String>  bookedRidesList, ArrayList<String> ownedVehiclesList, String phoneNumber, String UID) {
        this.userName = userName;
        this.role = role;
        this.UID = UID;
        this.phoneNumber = phoneNumber;
        this.balance = balance;
        this.bookedRideList = bookedRidesList;
        this.ownedVehicleList = ownedVehiclesList;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public ArrayList<String>  getBookedRideList() {
        return bookedRideList;
    }

    public ArrayList<String>  getOwnedVehicleList() {
        return ownedVehicleList;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}

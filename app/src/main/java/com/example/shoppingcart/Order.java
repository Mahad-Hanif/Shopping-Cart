package com.example.shoppingcart;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {
    private int id;
    private String fName;
    private String lName;
    private String email;
    private String pNno;
    private String cName;
    private String zip;
    private String state;
    private String city;
    private String boxes;
    private String category;
    private String mLocalDateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalDateTime() {
        return mLocalDateTime;
    }

    public void setLocalDateTime(String localDateTime) {
        mLocalDateTime = localDateTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNo() {
        return pNno;
    }

    public void setNo(String no) {
        this.pNno = no;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBoxes() {
        return boxes;
    }

    public void setBoxes(String boxes) {
        this.boxes = boxes;
    }
}

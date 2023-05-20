package com.example.test;

public class AcceptedR {
    private String fname,lname,phone,longt,lat;

    public AcceptedR(String fname, String lname, String phone, String longt, String lat) {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.longt = longt;
        this.lat = lat;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLongt() {
        return longt;
    }

    public void setLongt(String longt) {
        this.longt = longt;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}

package com.example.test;

public class Fetched {
    private String cname,longt,lat,expneeded,jtitle,jdesc,id,category,days,hours;
    public Fetched(){
        this.cname="";
        this.longt="";
        this.lat="";
        this.expneeded="";
        this.jtitle="";
        this.jdesc="";
        this.id="";
        this.category="";
        this.days="";
        this.hours="";
    }

    public Fetched(String cname, String longt, String lat, String expneeded, String jtitle, String jdesc, String id, String category) {
        this.cname = cname;
        this.longt = longt;
        this.lat = lat;
        this.expneeded = expneeded;
        this.jtitle = jtitle;
        this.jdesc = jdesc;
        this.id = id;
        this.category=category;
    }

    public Fetched(String cname, String longt, String lat, String expneeded, String jtitle, String jdesc, String id, String category, String days, String hours) {
        this.cname = cname;
        this.longt = longt;
        this.lat = lat;
        this.expneeded = expneeded;
        this.jtitle = jtitle;
        this.jdesc = jdesc;
        this.id = id;
        this.category = category;
        this.days = days;
        this.hours = hours;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
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

    public String getExpneeded() {
        return expneeded;
    }

    public void setExpneeded(String expneeded) {
        this.expneeded = expneeded;
    }

    public String getJtitle() {
        return jtitle;
    }

    public void setJtitle(String jtitle) {
        this.jtitle = jtitle;
    }

    public String getJdesc() {
        return jdesc;
    }

    public void setJdesc(String jdesc) {
        this.jdesc = jdesc;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

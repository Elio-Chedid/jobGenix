package com.example.test;

public class Accepted {
    private String Jtitle,Jdesc,c_name;

    public Accepted(String jtitle, String jdesc, String c_name) {
        this.Jtitle = jtitle;
        this.Jdesc = jdesc;
        this.c_name = c_name;
    }

    public String getJtitle() {
        return Jtitle;
    }

    public void setJtitle(String jtitle) {
        Jtitle = jtitle;
    }

    public String getJdesc() {
        return Jdesc;
    }

    public void setJdesc(String jdesc) {
        Jdesc = jdesc;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }
}

package com.example.wifi.Model.wifi;

public class WifiApList {

    private int ap1,ap2,ap3,ap4;
    private String createTime;

    public WifiApList(int ap1,int ap2,int ap3,int ap4,String createTime) {
        this.ap1 = ap1;
        this.ap2 = ap2;
        this.ap3 = ap3;
        this.ap4 = ap4;
        this.createTime = createTime;
    }
    public WifiApList(){}

    public int getAp1() {
        return ap1;
    }

    public void setAp1(int ap1) {
        this.ap1 = ap1;
    }

    public int getAp2() {
        return ap2;
    }

    public void setAp2(int ap2) {
        this.ap2 = ap2;
    }

    public int getAp3() {
        return ap3;
    }

    public void setAp3(int ap3) {
        this.ap3 = ap3;
    }

    public int getAp4() {
        return ap4;
    }

    public void setAp4(int ap4) {
        this.ap4 = ap4;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "WifiApList{" +
                "ap1=" + ap1 +
                ", ap2=" + ap2 +
                ", ap3=" + ap3 +
                ", ap4=" + ap4 +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}

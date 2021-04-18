package com.example.wifi.Model.wifi;


public class Wifi {

    private String mapName;

    private String mapX;

    private String mapY;

    private int ap1,ap2,ap3,ap4;

    private String createTime;

    public Wifi(){
        ap1 = 0;
        ap2 = 0;
        ap3 = 0;
        ap4 = 0;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getMapX() {
        return mapX;
    }

    public void setMapX(String mapX) {
        this.mapX = mapX;
    }

    public String getMapY() {
        return mapY;
    }

    public void setMapY(String mapY) {
        this.mapY = mapY;
    }

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
        return "{" +
                "mapName='" + mapName + '\'' +
                ", mapX='" + mapX + '\'' +
                ", mapY='" + mapY + '\'' +
                ", ap1=" + ap1 +
                ", ap2=" + ap2 +
                ", ap3=" + ap3 +
                ", ap4=" + ap4 +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}

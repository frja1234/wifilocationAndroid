package com.example.wifi.Model.wifi;

public class WifiMap {

    private String wifiMapX;

    private String wifiMapY;

    public WifiMap(){
        wifiMapX = "0";
        wifiMapY = "0";
        System.out.println(wifiMapX+wifiMapY);
    }

    public WifiMap(String wifiMapX,String wifiMapY){
        this.wifiMapX = wifiMapX;
        this.wifiMapY = wifiMapY;
    }

    public String getWifiMapY() {
        return wifiMapY;
    }

    public void setWifiMapY(String wifiMapY) {
        this.wifiMapY = wifiMapY;
    }

    public String getWifiMapX() {
        return wifiMapX;
    }

    public void setWifiMapX(String wifiMapX) {
        this.wifiMapX = wifiMapX;
    }

    @Override
    public String toString() {
        return "WifiMapPoint{" +
                "wifiMapX='" + wifiMapX + '\'' +
                ", wifiMapY='" + wifiMapY + '\'' +
                '}';
    }
}

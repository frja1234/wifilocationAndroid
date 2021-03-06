package com.example.wifi.Model.wifi;

import java.util.ArrayList;

public class wifi {

    private String wifiPointId;

    private String mapX;

    private String mapY;

    private String ap;

    public String getWifiPointId() {
        return wifiPointId;
    }

    public void setWifiPointId(String wifiPointId) {
        this.wifiPointId = wifiPointId;
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

    public String getAp() {
        return ap;
    }

    public void setAp(String ap) {
        this.ap = ap;
    }

    public ArrayList<String> getEveryAp(String ap){
        ArrayList<String> everyAp = new ArrayList<String>();
        StringBuilder sb = new StringBuilder();

        return everyAp;
    }
}

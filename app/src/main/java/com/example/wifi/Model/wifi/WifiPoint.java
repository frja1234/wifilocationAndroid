package com.example.wifi.Model.wifi;

import com.example.wifi.Model.wifi.WifiMap;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WifiPoint {

    private String wifiPointId;

    private WifiMap wifiMap;

    private String createTime;

    public WifiPoint (){
        Date date = new Date();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX" );
        String str = sdf.format(date);
        wifiPointId = date.toString();
        wifiMap = new WifiMap();
        createTime = str;
    }


    public WifiMap getWifiMap() {
        return wifiMap;
    }

    public void setWifiMap(WifiMap wifiMap) {
        this.wifiMap = wifiMap;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getWifiPointId() {
        return wifiPointId;
    }

    public void setWifiPointId(String wifiPointId) {
        this.wifiPointId = wifiPointId;
    }

    @Override
    public String toString() {
        return "WifiPoint{" +
                "wifiPointId='" + wifiPointId + '\'' +
                ", wifiMap=" + wifiMap +
                ", createTime=" + createTime +
                '}';
    }
}

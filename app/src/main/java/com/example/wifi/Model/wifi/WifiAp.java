package com.example.wifi.Model.wifi;

public class WifiAp {

    private String wifiPointId;

    private String wifiName;

    private String wifiBssid;

    private int wifiRssi;

    public WifiAp(){
        wifiName = "frja";
        wifiBssid = "0.0.0.0";
        wifiRssi = -100;
    }

    public String getWifiPointId() {
        return wifiPointId;
    }

    public void setWifiPointId(String wifiPointId) {
        this.wifiPointId = wifiPointId;
    }

    public String getWifiName() {
        return wifiName;
    }

    public void setWifiName(String wifiName) {
        this.wifiName = wifiName;
    }

    public String getWifiBssid() {
        return wifiBssid;
    }

    public void setWifiBssid(String wifiBssid) {
        this.wifiBssid = wifiBssid;
    }

    public int getWifiRssi() {
        return wifiRssi;
    }

    public void setWifiRssi(int wifiRssi) {
        this.wifiRssi = wifiRssi;
    }

    @Override
    public String toString() {
        return "WifiAp{" +
                "wifiPointId='" + wifiPointId + '\'' +
                ", wifiName='" + wifiName + '\'' +
                ", wifiBssid='" + wifiBssid + '\'' +
                ", wifiRssi=" + wifiRssi +
                '}';
    }
}

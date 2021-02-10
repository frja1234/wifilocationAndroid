package com.example.wifi.Model;

public class WifiPoint {
    public String name;
    public String BSSID;
    public int rssi;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    @Override
    public String toString() {
        return "WifiPoint{" +
                "name='" + name + '\'' +
                ", BSSID='" + BSSID + '\'' +
                ", rssi=" + rssi +
                '}';
    }
}

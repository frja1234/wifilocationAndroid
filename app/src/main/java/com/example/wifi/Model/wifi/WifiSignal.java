package com.example.wifi.Model.wifi;

import android.net.wifi.ScanResult;

import com.example.wifi.Model.wifi.WifiAp;
import com.example.wifi.Model.wifi.WifiPoint;

import java.util.ArrayList;
import java.util.List;

public class WifiSignal {

    private WifiPoint wifiPoint;

    private ArrayList<WifiAp> ap;

    public WifiSignal(){
        wifiPoint = new WifiPoint();
        ap = new ArrayList<WifiAp>();
        WifiAp wifiAp = new WifiAp();
        for(int i=0;i<4;i++){
            ap.add(wifiAp);
        }
        System.out.println("wifiSignal");
    }

    public ArrayList<WifiAp> getAp() {
        return ap;
    }

    public void setAp(ArrayList<WifiAp> ap) {
        this.ap = ap;
    }

    public WifiPoint getWifiPoint() {
        return wifiPoint;
    }

    public void setWifiPoint(WifiPoint wifiPoint) {
        this.wifiPoint = wifiPoint;
    }

    @Override
    public String toString() {
        return "WifiSignal{" +
                "wifiPoint=" + wifiPoint +
                ", ap=" + ap +
                '}';
    }
}

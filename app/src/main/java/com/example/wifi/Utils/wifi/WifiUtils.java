package com.example.wifi.Utils.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.example.wifi.Model.wifi.WifiMap;
import com.example.wifi.Model.wifi.WifiPoint;
import com.example.wifi.Model.wifi.WifiAp;
import com.example.wifi.Model.wifi.WifiSignal;
import com.example.wifi.Utils.IdUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WifiUtils {
    // 定义WifiManager对象
    private WifiManager mWifiManager;
    // 定义WifiInfo对象
    private WifiInfo mWifiInfo;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    //



    // 构造器
    public WifiUtils(Context context) {
        // 取得WifiManager对象
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // 取得WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();
        startScan();
    }
    //扫描wifi
    public void startScan(){
        mWifiManager.startScan();
        mWifiList = mWifiManager.getScanResults();
    }

    //收集
    public WifiSignal collect(WifiMap wifiMap){
        WifiSignal wifiSignal = new WifiSignal();
        WifiPoint wifiPoint = new WifiPoint();
        ArrayList<WifiAp> ap = new ArrayList<WifiAp>();
        IdUtils idUtils = new IdUtils();
        Date date = new Date();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX" );
        String str = sdf.format(date);
        WifiAp wifiAp = new WifiAp();
        for(ScanResult result : mWifiList){
            wifiAp.setWifiBssid(result.BSSID);
            wifiAp.setWifiName(result.SSID);
            wifiAp.setWifiRssi(result.level);
            wifiAp.setWifiPointId(idUtils.getId());
            ap.add(wifiAp);
            System.out.println("wifiscan");
            System.out.println(wifiAp.toString());

        }
        wifiSignal.setAp(ap);
        wifiPoint.setWifiMap(wifiMap);
        wifiPoint.setCreateTime(str);
        wifiPoint.setWifiPointId(wifiAp.getWifiPointId());
        wifiSignal.setWifiPoint(wifiPoint);

        return wifiSignal;
    }


    // 得到网络列表
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }
}
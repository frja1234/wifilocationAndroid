package com.example.wifi.Utils.wifi;

import android.content.Context;
import android.graphics.Point;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.example.wifi.Model.WifiFlag;
import com.example.wifi.Model.WifiPoint;

import java.util.ArrayList;
import java.util.List;

public class WifiUtils {
    // ssid对应的wifi名字
    public String name;

    public int rssi;
    //macid
    public String BSSID;
    // 定义WifiManager对象
    private WifiManager mWifiManager;
    // 定义WifiInfo对象
    private WifiInfo mWifiInfo;
    // 扫描出的网络连接列表
    private List<ScanResult> mWifiList;
    // 定义一个WifiLock
    WifiManager.WifiLock mWifiLock;
    //
    private WifiFlag wifiFlag;

    // 构造器
    public WifiUtils(Context context) {
        // 取得WifiManager对象
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        // 取得WifiInfo对象
        mWifiInfo = mWifiManager.getConnectionInfo();
    }
    //加载所有wifi
    public WifiPoint getAllWifiMessage() {
        mWifiManager.startScan();
        mWifiList = mWifiManager.getScanResults();
        WifiPoint wifiPoint = new WifiPoint();
        System.out.println("sfbj");
        wifiPoint.setName(mWifiList.get(0).SSID);
        wifiPoint.setBSSID(mWifiList.get(0).BSSID);
        wifiPoint.setRssi(mWifiList.get(0).level);
        //wifiInfo.add(point);
        System.out.println(wifiPoint.toString());
        return wifiPoint;
    }


    // 打开WIFI
    public void openWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }
    }

    // 关闭WIFI
    public void closeWifi() {
        if (mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(false);
        }
    }

    // 检查当前WIFI状态
    public int checkState() {
        return mWifiManager.getWifiState();
    }

    // 锁定WifiLock
    public void acquireWifiLock() {
        mWifiLock.acquire();
    }

    // 解锁WifiLock
    public void releaseWifiLock() {
        // 推断时候锁定
        if (mWifiLock.isHeld()) {
            mWifiLock.acquire();
        }
    }

    // 创建一个WifiLock
    public void creatWifiLock() {
        mWifiLock = mWifiManager.createWifiLock("Test");
    }

    public void startScan() {
        mWifiManager.startScan();
        // 得到扫描结果x
        mWifiList = mWifiManager.getScanResults();
    }

    // 得到网络列表
    public List<ScanResult> getWifiList() {
        return mWifiList;
    }


    public String getBSSID() {

        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getBSSID();
    }

    public void setBSSID(String Bssid) {

        this.BSSID = BSSID;
    }

    public int getRssi() {

        return (mWifiInfo == null) ? Integer.parseInt("NULL") : mWifiInfo.getRssi();
    }

    public void setRssi(int level) {
        this.rssi = rssi;
    }

    public String getName() {

        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getSSID();
    }

    public void setName(String name) {
        this.name = name;
    }

    // 得到MAC地址
    public String getMacAddress() {

        return (mWifiInfo == null) ? "NULL" : mWifiInfo.getMacAddress();
    }

    // 得到IP地址
    public int getIPAddress() {

        return (mWifiInfo == null) ? 0 : mWifiInfo.getIpAddress();
    }

    // 得到连接的ID
    public int getNetworkId() {

        return (mWifiInfo == null) ? 0 : mWifiInfo.getNetworkId();
    }

    // 得到WifiInfo的全部信息包
    public String getWifiInfo() {

        return (mWifiInfo == null) ? "NULL" : mWifiInfo.toString();
    }

    // 加入一个网络并连接
    public void addNetwork(WifiConfiguration wcg) {
        int wcgID = mWifiManager.addNetwork(wcg);
        mWifiManager.enableNetwork(wcgID, true);
    }

    // 断开指定ID的网络
    public void disconnectWifi(int netId) {
        mWifiManager.disableNetwork(netId);
        mWifiManager.disconnect();
    }
}
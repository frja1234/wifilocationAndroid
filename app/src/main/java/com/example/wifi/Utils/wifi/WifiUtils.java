package com.example.wifi.Utils.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.example.wifi.Model.wifi.Wifi;
import com.example.wifi.Model.wifi.WifiMessageList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private ArrayList<String> wifiNameList = new ArrayList<>();



    // 构造器
    public WifiUtils(Context context) {
        wifiNameList.add("CT-Young");
        wifiNameList.add("CT-Young");
        wifiNameList.add("CT-Young");
        wifiNameList.add("CT-Young");
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

    //判断wifi是否开启
    public boolean wifiIsEnable(){
        return mWifiManager.isWifiEnabled();
    }


    //收集
    public Wifi wifiCollect(String X,String Y,String mapName){

        StringBuilder wifiId = new StringBuilder();
        int x = Integer.parseInt(X.replace(".00",""));
        int y = Integer.parseInt(Y.replace(".00",""));
        Date date = new Date();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss" );
        String dateByFormat = sdf.format(date);
        Wifi wifi = new Wifi();
        wifiId.append(date.getTime()).append(x).append(y);
        wifi.setMapName(mapName);
        wifi.setMapX(X);
        wifi.setMapY(Y);
        //wifi.setCreateTime(dateByFormat);
        wifi.setCreateTime(dateByFormat);
        mWifiList = getWifiListByLevel();
        for(ScanResult scanResult :mWifiList){
            if(scanResult.SSID.equals(wifiNameList.get(0)))
                wifi.setAp1(scanResult.level);
            if(scanResult.SSID.equals(wifiNameList.get(1)))
                wifi.setAp2(scanResult.level);
            if(scanResult.SSID.equals(wifiNameList.get(2)))
                wifi.setAp3(scanResult.level);
            if(scanResult.SSID.equals(wifiNameList.get(3)))
                wifi.setAp4(scanResult.level);
        }

        return wifi;
    }

    //定位扫描
    public Wifi locationScan(){
        Wifi wifi = new Wifi();
        mWifiList = getWifiListByLevel();
        for(ScanResult scanResult :mWifiList){
            if(scanResult.SSID.equals(wifiNameList.get(0)))
                wifi.setAp1(-scanResult.level);
            if(scanResult.SSID.equals(wifiNameList.get(1)))
                wifi.setAp2(-scanResult.level);
            if(scanResult.SSID.equals(wifiNameList.get(2)))
                wifi.setAp3(-scanResult.level);
            if(scanResult.SSID.equals(wifiNameList.get(3)))
                wifi.setAp4(-scanResult.level);
        }
        return wifi;
    }


    public ArrayList<WifiMessageList> getWifiListData(){
        ArrayList<WifiMessageList> wifiList = new ArrayList<>();
        int i=1;
        for(ScanResult result : getWifiListByLevel()){
            wifiList.add(new WifiMessageList(""+i++,result.SSID,""+result.level,true));
        }
        return wifiList;
    }


    public boolean isExists(String ssid,ArrayList<ScanResult> wifiList){
        boolean flag = false;
        for(ScanResult scanResult :wifiList){
            if (scanResult.SSID.equals(ssid)) return  true;
        }


        return flag;
    }



    // 得到网络列表
    public List<ScanResult> getWifiListByLevel() {
        ArrayList<ScanResult> wifiList = new ArrayList<>();
        Collections.sort(mWifiList,new SortRssi());
        for(ScanResult scanResult :mWifiList){
            if(!isExists(scanResult.SSID,wifiList)){
                scanResult.level= - scanResult.level;
                wifiList.add(scanResult);
            }
        }

        return wifiList;
    }


    //对扫描结果按强度排序
    private class SortRssi implements Comparator<ScanResult> {
        @Override
        public int compare(ScanResult o1, ScanResult o2) {
            return o1.level-o2.level;
        }
    }

}
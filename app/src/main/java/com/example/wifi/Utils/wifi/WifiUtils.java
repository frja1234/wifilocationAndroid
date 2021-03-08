package com.example.wifi.Utils.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.example.wifi.Model.wifi.Wifi;
import com.example.wifi.Model.wifi.WifiMessageList;
import com.example.wifi.Model.wifi.WifiMap;
import com.example.wifi.Model.wifi.WifiPoint;
import com.example.wifi.Model.wifi.WifiAp;
import com.example.wifi.Model.wifi.WifiSignal;
import com.example.wifi.Utils.IdUtils;

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

    public Wifi wifiCollect(String X,String Y){
        ArrayList<String> wifiNameList = new ArrayList<>();
        wifiNameList.add("CT-Young");
        wifiNameList.add("NIT-WIFI");
        wifiNameList.add("皮卡丘");
        wifiNameList.add("NIT—WIFI");
        StringBuilder wifiId = new StringBuilder();
        int x = Integer.parseInt(X.replace(".00",""));
        int y = Integer.parseInt(Y.replace(".00",""));
        Date date = new Date();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss" );
        String dateByFormat = sdf.format(date);
        Wifi wifi = new Wifi();
        wifiId.append(date.getTime()).append(x).append(y);
        wifi.setWifiPointId(wifiId.toString());
        wifi.setMapX(X);
        wifi.setMapY(Y);
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
            System.out.println(scanResult.SSID);
        }

        return wifi;
    }


    public ArrayList<WifiMessageList> getWifiListData(){
        ArrayList<WifiMessageList> wifiList = new ArrayList<>();
        int i=1;
        for(ScanResult result : getWifiListByLevel()){
            wifiList.add(new WifiMessageList(""+i++,result.SSID,""+result.level));
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

    private class SortRssi implements Comparator<ScanResult> {
        @Override
        public int compare(ScanResult o1, ScanResult o2) {
            return o1.level-o2.level;
        }
    }

}
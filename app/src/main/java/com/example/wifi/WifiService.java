package com.example.wifi;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.wifi.Model.wifi.Wifi;
import com.example.wifi.Model.wifi.WifiMap;
import com.example.wifi.Utils.http.HttpWifiUtils;
import com.example.wifi.Utils.wifi.WifiUtils;

public class WifiService extends Service {
    @Nullable
    // 绑定服务时才会调用,必须要实现的方法
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("TAG","-MyService---onBind()----");
        return null;
    }
    //首次创建服务时，系统将调用此方法来执行一次性设置程序，如果服务已在运行，则不会调用此方法。该方法只被调用一次
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("MyService","-MyService---onCreate()----");
    }
    //每次通过startService()方法启动Service时都会被回调。
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        WifiUtils wifiUtils = new WifiUtils(getApplicationContext());
        HttpWifiUtils httpWifiUtils = new HttpWifiUtils();
        Wifi wifi = wifiUtils.locationScan();
        wifi.setMapY("0");
        wifi.setMapX("0");
        System.out.println(wifi+"service");
        WifiMap wifiMap = httpWifiUtils.wifiLocation(wifi);
        Intent mIntent=new Intent();
        mIntent.putExtra("mapX", Float.parseFloat(wifiMap.getWifiMapX()));
        mIntent.putExtra("mapY", Float.parseFloat(wifiMap.getWifiMapY()));
        mIntent.setAction("com.example.wifi.WifiService");
        sendBroadcast(mIntent);
        return super.onStartCommand(intent, flags, startId);
    }
    //服务销毁时的回调
    @Override
    public void onDestroy() {
        Log.e("MyService","-MyService---onDestroy()----");
        super.onDestroy();
    }

}
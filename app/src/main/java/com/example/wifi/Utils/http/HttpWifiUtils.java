package com.example.wifi.Utils.http;

import com.example.wifi.Model.Internet;
import com.example.wifi.Model.wifi.WifiAp;
import com.example.wifi.Model.wifi.WifiMap;
import com.example.wifi.Model.wifi.WifiSignal;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpWifiUtils {
    RequestBody requestBody;
    OkHttpClient okHttpClient;
    Internet internet;
    String res;

    //wifi指纹存储
    public Boolean wifiPointStore(WifiSignal wifiSignal) {
        // 构建请求参数
        Gson gson = new Gson();
        String json = gson.toJson(wifiSignal);
        System.out.println(json);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.SECONDS)
                .callTimeout(3000, TimeUnit.SECONDS)
                .build();
        //Okhttp3同步请求 开启线程
        Thread thread =  new Thread() {
            @Override
            public void run() {
                //设置请求的地址
                Request request = new Request.Builder()
                        .url("http://37533an013.wicp.vip/wifilocation/wifi/wifipointstore").post(body).build();
                Response response = null;
                try {
                    //同步请求
                    response = okHttpClient.newCall(request).execute();
                    System.out.println(response.body());
                    if (response.isSuccessful()) {
                        res = response.body().string();
                        //Gson解析
                        System.out.println(res);
                        Gson gson = new Gson();
                        internet = gson.fromJson(res,Internet.class);
                        WifiMap wifiMap = gson.fromJson(internet.getData(),WifiMap.class);
                        System.out.println(internet.toString()+"返回的信息");
                        System.out.println(wifiMap.toString()+"返回的信息");
                    } else{
                        System.out.println("服务器连接失败");
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        };
        //start开启线程 ，join()主线程等待子线程执行完成在继续执行
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //判断密码是否正确 正确后进行跳转
        if (internet.getCode().equals("200")){
            return true;
        }else
            return false;
    }
    //wifi定位
    public Boolean wifiLocation(WifiSignal wifiSignal) {
        // 构建请求参数
        Gson gson = new Gson();
        String json = gson.toJson(wifiSignal);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.SECONDS)
                .callTimeout(3000, TimeUnit.SECONDS)
                .build();
        //Okhttp3同步请求 开启线程
        Thread thread =  new Thread() {
            @Override
            public void run() {
                //设置请求的地址
                Request request = new Request.Builder()
                        .url("http://192.168.0.107:8888/wifiLocation/wifi/wifiLocation").post(body).build();
                Response response = null;
                try {
                    //同步请求
                    response = okHttpClient.newCall(request).execute();
                    System.out.println(response.body());
                    if (response.isSuccessful()) {
                        res = response.body().string();
                        //Gson解析
                        System.out.println(res);
                        Gson gson = new Gson();
                        internet = gson.fromJson(res,Internet.class);
                        WifiMap wifiMap = gson.fromJson(internet.getData(),WifiMap.class);
                        System.out.println(internet.toString()+"返回的信息");
                        System.out.println(wifiMap.toString()+"返回的信息");
                    } else{
                        System.out.println("服务器连接失败");
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }
        };
        //start开启线程 ，join()主线程等待子线程执行完成在继续执行
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //判断密码是否正确 正确后进行跳转
        if (internet.getCode().equals("200")){
            return true;
        }else
            return false;
    }

}

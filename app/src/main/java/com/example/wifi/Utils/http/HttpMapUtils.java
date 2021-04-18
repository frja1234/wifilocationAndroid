package com.example.wifi.Utils.http;

import com.example.wifi.Model.Internet;
import com.example.wifi.Model.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpMapUtils {
    OkHttpClient okHttpClient;
    Internet internet;
    ArrayList<Map> mapList = new ArrayList<>();
    Map map = new Map();
    String res;
    String url = "http://172.33.253.102:8888/wifilocation/map/",mapUrl;
    //地图存储
    public boolean mapSave(Map map) {
        // 构建请求参数
        mapUrl = url + "mapSave";
        Gson gson = new Gson();
        String json = gson.toJson(map);
        System.out.println(json+"123");
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
                        .url(mapUrl).post(body).build();
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
        }else return false;
    }

    //获取所有地图信息
    public ArrayList<Map> getAllMap() {
        // 构建请求参数
        mapUrl = url + "getAllMap";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),"");
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
                        .url(mapUrl).post(body).build();
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
                        Type type = new TypeToken<ArrayList<Map>>() {
                        }.getType();
                        mapList = gson.fromJson(internet.getData(),type);
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
            return mapList;

        }else return new ArrayList<Map>();
    }

    //根据名字获取地图
    public Map getMapByName(String name) {
        // 构建请求参数
        mapUrl = url + "getMapByName";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),name);
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
                        .url(mapUrl).post(body).build();
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
                        map = gson.fromJson(internet.getData(),Map.class);
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
            return map;
        }else return new Map();
    }

    //删除地图
    public boolean deleteMapByName(String name) {
        // 构建请求参数
        mapUrl = url + "deleteMap";
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),name);
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
                        .url(mapUrl).post(body).build();
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
        }else return false;
    }
}

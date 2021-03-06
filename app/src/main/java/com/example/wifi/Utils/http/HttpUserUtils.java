package com.example.wifi.Utils.http;


import com.example.wifi.Model.User;
import com.example.wifi.Model.Internet;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUserUtils {
    RequestBody requestBody;
    OkHttpClient okHttpClient;
    Internet internet;
    String res;
    //用户登录
    public Boolean login(String name, String password) {
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
                        .url("http://37533an013.wicp.vip/wifilocation/user/login?userId="+name+"&userPassword="+password).get().build();
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
                        System.out.println(internet.toString()+"返回的信息");
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
    //用户注册
    public boolean register(User user){
        FormBody.Builder formBody = new FormBody.Builder();

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3000, TimeUnit.SECONDS)
                .callTimeout(3000, TimeUnit.SECONDS)
                .build();
        requestBody = formBody.build();
        //Okhttp3同步请求 开启线程
        Thread thread =  new Thread() {
            @Override
            public void run() {
                //设置请求的地址
                Request request = new Request.Builder()
                        .url("http://192.168.0.110:8888/wifilocation/user/register?userId="+user.getUserId()+"&userName="+user.getUserName()+"&userAuthority="+user.getUserAuthority()+"&userPassword="+user.getPassword()+"&createTime="+user.getCreateTime()).get().build();
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
                        System.out.println(internet.toString()+"返回的信息");
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
    //用户信息
}

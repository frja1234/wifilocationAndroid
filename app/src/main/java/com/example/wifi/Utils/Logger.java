package com.example.wifi.Utils;

import android.graphics.PointF;
import android.os.Environment;

import com.example.wifi.Model.wifi.Wifi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class Logger {
    private static String TAG = "Logger";

    private static String getRootDir() {
        System.out.println(Environment.getExternalStorageDirectory()+"123");
        return Environment.getExternalStorageDirectory() + "/wifiLocation";
    }

    public static void saveFingerprintData(String type, Wifi wifi) {
        List<String> data = new LinkedList<>();
        data.add(String.format(Locale.ENGLISH, "%s %s %s %s %s %s %s", wifi.getMapX(),wifi.getMapY(),wifi.getAp1(), wifi.getAp2(), wifi.getAp3(),wifi.getAp4(),wifi.getCreateTime()));

        String rootPath = getRootDir();
        String filePath = rootPath + "/" + type + getTimeStamp() + ".txt";

        try {
            File dir = new File(rootPath);
            if(!dir.exists()){
                System.out.println("文件夹存在");
                dir.mkdir();
                BufferedReader in = new BufferedReader(new FileReader(filePath));
                try {
                    String line = in.readLine();
                    while (line != null) {
                        System.out.println(line);
                        if (!line.trim().equals("")) { //no blank line
                            if (!line.contains("|")) {
                                String[] attr = line.split(" ");
                                if(attr[0].equals(wifi.getMapX())&&attr[1].equals(wifi.getMapY())){
                                    data.add(String.format(Locale.ENGLISH, "%s %s %s %s %s %s %s", wifi.getMapX(),wifi.getMapY(),wifi.getAp1(), wifi.getAp2(), wifi.getAp3(),wifi.getAp4(),wifi.getCreateTime()));

                                }
                            }
                        }
                        line = in.readLine();
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }else{
                System.out.println("文件不存在");
                dir.createNewFile();
            }
            //StringBuilder sb = new StringBuilder();
            FileWriter fw = new FileWriter(filePath, true);
            try {
                fw.write(data.toString().replace("[","").replace("]","")+"\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<PointF> getCollectedGrid(String type) {
        String rootPath = getRootDir();
        String filePath = rootPath + "/" + type + getTimeStamp() + ".txt";

        List<PointF> result = new ArrayList<>();

        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        }

        try {
            String line = in.readLine();
            while (line != null) {

                System.out.println(line);
                if (!line.trim().equals("")) { //no blank line
                    if (!line.contains("|")) {
                        String[] attr = line.split(" ");
                        PointF p = new PointF(Float.valueOf(attr[0]), Float.valueOf(attr[1]));
                        result.add(p);
                    }
                }
                line = in.readLine();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    //For create file name
    private static String getDateUnderLine() {
        //SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat();
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    private static String getTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

}

package com.example.wifi.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wifi.Model.FingerPrint;
import com.example.wifi.Model.WifiFlag;
import com.example.wifi.Model.WifiPoint;
import com.example.wifi.R;
import com.example.wifi.Utils.wifi.WifiUtils;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private HomeViewModel homeViewModel;
    private final Context context = getActivity();
    private WifiUtils wifiTool;
    private WifiFlag wifiFlag;
    private WifiPoint wifiPoint;
    private FingerPrint fingerPrint;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        System.out.println("jvnjsdn");
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final Button BCollect = root.findViewById(R.id.collect);
        BCollect.setOnClickListener(this);
        return root;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //采集
            case R.id.collect:
                wifiTool = new WifiUtils(getActivity());
                System.out.println("sdb");
                wifiPoint = wifiTool.getAllWifiMessage();
                //wifiFlag = collect();
                break;
            //更新
            case R.id.update:
                break;
        }
    }
    public WifiFlag collect(){

        WifiFlag flag = new WifiFlag();
        FingerPrint print = new FingerPrint();
        //flag.setAp1(wifiTool.getAllWifiMessage().get(1));
       // flag.setAp2(wifiTool.getAllWifiMessage().get(2));
        //flag.setAp3(wifiTool.getAllWifiMessage().get(3));
        //flag.setAp4(wifiTool.getAllWifiMessage().get(4));
        flag.setPrint(print);
        System.out.println(flag.toString());
        return flag;
    }

}
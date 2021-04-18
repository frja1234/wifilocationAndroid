package com.example.wifi.ui.set;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wifi.loginActivity;
import com.example.wifi.R;
import com.example.wifi.mapview.PinView;

import static android.content.Context.MODE_PRIVATE;

public class NotificationsFragment extends Fragment implements View.OnClickListener {



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_set, container, false);
        final Button register = root.findViewById(R.id.register);
        final TextView mSelectWifi = root.findViewById(R.id.selectWifi);
        final TextView mSelectMap = root.findViewById(R.id.selectMap);
        mSelectWifi.setOnClickListener(this);
        mSelectMap.setOnClickListener(this);
        register.setOnClickListener(this);
        return root;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //注册
            case R.id.register:
                System.out.println("shb");
                Intent intent = new Intent(getActivity().getApplicationContext(), loginActivity.class);
                startActivity(intent);
                break;
            case R.id.selectWifi:
                System.out.println("selectWifi");

                break;
            case R.id.selectMap:

                break;
        }
    }
    public void saveWifi(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("wifiName", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

    }


}
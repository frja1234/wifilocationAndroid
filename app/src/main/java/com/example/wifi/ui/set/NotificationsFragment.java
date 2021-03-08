package com.example.wifi.ui.set;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.wifi.loginActivity;
import com.example.wifi.R;
import com.example.wifi.mapview.PinView;

public class NotificationsFragment extends Fragment implements View.OnClickListener {



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_set, container, false);
        final Button register = root.findViewById(R.id.register);
        register.setOnClickListener(this);
        return root;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //采集
            case R.id.register:
                System.out.println("shb");
                Intent intent = new Intent(getActivity().getApplicationContext(), loginActivity.class);
                startActivity(intent);
                break;
        }
    }
}
package com.example.wifi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifi.Model.User;
import com.example.wifi.Utils.http.HttpUserUtils;

import java.util.Date;

public class registerActivity extends AppCompatActivity implements View.OnClickListener {
    private HttpUserUtils userUtils = new HttpUserUtils();
    private User user = new User();
    private String userName;
    private String userPassword;
    private String userId;
    private int authority;
    private TextView tUserName;
    private TextView tUserPassword;
    private TextView tUserId;
    private RadioButton userAuthority;
    private RadioButton managerAuthority;
    private Button register;


    private boolean userIsChecked;
    private boolean managerIsChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initData();


    }
    public void initView(){

        userAuthority = findViewById(R.id.radio1);
        tUserName = findViewById(R.id.user_register_name);
        tUserPassword = findViewById(R.id.user_register_password);
        tUserId = findViewById(R.id.user_register_id);
        register = findViewById(R.id.user_register);
        userAuthority = findViewById(R.id.radio1);
        managerAuthority = findViewById(R.id.radio2);

    }
    public void initData(){

        register.setOnClickListener(this);
        userIsChecked = userAuthority.isChecked();
        managerIsChecked = managerAuthority.isChecked();

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_register :
                userName = tUserName.getText().toString().trim();
                userPassword = tUserPassword.getText().toString().trim();
                userId = tUserId.getText().toString().trim();
                if(userIsChecked)authority = 1;
                else if(managerIsChecked)authority = 2;
                user.setUserName(userName);
                user.setPassword(userPassword);
                user.setUserAuthority(authority);
                user.setUserId(userId);
                user.setCreateTime(new Date());

                System.out.println(user.toString());
                if (user.toString().length()<=0)
                    Toast.makeText(this,"请正确输入",Toast.LENGTH_SHORT).show();
                else{

                    if(userUtils.register(user)){
                        Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(this,"注册失败",Toast.LENGTH_SHORT).show();
                    };

                }




                break;
        }

    }
}
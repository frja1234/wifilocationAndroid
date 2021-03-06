package com.example.wifi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifi.Utils.http.HttpUserUtils;

public class loginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tUserName;
    private TextView tUserPassword;
    private HttpUserUtils userUtils = new HttpUserUtils();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tUserName = findViewById(R.id.user_name);
        tUserPassword = findViewById(R.id.user_password);
        Button login = findViewById(R.id.user_login);
        TextView register = findViewById(R.id.set_register);
        register.setOnClickListener(this);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_login:
                String userName = tUserName.getText().toString().trim();
                String userPassword = tUserPassword.getText().toString().trim();

                if (userName.length()<=0 ||userPassword.length()<=0)
                    Toast.makeText(this,"请正确输入",Toast.LENGTH_SHORT).show();
                else

                if(userUtils.login(userName,userPassword)){
                    Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(this,"账号或密码错误",Toast.LENGTH_SHORT).show();
                };

                break;
            case R.id.set_register:
                Intent intent = new Intent(this, registerActivity.class);
                startActivity(intent);
                break;


        }
    }

}
package com.example.wifi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class registerActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner userAuthority;
    private ArrayList<String> list1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView tUserName = findViewById(R.id.user_register_name);
        TextView tUserPassword = findViewById(R.id.user_register_password);
        Button register = findViewById(R.id.user_register);
        userAuthority = findViewById(R.id.userAuthority);
        register.setOnClickListener(this);
        list1 = new ArrayList<>();
        list1.add("Android");
        list1.add("IOS");
        list1.add("H5");
        userAuthority.setAdapter(new MyAdapter());


    }
    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list1.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RecyclerView.ViewHolder holder ;
            if(convertView==null){
                convertView = LayoutInflater.from(registerActivity.this).inflate(R.layout.activity_register, viewGroup, false);
                holder = new RecyclerView.ViewHolder();
                holder.itemText= (TextView) convertView.findViewById(R.id.item_text);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.itemText.setText(list2.get(position));
            return convertView;
        }

    }


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_register :

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

                break;
        }

    }
}
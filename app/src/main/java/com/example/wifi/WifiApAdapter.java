package com.example.wifi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wifi.Model.wifi.WifiApList;

import java.util.List;

public class WifiApAdapter extends ArrayAdapter<WifiApList> {
    private int resourceId;

    // 适配器的构造函数，把要适配的数据传入这里
    public WifiApAdapter(Context context, int textViewResourceId, List<WifiApList> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    // convertView 参数用于将之前加载好的布局进行缓存
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        WifiApList wifiAplist=getItem(position); //获取当前项的Fruit实例

        // 加个判断，以免ListView每次滚动时都要重新加载布局，以提高运行效率
        View view;
        WifiApAdapter.ViewHolder viewHolder;
        if (convertView==null){

            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder= new WifiApAdapter.ViewHolder();
            viewHolder.ap1=view.findViewById(R.id.ap1);
            viewHolder.ap2=view.findViewById(R.id.ap2);
            viewHolder.ap3=view.findViewById(R.id.ap3);
            viewHolder.ap4=view.findViewById(R.id.ap4);
            viewHolder.createTime=view.findViewById(R.id.collectDate);
            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(WifiApAdapter.ViewHolder) view.getTag();
        }

        // 获取控件实例，并调用set...方法使其显示出来
        System.out.println(wifiAplist.toString());
        viewHolder.ap1.setText(wifiAplist.getAp1()+"");
        viewHolder.ap2.setText(wifiAplist.getAp2()+"");
        viewHolder.ap3.setText(wifiAplist.getAp3()+"");
        viewHolder.ap4.setText(wifiAplist.getAp4()+"");
        viewHolder.createTime.setText(wifiAplist.getCreateTime());
        return view;
    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder{
        TextView ap1;
        TextView ap2;
        TextView ap3;
        TextView ap4;
        TextView createTime;
    }

}

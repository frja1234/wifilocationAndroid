package com.example.wifi.ui.test;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.wifi.MainActivity;
import com.example.wifi.R;
import com.example.wifi.WifiService;
import com.example.wifi.mapview.PinView;
import com.example.wifi.mapview.StepView;
import com.example.wifi.step.OrientSensor;
import com.example.wifi.step.StepSensorAcceleration;
import com.example.wifi.step.StepSensorBase;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class DashboardFragment extends Fragment implements StepSensorBase.StepCallBack, OrientSensor.OrientCallBack,View.OnClickListener{

    private StepView mapView;
    private TextView mStepText;
    private TextView mOrientText;
    private TextView mLocationMapX;
    private TextView mLocationMapY;
    private boolean flag = true;
    private Button bStart;
    private Button bStop;
    private StepSensorBase mStepSensor; // 计步传感器
    private  OrientSensor mOrientSensor; // 方向传感器
    private int mStepLen = 50; // 步长
    private MyReceiver receiver=null;
    private static final int DEFAULT_TRAIN_TIME = 6000;
    private static final int REQUEST_PICK_MAP = 1;
    private static final int REQUEST_PERMISSION_CODE = 2;
    private static final String MAP_INFO = "map_info";
    private static final String MAP_PATH = "map_path";
    private static final String MAP_WIDTH = "width";
    private static final String MAP_height = "height";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location, container, false);
        mapView = root.findViewById(R.id.locationMapImageView);
        mStepText = (TextView) root.findViewById(R.id.step_text);
        mOrientText = (TextView) root.findViewById(R.id.orient_text);
        mLocationMapX = root.findViewById(R.id.locationMapX);
        mLocationMapY = root.findViewById(R.id.locationMapY);
        bStart = root.findViewById(R.id.locationStart);
        bStop = root.findViewById(R.id.locationStop);
        bStart.setOnClickListener( this);
        bStop.setOnClickListener(this);
        //注册广播接收器
        receiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.example.wifi.WifiService");
        getActivity().registerReceiver(receiver,filter);

        try {
            if (!tryLoadOldMap())
                selectMapFromPhone();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 注册计步监听
        mStepSensor = new StepSensorAcceleration(getActivity(), this);
        if (!mStepSensor.registerStep()) {
            Toast.makeText(getActivity(), "计步功能不可用！", Toast.LENGTH_SHORT).show();
        }
//        }
        // 注册方向监听
        mOrientSensor = new OrientSensor(getActivity(), this);
        if (!mOrientSensor.registerOrient()) {
            Toast.makeText(getActivity(), "方向功能不可用！", Toast.LENGTH_SHORT).show();
        }
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.locationStart:
                Intent intent = new Intent(getActivity(), WifiService.class);
                //我们启动一个Activity的时候是startActivity();
                //在这里我们是启动一个service
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            while (flag){
                                getActivity().startService(intent);
                                Thread.sleep(3000);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                break;
            case R.id.locationStop:
                intent = new Intent();
                intent.setClass(getActivity(), WifiService.class);
                getActivity().stopService(intent);
                flag = false;
                break;
        }

    }
    /**
     * 获取广播数据
     *
     * @author jiqinlin
     *
     */
    public class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("广播");
            Bundle bundle=intent.getExtras();
            float mapX = bundle.getFloat("mapX");
            float mapY = bundle.getFloat("mapY");
            mLocationMapX.setText(mapX+"");
            mLocationMapY.setText(mapY+"");

            mapView.getCoord(mapX,mapY);
        }

    }


    @Override
    public void Step(int stepNum) {           //使用接口来定义step方法的具体实现
        //  计步回调
        mStepText.setText(stepNum+"");
        mapView.autoAddPoint(mStepLen);
    }

    @Override
    public void Orient(int orient) {         //使用接口来定义Orient方法的具体实现
        // 方向回调
        mOrientText.setText(orient+"");
//        获取手机转动停止后的方向
//        orient = SensorUtil.getInstance().getRotateEndOrient(orient);
        mapView.autoDrawArrow(orient);
    }


    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    //导入以前的地图
    private boolean tryLoadOldMap() throws IOException {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MAP_INFO, MODE_PRIVATE);
        String path = sharedPreferences.getString(MAP_PATH, null);
        if (path == null)
            return false;
        else {
            float width = sharedPreferences.getFloat(MAP_WIDTH, 0);
            float height = sharedPreferences.getFloat(MAP_height, 0);
            loadMapImage(Uri.fromFile(new File(path)), width, height);
            return true;
        }
    }

    //保存地图
    private void saveMapInfo(Uri uri, float width, float height) {
        //SharedPreferences是一种轻量级的数据存储方式，采用键值对的存储方式。
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MAP_INFO, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MAP_PATH, getRealPathFromURI(uri));
        editor.putFloat(MAP_WIDTH, width);
        editor.putFloat(MAP_height, height);
        editor.apply();
    }
    //Pick picture from gallery is a uri not the actual file.
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }

        return result;
    }
    //选择地图
    public void selectMapFromPhone() {
        showToast("Please choose a image.");
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, REQUEST_PICK_MAP);  //one can be replaced with any action code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case REQUEST_PICK_MAP:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    System.out.println(selectedImage);
                    setMapWidthHeight(selectedImage);
                } else {
                    getActivity().finish();
                    showToast("You must pick map to train data.");
                }
                break;

            default:
                break;
        }
    }
    //设置地图宽高
    private void setMapWidthHeight(final Uri selectedImage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Map Info");

        // Get the layout inflater
        //LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化；对于一个没有被载入或者想要动态载入的界面，都需要使用LayoutInflater.inflate()来载入；
        LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_edit_map, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                float width = mapView.getWidth();
                float height = mapView.getHeight();
                saveMapInfo(selectedImage, width, height);
                System.out.println(width+height);
                try {
                    loadMapImage(selectedImage, width, height);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    //导入地图>设置地图宽高
    private void loadMapImage(final Uri selectedImage, float width, float height) throws IOException {
        Bitmap bitmap = null;
        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
        mapView.setImage(bitmap);
        mapView.setStepX(width);
        mapView.setStepY(height);
        //mapView.initialCoordManager(width, height);
        //mapView.setCurrentTPosition(new PointF(1.0f, 1.0f)); //initial current position
    }

}
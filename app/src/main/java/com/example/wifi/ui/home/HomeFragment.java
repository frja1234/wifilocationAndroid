package com.example.wifi.ui.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.wifi.Model.FingerPrint;
import com.example.wifi.Model.Map;
import com.example.wifi.Model.wifi.Wifi;
import com.example.wifi.Model.wifi.WifiApList;
import com.example.wifi.Model.wifi.WifiMap;
import com.example.wifi.Model.wifi.WifiMessageList;
import com.example.wifi.R;
import com.example.wifi.Utils.http.HttpMapUtils;
import com.example.wifi.Utils.http.HttpWifiUtils;
import com.example.wifi.Utils.wifi.WifiUtils;
import com.example.wifi.WifiApAdapter;
import com.example.wifi.WifiMessageAdapter;
import com.example.wifi.mapview.PinView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private PinView mapView;

    private Button BCollect;
    private Button BLoadMap;
    private Button BWifiMessage;
    private Button BWIfiAp;

    private TextView textViewX;
    private TextView textViewY;
    private TextView textViewStep;
    private ListView wifiList;
    private AlertDialog alertDialog;

    private WifiUtils wifiTool;
    private WifiMap wifiMap = new WifiMap();
    private HttpWifiUtils httpWifiUtils = new HttpWifiUtils();
    private HttpMapUtils httpMapUtils = new HttpMapUtils();
    private ArrayList<WifiMessageList> wifiListData = new ArrayList<>();
    private ArrayList<WifiApList> wifiApListData = new ArrayList<>();
    private Map map = new Map();
    private static final String TAG = "MainActivity";

    private static final int DEFAULT_TRAIN_TIME = 6000;
    private static final int REQUEST_PICK_MAP = 1;
    private static final int PERMISSION_GRANTED = 2;

    private static final String MAP_INFO = "map_info";
    private static final String MAP_PATH = "map_path";
    private static final String MAP_WIDTH = "map_width";
    private static final String MAP_height = "map_height";
    private  String MAP_NAME = "map_name";
    private int index = 0;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mapView = root.findViewById(R.id.mapImageView);
        BCollect = root.findViewById(R.id.collect);
        BLoadMap = root.findViewById(R.id.loadmap);
        BWifiMessage = root.findViewById(R.id.wifimessage);
        BWIfiAp = root.findViewById(R.id.selectMap);
        textViewX = root.findViewById(R.id.mapX);
        textViewY = root.findViewById(R.id.mapY);
        textViewStep = root.findViewById(R.id.mapStep);
        wifiList = root.findViewById(R.id.wifiList);
        textViewX.addTextChangedListener(textWatcher);
        textViewY.addTextChangedListener(textWatcher);
        textViewStep.addTextChangedListener(textWatcher);
        wifiTool = new WifiUtils(getActivity());
        BCollect.setOnClickListener(this);
        BLoadMap.setOnClickListener(this);
        BWifiMessage.setOnClickListener(this);
        BWIfiAp.setOnClickListener(this);
        requestPermission();
        try {
            if (!tryLoadOldMap())
                selectMapFromPhone();
        } catch (IOException e) {
            e.printStackTrace();
        }
        getWifiApMessage();
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //采集
            case R.id.collect:
                wifiTool = new WifiUtils(getActivity());
                if(wifiTool.wifiIsEnable()){
                    String X = textViewX.getText().toString().trim();
                    String Y = textViewY.getText().toString().trim();
                    Wifi wifi = wifiTool.wifiCollect(X, Y, MAP_NAME);
                    httpWifiUtils.wifiPointStore(wifi);
                    saveFingerprintData(wifi);
                    getWifiApMessage();
                }else showToast("wifi未开启");
                break;
            case R.id.loadmap:
                selectMapFromPhone();
                break;
            case R.id.wifimessage:
                getWifiMessage();
                break;
            case R.id.selectMap:
                showSingleAlertDialog(getView());
                break;
        }
    }

    //权限申请
    public void requestPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {//判断是否已经赋予权限
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //这里可以写个对话框之类的项向用户解释为什么要申请权限，并在对话框的确认键后续再次申请权限.它在用户选择"不再询问"的情况下返回false

            } else {
                //申请权限，字符串数组内是一个或多个要申请的权限，1是申请权限结果的返回参数，在onRequestPermissionsResult可以得知申请结果
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_WIFI_STATE,
                                Manifest.permission.CHANGE_WIFI_STATE,Manifest.permission.ACCESS_NETWORK_STATE,
                                Manifest.permission.CHANGE_NETWORK_STATE,Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }}


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    showToast("" + "权限" + permissions[i] + "申请成功");
                } else {
                    showToast("" + "权限" + permissions[i] + "申请失败");
                }
            }
        }
    }

    //地图弹出框
    public void showSingleAlertDialog(View view) {
        ArrayList<String> mapList = new ArrayList<>();
        mapList = mapList();
        String[] items = new String[mapList.size()];
        //String[] items = new String[0];
        for (int i = 0; i < mapList.size(); i++) {
            items[i] = mapList.get(i);
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("请选择地图");
        alertBuilder.setSingleChoiceItems(items, index, (dialogInterface, i) -> {
            showToast(items[1]);
            MAP_NAME = items[i];
            index = i;
        });

        alertBuilder.setPositiveButton("选择", (dialogInterface, i) -> {
            try {
                selectMap(MAP_NAME);
            } catch (IOException e) {
                e.printStackTrace();
            }
            alertDialog.dismiss();
        });

        alertBuilder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(deleteMap(MAP_NAME)) System.out.println("删除成功");
                else System.out.println("删除失败");
                showSingleAlertDialog(getView());
            }
        });

        alertDialog = alertBuilder.create();
        alertDialog.show();
    }


    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    //导入以前的地图
    private boolean tryLoadOldMap() throws IOException {
        SharedPreferences mapInfo = getActivity().getSharedPreferences(MAP_INFO, MODE_PRIVATE);
        String mapName = mapInfo.getString("currentMap", null);
        MAP_NAME = mapName;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(mapName, MODE_PRIVATE);
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

    //显示所有地图
    private ArrayList<String> mapList() {
        ArrayList<Map> mapList = new ArrayList<>();
        ArrayList<String> mapNameList = new ArrayList<>();
        mapList = httpMapUtils.getAllMap();
        for(Map map :mapList){
            mapNameList.add(map.getName());
        }
        return mapNameList;
    }

    //选择地图
    private void selectMap(String mapName) throws IOException {
        SharedPreferences mapInfo = getActivity().getSharedPreferences(MAP_INFO, MODE_PRIVATE);
        SharedPreferences.Editor mapEditor = mapInfo.edit();
        mapEditor.putString("currentMap", mapName);
        mapEditor.apply();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(mapName, MODE_PRIVATE);
        String path = sharedPreferences.getString(MAP_PATH, null);
        float width = sharedPreferences.getFloat(MAP_WIDTH, 0);
        float height = sharedPreferences.getFloat(MAP_height, 0);
        loadMapImage(Uri.fromFile(new File(path)), width, height);

    }

    //保存地图
    private void saveMapInfo(Uri uri, float width, float height, String mapName) {
        if(httpMapUtils.getMapByName(mapName).getName()!=null) showToast("该地图已经存在");
        else{
            SharedPreferences mapInfo = getActivity().getSharedPreferences(MAP_INFO, MODE_PRIVATE);
            SharedPreferences.Editor mapEditor = mapInfo.edit();//redPreferences是一种轻量级的数据存储方式，采用键值对的存储方式。
            mapEditor.putString("currentMap",mapName);
            mapEditor.apply();
            map.setName(mapName);
            map.setSizeX(width);
            map.setSizeY(height);
            map.setUrl(uri.toString());
            httpMapUtils.mapSave(map);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(mapName, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(MAP_PATH, getRealPathFromURI(uri));
            editor.putFloat(MAP_WIDTH, width);
            editor.putFloat(MAP_height, height);
            editor.apply();
        }

    }

    //删除地图
    private boolean deleteMap(String mapName) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(mapName, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear().apply();
        return  httpMapUtils.deleteMapByName(mapName);
    }

    //获取所有WiFi数据
    private void getWifiMessage() {
        wifiListData.clear();
        wifiTool = new WifiUtils(getActivity());
        WifiMessageList wifiListFirst = new WifiMessageList("id", "name", "level", true);
        wifiListData.add(wifiListFirst);
        for (WifiMessageList wifilist : wifiTool.getWifiListData()) {
            wifiListData.add(wifilist);
        }
        // 先拿到数据并放在适配器上
        WifiMessageAdapter adapter = new WifiMessageAdapter(getActivity(), R.layout.wifi_message_item, wifiListData);
        wifiList.setAdapter(adapter);
    }

    //获取该点指纹
    public void getWifiApMessage() {
        wifiApListData.clear();
        WifiApList wifiApList = new WifiApList();
        String X = textViewX.getText().toString().trim();
        String Y = textViewY.getText().toString().trim();
        Wifi wifi = new Wifi();
        wifi.setMapX(X);
        wifi.setMapY(Y);
        wifi.setMapName(MAP_NAME);
        wifi = httpWifiUtils.getWifiAp(wifi);
        System.out.println(wifi);
        wifiApList.setAp1(wifi.getAp1());
        wifiApList.setAp2(wifi.getAp2());
        wifiApList.setAp3(wifi.getAp3());
        wifiApList.setAp4(wifi.getAp4());
        wifiApList.setCreateTime(wifi.getCreateTime());
        wifiApListData.add(wifiApList);
        WifiApAdapter adapter = new WifiApAdapter(getActivity(), R.layout.wifi_ap_item, wifiApListData);
        wifiList.setAdapter(adapter);
    }


    //获取绝对路径
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

    //检测文本状态，TextWatcher作用为监测键盘输入并根据输入内容展示不同显示效果，当文本改变时及时修改相应的值
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.hashCode() == textViewStep.getText().hashCode()) {
                if (textViewStep.getText().toString().trim().equals("")) {
                    showToast("Stride length can't be null");
                } else {
                    float strideLength = Float.valueOf(textViewStep.getText().toString().trim());
                    mapView.setStride(strideLength);
                }
            } else if (ifUserInput) {
                if (!textViewX.getText().toString().equals("") && !textViewY.getText().toString().equals("")) {
                    PointF p = new PointF(Float.valueOf(textViewX.getText().toString().trim()),
                            Float.valueOf(textViewY.getText().toString().trim()));
                    mapView.setCurrentTPosition(p);
                    getWifiApMessage();
                }
            }
        }
    };

    //检查已完成的点,判断是收集数据还是定位，检查已经采集完指纹的点
    private void checkFinishedPoints() {
        //String type = typeRadioButton.isChecked() ? "train" : "test";
        String type = "0";
        System.out.println("检查完成的点");

        List<FingerPrint> fingerprints = new ArrayList<>();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MAP_NAME, MODE_PRIVATE);
        System.out.println(sharedPreferences.getFloat(MAP_WIDTH, 0) + "zhub");
        for (int i = 1; i <= sharedPreferences.getFloat(MAP_WIDTH, 0); i++) {
            for (int j = 1; j <= sharedPreferences.getFloat(MAP_height, 0); j++) {
                String data = sharedPreferences.getString(i + ".00" + "+" + j + ".00", null);
                if (data != null) {
                    System.out.println("数据不为空");
                    String[] dataList = data.split(" ");
                    fingerprints.add(new FingerPrint(Float.valueOf(dataList[0]), Float.valueOf(dataList[1])));
                }
            }
        }
        mapView.setFingerprintPoints(fingerprints);
        showToast(type + " points number:" + fingerprints.size());
    }

    //选择地图
    public void selectMapFromPhone() {
        showToast("选择一张图片.");
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

        final EditText editMapWidth = view.findViewById(R.id.map_width);
        final EditText editMapHeight = view.findViewById(R.id.map_height);
        final EditText editMapName = view.findViewById(R.id.map_name);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //float width = mapView.getWidth();
                //float height = mapView.getHeight();
                float width = Float.valueOf(editMapWidth.getText().toString().trim());
                float height = Float.valueOf(editMapHeight.getText().toString().trim());
                String name = editMapName.getText().toString().trim();
                saveMapInfo(selectedImage, width, height, name);
                System.out.println(width + height);
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

    //更新收集状态
    private void updateCollectStatus(final WifiMap fingerprint) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BCollect.setClickable(true);
                BCollect.setText(getResources().getText(R.string.start));//更新按钮文本，正在扫描....
                mapView.addFingerprintPoint(fingerprint);//标注该点
            }
        });
    }

    //保存点击数据
    private void saveFingerprintData(final Wifi wifiData) {
        PointF pos = mapView.getCurrentTCoord();
        WifiMap fingerprint = new WifiMap(String.valueOf(pos.x), String.valueOf(pos.y));
        updateCollectStatus(fingerprint);
        String data = String.format(Locale.ENGLISH, "%s %s %s %s %s %s %s", wifiData.getMapX(), wifiData.getMapY(), wifiData.getAp1(), wifiData.getAp2(), wifiData.getAp3(), wifiData.getAp4(), wifiData.getCreateTime());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MAP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        System.out.println(data + "保存的数据");
        editor.putString(wifiData.getMapY() + "+" + wifiData.getMapY(), data);
        editor.apply();
        //Logger.saveFingerprintData("0", wifiData);
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    //导入地图>设置地图宽高
    private void loadMapImage(final Uri selectedImage, float width, float height) throws IOException {
        Bitmap bitmap = null;
        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
        mapView.setImage(ImageSource.bitmap(bitmap));
        mapView.initialCoordManager(width, height);
        mapView.setCurrentTPosition(new PointF(1.0f, 1.0f)); //initial current position
        //textViewX.setText(String.format(Locale.ENGLISH, "X(max:%.1f)", width));
        //textViewY.setText(String.format(Locale.ENGLISH, "Y(max:%.1f)", height));
        checkFinishedPoints();
        setGestureDetectorListener(true);
    }
    //监听手势触摸

    private GestureDetector gestureDetector = null;//用户手势检测

    private void setGestureDetectorListener(boolean enable) {
        if (!enable)
            mapView.setOnTouchListener(null);
        else {
            if (gestureDetector == null) {
                gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {
                        if (mapView.isReady()) {
                            mapView.moveBySingleTap(e);
                            setTextWithoutTriggerListener();
                        } else {
                            Toast.makeText(getContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
            }
            //若检测则设置触摸事件
            mapView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return gestureDetector.onTouchEvent(motionEvent);
                }
            });
        }
    }

    private boolean ifUserInput = true;

    private void setTextWithoutTriggerListener() {
        ifUserInput = false;

        textViewX.setText(String.format(Locale.ENGLISH, "%.2f", mapView.getCurrentTCoord().x));
        textViewY.setText(String.format(Locale.ENGLISH, "%.2f", mapView.getCurrentTCoord().y));
        getWifiApMessage();

        ifUserInput = true;
    }
}
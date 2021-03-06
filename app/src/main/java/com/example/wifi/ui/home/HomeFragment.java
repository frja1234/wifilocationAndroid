package com.example.wifi.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.wifi.Model.FingerPrint;
import com.example.wifi.Model.wifi.WifiMap;
import com.example.wifi.Model.wifi.WifiSignal;
import com.example.wifi.R;
import com.example.wifi.Utils.Logger;
import com.example.wifi.Utils.http.HttpWifiUtils;
import com.example.wifi.Utils.wifi.WifiUtils;
import com.example.wifi.mapview.PinView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private PinView mapView;
    private WifiUtils wifiTool;
    private Button BCollect;
    private Button BLoadMap;
    private TextView textViewX;
    private TextView textViewY;
    private TextView textViewStep;
    private WifiMap wifiMap = new WifiMap();
    private WifiSignal wifiSignal = new WifiSignal();
    private HttpWifiUtils httpWifiUtils = new HttpWifiUtils();

    private static final String TAG = "MainActivity";

    private static final int DEFAULT_TRAIN_TIME = 6000;
    private static final int REQUEST_PICK_MAP = 1;
    private static final int REQUEST_PERMISSION_CODE = 2;



    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mapView = root.findViewById(R.id.mapImageView);
        BCollect = root.findViewById(R.id.collect);
        BLoadMap = root.findViewById(R.id.loadmap);
        textViewX = root.findViewById(R.id.mapX);
        textViewY = root.findViewById(R.id.mapY);
        textViewStep = root.findViewById(R.id.mapStep);
        textViewX.addTextChangedListener(textWatcher);
        textViewY.addTextChangedListener(textWatcher);
        textViewStep.addTextChangedListener(textWatcher);
        BCollect.setOnClickListener(this);
        BLoadMap.setOnClickListener(this);
        try {
            if (!tryLoadOldMap())
                selectMapFromPhone();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //采集
            case R.id.collect:
                wifiTool = new WifiUtils(getActivity());
                wifiMap.setWifiMapX(textViewX.getText().toString().trim());
                wifiMap.setWifiMapY(textViewY.getText().toString().trim());
                wifiSignal = wifiTool.collect(wifiMap);
                System.out.println(wifiSignal.toString());
                httpWifiUtils.wifiPointStore(wifiSignal);
                System.out.println("sdb");
                break;
            //更新
            case R.id.update:

            case R.id.loadmap:
                selectMapFromPhone();

                break;
        }
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private static final String MAP_INFO = "map_info";
    private static final String MAP_PATH = "map_path";
    private static final String MAP_WIDTH = "width";
    private static final String MAP_height = "height";
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
                    float strideLength = Float.valueOf(textViewStep.getText().toString());
                    mapView.setStride(strideLength);
                }
            } else if (ifUserInput) {
                if (!textViewX.getText().toString().equals("") && !textViewY.getText().toString().equals("")) {
                    PointF p = new PointF(Float.valueOf(textViewX.getText().toString()),
                            Float.valueOf(textViewY.getText().toString()));
                    mapView.setCurrentTPosition(p);
                }
            }
        }
    };
    //检查已完成的点,判断是收集数据还是定位，检查已经采集完指纹的点
    private void checkFinishedPoints() {
        //String type = typeRadioButton.isChecked() ? "train" : "test";
        String type = "0";

        List<FingerPrint> fingerprints = new ArrayList<>();
        for (PointF p : Logger.getCollectedGrid(type)) {
            fingerprints.add(new FingerPrint(p.x, p.y));
        }

        mapView.setFingerprintPoints(fingerprints);
        showToast(type + " points number:" + fingerprints.size());
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

        final EditText editMapWidth = view.findViewById(R.id.map_width);
        final EditText editMapHeight = view.findViewById(R.id.map_height);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                float width = Float.valueOf(editMapWidth.getText().toString().trim());
                float height = Float.valueOf(editMapHeight.getText().toString().trim());
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
    //更新收集状态
    private void updateCollectStatus(final FingerPrint fingerprint) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                BCollect.setClickable(true);
                BCollect.setText(getResources().getText(R.string.start));//更新按钮文本，正在扫描....
                mapView.addFingerprintPoint(fingerprint);//标注该点
            }
        });
    }

    private static int getPowerOfTwoForSampleRatio(double ratio){
        int k = Integer.highestOneBit((int)Math.floor(ratio));
        if(k==0) return 1;
        else return k;
    }

    //导入地图>设置地图宽高
    private void loadMapImage(final Uri selectedImage, float width, float height) throws IOException {
        Bitmap bitmap = null;
        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
            mapView.setImage(ImageSource.bitmap(bitmap));
            mapView.initialCoordManager(width, height);
            mapView.setCurrentTPosition(new PointF(1.0f, 1.0f)); //initial current position
            textViewX.setText(String.format(Locale.ENGLISH, "X(max:%.1f)", width));
            textViewX.setText(String.format(Locale.ENGLISH, "Y(max:%.1f)", height));
            checkFinishedPoints();
            setGestureDetectorListener(true);
    }
    //监听手势触摸

    private GestureDetector gestureDetector = null;//用户手势检测

    private void setGestureDetectorListener(boolean enable){
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

        ifUserInput = true;
    }
}
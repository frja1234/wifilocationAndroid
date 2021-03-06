package com.example.wifi.ui.test;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.wifi.R;
import com.example.wifi.mapview.PinView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class DashboardFragment extends Fragment {

    private PinView mapView;
    private static final int DEFAULT_TRAIN_TIME = 6000;
    private static final int REQUEST_PICK_MAP = 1;
    private static final int REQUEST_PERMISSION_CODE = 2;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location, container, false);
        mapView = root.findViewById(R.id.locationMapImageView);
        try {
            if (!tryLoadOldMap())
                selectMapFromPhone();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
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
        mapView.setImage(ImageSource.bitmap(bitmap));
        mapView.initialCoordManager(width, height);
        mapView.setCurrentTPosition(new PointF(1.0f, 1.0f)); //initial current position
    }
}
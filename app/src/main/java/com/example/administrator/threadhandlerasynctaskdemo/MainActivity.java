package com.example.administrator.threadhandlerasynctaskdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnDownLoadBitmapListener {


    private ImageView imgDownload;
    private TextView txtProgress;

    private MyAsyncTask myAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgDownload = findViewById(R.id.img_download);
        txtProgress = findViewById(R.id.txt_percent);
        findViewById(R.id.btn_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAsyncTask.execute("http://v.phinf.naver.net/20180209_199/1518162228402UdBSG_JPEG/upload_%BA%ED%C7%CE%C7%CF%BF%EC%BD%BA6-4.jpg");
            }
        });

        myAsyncTask = new MyAsyncTask(this);
        requestPermission();
    }

    @Override
    public void onDownloadError() {
        Toast.makeText(this, "Đã có lỗi xảy ra", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloading(int progress) {
        txtProgress.setText(String.valueOf(progress) + " %");
    }

    @Override
    public void onDownloadSuccess(Bitmap bitmap) {
        imgDownload.setImageBitmap(bitmap);
    }
    public void requestPermission() {
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
        };
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Toast.makeText(this, "Permission deny", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



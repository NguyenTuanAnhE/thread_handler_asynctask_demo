package com.example.administrator.threadhandlerasynctaskdemo;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 4/25/2018.
 */

public interface OnDownLoadBitmapListener {

    void onDownloadError();

    void onDownloading(int progress);

    void onDownloadSuccess(Bitmap bitmap);


}

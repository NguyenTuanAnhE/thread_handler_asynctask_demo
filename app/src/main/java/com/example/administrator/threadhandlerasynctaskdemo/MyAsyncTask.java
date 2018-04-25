package com.example.administrator.threadhandlerasynctaskdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 4/25/2018.
 */

public class MyAsyncTask extends AsyncTask<String, Integer, Bitmap> {
    private static final String TAG = MyAsyncTask.class.getSimpleName();
    private OnDownLoadBitmapListener onDownLoadBitmapListener;
    int progress;

    public MyAsyncTask(OnDownLoadBitmapListener onDownLoadBitmapListener) {
        this.onDownLoadBitmapListener = onDownLoadBitmapListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        OutputStream output = null;
        try {
            String link = strings[0];
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                onDownLoadBitmapListener.onDownloadError();
                return null;
            }

            int fileLength = connection.getContentLength();
            Log.d("hi", "doInBackground: " + fileLength);
            InputStream inputStream = connection.getInputStream();
            File imgFile = new  File("/sdcard/file_name.jpg");
            output = new FileOutputStream(imgFile);
            byte data[] = new byte[1024];
            long total = 0;
            int count = 0;
            while ((count = inputStream.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    inputStream.close();
                    return null;
                }
                total += count;
                Log.d("hi", "doInBackground: total " + total);
                // publishing the progress....
                if (fileLength > 0) {
                    // only if total length is known
                    progress = (int) (total * 100 / fileLength);
                    publishProgress(progress);
                    output.write(data, 0, count);
                }
            }
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inJustDecodeBounds = true;
//            Bitmap bitmap = BitmapFactory.decodeStream(inputStream,null,options);
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            connection.disconnect();
            inputStream.close();
            output.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
            onDownLoadBitmapListener.onDownloadError();
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        onDownLoadBitmapListener.onDownloading(values[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Log.d(TAG, "onDownloadSuccess: "+ bitmap);
        Log.d("hi", "onPostExecute: " + progress);
        onDownLoadBitmapListener.onDownloadSuccess(bitmap);

    }
}

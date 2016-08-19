package com.androidxx.yangjw.day18_update_homework;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "androidxx";
    private NotificationManager notificationManager;

    private Notification.Builder builder;
    String path;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    showDialog(msg.obj.toString());
                    break;
                case 1:
                    intall();
                    break;
            }
        }
    };

    private void intall() {
        File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(externalStoragePublicDirectory + "/abc.apk");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        startActivity(intent);

    }

    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("版本更新");
        builder.setMessage(msg);

        try {
            JSONObject jsonObject = new JSONObject(msg);
            path = jsonObject.getString("path");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        builder.setNegativeButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download(path);
                dialog.dismiss();
            }
        });

        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
//        alertDialog.setCancelable(false);
        alertDialog.show();

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        update();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private void showNotification() {
        builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("正在下载...");
        Notification notification = builder.getNotification();
        notificationManager.notify(88002201,notification);
    }

    private void download(final String path) {

        final File externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(externalStoragePublicDirectory + "/abc.apk");
        if (file.exists()) {
            Toast.makeText(MainActivity.this, "已经下载", Toast.LENGTH_SHORT).show();
            intall();
            return;
        }

        showNotification();
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                try {
                    URL url = new URL(path);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();

                    if (urlConnection.getResponseCode() == 200) {
                        fileOutputStream = new FileOutputStream(externalStoragePublicDirectory+"/abc.apk");

                        inputStream = urlConnection.getInputStream();
                        int len = 0;
                        byte[] buffer = new byte[1024];
                        Log.d(TAG, "run: ");

                        builder.setProgress(100,1,true);
                        notificationManager.notify(88002201,builder.getNotification());
                        while((len = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer,0,len);
                            Log.d(TAG, "run: =---->");
                        }
                        fileOutputStream.flush();

                        builder.setProgress(100,100,false);
                        builder.setContentTitle("提示");
                        builder.setContentText("下载完成");
                        notificationManager.notify(88002201,builder.getNotification());
                        Message message = mHandler.obtainMessage();
                        message.what = 1;
                        message.sendToTarget();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    close(inputStream);
                    close(fileOutputStream);
                }

            }
        }).start();
    }

    private void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void update() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://au.umeng.com/api/check_app_update");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.getOutputStream().write("content={\"delta\":false,\"package\":\"com.shunyou.gifthelper\",\"appkey\":\"54d32548fd98c5fd57000a56\",\"sdk_version\":\"2.5.0.20141210\",\"type\":\"update\",\"channel\":\"1688wan\",\"old_md5\":\"9be6f645823329d5bb70d76008edd6a3\",\"proto_ver\":\"1.4\",\"idmd5\":\"528447f4ffb4e4824a2fd1d1f0cd62\",\"version_code\":\"10\"}".getBytes());
                    urlConnection.getOutputStream().flush();
                    urlConnection.connect();
                    if (urlConnection.getResponseCode() == 200) {
                        InputStream inputStream = urlConnection.getInputStream();
                        int len = 0;
                        byte[] buffer = new byte[1024];
                        StringBuilder builder = new StringBuilder();
                        while ((len = inputStream.read(buffer)) != -1) {
                            builder.append(new String(buffer,0,len));
                        }

                        Message message = mHandler.obtainMessage();
                        message.obj = builder.toString();
                        message.what = 0;
                        message.sendToTarget();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

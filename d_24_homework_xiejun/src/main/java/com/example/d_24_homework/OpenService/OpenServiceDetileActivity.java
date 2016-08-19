package com.example.d_24_homework.OpenService;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidxx.yangjw.httplibrary.HttpUtils;
import com.androidxx.yangjw.httplibrary.ICallback;
import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.d_24_homework.R;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/8/16.
 */
public class OpenServiceDetileActivity extends AppCompatActivity implements ICallback{
    private ActionBar mActionBar;
    private Context context;
    private TextView mTv,mTitle,mType,mSize,mIntroduce;
    private ImageView mLogoIv;
    private ViewPager mVp;
    private Button mBackBtn,downBtn;
    private String down_addr="";
    private String size="";
    private Random random;
    private int notifiNUmber;
    private final String TAG="androidxx";
    private NotificationManager mManager;
    private Notification.Builder builder;
    private String gName;
    private List<String>imgList=new ArrayList<>();
    private final String URL_PATH="http://www.1688wan.com/majax.action?method=getAppInfo";
    private final String BODY="http://www.1688wan.com";
    private Map<String,Object>bodyMap=new HashMap<>();
    private ExecutorService mExeService;
    private File externalStoragePublicDirectory;
    private HorizontalScrollView mScrollView;
    private LinearLayout mLinearLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_service_righr_detile_layout);
        mExeService= Executors.newFixedThreadPool(3);
        context=this;
        random=new Random();
        mManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initView();
        initActionBar();
         HttpUtils.load(URL_PATH).post(bodyMap).callback(this,3);
    }

    private void initView() {
        mScrollView= (HorizontalScrollView) findViewById(R.id.open_right_detile_scroll_view);
        mLinearLayout= (LinearLayout) findViewById(R.id.open_right_detile_linear_layout);
        mTitle= (TextView) findViewById(R.id.open_right_detile_title);
        mType= (TextView) findViewById(R.id.open_right_detile_type);
        mSize= (TextView) findViewById(R.id.open_right_detile_size);
        mLogoIv= (ImageView) findViewById(R.id.open_service_right_img);
        mIntroduce= (TextView) findViewById(R.id.open_right_introduce);
        downBtn= (Button) findViewById(R.id.detile_down_load_btn);
        downBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(null);
            }
        });
    }
    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("下载提醒");
        builder.setMessage("名称："+down_addr);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!TextUtils.isEmpty(down_addr)){
                    showNotification();
                    download(down_addr);
                    dialog.dismiss();
                }else{
                    Toast.makeText(OpenServiceDetileActivity.this, "暂不支持下载", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
    }

    private void install(String path) {
        String pathUrl=getPath(path);
        externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(externalStoragePublicDirectory + pathUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        startActivity(intent);

    }
    private String getPath(String url){
        int startIndex=url.lastIndexOf("/");
        String pathUrl=url.substring(startIndex,url.length());
        if(TextUtils.isEmpty(pathUrl)){
            return null;
        }else{
            return pathUrl;
        }
    }
    private void download(final String path) {
        String pathUrl=getPath(path);
       if(TextUtils.isEmpty(pathUrl)){
           return ;
       }
        externalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(externalStoragePublicDirectory + pathUrl);
        if(file.exists()){
            Toast.makeText(OpenServiceDetileActivity.this, "已经下载", Toast.LENGTH_SHORT).show();
            install(path);
            return;
        }
            downBtn.setText("正在下载");
            downBtn.setEnabled(false);
            downBtn.setBackgroundColor(Color.GRAY);
            ExecutorTool.executorService.execute(new MyRunnable(path));
    }
    class MyRunnable implements Runnable {
        private String path;
        public MyRunnable(String path){
            this.path=path;
        }
        @Override
        public void run() {
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                URL url = new URL(path);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    String pathUrl=getPath(path);
                    if(TextUtils.isEmpty(pathUrl)){
                        return;
                    }
                    fileOutputStream = new FileOutputStream(externalStoragePublicDirectory+pathUrl);
                    inputStream = urlConnection.getInputStream();
                    int len = 0;
                    byte[] buffer = new byte[1024];
                    int totalLenth=urlConnection.getContentLength();
                    int loadLen = 0;
                    while((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer,0,len);
                        loadLen+=len;
                        int progress=loadLen*100/totalLenth;
                        Log.d(TAG, "run: "+totalLenth);
                        Log.d(TAG, "run: "+loadLen);
                        builder.setContentTitle(gName+"--正在下载");
                        builder.setProgress(totalLenth,loadLen,false);
                        builder.setContentText("下载进度:"+progress+"%");
                        mManager.notify(notifiNUmber,builder.getNotification());
                    }
                    fileOutputStream.flush();
                    builder.setProgress(100,100,false);
                    builder.setContentTitle("提示");
                    builder.setContentText("下载完成");
                    mManager.notify(notifiNUmber,builder.getNotification());
                    Message message=mHandler.obtainMessage();
                    message.what=2;
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
    }
    private void showNotification() {
        builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(gName+"--准备下载...");
        Notification notification = builder.getNotification();
        notifiNUmber=random.nextInt(Integer.MAX_VALUE);
        mManager.notify(notifiNUmber,notification);
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
    private void initActionBar() {
        Intent intent = getIntent();
        gName=intent.getStringExtra("gName");
        String gId=intent.getStringExtra("gId");
        bodyMap.put("id",gId);
        View view= LayoutInflater.from(context).inflate(R.layout.open_detile_action_bar,null,false);
        mActionBar=getSupportActionBar();
        mActionBar.setCustomView(view);
        mTv= (TextView) view.findViewById(R.id.open_right_detile_game_name);
        mTv.setText(gName);
        mBackBtn= (Button) view.findViewById(R.id.open_right_detile_back_btn);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setTitle("");
    }
    public void onBackClick(View view){
        finish();
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 1:
                    for (int i = 0; i < imgList.size(); i++) {
                        ImageView imageView=new ImageView(context);
                        TextView textView=new TextView(context);
                        imageView.setImageResource(R.drawable.ic_launcher);
                        ImageLoader.init(context).load(BODY+imgList.get(i),imageView);
                        imageView.setLayoutParams(new LinearLayout.LayoutParams(550, ViewGroup.LayoutParams.MATCH_PARENT));
                        textView.setLayoutParams(new LinearLayout.LayoutParams(5,ViewGroup.LayoutParams.MATCH_PARENT));
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        mLinearLayout.addView(imageView);
                        mLinearLayout.addView(textView);
                    }
                    break;
                case 2:
                    install(down_addr);
                    break;
            }

        }
    };
    @Override
    public void success(String result, int requestCode) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            String jsonStr=jsonObject.getString("app");
            JSONArray jsonArray=jsonObject.getJSONArray("img");
            JSONObject jsonObject1=new JSONObject(jsonStr);
            String title=jsonObject1.getString("name");
            String type=jsonObject1.getString("typename");
            size="未知";
            String imgUrl=jsonObject1.getString("logo");
            String introduce=jsonObject1.getString("description");
            down_addr=jsonObject1.getString("download_addr");
            if(TextUtils.isEmpty(down_addr)){
                downBtn.setBackgroundColor(Color.GRAY);
                downBtn.setText("暂无下载");
                downBtn.setEnabled(false);
            }
            ImageLoader.init(context).load(BODY+imgUrl,mLogoIv);
            mTitle.setText(title);
            mType.setText(type);
            mSize.setText(size);
            mIntroduce.setText(introduce);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject2=jsonArray.getJSONObject(i);
                String url=jsonObject2.getString("address");
                imgList.add(url);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Message message = mHandler.obtainMessage();
        message.what=1;
        message.sendToTarget();
    }
}

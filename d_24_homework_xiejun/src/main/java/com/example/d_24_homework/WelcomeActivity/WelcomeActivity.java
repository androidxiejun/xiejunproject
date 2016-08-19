package com.example.d_24_homework.WelcomeActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.example.d_24_homework.MainActivity;
import com.example.d_24_homework.R;

/**
 * Created by Administrator on 2016/8/18.
 */
public class WelcomeActivity extends AppCompatActivity{
    private Context context;
    private ActionBar actionBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        actionBar=getSupportActionBar();
        actionBar.hide();
//        ActionBar supportActionBar = getSupportActionBar();
//        supportActionBar.hide();
        context=this;
        mHandler.sendEmptyMessageDelayed(1,2000);
    }
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Intent intent=new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
}

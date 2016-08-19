package com.androidxx.yangjw.day18_update_homework;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 1、自动更新
 *
 */
public class WelcomeActivity extends AppCompatActivity {

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler.sendEmptyMessageDelayed(1,2000);
    }
}

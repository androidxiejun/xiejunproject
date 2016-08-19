package com.example.servicetext;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/8/18.
 */
public class Myservice extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }
    class MyBinder extends Binder{
        public Myservice getService(){
            return Myservice.this;
        }
    }
    public String getStr(String str){
        return str;
    }
}

package com.example.servicetext;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Myservice myservice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(this,Myservice.class);
        bindService(intent,serviceConnection, Service.BIND_AUTO_CREATE);
    }
    ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Myservice.MyBinder myBinder= (Myservice.MyBinder) iBinder;
            myservice=myBinder.getService();
            String str = myservice.getStr("xiejun");
            Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
}

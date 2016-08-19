package com.example.d_24_homework.DetileActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.d_24_homework.R;

/**
 * Created by Administrator on 2016/8/15.
 */
public class LoginActivity extends AppCompatActivity {
    private ActionBar mActionBar;
    private Context context;
    private Button backBtn;
    private TextView registerTv;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);
        context=this;
        initActionBar();
        registerTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    private void initActionBar(){
        View view= LayoutInflater.from(context).inflate(R.layout.login_action_bar,null);
        backBtn= (Button) view.findViewById(R.id.login_back_btn);
        mActionBar=getSupportActionBar();
        mActionBar.setTitle("");
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(view);
        registerTv= (TextView) view.findViewById(R.id.login_detile_register);

    }
public void onClick(View view){
    finish();
}
}

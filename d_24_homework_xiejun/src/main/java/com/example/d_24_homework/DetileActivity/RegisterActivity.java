package com.example.d_24_homework.DetileActivity;

import android.content.Context;
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
 * Created by Administrator on 2016/8/17.
 */
public class RegisterActivity extends AppCompatActivity {
    private Context context;
    private Button backBtn;
    private ActionBar mActionBar;
    private TextView registerTv,registerTitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.register_layout);
        initActionBar();
    }

    private void initActionBar() {
        View view= LayoutInflater.from(context).inflate(R.layout.login_action_bar,null);
        backBtn= (Button) view.findViewById(R.id.login_back_btn);
        mActionBar=getSupportActionBar();
        mActionBar.setTitle("");
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(view);
        registerTitle= (TextView) view.findViewById(R.id.login_tv);
        registerTitle.setText("会员注册");
        registerTv= (TextView) view.findViewById(R.id.login_detile_register);
        registerTv.setText("");
    }
    public void onClick(View view){
        finish();
    }
}

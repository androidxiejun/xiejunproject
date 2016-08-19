package com.androidxx.yangjw.day21_toolbar_helloworld;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

/**
 * ToolBar的使用
 * 步骤：
 * 1、去掉ActionBar：修改样式主题：Theme.AppCompat.Light.NoActionBar
 * 2、初始化ToolBar
 * 3、将ToolBar放在ActionBar的位置
 */
public class MainActivity extends AppCompatActivity {

    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar();
    }

    private void initToolBar() {
        mToolBar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolBar);
        setTitle("ToolBar演示");
//        mToolBar.setTitle("主标题");
        mToolBar.setSubtitle("副标题");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_menu,menu);
        return true;
    }
}

package com.androidxx.yangjw.day19_viewpager_tablayout;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * TabLayout的基本使用
 * 5.0之后的新控件。design
 * 1、导入design包
 * 2、在布局文件中创建Tablayout标签
 * 3、在Activity中初始化Tablayout对象
 * 4、在TabLayout中添加Item（Tab）
 */
public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    private ViewPager mVIewPager;
    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private MyPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        loadDatas();
        mTabLayout = (TabLayout) findViewById(R.id.main_tab_layout);
        mVIewPager = (ViewPager) findViewById(R.id.main_view_pager);
        /**
         * MODE_SCROLLABLE:允许TabLayout滚动
         * MODE_FIXED:Tab平分宽度，不能滚动
         */
//        mTabLayout.addTab(mTabLayout.newTab().setText("精选").setIcon(R.mipmap.ic_launcher));
//        mTabLayout.addTab(mTabLayout.newTab().setText("送女票"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("送女票"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("送女票"));
//        mTabLayout.addTab(mTabLayout.newTab().setText("送女票"));
        //指定选择的Tab位置
//        mTabLayout.getTabAt(1).select();

        //初始化适配器
        mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        mVIewPager.setAdapter(mPagerAdapter);

        //TabLayout和ViewPager 进行关联
        mTabLayout.setupWithViewPager(mVIewPager);

    }

    private void loadDatas() {
        for (int i = 0; i < 2; i++) {
            fragments.add(MyFragment.newInstance());
            titles.add("TITLE" + i);
        }
    }


    class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}

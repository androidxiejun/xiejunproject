package com.androidxx.yangjw.day29_pulltorefresh_listview;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 下拉刷新的ListView
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "androidxx";
    private PullToRefreshListView mRefreshListView;
    private List<String> datas = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //加载数据完成之后，通知刷新控件结束刷新动作
            mRefreshListView.onRefreshComplete();
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadDatas();
    }

    private void loadDatas() {
        for (int i = 0; i < 20; i++) {
            datas.add("ITEM" + i);
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mRefreshListView = (PullToRefreshListView) findViewById(R.id.pulltorefresh_listview);
        mRefreshListView.setLastUpdatedLabel("上次刷新:2016-8-11");
        //设置支持加载更多功能
        //同时支持刷新和加载更多功能
        mRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        setupAdapter();
        setupRefreshControl();
    }

    /**
     * 监听下拉刷新的动作
     */
    private void setupRefreshControl() {
        mRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            /**
             * 下拉刷新
             * @param refreshView
             */
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                Log.d(TAG, "onPullDownToRefresh: ");


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        datas.clear();
                        //需要重新加载数据
                        for (int i = 0; i < 20; i++) {
                            datas.add("刷新" + i);
                        }

                        mHandler.sendEmptyMessageDelayed(1,100);
                    }
                }).start();


            }

            /**
             * 加载更多
             * @param refreshView
             */
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                for (int i = 0; i < 20; i++) {
                    datas.add("更多" + i);
                }
                mHandler.sendEmptyMessageDelayed(1,100);
            }
        });
    }

    private void setupAdapter() {
        mAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,datas);
        mRefreshListView.setAdapter(mAdapter);

    }


}

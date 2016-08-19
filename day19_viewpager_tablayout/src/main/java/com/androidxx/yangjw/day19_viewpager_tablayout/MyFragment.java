package com.androidxx.yangjw.day19_viewpager_tablayout;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by yangjw on 2016/7/30.
 */
public class MyFragment extends Fragment {

    private Context mContext;

    static MyFragment newInstance() {
        MyFragment myFragment = new MyFragment();
        return myFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView textView = new TextView(mContext);
        textView.setText("这是一个Fragment");
        return textView;
    }
}

package com.example.d_24_homework.OpenService;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.d_24_homework.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/7.
 */
public class OpenFragment extends Fragment {
    private Context mContext;
    private TabLayout mTabLayout;
    private FragmentManager manager;
    private MyAdapter mAdapter;
    private ViewPager mVp;
    private List<Fragment>fragmentList=new ArrayList<>();
    private OpenFragmentLeft leftFragment;
    private OpenFragmentRight rightFragment;
    private List<String>titleList=new ArrayList<>();
    public static OpenFragment  newInstance(){
        return new OpenFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getContext();
       mAdapter=new MyAdapter(getFragmentManager());
        leftFragment=OpenFragmentLeft.newInstance();
        rightFragment=OpenFragmentRight.newInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.open_service_activity,container,false);
        mVp= (ViewPager) view.findViewById(R.id.view_pager_oopen_service);
        initTabLayout(view);
        initTitle();
        mVp.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mVp);
        return view;
    }
    private void initTitle(){
        titleList.add("                 开服                     ");
        titleList.add("                 开测                     ");
        fragmentList.add(leftFragment);
        fragmentList.add(rightFragment);
    }
    private void initTabLayout(View view){
        mTabLayout= (TabLayout) view.findViewById(R.id.main_tab_layout);
    }
    class MyAdapter extends FragmentPagerAdapter{


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList==null? 0:fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}

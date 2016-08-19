package com.example.d_24_homework.Special;

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
public class SpecialFragment extends Fragment{
    private Context mContext;
    private TabLayout mTabLayout;
    private ViewPager mVp;
    private MySpecialAdapter mAdapter;
    private SpecialFragmentLeft specialFragmentLeft;
    private SpecialFragmentRight specialFragmentRight;
    private List<String>titleList=new ArrayList<>();
    private List<Fragment>fragmentList=new ArrayList<>();
    public static SpecialFragment newInstance(){
        return new SpecialFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getContext();
        specialFragmentLeft=SpecialFragmentLeft.newInstance();
        specialFragmentRight=SpecialFragmentRight.newInstance();
        mAdapter=new MySpecialAdapter(getFragmentManager());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.special_fragment_layout,container,false);
        mVp= (ViewPager) view.findViewById(R.id.view_pager_special);
        initTabLayout(view);
        initTitle();
        mVp.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mVp);
        return view;
    }
    private void initTitle(){
        titleList.add("              暴打星期三            ");
        titleList.add("                新游周刊             ");
        fragmentList.add(specialFragmentLeft);
        fragmentList.add(specialFragmentRight);

    }
    private void initTabLayout(View view){
        mTabLayout= (TabLayout) view.findViewById(R.id.special_tab_layout);
    }
    class MySpecialAdapter extends FragmentPagerAdapter {


        public MySpecialAdapter(FragmentManager fm) {
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

package com.example.d_24_homework;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.d_24_homework.DetileActivity.LoginActivity;
import com.example.d_24_homework.DetileActivity.RegisterActivity;
import com.example.d_24_homework.HotGame.HotFragmrnt;
import com.example.d_24_homework.OpenService.OpenFragment;
import com.example.d_24_homework.Search.SearchMainActivity;
import com.example.d_24_homework.Special.SpecialFragment;
import com.example.d_24_homework.WelcomeActivity.ValueCallBack;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ValueCallBack{
    private FrameLayout mFrameLayout;
    private MyFragment mFragment;
    private FragmentManager mManager;
    private RadioGroup radioGroup;
    private ActionBar mActionBar;
    private Fragment mCurrentShowFragment;
    private OpenFragment openFragment;
    private HotFragmrnt hotFragmrnt;
    private SpecialFragment specialFragment;
    private TextView mTvTitle,mTvSearch;
    private DrawerLayout mDrawerLayout;
    private ImageView mainHome,myFift,sendEmail,aboutUs,circleImg,orderImg;
    private Context context;
    private ImageView menuImg;
    private Map<Integer,Integer>msgMap=new HashMap<>();
    private static boolean isExit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=this;
        initView();
    }
   private Handler mHandler=new Handler(){
       @Override
       public void handleMessage(Message msg) {
           isExit=false;
           if(msg.what==1){
               mTvTitle.setText("开服");
               orderImg.setVisibility(View.VISIBLE);
           }
       }
   };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }
    private void initView() {
        initGroup();
        initFragment();
        initActionBar();
        mDrawerLayout= (DrawerLayout) findViewById(R.id.menu_main_draw_layout);
        mFrameLayout= (FrameLayout) findViewById(R.id.frame_layout_main);
        mManager=getSupportFragmentManager();
        mainHome= (ImageView) findViewById(R.id.menu_main_home);
        myFift= (ImageView) findViewById(R.id.menu_gift);
        sendEmail= (ImageView) findViewById(R.id.menu_email);
        aboutUs= (ImageView) findViewById(R.id.menu_me);
        circleImg= (ImageView) findViewById(R.id.menu_image_view);
        orderImg= (ImageView) findViewById(R.id.image_view_order);
        mainHome.setOnClickListener(this);
        myFift.setOnClickListener(this);
        sendEmail.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        circleImg.setOnClickListener(this);
        ctrlFragment(mFragment);
    }
    private void initActionBar(){
        mActionBar=getSupportActionBar();
        mActionBar.setDisplayShowCustomEnabled(true);
        View view = getLayoutInflater().inflate(R.layout.action_bar_layout, null);
        mTvTitle= (TextView) view.findViewById(R.id.title);
        mTvSearch= (TextView) view.findViewById(R.id.search);
        mActionBar.setTitle("");
        mActionBar.setCustomView(view);
        menuImg= (ImageView) view.findViewById(R.id.image_view_order);
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, SearchMainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initFragment() {
        mFragment=MyFragment.newInstance();
        openFragment=OpenFragment.newInstance();
        hotFragmrnt=HotFragmrnt.newInstance();
        specialFragment=SpecialFragment.newInstance();
    }
    private void initGroup() {
        radioGroup= (RadioGroup) findViewById(R.id.main_bottom_radio_group);
        radioGroup.check(R.id.main_book_rb);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case  R.id.main_book_rb:
                        mTvTitle.setText("礼包精灵");
                        mTvSearch.setText("搜索");
                        ctrlFragment(mFragment);
                    break;
                    case  R.id.main_game_blue_rb:
                        mTvTitle.setText("开服");
                        mTvSearch.setText("");
                        if(msgMap.size()==2){
                            int number=msgMap.get(1)==0?0:msgMap.get(1);
                            if(number!=0){
                                passValue(number,1);

                            }
                        }
                        ctrlFragment(openFragment);
                        break;
                    case  R.id.main_game_u_rb:
                        mTvTitle.setText("热门游戏");
                        mTvSearch.setText("");
                        ctrlFragment(hotFragmrnt);
                        break;
                    case  R.id.main_me_u_rb:
                        mTvTitle.setText("独家企划");
                        mTvSearch.setText("");
                        if(msgMap.size()==2){
                            int number2=msgMap.get(2)==0?0:msgMap.get(2);
                            if(number2!=0){
                                passValue(number2,2);

                            }
                        }
                        ctrlFragment(specialFragment);
                        break;
                }
            }
        });
    }
        private void ctrlFragment(Fragment fragment) {
            FragmentTransaction fragmentTransaction = mManager.beginTransaction();
            if (mCurrentShowFragment != null && mCurrentShowFragment.isAdded()) {
                fragmentTransaction.hide(mCurrentShowFragment);
            }
            if (!fragment.isAdded()) {
                fragmentTransaction.add(R.id.frame_layout_main,fragment);
            } else {
                fragmentTransaction.show(fragment);
            }

            fragmentTransaction.commit();
            mCurrentShowFragment = fragment;
        }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id){
            case R.id.menu_main_home:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
            case R.id.menu_gift:
                Intent intent1=new Intent(context, LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.menu_email:
                Intent intent2=new Intent(context,LoginActivity.class);
                startActivity(intent2);
                break;
            case R.id.menu_me:
                Intent intent3=new Intent(context, RegisterActivity.class);
                startActivity(intent3);
                break;
            case R.id.menu_image_view:
                Intent intent4=new Intent(context, LoginActivity.class);
                startActivity(intent4);
                break;
        }
    }

    @Override
    public void passValue(int number,int code) {
       msgMap.put(code,number);
        mTvTitle.setText("共发现"+number+"条开服数据");
        orderImg.setVisibility(View.INVISIBLE);
        mHandler.sendEmptyMessageDelayed(1,1000);
    }
}

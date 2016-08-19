package com.example.d_24_homework;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.d_24_homework.DetileActivity.DetileActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by my on 2016/8/6.
 */
public class MyFragment extends Fragment {
    private List<GameGift> giftList=new ArrayList<>();
    private PullToRefreshListView mLv;
    private Context mContext;
    private String Host="http://www.1688wan.com";
    private String urlPath="http://www.1688wan.com/majax.action?method=getGiftList";
    private int pageNumber=1;
    private String body="";
    private List<String>imageList=new ArrayList<>();
    private List<String>viewImageList=new ArrayList<>();
    private MyAdapter mAdapter;
    private ViewPager mVp;
    private MyViewPagerAdapter mViewAdapter;
    private LinearLayout mLinearLayout;
    private int childCount;
    private int index;
    private int mLastListView;
    private TextView searchTv;
    private NotificationManager mManager;
    private int id;
    private boolean circle=true;
    static MyFragment newInstance(){
        return new MyFragment();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getContext();
    }
    private Handler mHandler2=new Handler(){
        @Override
        public void handleMessage(Message msg) {
                mVp.setCurrentItem(index++%5);
                mHandler2.sendEmptyMessageDelayed(1,4000);
        }
    };

    @TargetApi(Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       new Thread(new MyRunable()).start();
        mManager= (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        View view= inflater.inflate(R.layout.list_view_layout,container,false);
        View view2=inflater.inflate(R.layout.view_pager_layout,null);
        mLv= (PullToRefreshListView) view.findViewById(R.id.list_view_fragment);
        mVp= (ViewPager) view2.findViewById(R.id.view_pager_fragment);
        mLinearLayout= (LinearLayout) view2.findViewById(R.id.indicator_layout);
        childCount=mLinearLayout.getChildCount();

        mAdapter=new MyAdapter();
        mViewAdapter=new MyViewPagerAdapter();

        mVp.setAdapter(mViewAdapter);
        ListView listView = mLv.getRefreshableView();
        listView.addHeaderView(view2);
        mLv.setAdapter(mAdapter);
        mLv.setMode(PullToRefreshBase.Mode.BOTH);
        controlIndicator(0);
        mVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                controlIndicator(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber=1;
                new Thread(new MyRunable()).start();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageNumber+=2;
                new Thread(new MyRunable()).start();
            }
        });
        return view;
    }
    private void controlIndicator(int index) {
        ImageView view = (ImageView) mLinearLayout.getChildAt(index%5);
        for (int i = 0; i < childCount; i++) {
            ImageView childView = (ImageView) mLinearLayout.getChildAt(i);
            childView.setEnabled(false);
        }
        view.setEnabled(true);
    }
    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return giftList.size();
        }

        @Override
        public Object getItem(int position) {
            return giftList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, final View convertView, ViewGroup parent) {
            View view=convertView;
            ViewHolder viewHolder=null;
            if(view==null){
                view= LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false);
                viewHolder=new ViewHolder(view);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }
            GameGift gift=new GameGift();
            gift=giftList.get(position);
            viewHolder.imageView.setImageResource(R.drawable.ic_launcher);
            viewHolder.title.setText(gift.content);
            viewHolder.content.setText(gift.title);
            viewHolder.number.setText(gift.number);
            viewHolder.time.setText(gift.time);
            final String giftId=gift.id;
            final String title=gift.content;
            final String giftName=gift.title;
            final String giftTitle=gift.content;
            int number=Integer.valueOf(gift.number);
            if(number==0){
               viewHolder.btn.setText("淘号");
                viewHolder.btn.setBackgroundColor(Color.GRAY);
            }else{
                viewHolder.btn.setText("确定领取");
                viewHolder.btn.setBackgroundColor(Color.rgb(253,100,100));
            }
            viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mContext, DetileActivity.class);
                    intent.putExtra("id",giftId);
                    intent.putExtra("giftName",giftName);
                    intent.putExtra("giftTitle",giftTitle);
                    startActivity(intent);
                }
            });
            ImageLoader imageLoader = ImageLoader.init(mContext);
            imageLoader.load(imageList.get(position),viewHolder.imageView);
            return view;

        }
        class ViewHolder{
            public ImageView imageView;
            public TextView title;
            public TextView content;
            public TextView giftNumber;
            public TextView number;
            public TextView giftTime;
            public TextView time;
            private Button btn;

            public ViewHolder(View view){
                view.setTag(this);
                this.imageView = (ImageView) view.findViewById(R.id.image_list);
                this.title= (TextView) view.findViewById(R.id.title_list);
                this.content= (TextView) view.findViewById(R.id.content_list);
                this.giftNumber= (TextView) view.findViewById(R.id.gift_number);
                this.number= (TextView) view.findViewById(R.id.number);
                this.giftTime= (TextView) view.findViewById(R.id.gift_time);
                this.time= (TextView) view.findViewById(R.id.time);
                this.btn= (Button) view.findViewById(R.id.gift_btn);
            }
        }
    }
    class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            Log.d("androidxx", "getCount: "+viewImageList.size());
            return viewImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            index=position;
            ImageView imageView=new ImageView(mContext);
            imageView.setImageResource(R.drawable.ic_launcher);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            ImageLoader imageLoader = ImageLoader.init(mContext);
            imageLoader.load(viewImageList.get(position%5 ),imageView);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
    class MyRunable implements Runnable {
        String body="pageno="+pageNumber;
        private Handler mHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                mLv.onRefreshComplete();
                mViewAdapter.notifyDataSetChanged();
                mAdapter.notifyDataSetChanged();
                while(circle){
                    if(viewImageList.size()>0){
                        mHandler2.sendEmptyMessageDelayed(1,2000);
                        circle=false;
                    }
                }

            }
        };
        @Override
        public void run() {
            try {
                URL url = new URL(urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.getOutputStream().write(body.getBytes());
                urlConnection.connect();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    StringBuilder stringBuilder = new StringBuilder();
                    InputStream is = urlConnection.getInputStream();
                    byte[] buffer = new byte[1024];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        stringBuilder.append(new String(buffer, 0, len));
                    }
                    String jsonStr = stringBuilder.toString();
                    try {
                        JSONObject jsonObject = new JSONObject(jsonStr);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        JSONArray jsonArray1 = jsonObject.getJSONArray("ad");
                        for(int i=0;i<jsonArray1.length();i++){
                            JSONObject jsonViewImageList=jsonArray1.getJSONObject(i);
                            String viewUrl= jsonViewImageList.getString("iconurl");
                            viewImageList.add(Host+viewUrl);
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            GameGift gift=new GameGift();
                            JSONObject jsonListView = jsonArray.getJSONObject(i);
                            gift.title = jsonListView.getString("giftname");
                            gift.content = jsonListView.getString("gname");
                            gift.url = jsonListView.getString("iconurl");
                            gift.number=jsonListView.getString("number");
                            gift.time=jsonListView.getString("addtime");
                            gift.id=jsonListView.getString("id");
                            imageList.add(Host+jsonListView.getString("iconurl"));
                            giftList.add(gift);
                        }
                        mHandler.sendEmptyMessage(1);



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

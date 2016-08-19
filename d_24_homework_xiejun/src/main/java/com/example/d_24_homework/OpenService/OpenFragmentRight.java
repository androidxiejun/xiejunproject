package com.example.d_24_homework.OpenService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidxx.yangjw.httplibrary.HttpUtils;
import com.androidxx.yangjw.httplibrary.ICallback;
import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.d_24_homework.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class OpenFragmentRight extends Fragment implements ICallback{
    private Context context;
    private ListView mLv;
    private MyOpenAdapter myOpenAdapter;
    private List<String>imgList=new ArrayList<>();
    private List<OpenServiceInfo>dataList=new ArrayList<>();
    private final String HOST="http://www.1688wan.com";
    private final String URL_PATH="http://www.1688wan.com/majax.action?method=getWebfutureTest";
    public static OpenFragmentRight newInstance(){
        return new OpenFragmentRight();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
        myOpenAdapter=new MyOpenAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HttpUtils.load(URL_PATH).callback(this,1);
        View view=inflater.inflate(R.layout.open_service_list_right,container,false);
        mLv= (ListView) view.findViewById(R.id.list_view_open_right);
        mLv.setAdapter(myOpenAdapter);
        return view;
    }
    private Handler mHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            myOpenAdapter.notifyDataSetChanged();
        }
    };

        @Override
    public void success(String result, int requestCode) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            JSONArray jsonArray=jsonObject.getJSONArray("info");
            for (int i = 0; i < jsonArray.length(); i++) {
                 OpenServiceInfo serviceInfo=new OpenServiceInfo();
                JSONObject object=jsonArray.getJSONObject(i);
                serviceInfo.imgUrl=object.getString("iconurl");
                serviceInfo.title=object.getString("gname");
                serviceInfo.business=object.getString("operators");
                serviceInfo.time=object.getString("addtime");
                serviceInfo.gId=object.getString("gid");
                imgList.add(serviceInfo.imgUrl);
                dataList.add(serviceInfo);
            }
            mHandler.sendEmptyMessage(1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    class MyOpenAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=convertView;
            ViewHolder viewHolder=null;
            if(view==null){
                view= LayoutInflater.from(context).inflate(R.layout.open_service_list_item,parent,false);
                viewHolder=new ViewHolder(view);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }
            OpenServiceInfo openServiceInfo=new OpenServiceInfo();
            openServiceInfo=dataList.get(position);
            viewHolder.imageView.setImageResource(R.drawable.ic_launcher);
            viewHolder.title.setText(openServiceInfo.title);
            viewHolder.business.setText(openServiceInfo.business);
            viewHolder.time.setText(openServiceInfo.time);
            String imag=imgList.get(position);
            ImageLoader.init(context).load(HOST+imgList.get(position),viewHolder.imageView);
            final String gName=openServiceInfo.title;
            final String gId=openServiceInfo.gId;
            viewHolder.btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,OpenServiceDetileActivity.class);
                    intent.putExtra("gName",gName);
                    intent.putExtra("gId",gId);
                    startActivity(intent);
                }
            });
            return view;

        }
        class ViewHolder{
            public ImageView imageView;
            public TextView title;
            public TextView business;
            public TextView time;
            public  Button btn;


            public ViewHolder(View view){
                view.setTag(this);
                this.imageView = (ImageView) view.findViewById(R.id.open_service_right_img);
                this.title= (TextView) view.findViewById(R.id.open_right_title);
                this.business= (TextView) view.findViewById(R.id.open_service_business);
                this.time= (TextView) view.findViewById(R.id.open_service_time);
                this.btn= (Button) view.findViewById(R.id.open_right_ntn);
            }
        }
        }
    }

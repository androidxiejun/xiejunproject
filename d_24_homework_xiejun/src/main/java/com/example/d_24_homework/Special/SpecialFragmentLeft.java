package com.example.d_24_homework.Special;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidxx.yangjw.httplibrary.HttpUtils;
import com.androidxx.yangjw.httplibrary.ICallback;
import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.d_24_homework.R;
import com.example.d_24_homework.WelcomeActivity.ValueCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class SpecialFragmentLeft extends Fragment implements ICallback{
    private Context context;
    private ListView mLv;
    private MyBounchAdapter mBounchAdapter;
    private ValueCallBack callBack;
    private List<SpecialBounchInfo>bounchList=new ArrayList<>();
    private final String URL_PATH="http://www.1688wan.com/majax.action?method=bdxqs&pageNo=0";
    private final String BODY="http://www.1688wan.com";
    public static SpecialFragmentLeft newInstance(){
        return new SpecialFragmentLeft();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HttpUtils.load(URL_PATH).callback(this,3);
        View view=inflater.inflate(R.layout.special_bunch_layout,container,false);
        mLv= (ListView) view.findViewById(R.id.special_list_view_layout);
        mBounchAdapter=new MyBounchAdapter();
        mLv.setAdapter(mBounchAdapter);
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 SpecialBounchInfo info=bounchList.get(position);
                int sId=info.boundId;
                String descs=info.boundDescs;
                String imgUrl=info.bounchImgUrl;
                Intent intent=new Intent(context,BoundDetileActivity.class);
                intent.putExtra("sId",sId);
                intent.putExtra("descs",descs);
                intent.putExtra("imgUrl",imgUrl);
                intent.putExtra("title",info.bounchTitle);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  ValueCallBack){
            callBack= (ValueCallBack) context;
        }
    }

    @Override
    public void success(String result, int requestCode) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            JSONArray jsonArray=jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                SpecialBounchInfo info=new SpecialBounchInfo();
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                info.bounchImgUrl=jsonObject1.getString("iconurl");
                info.bounchTitle=jsonObject1.getString("name");
                info.bounchTime=jsonObject1.getString("addtime");
                info.boundId=jsonObject1.getInt("sid");
                info.boundDescs=jsonObject1.getString("descs");
                bounchList.add(info);
            }
            callBack.passValue(bounchList.size(),2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
             mBounchAdapter.notifyDataSetChanged();
    }
    class MyBounchAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return bounchList==null? 0:bounchList.size();
        }

        @Override
        public Object getItem(int position) {
            return bounchList.get(position);
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
                view=LayoutInflater.from(context).inflate(R.layout.special_list_view_item,parent,false);
                viewHolder=new ViewHolder(view);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }
            SpecialBounchInfo info=bounchList.get(position);
            viewHolder.bounchImg.setImageResource(R.mipmap.ic_launcher);
            viewHolder.bounchTitle.setText(info.bounchTitle);
            viewHolder.bounchTime.setText(info.bounchTime);
            ImageLoader.init(context).load(BODY+info.bounchImgUrl,viewHolder.bounchImg);
            return view;
        }
        class ViewHolder {
            public ImageView bounchImg;
            public TextView bounchTitle;
            public TextView bounchTime;
            public ViewHolder(View view){
                view.setTag(this);
                this.bounchImg = (ImageView) view.findViewById(R.id.special_list_view_img);
                this.bounchTitle= (TextView) view.findViewById(R.id.special_list_view_title);
                this.bounchTime= (TextView) view.findViewById(R.id.special_list_view_time);
            }
        }
    }
}

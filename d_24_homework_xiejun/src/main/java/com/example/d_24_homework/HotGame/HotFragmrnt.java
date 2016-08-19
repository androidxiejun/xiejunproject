package com.example.d_24_homework.HotGame;

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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidxx.yangjw.httplibrary.HttpUtils;
import com.androidxx.yangjw.httplibrary.ICallback;
import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.d_24_homework.OpenService.OpenServiceDetileActivity;
import com.example.d_24_homework.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/7.
 */
public class HotFragmrnt extends Fragment implements ICallback{
    private Context mContext;
    private ListView mLv;
    private MyListAdapter mListAdapter;
    private MyGridAdapter mGridAdapter;
    private GridView mGv;
    private List<HotGridInfo>hotGridInfoList=new ArrayList<>();
    private List<HotListInfo>hotList=new ArrayList<>();
    private final String BODY="http://www.1688wan.com";
    private final String URL_PATH="http://www.1688wan.com//majax.action?method=hotpushForAndroid";
  public static HotFragmrnt newInstance(){
        return new HotFragmrnt();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.hot_frgament_layout,container,false);
        HttpUtils.load(URL_PATH).callback(this,4);
        mLv= (ListView) view.findViewById(R.id.hot_list_view_layout);
        mGv= (GridView) view.findViewById(R.id.hot_grid_view_layout);
        //TODO
        mListAdapter=new MyListAdapter();
        mGridAdapter=new MyGridAdapter();
        mLv.setAdapter(mListAdapter);
        mGv.setAdapter(mGridAdapter);
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent=new Intent(mContext, OpenServiceDetileActivity.class);
                        HotListInfo info=hotList.get(position);
                        String transId=info.id;
                        String transTitle=info.hotTitle;
                        intent.putExtra("gName",transTitle);
                        intent.putExtra("gId",transId);
                        startActivity(intent);
            }
        });
        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(mContext, OpenServiceDetileActivity.class);
                HotGridInfo info=hotGridInfoList.get(position);
                String transId=info.gridId;
                String transTitle=info.gridTitle;
                intent.putExtra("gName",transTitle);
                intent.putExtra("gId",transId);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void success(String result, int requestCode) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            String jsonStr=jsonObject.getString("info");
            JSONObject jsonObject1=new JSONObject(jsonStr);
            JSONArray jsonArray=jsonObject1.getJSONArray("push1");
            JSONArray jsonArray1=jsonObject1.getJSONArray("push2");
            for (int i = 0; i < jsonArray.length(); i++) {
                HotListInfo hotListInfo=new HotListInfo();
                JSONObject json=jsonArray.getJSONObject(i);
                hotListInfo.hotTitle=json.getString("name");
                hotListInfo.hotType=json.getString("typename");
                hotListInfo.hotSize=json.isNull("size")?"未知":json.getString("size");
                hotListInfo.hotImgUrl=json.getString("logo");
                hotListInfo.id=json.getString("appid");
                hotListInfo.person=json.getInt("clicks");
                hotList.add(hotListInfo);
            }
            for (int i = 0; i < jsonArray1.length(); i++) {
                JSONObject jsonObject2=jsonArray1.getJSONObject(i);
                HotGridInfo hotGridInfo=new HotGridInfo();
                hotGridInfo.gridId=jsonObject2.getString("appid");
                hotGridInfo.gridImgUrl=jsonObject2.getString("logo");
                hotGridInfo.gridTitle=jsonObject2.getString("name");
                hotGridInfoList.add(hotGridInfo);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
             mListAdapter.notifyDataSetChanged();
             mGridAdapter.notifyDataSetChanged();
    }
    class MyListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return hotList==null? 0:hotList.size();
        }

        @Override
        public Object getItem(int position) {
            return hotList.get(position);
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
                view=LayoutInflater.from(mContext).inflate(R.layout.hot_list_view_item,parent,false);
                viewHolder=new ViewHolder(view);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }
            HotListInfo hotListInfo=new HotListInfo();
            hotListInfo=hotList.get(position);
            viewHolder.hotTitle.setText(hotListInfo.hotTitle);
            viewHolder.hotType.setText(hotListInfo.hotType);
            viewHolder.hotSize.setText(hotListInfo.hotSize);
            viewHolder.hotPerson.setText(hotListInfo.person+"人在玩");
            viewHolder.hotImg.setImageResource(R.mipmap.ic_launcher);
            ImageLoader.init(mContext).load(BODY+hotListInfo.hotImgUrl,viewHolder.hotImg);
            return view;
        }
        class ViewHolder {
            public ImageView hotImg;
            public TextView hotTitle;
            public TextView hotType;
            public TextView hotSize;
            public TextView hotPerson;
            public ViewHolder(View view){
                view.setTag(this);
                this.hotImg = (ImageView) view.findViewById(R.id.hot_list_view_img);
                this.hotTitle= (TextView) view.findViewById(R.id.hot_list_view_title);
                this.hotType= (TextView) view.findViewById(R.id.hot_list_view_type);
                this.hotSize= (TextView) view.findViewById(R.id.hot_list_view_size);
                this.hotPerson= (TextView) view.findViewById(R.id.hot_list_view_person);
            }
        }
    }
    class MyGridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return hotGridInfoList==null? 0:hotGridInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            return hotGridInfoList.get(position);
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
                view= LayoutInflater.from(mContext).inflate(R.layout.hot_grid_view_item,parent,false);
                viewHolder=new ViewHolder(view);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }
            HotGridInfo hotGridInfo=hotGridInfoList.get(position);
            viewHolder.title.setText(hotGridInfo.gridTitle);
            viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
            ImageLoader.init(mContext).load(BODY+hotGridInfo.gridImgUrl,viewHolder.imageView);
            return view;
        }
        class ViewHolder{
            public ImageView imageView;
            public TextView title;


            public ViewHolder(View view){
                view.setTag(this);
                this.imageView = (ImageView) view.findViewById(R.id.hot_grid_view_img);
                this.title= (TextView) view.findViewById(R.id.hot_grid_view_title);
            }
        }
    }
}

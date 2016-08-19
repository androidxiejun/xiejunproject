package com.example.d_24_homework.Special;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/16.
 */
public class BoundDetileActivity extends AppCompatActivity implements ICallback{
    private int postId;
    private String text="";
    private String imgUrl="";
    private ImageView mIv;
    private TextView mTv,mActionTv;
    private GridView mGv;
    private Context context;
    private ActionBar mActionBar;
    private Button actionBtn;
    private MyGridAdapter mGridAdapter;
    private List<SpecialBoundDetileGridInfo>gridList=new ArrayList<>();
    private Map<String,Object>urlMap=new HashMap<>();
    private final String BODY="http://www.1688wan.com";
    private final String URL_PATH="http://www.1688wan.com/majax.action?method=bdxqschild";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.special_bound_detile_layout);
       initActionBar();
        initIntent();
        initView();

    }

    private void initActionBar() {
        View view=LayoutInflater.from(context).inflate(R.layout.special_bound_detile_action_bar,null);
        mActionTv= (TextView) view.findViewById(R.id.special_bound_detile_action_title);
        actionBtn= (Button) view.findViewById(R.id.special_bound_detile_action_img);
        mActionBar=getSupportActionBar();
        mActionBar.setTitle("");
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(view);

    }
    public void onClick(View view){
        finish();
    }

    private void initIntent(){
        Intent intent = getIntent();
        postId=intent.getIntExtra("sId",0);
        text=intent.getStringExtra("descs");
        imgUrl=intent.getStringExtra("imgUrl");
        String title=intent.getStringExtra("title");
        mActionTv.setText(title);
        urlMap.put("id",postId);
    }
    private void initView() {
        HttpUtils.load(URL_PATH).post(urlMap).callback(this,11);
        mIv= (ImageView) findViewById(R.id.special_bound_detile_img);
        mTv= (TextView) findViewById(R.id.special_bound_detile_title);
        mGv= (GridView) findViewById(R.id.special_bound_detile_grid_view);
        mGridAdapter=new MyGridAdapter();
        mIv.setImageResource(R.drawable.ic_launcher);
        ImageLoader.init(context).load(BODY+imgUrl,mIv);
        mTv.setText(text);
        mGv.setAdapter(mGridAdapter);
        mGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(context, OpenServiceDetileActivity.class);
                SpecialBoundDetileGridInfo info=gridList.get(position);
                String transId=info.gridAppId;
                String transTitle=info.gridTitle;
                intent.putExtra("gName",transTitle);
                intent.putExtra("gId",transId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void success(String result, int requestCode) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            JSONArray jsonArray=jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                SpecialBoundDetileGridInfo info=new SpecialBoundDetileGridInfo();
                info.gridAppId=jsonObject1.getString("appid");
                info.gridImg=jsonObject1.getString("appicon");
                info.gridTitle=jsonObject1.getString("appname");
                gridList.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
           mGridAdapter.notifyDataSetChanged();
    }
    class MyGridAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return gridList==null?0:gridList.size();
        }

        @Override
        public Object getItem(int position) {
            return gridList.get(position);
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
                view= LayoutInflater.from(context).inflate(R.layout.special_bound_detile_grid_view_item,parent,false);
                viewHolder=new ViewHolder(view);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }
            SpecialBoundDetileGridInfo info=gridList.get(position);
            viewHolder.gridImg.setImageResource(R.drawable.ic_launcher);
            viewHolder.gridTitle.setText(info.gridTitle);
            ImageLoader.init(context).load(BODY+info.gridImg,viewHolder.gridImg);
            return view;
        }
        class ViewHolder {
            public ImageView gridImg;
            public TextView gridTitle;
            public ViewHolder(View view){
                view.setTag(this);
                this.gridImg = (ImageView) view.findViewById(R.id.special_bound_detile_grid_img);
                this.gridTitle= (TextView) view.findViewById(R.id.special_bound_detile_grid_title);
            }
        }
    }
}

package com.example.d_24_homework.Search;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidxx.yangjw.httplibrary.HttpUtils;
import com.androidxx.yangjw.httplibrary.ICallback;
import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.d_24_homework.DetileActivity.DetileActivity;
import com.example.d_24_homework.GameGift;
import com.example.d_24_homework.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/17.
 */
public class SearchMainActivity extends AppCompatActivity implements ICallback{
    private Context context;
    private ActionBar mActionBar;
    private ListView mLv;
    private MyAdapter mAdapter;
    private Button backBtn;
    private TextView searchTv;
    private EditText mEt;
    private String keyValue="";
    private final String BODY="http://www.1688wan.com";
    private final String URL_PATH="http://www.1688wan.com/majax.action?method=searchGift";
    private List<GameGift>giftList=new ArrayList<>();
    private Map<String,Object>dataMap=new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_layout);
        context=this;
        initActionBar();
        initIntent();
        initView();
        HttpUtils.load(URL_PATH).post(dataMap).callback(this,22);
        dataMap=null;
        searchTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=mEt.getText().toString();
                if(TextUtils.isEmpty(text)){
                    Toast.makeText(SearchMainActivity.this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent=new Intent(context,SearchMainActivity.class);
                    intent.putExtra("value",text);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private void initIntent(){
        Intent intent = getIntent();
        keyValue=intent.getStringExtra("value");
        if(keyValue==null){
            dataMap.put("key","");
        }else{
            dataMap.put("key",keyValue);
        }
    }
    private void initView() {
        mLv= (ListView) findViewById(R.id.search_list_view_layout);
        mAdapter=new MyAdapter();
        mLv.setAdapter(mAdapter);
        mEt= (EditText) findViewById(R.id.search_edit_text);
//        dataMap.put("key","");
    }

    private void initActionBar() {
        View view= LayoutInflater.from(context).inflate(R.layout.search_action_bar,null);
        mActionBar=getSupportActionBar();
        mActionBar.setTitle("");
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(view);
        backBtn= (Button) view.findViewById(R.id.search_back_btn);
        searchTv= (TextView) view.findViewById(R.id.searct_second);
    }
    public void onClick(View view){
        finish();
    }
    @Override
    public void success(String result, int requestCode) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            JSONArray jsonArray=jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                GameGift gift=new GameGift();
                gift.time=jsonObject1.getString("addtime");
                gift.number=jsonObject1.getString("exchanges");
                gift.content=jsonObject1.getString("giftname");
                gift.title=jsonObject1.getString("gname");
                gift.id=jsonObject1.getString("id");
                gift.url=jsonObject1.getString("iconurl");
                giftList.add(gift);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
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
                view= LayoutInflater.from(context).inflate(R.layout.search_list_view_item,parent,false);
                viewHolder=new ViewHolder(view);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }
            GameGift gift=new GameGift();
            gift=giftList.get(position);
            viewHolder.imageView.setImageResource(R.drawable.ic_launcher);
            viewHolder.title.setText(gift.title);
            viewHolder.content.setText(gift.content);
            viewHolder.number.setText(gift.number);
            viewHolder.time.setText(gift.time);
            final String giftId=gift.id;
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
                    Intent intent=new Intent(context, DetileActivity.class);
                    intent.putExtra("id",giftId);
                    intent.putExtra("giftName",giftName);
                    intent.putExtra("giftTitle",giftTitle);
                    startActivity(intent);
                }
            });
            ImageLoader imageLoader = ImageLoader.init(context);
            imageLoader.load(BODY+gift.url,viewHolder.imageView);
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
                this.imageView = (ImageView) view.findViewById(R.id.search_image_list);
                this.title= (TextView) view.findViewById(R.id.search_title_list);
                this.content= (TextView) view.findViewById(R.id.search_content_list);
                this.giftNumber= (TextView) view.findViewById(R.id.search_gift_number);
                this.number= (TextView) view.findViewById(R.id.search_number);
                this.time= (TextView) view.findViewById(R.id.search_time);
                this.btn= (Button) view.findViewById(R.id.search_gift_btn);
            }
        }
    }
}

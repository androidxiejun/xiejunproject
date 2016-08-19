package com.example.d_24_homework.Special;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/16.
 */
public class GameDetileActivity extends AppCompatActivity implements ICallback{
    private Context context;
    private ImageView mIv;
    private ListView mLv;
    private TextView actionTv,textDescs;
    private Button actionBtn;
    private ActionBar mActionBar;
    private String id="";
    private String text="";
    private String imgUrl="";
    private String message="";
    private Button mShareIv;
    private MyGameDetileAdapter mAdapter;
    private List<SpecialGameDetileListInfo>gameList=new ArrayList<>();
    private Map<String,Object>dataMap=new HashMap<>();
    private final String URL_PATH="http://www.1688wan.com/majax.action?method=getWeekllChid";
    private final String BODY="http://www.1688wan.com";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.special_game_detile_activity_layout);
        context=this;
        initIntent();
        initActionBar();
        initView();
        HttpUtils.load(URL_PATH).post(dataMap).callback(this,9);

    }

    private void initView() {
        mIv= (ImageView) findViewById(R.id.special_game_detile_activity_img);
        mLv= (ListView) findViewById(R.id.special_game_detile_activity_list_view_layout);
        textDescs= (TextView) findViewById(R.id.special_game_detile_activity_message);
        textDescs.setText(message);
        mAdapter=new MyGameDetileAdapter();
        mLv.setAdapter(mAdapter);
        ImageLoader.init(context).load(BODY+imgUrl,mIv);
    }
    private void initIntent(){
        Intent intent = getIntent();
        id=intent.getStringExtra("id");
        text=intent.getStringExtra("text");
        imgUrl=intent.getStringExtra("imgUrl");
        message=intent.getStringExtra("descs");
        dataMap.put("id",id);
    }
   private void initActionBar(){
       View view= LayoutInflater.from(context).inflate(R.layout.special_game_detile_action_bar,null);
       actionTv= (TextView) view.findViewById(R.id.special_game_detile_action_title);
       actionBtn= (Button) view.findViewById(R.id.special_game_detile_action_img);
       mShareIv= (Button) view.findViewById(R.id.special_game_detile_action_bar_share);
       actionTv.setText(text);
       mActionBar=getSupportActionBar();
       mActionBar.setDisplayShowCustomEnabled(true);
       mActionBar.setCustomView(view);
       mActionBar.setTitle("");
       actionTv.setText(text);
   }
    public void onShareClick(View view){
        showDialog();
    }
    private void showDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view=LayoutInflater.from(context).inflate(R.layout.share_alert_dialog_item,null);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        Window window=dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        dialog.show();
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
                SpecialGameDetileListInfo info=new SpecialGameDetileListInfo();
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                info.detileActor=jsonObject1.getString("typename");
                info.detileId=jsonObject1.getString("appid");
                info.detileMessge=jsonObject1.getString("descs");
                info.detileIntroduce=jsonObject1.getString("critique");
                info.detileSize=jsonObject1.getString("appsize");
                info.detileTitle=jsonObject1.getString("appname");
                info.imgUrl=jsonObject1.getString("iconurl");
                gameList.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter.notifyDataSetChanged();
    }
    class MyGameDetileAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return gameList==null?0:gameList.size();
        }

        @Override
        public Object getItem(int position) {
            return gameList.get(position);
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
                view= LayoutInflater.from(context).inflate(R.layout.special_game_detile_list_view_item,parent,false);
                viewHolder=new ViewHolder(view);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }
            SpecialGameDetileListInfo info=gameList.get(position);
            viewHolder.gameImg.setImageResource(R.drawable.ic_launcher);
            viewHolder.gameMessage.setText(info.detileMessge);
            viewHolder.gameSize.setText(info.detileSize);
            viewHolder.gameType.setText(info.detileActor);
            viewHolder.gsmeTitle.setText(info.detileTitle);
            final String transTitle=info.detileTitle;
            final String transId=info.detileId;
            ImageLoader.init(context).load(BODY+info.imgUrl,viewHolder.gameImg);
            viewHolder.gameBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, OpenServiceDetileActivity.class);
                    intent.putExtra("gName",transTitle);
                    intent.putExtra("gId",transId);
                    startActivity(intent);
                }
            });
            return view;
        }
        class ViewHolder {
            public ImageView gameImg;
            public TextView gsmeTitle;
            public TextView gameType;
            public TextView gameSize;
            public TextView gameMessage;
            public Button gameBtn;
            public ViewHolder(View view){
                view.setTag(this);
                this.gameImg = (ImageView) view.findViewById(R.id.special_game_detile_list_view_img);
                this.gsmeTitle= (TextView) view.findViewById(R.id.special_game_detile_list_view_title);
                this.gameType = (TextView) view.findViewById(R.id.special_game_detile_list_view_actor);
                this.gameSize= (TextView) view.findViewById(R.id.special_game_detile_list_view_size);
                this.gameMessage= (TextView) view.findViewById(R.id.special_game_detile_list_view_message);
                this.gameBtn= (Button) view.findViewById(R.id.special_game_detile_download_btn);
            }
        }
    }
}

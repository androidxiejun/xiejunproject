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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/15.
 */
public class SpecialFragmentRight extends Fragment implements ICallback{
    private Context context;
    private ListView mLv;
    private  MyGameAdapter mGameAdapter;
    private List<SpecialGameInfo>gameList=new ArrayList<>();
    private final String URL_PATH="http://www.1688wan.com/majax.action?method=getWeekll&pageNo=0";
    private final String BODY="http://www.1688wan.com";
    public static SpecialFragmentRight newInstance(){
        return new SpecialFragmentRight();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context=getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HttpUtils.load(URL_PATH).callback(this,8);
        View view=inflater.inflate(R.layout.special_game_list_view_layout,container,false);
        mLv= (ListView) view.findViewById(R.id.special_game_list_view_layout);
        mGameAdapter=new MyGameAdapter();
        mLv.setAdapter(mGameAdapter);
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpecialGameInfo info=gameList.get(position);
                Intent intent=new Intent(context,GameDetileActivity.class);
                intent.putExtra("id",info.gameId);
                intent.putExtra("text",info.gameTitle);
                intent.putExtra("imgUrl",info.gameIngUrl);
                intent.putExtra("descs",info.gameDescs);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void success(String result, int requestCode) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            JSONArray jsonArray=jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                SpecialGameInfo info=new SpecialGameInfo();
                info.gameIngUrl=jsonObject1.getString("iconurl");
                info.gameTitle=jsonObject1.getString("name");
                info.gameDescs=jsonObject1.getString("descs");
                info.gameId=jsonObject1.getString("id");
                gameList.add(info);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
       mGameAdapter.notifyDataSetChanged();
    }
    class MyGameAdapter extends BaseAdapter{

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
                view=LayoutInflater.from(context).inflate(R.layout.special_game_list_view_item,parent,false);
                viewHolder=new ViewHolder(view);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }
            SpecialGameInfo info=gameList.get(position);
            viewHolder.gameImg.setImageResource(R.drawable.ic_launcher);
            viewHolder.gameTitle.setText(info.gameTitle);
            ImageLoader.init(context).load(BODY+info.gameIngUrl,viewHolder.gameImg);
            return view;
        }
        class ViewHolder {
            public ImageView gameImg;
            public TextView gameTitle;
            public ViewHolder(View view){
                view.setTag(this);
                this.gameImg = (ImageView) view.findViewById(R.id.special_game_list_view_img);
                this.gameTitle= (TextView) view.findViewById(R.id.special_game_list_view_title);
            }
        }
    }
}

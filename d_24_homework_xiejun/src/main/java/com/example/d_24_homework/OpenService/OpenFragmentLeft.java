package com.example.d_24_homework.OpenService;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/8/15.
 */
@TargetApi(Build.VERSION_CODES.M)
public class OpenFragmentLeft extends Fragment implements ICallback{
    private Context context;
    private ExpandableListView mExpandableLv;
    private List<OpenServiceRightInfo>infoList=new ArrayList<>();
    private final String URL_PATH="http://www.1688wan.com/majax.action?method=getJtkaifu";
    private final String HOST="http://www.1688wan.com";
    private Set<String>keySet=new LinkedHashSet<>();
    private List<String>keyList;
    private ValueCallBack callBack;
    private int messageNum;
    private Map<String,List<OpenServiceRightInfo>>mapList=new HashMap<>();
    private MyExpandAdapter mExpandAdapter;
    public static OpenFragmentLeft newInstance(){
        return new OpenFragmentLeft();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ValueCallBack){
            callBack= (ValueCallBack) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.open_service_left,container,false);
        mExpandableLv= (ExpandableListView) view.findViewById(R.id.expandable_list_layout);
        HttpUtils.load(URL_PATH).callback(this,7);
        mExpandAdapter=new MyExpandAdapter();
        mExpandableLv.setAdapter(mExpandAdapter);
        return view;
    }
    @Override
    public void success(String result, int requestCode) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            JSONArray jsonArray=jsonObject.getJSONArray("info");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                String time=jsonObject1.getString("addtime");
                keySet.add(time);
                OpenServiceRightInfo rightInfo=new OpenServiceRightInfo();
                rightInfo.title=jsonObject1.getString("gname");
                rightInfo.time=jsonObject1.getString("linkurl");
                rightInfo.business=jsonObject1.getString("operators");
                rightInfo.area=jsonObject1.getString("area");
                rightInfo.gId=jsonObject1.getString("gid");
                rightInfo.imgUrl=jsonObject1.isNull("iconurl")?null:jsonObject1.getString("iconurl");
                rightInfo.gTime=jsonObject1.getString("addtime");
                infoList.add(rightInfo);
            }
            messageNum=infoList.size();
            callBack.passValue(messageNum,1);
            keyList=new ArrayList<>(keySet);
            for (int i = 0; i < keyList.size(); i++) {
                String keyTime=keyList.get(i);
                List <OpenServiceRightInfo>valueList = new ArrayList<>();
                for (int j = 0; j < infoList.size(); j++) {
                    OpenServiceRightInfo rightInfo=infoList.get(j);
                    String time=rightInfo.time;
                   if(keyTime.equals(rightInfo.gTime)){
                       valueList.add(rightInfo);
                   }
                }
                mapList.put(keyTime,valueList);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mExpandAdapter.notifyDataSetChanged();
        for (int i = 0; i < keyList.size(); i++) {
            mExpandableLv.expandGroup(i);
        }
        mExpandableLv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
    }

    class MyExpandAdapter extends BaseExpandableListAdapter{

        @Override
        public int getGroupCount() {
            return keyList==null? 0:keyList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            String keyName=keyList.get(groupPosition);
            List<OpenServiceRightInfo>valueList=mapList.get(keyName);
            return valueList==null? 0:valueList.size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupPosition;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            View view=convertView;
            String time=keyList.get(groupPosition);
            if(view==null){
                view=LayoutInflater.from(context).inflate(R.layout.open_right_group_layout,parent,false);
            }
            TextView tv= (TextView) view.findViewById(R.id.open_left_group_time);
            tv.setText(time);
            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View view=convertView;
            ViewHolder viewHolder=null;
            String keyName=keyList.get(groupPosition);
            List<OpenServiceRightInfo>valueList= mapList.get(keyName);
            if(view==null){
                view= LayoutInflater.from(context).inflate(R.layout.open_left_list_view,parent,false);
                viewHolder=new ViewHolder(view);
            }else{
                viewHolder= (ViewHolder) view.getTag();
            }
            OpenServiceRightInfo rightInfo=new OpenServiceRightInfo();
            rightInfo=valueList.get(childPosition);

            viewHolder.viewTitle.setText(rightInfo.title);
            viewHolder.viewTime.setText(rightInfo.time);
            viewHolder.viewBusiness.setText(rightInfo.business);
            viewHolder.viewArea.setText(rightInfo.area);
            viewHolder.viewImg.setImageResource(R.mipmap.ic_launcher);
            final String gName=rightInfo.title;
            final String gId=rightInfo.gId;
            ImageLoader.init(context).load(HOST+rightInfo.imgUrl,viewHolder.viewImg);
            viewHolder.viewBtn.setOnClickListener(new View.OnClickListener() {
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
        class ViewHolder {
            public ImageView viewImg;
            public TextView viewTitle;
            public TextView viewBusiness;
            public TextView viewArea;
            public TextView viewTime;
            public Button viewBtn;
            public ViewHolder(View view){
                view.setTag(this);
                this.viewImg = (ImageView) view.findViewById(R.id.open_left_logo_img);
                this.viewTitle= (TextView) view.findViewById(R.id.open_left__listview_title);
                this.viewBusiness= (TextView) view.findViewById(R.id.open_left_listview_business);
                this.viewArea= (TextView) view.findViewById(R.id.open_left_listview_area);
                this.viewBtn= (Button) view.findViewById(R.id.open_left_listview_chack);
                this.viewTime= (TextView) view.findViewById(R.id.open_left_listview_time);
            }
        }
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}

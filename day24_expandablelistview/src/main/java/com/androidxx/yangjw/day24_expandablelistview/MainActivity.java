package com.androidxx.yangjw.day24_expandablelistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ExpandableListView mExpandListView;
    private MyAdapter myAdapter;
    private List<String> keys = new ArrayList<>();
    private Map<String,List<String>> mapDatas = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mExpandListView = (ExpandableListView) findViewById(R.id.expand_list_view);
        myAdapter = new MyAdapter();
        mExpandListView.setAdapter(myAdapter);
        loadDatas();
        for (int i = 0; i < keys.size(); i++) {
            mExpandListView.expandGroup(i);
        }
    }

    private void loadDatas() {
        for (int i = 0; i < 10; i++) {
            ArrayList<String> arrayList = new ArrayList<>();
            String key = "GROUP" + i;
            keys.add(key);
            mapDatas.put(key,arrayList);
            for (int j = 0; j <= i ; j++) {
                arrayList.add("CHILD" + j);
            }
        }
        myAdapter.notifyDataSetChanged();
    }


    class MyAdapter extends BaseExpandableListAdapter {

        /**
         * 组的数量
         * @return
         */
        @Override
        public int getGroupCount() {
            return keys == null ? 0 : keys.size();
        }


        /**
         * 每一个Group中的Child的数量
         * @param groupPosition
         * @return
         */
        @Override
        public int getChildrenCount(int groupPosition) {
            String keyName = keys.get(groupPosition);
            List<String> stringList = mapDatas.get(keyName);
            return stringList == null ? 0 : stringList.size();
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
//            TextView button = (TextView) convertView;
//            if (button == null) {
//                button = new TextView(MainActivity.this);
//            }
//            button.setText(keys.get(groupPosition));
//            button.setBackgroundColor(Color.RED);
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(MainActivity.this).inflate(R.layout.group_item_view,parent,false);
            }

            return view;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            TextView textView = (TextView) convertView;
            if (textView == null) {
                textView = new TextView(MainActivity.this);
            }
            String keyName = keys.get(groupPosition);
            textView.setText(mapDatas.get(keyName).get(childPosition));
            return textView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}

package com.example.d_24_homework.DetileActivity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidxx.yangjw.httplibrary.HttpUtils;
import com.androidxx.yangjw.httplibrary.ICallback;
import com.androidxx.yangjw.imageloader.ImageLoader;
import com.example.d_24_homework.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Administrator on 2016/8/15.
 */
public class DetileActivity extends AppCompatActivity implements ICallback{
    private TextView mBarTv;
    private Context context;
    private ActionBar mActionBar;
    private String giftName="";
    private String giftTitle="";
    private String id="";
    private Button mBtn,mGiftBtn;
    private TextView mTv,mTv2;
    private ImageView giftIv;
    private String imageUrl="";
    private Dialog dialog;
    private CircleImageView mCircleIv;
    public Resources r;
    private String picPath="D:\\pic\\def_head.jpg";
    private Map<String,Object>bodyMap=new HashMap<>();
    private final String HOST="http://www.1688wan.com";
    private final String URL_PATH="http://www.1688wan.com/majax.action?method=getGiftInfo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detile_layout);
        r = this.getResources();
        context=this;
        getInfo();
        bodyMap.put("id",id);
        HttpUtils.load(URL_PATH).post(bodyMap).callback(this,1);
        initView();


    }
    private void initView(){
        mTv= (TextView) findViewById(R.id.detile_text_view);
        mCircleIv= (CircleImageView) findViewById(R.id.detile_image_view);
        mTv2= (TextView) findViewById(R.id.detile_text_view_two);
        mGiftBtn= (Button) findViewById(R.id.detile_get_gift_btn);
        giftIv= (ImageView) findViewById(R.id.main_detile_gift_bag);
        giftIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void getInfo(){
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        giftName=intent.getStringExtra("giftName");
        giftTitle=intent.getStringExtra("giftTitle");
        initActionBar();
    }
    private void initActionBar(){
        View view= LayoutInflater.from(context).inflate(R.layout.detile_action_bar,null,false);
        mBarTv= (TextView) view.findViewById(R.id.detile_text_view_bar);
        mBtn= (Button)view. findViewById(R.id.detile_back_btn_bar);
        mActionBar=getSupportActionBar();
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setTitle("");
        mBarTv.setText(giftTitle+"-"+giftName);
        mActionBar.setCustomView(view);
    }
  public void onClick(View view){
      finish();
  }
    public void onClick2(View view){
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);
    }
    public void onClickShare(View view){
        //分享字符信息
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT,"快来领取礼包"+URL_PATH);
//        sendIntent.setType("text/*");
//        startActivity(sendIntent);
        //分享图片信息
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                r.getResourcePackageName(R.drawable.ic_launcher) + "/" +
                r.getResourceTypeName(R.drawable.ic_launcher) + "/" +
                r.getResourceEntryName(R.drawable.ic_launcher));
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM,uri);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.app_name)));
   //分享图片列表
//        List<Uri>uriList=new ArrayList<>();
//        uriList.add(uri);
//        uriList.add(uri);
//        uriList.add(uri);
//        uriList.add(uri);
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
//        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, (ArrayList<? extends Parcelable>) uriList);
//        shareIntent.setType("image/*");
//        startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.app_name)));

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
    @Override
    public void success(String result, int requestCode) {
        try {
            JSONObject jsonObject=new JSONObject(result);
            String jasonStr= jsonObject.getString("info");
            JSONObject jsonObject1=new JSONObject(jasonStr);
            imageUrl=jsonObject1.getString("iconurl");
            mCircleIv.setImageResource(R.drawable.ic_launcher);
            String contentOne=jsonObject1.getString("explains");
            String contentTwo=jsonObject1.getString("descs");
            int number=Integer.valueOf(jsonObject1.getString("exchanges"));
            if(number==0){
              mGiftBtn.setText("马上淘号");
            }else{
                mGiftBtn.setText("立即领取");
            }
            mTv.setText(contentOne);
            mTv2.setText(contentTwo);
            ImageLoader.init(context).load(HOST+imageUrl,mCircleIv);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

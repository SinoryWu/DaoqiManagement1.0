package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.daoqimanagement.adapter.PartnerPrepareAdapter;
import com.example.daoqimanagement.adapter.ScheduleListAdapter;
import com.example.daoqimanagement.bean.PartnerPrepareListResponse;
import com.example.daoqimanagement.bean.PrepareDetailForUserResponse;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.fragment.MentorsFragment;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.ToastUtils;
import com.google.gson.Gson;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PartnerDetailActivity extends AppCompatActivity {

    private RelativeLayout mRlFinish,mRlServiceTeam,mRlEditInformation,mRlContract;
    private CircleImageView mIvHeadPic;
    private TextView mTvUserType,mTvCreateTime,mTvTrueName;
    private Button mBtnCall;
    private RecyclerView mRcPrepareList;

    private PartnerPrepareAdapter partnerPrepareAdapter;
    DialogTokenIntent dialogTokenIntent = null;
    String phoneNumber,trueName,headPic,createTime;
    int userType,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        initView();
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("phoneNumber");
        trueName = intent.getStringExtra("trueName");
        headPic = intent.getStringExtra("headPic");
        createTime = intent.getStringExtra("createTime");
        userType = intent.getIntExtra("userType",0);
        uid = intent.getIntExtra("uid",0);


        mBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(PartnerDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Log.i("requestMyPermissions", ": 【 " + Manifest.permission.CALL_PHONE + " 】没有授权，申请权限");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        PartnerDetailActivity.this.requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
                    }


                } else {
                    Log.i("requestMyPermissions", ": 【 " + Manifest.permission.CALL_PHONE + " 】有权限");
                    call(phoneNumber);
                }
            }
        });

        mTvTrueName.setText(trueName);

        /**
         * Glide异步加载图片,设置默认图片，加载错误时图片，加载成功前显示的图片
         */
        Glide.with(PartnerDetailActivity.this).load(Api.URL+headPic)
                .error(R.mipmap.partner_detail_head_icon)//异常时候显示的图片
                .fallback(R.mipmap.partner_detail_head_icon)//url为空的时候,显示的图片
                .placeholder(R.mipmap.partner_detail_head_icon)//加载成功前显示的图片
                .into(mIvHeadPic);

        if (userType== 1 ){
            mTvUserType.setText("星火合伙人");
        }else if (userType ==2){
            mTvUserType.setText("直营团队");
        }else if (userType == 3 ){
            mTvUserType.setText("生态链合伙人");
        }else if (userType== 4 ){
            mTvUserType.setText("财务");
        }else if (userType == 5 ){
            mTvUserType.setText("初审");
        }else if (userType == 6){
            mTvUserType.setText("复审");
        }else if (userType == 7 ){
            mTvUserType.setText("终审");
        }else if (userType== 8 ){
            mTvUserType.setText("运维");
        }


        if (!TextUtils.isEmpty(createTime)){
            String createAt= createTime.substring(0,10);
            StringBuffer buffer = new StringBuffer(createAt);
            buffer.replace(4,5,"-");
            buffer.replace(7,8,"-");



            mTvCreateTime.setText("注册事件："+buffer);
        }

        mRlFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCollector.removeActivity(PartnerDetailActivity.this);
                finish();
            }
        });

        getScheduleListRes(Api.URL+"/v1/team/prepareList?partnerId="+uid);

        mRlServiceTeam.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                String uid1 = String.valueOf(uid);
                Intent intent = new Intent(PartnerDetailActivity.this,PartnerTeamActivity.class);
                intent.putExtra("headPic",headPic);
                intent.putExtra("trueName",trueName);
                intent.putExtra("uid",uid1);
                startActivity(intent);
            }
        });
    }

    public void initView(){
        setContentView(R.layout.activity_partner_detail);
        mRlFinish  = findViewById(R.id.partner_detail_rl_finish);
        mRlServiceTeam  = findViewById(R.id.partner_detail_rl_service_team);
        mRlEditInformation  = findViewById(R.id.partner_detail_rl_edit_information);
        mRlContract  = findViewById(R.id.partner_detail_rl_contract_management);
        mIvHeadPic  = findViewById(R.id.partner_detail_iv_head_pic);
        mTvUserType  = findViewById(R.id.partner_detail_tv_usertype);
        mTvCreateTime  = findViewById(R.id.partner_detail_tv_createTime);
        mBtnCall= findViewById(R.id.partner_detail_btn_call);
        mTvTrueName= findViewById(R.id.partner_detail_tv_truename);
        mRcPrepareList= findViewById(R.id.partner_detail_rc_prepare_list);


    }

    public void call(String mobile) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + mobile));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void getScheduleListRes(String url) {


        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.构造request
        //2.1构造requestbody

//        JSONObject jsonObject = new JSONObject();
//        String jsonStr = jsonObject.toString();
//
//        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
//                .addHeader("token", "30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid", "8")
                .addHeader("token", GetSharePerfenceSP.getToken(this))
                .addHeader("uid", GetSharePerfenceSP.getUid(this))
                .get()
                .build();
        //3.将request封装为call
        Call call = Api.ok().newCall(request);
        L.e(String.valueOf(call));
        //4.执行call
//        同步执行
//        Response response = call.execute();

        //异步执行
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("OnFailure   " + e.getMessage());
                e.printStackTrace();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(PartnerDetailActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);
                Log.d("TAGq23123eqweq", res);


                final PartnerPrepareListResponse partnerPrepareListResponse = new Gson().fromJson(res,PartnerPrepareListResponse.class);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (partnerPrepareListResponse.getCode() == 0) {

                            partnerPrepareAdapter = new PartnerPrepareAdapter(PartnerDetailActivity.this,partnerPrepareListResponse.getData().getData());
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(PartnerDetailActivity.this){
                                @Override
                                public boolean canScrollVertically() {
                                    return false;
                                }
                            };
//
                            mRcPrepareList.setLayoutManager(linearLayoutManager);
                            mRcPrepareList.setAdapter(partnerPrepareAdapter);
                            partnerPrepareAdapter.setOnItemClickListener(new PartnerPrepareAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int prepareId, int position) {
                                    String prepareid  = String.valueOf(prepareId);
                                    Intent intent = new Intent(PartnerDetailActivity.this,PrepareDetailForUserActivity.class);
                                    intent.putExtra("prepareId",prepareid);
                                    startActivity(intent);
                                }
                            });

                        } else if (partnerPrepareListResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(PartnerDetailActivity.this, R.style.CustomDialog);
                                dialogTokenIntent.setTitle("提示").setMessage("您好，您的登陆信息已过期，请重新登陆!").setConfirm("确认", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(PartnerDetailActivity.this, LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = partnerPrepareListResponse.getMsg();
                            ToastUtils.showTextToast2(PartnerDetailActivity.this, msg);
                        }
                    }
                });

            }
        });

    }
}
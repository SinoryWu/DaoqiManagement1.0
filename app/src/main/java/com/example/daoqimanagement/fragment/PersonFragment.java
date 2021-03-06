package com.example.daoqimanagement.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.library.YLCircleImageView;
import com.bumptech.glide.Glide;
import com.example.daoqimanagement.AddPrepareActivity;
import com.example.daoqimanagement.EcologyCompleteInformationActivity;
import com.example.daoqimanagement.FitActivity;
import com.example.daoqimanagement.LoginActivity;
import com.example.daoqimanagement.PayEarnestMoneyActivity;
import com.example.daoqimanagement.PayJoinActivity;
import com.example.daoqimanagement.PayJoinExamineActivity;
import com.example.daoqimanagement.R;
import com.example.daoqimanagement.StarFireCompleteInformationActivity;
import com.example.daoqimanagement.UpDateProgressActivity;
import com.example.daoqimanagement.UserInterFaceActivity;
import com.example.daoqimanagement.bean.LoginDataResponse;
import com.example.daoqimanagement.bean.LoginOutResponse;
import com.example.daoqimanagement.bean.LoginResponse;
import com.example.daoqimanagement.bean.PersonFragmentUserInfoResponse;
import com.example.daoqimanagement.dialog.DialogTokenIntent;
import com.example.daoqimanagement.dialog.LoginOutDialog;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.ToastUtils;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class PersonFragment extends Fragment {

    private YLCircleImageView mIvUserPortrait;
    private TextView mTvIdentify, mTvHospital, mTvContract,mTvModifyInformation,mTvModifyPassword,mTvAccountStatement,mTvSignOut
            ,mTvTrueName,mTvUserType,mTvUserFit;

    DialogTokenIntent dialogTokenIntent = null;
    private PopupWindow popupWindow;

    private Animation animation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person,container,false);
        initView(view);
        return view;
    }

    public void initView(View view){
        mIvUserPortrait = view.findViewById(R.id.person_fragment_iv_user_portrait);
        mTvIdentify = view.findViewById(R.id.person_fragment_tv_identify);
        mTvHospital = view.findViewById(R.id.person_fragment_tv_hospital);
        mTvContract = view.findViewById(R.id.person_fragment_tv_contract);
        mTvModifyInformation = view.findViewById(R.id.person_fragment_tv_modify_information);
        mTvModifyPassword = view.findViewById(R.id.person_fragment_tv_modify_password);
        mTvAccountStatement = view.findViewById(R.id.person_fragment_tv_account_statement);
        mTvUserFit = view.findViewById(R.id.person_fragment_tv_account_fit);

        mTvSignOut = view.findViewById(R.id.person_fragment_tv_sign_out);
        mTvTrueName = view.findViewById(R.id.person_fragment_tv_user_name);
        mTvUserType = view.findViewById(R.id.person_fragment_tv_user_type);
        getUseInfoRes(Api.URL+"/v1/user/userInfo");

        mTvModifyInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showTextToast2(getContext(),"?????????????????????");
            }
        });
        mTvModifyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showTextToast2(getContext(),"?????????????????????");
            }
        });
        mTvAccountStatement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showTextToast2(getContext(),"?????????????????????");
            }
        });
        mTvSignOut.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                LoginOutDialog loginOutDialog = new LoginOutDialog(getContext(),R.style.CustomDialog);
                loginOutDialog.setConfirm("??????", new LoginOutDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(LoginOutDialog dialog) {
                        loginOut(Api.URL+"/v1/user/logout");

                    }
                }).show();
            }
        });
        mTvUserFit.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                Intent intent = new Intent(getContext(), FitActivity.class);
                startActivity(intent);
            }
        });

    }


    public void showPuPopWindow(View popupView){
        if (popupWindow == null) {
            popupView = View.inflate(getContext(), R.layout.login_out_popview_item, null);
            // ??????2,3?????????popupwindow??????????????????
            popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);

            // ????????????????????? ????????????????????????????????????
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            // ???????????????????????????????????????????????????X????????????Y??????1???0
            animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
            animation.setInterpolator(new AccelerateInterpolator());
            animation.setDuration(250);

            // ??????popupWindow????????????????????????????????????????????????????????????????????????
            popupWindow.showAtLocation(getActivity().findViewById(R.id.person_fragment_bottom_line), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            popupView.startAnimation(animation);

            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    popupWindow = null;
                }
            });

            popupView.findViewById(R.id.login_out_popwindow_tv_out).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginOut(Api.URL+"/v1/user/logout");
                    popupWindow.dismiss();
                }
            });

            popupView.findViewById(R.id.login_out_popwindow_tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });



        }
    }

    private void loginOut(String url) {

        HashMap<String,String> map = new HashMap<>();

//        String url = (ApiConfig.BASE_URl+ApiConfig.LOGIN);

        Log.e("tags", String.valueOf(map));



        postResLoginOut(url,map);
//       navigateTo(SecondActivity3.class);

    }


    protected void postResLoginOut(String url, HashMap<String, String> map) {
        //1.??????okhttp??????
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.??????request
        //2.1??????requestbody

        HashMap<String, Object> params = new HashMap<String, Object>();

        Log.e("params:", String.valueOf(params));
        Set<String> keys = map.keySet();
        for (String key : keys) {
            params.put(key, map.get(key));

        }
        JSONObject jsonObject = new JSONObject(params);
        String jsonStr = jsonObject.toString();

        RequestBody requestBodyJson = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("token",GetSharePerfenceSP.getToken(getContext()))
                .addHeader("uid",GetSharePerfenceSP.getUid(getContext()))
                .post(requestBodyJson)
                .build();
        //3.???request?????????call
        Call call = Api.ok().newCall(request);
        L.e(String.valueOf(call));
        //4.??????call
//        ????????????
//        Response response = call.execute();

        //????????????
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                L.e("OnFailure   " + e.getMessage());
                e.printStackTrace();

                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(getContext(), "??????????????????");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                ((Activity)getContext()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        L.e(res);

                        Gson gson = new Gson();

                        LoginOutResponse loginOutResponse = gson.fromJson(res, LoginOutResponse.class);

                        if (loginOutResponse.getCode() == 0) {

                            cleanTokenUidToSp("token","");
                            cleanTokenUidToSp("uid","");
                            cleanTokenUidToSp("type","");
                            cleanTokenUidToSp("usertype","");
//                            ActivityCollector.removeActivity((Activity)getContext());
                            ActivityCollector.finishAll();

                            Intent intent = new Intent(getContext(),LoginActivity.class);
                            startActivity(intent);


                        } else if (loginOutResponse.getCode() == 10009) {
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(getContext(), R.style.CustomDialog);
                                dialogTokenIntent.setTitle("??????").setMessage("??????????????????????????????????????????????????????!").setConfirm("??????", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {

                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        } else {
                            String msg = loginOutResponse.getMsg();
                            ToastUtils.showTextToast2(getContext(), msg);
                        }
                    }
                });

            }
        });

    }

    public void getUseInfoRes(String url){
//        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)//?????????????????????
                .get()//?????????GET??????????????????????????????????????????
                .addHeader("token",getTokenToSp("token",""))
                .addHeader("uid",getUidToSp("uid",""))
//                .addHeader("token","30267f97bb1aeb1e2ddca1cda79d92b5")
//                .addHeader("uid","8")
                .build();
        Call call = Api.ok().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                L.e("OnFailure   " + e.getMessage());
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showTextToast2(getContext(), "??????????????????");
                    }
                });
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                Log.d("TAG", res);


                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();
                        PersonFragmentUserInfoResponse personFragmentUserInfoResponse = gson.fromJson(res,PersonFragmentUserInfoResponse.class);
                        if (personFragmentUserInfoResponse.getCode() == 0){
                            if (personFragmentUserInfoResponse.getData().isAuthentication() == false){
                                mTvIdentify.setText("??????????????????");
                            }else {
                                mTvIdentify.setText("??????????????????");
                            }

                            mTvHospital.setText("???????????????"+personFragmentUserInfoResponse.getData().getHospitalNum());
                            mTvContract.setText("?????????"+personFragmentUserInfoResponse.getData().getContract());
                            mTvTrueName.setText(personFragmentUserInfoResponse.getData().getTruename());
                            int userType = personFragmentUserInfoResponse.getData().getUserType();
                            if (userType == 1){
                                mTvUserType.setText("???????????????");
                            }else if (userType == 2){
                                mTvUserType.setText("????????????");
                            }else if (userType == 3){
                                mTvUserType.setText("??????????????????");
                            }else if (userType == 4){
                                mTvUserType.setText("??????");
                            }else if (userType == 5){
                                mTvUserType.setText("??????");
                            }else if (userType == 6){
                                mTvUserType.setText("??????");
                            }else if (userType == 7){
                                mTvUserType.setText("??????");
                            }else if (userType == 8){
                                mTvUserType.setText("??????");
                            }
                            /**
                             * Glide??????????????????,???????????????????????????????????????????????????????????????????????????
                             */
                            Glide.with(getContext()).load("sadasd")
                                    .error(R.mipmap.person_fragment_user_portrait_icon)//???????????????????????????
                                    .fallback(R.mipmap.person_fragment_user_portrait_icon)//url???????????????,???????????????
                                    .placeholder(R.mipmap.person_fragment_user_portrait_icon)//??????????????????????????????
                                    .into(mIvUserPortrait);


                        }else if (personFragmentUserInfoResponse.getCode() == 10009){
                            if (dialogTokenIntent == null) {
                                dialogTokenIntent = new DialogTokenIntent(getContext(), R.style.CustomDialog);
                                dialogTokenIntent.setTitle("??????").setMessage("??????????????????????????????????????????????????????!").setConfirm("??????", new DialogTokenIntent.IOnConfirmListener() {
                                    @Override
                                    public void OnConfirm(DialogTokenIntent dialog) {
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        ActivityCollector.finishAll();
                                        startActivity(intent);

                                    }
                                }).show();

                                dialogTokenIntent.setCanceledOnTouchOutside(false);
                                dialogTokenIntent.setCancelable(false);
                            }
                        }else {
                            ToastUtils.showTextToast2(getContext(),personFragmentUserInfoResponse.getMsg());
                        }
                    }
                });


            }
        });
    }





    public String getTokenToSp(String key, String val) {
        SharedPreferences sp = getContext().getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String token = sp.getString("token", "");
        return token;
    }

    public String getUidToSp(String key, String val) {
        SharedPreferences sp = getContext().getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String uid = sp.getString("uid", "");
        return uid;
    }

    public String getUserTypeToSp(String key, String val) {
        SharedPreferences sp = getContext().getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String userType = sp.getString("usertype", "");
        return userType;
    }

    public void cleanTokenUidToSp(String key, String val){
        SharedPreferences sp = getActivity().getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        sp.edit().clear().commit();
    }
}

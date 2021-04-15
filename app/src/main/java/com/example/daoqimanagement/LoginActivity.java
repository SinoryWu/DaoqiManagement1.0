package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daoqimanagement.bean.LoginDataResponse;
import com.example.daoqimanagement.bean.LoginResponse;
import com.example.daoqimanagement.bean.RegisterDateResponse;
import com.example.daoqimanagement.bean.RegisterResponse;
import com.example.daoqimanagement.bean.SendMessageResponse;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.ActivityCollectorLogin;
import com.example.daoqimanagement.utils.Api;
import com.example.daoqimanagement.utils.L;
import com.example.daoqimanagement.utils.ToastUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvRegisterChangeTeam, mTvRegisterStarFire, mTvRegisterEcology, mTvRegisterManufacturer, mTvRegisterGetCode,
            mTvSignIn, mTvRegister, mTvChangeSignType, mTvRegisterToast, mTvLoginCodePrivateLink, mTvLoginCodeUserLink, mTvLoginPasswordPrivateLink,
            mTvLoginPasswordUserLink, mTvLoginRegisterPrivateLink;
    private View customViewRegister, registerLine, signInLine, registerView;
    private PopupWindow popupwindowRegister;
    private ImageView mIvRegisterDropDownPopView;
    private RegisterTimeCount registerTime;
    private SignInTimeCount signInTime;
    private Button mBtnTimeCode, mBtnCodeSign, mBtnPasswordSign, mBtnRegister;
    private CardView mCvCode, mCvPassword, mCvRegister;
    private int isShowPassWordCard = 0;
    private EditText mEtCodeAccount, mEtCodeCode, mEtPasswordAccount, mEtPasswordCode, mEtRegisterUserName, mEtRegisterPhoneNumber, mEtRegisterCode, mEtRegisterPassword, mEtRegisterConfirmPassword;
    private CheckBox mCbLoginCode, mCbLoginPassword, mCbLoginRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollectorLogin.addActivity(this);
        ActivityCollector.addActivity(this);
        initView();
        mCvPassword.setVisibility(View.GONE);
        mCvRegister.setVisibility(View.GONE);
        registerLine.setVisibility(View.GONE);
        mBtnPasswordSign.setVisibility(View.GONE);
        mTvRegisterToast.setVisibility(View.GONE);
        registerView.setVisibility(View.GONE);
        registerTime = new RegisterTimeCount(60000, 1000);
        signInTime = new SignInTimeCount(60000, 1000);
        mTvRegisterChangeTeam.setOnClickListener(this);
        mBtnTimeCode.setOnClickListener(this);
        mTvRegisterGetCode.setOnClickListener(this);
        mTvRegister.setOnClickListener(this);
        mTvChangeSignType.setOnClickListener(this);
        mTvSignIn.setOnClickListener(this);
        mCbLoginCode.setOnClickListener(this);
        mCbLoginPassword.setOnClickListener(this);
        mCbLoginRegister.setOnClickListener(this);
        mTvLoginCodePrivateLink.setOnClickListener(this);
        mTvLoginCodeUserLink.setOnClickListener(this);
        mTvLoginPasswordPrivateLink.setOnClickListener(this);
        mTvLoginPasswordUserLink.setOnClickListener(this);
        mTvLoginRegisterPrivateLink.setOnClickListener(this);
        mBtnCodeSign.setOnClickListener(this);
        mBtnPasswordSign.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        String regId1 = JPushInterface.getRegistrationID(getApplicationContext());
        Log.d("regId", regId1);

    }

    public void initView() {
        setContentView(R.layout.activity_login);
        mTvRegisterChangeTeam = findViewById(R.id.login_register_tv_change_team);
        mIvRegisterDropDownPopView = findViewById(R.id.login_register_iv_dropdown_popview);
        mTvRegisterGetCode = findViewById(R.id.login_register_tv_getcode);
        mBtnTimeCode = findViewById(R.id.login_code_btn_catch_information);
        mCvCode = findViewById(R.id.login_cv_code);
        mCvPassword = findViewById(R.id.login_cv_password);
        mCvRegister = findViewById(R.id.login_cv_register);
        registerLine = findViewById(R.id.login_register_line);
        signInLine = findViewById(R.id.login_sign_line);
        mTvSignIn = findViewById(R.id.login_tv_sign);
        mTvRegister = findViewById(R.id.login_tv_register);
        mTvChangeSignType = findViewById(R.id.login_tv_change_type_code);
        mBtnCodeSign = findViewById(R.id.login_code_btn_login);
        mBtnPasswordSign = findViewById(R.id.login_password_btn_login);
        mBtnRegister = findViewById(R.id.login_btn_register);
        mTvRegisterToast = findViewById(R.id.login_register_toast);
        registerView = findViewById(R.id.login_register_view);
        mEtCodeAccount = findViewById(R.id.login_code_et_account);
        mEtCodeCode = findViewById(R.id.login_code_et_verification_code);
        mEtPasswordAccount = findViewById(R.id.login_password_et_account);
        mEtPasswordCode = findViewById(R.id.login_password_et_verification_code);
        mCbLoginCode = findViewById(R.id.login_code_cb);
        mCbLoginPassword = findViewById(R.id.login_password_cb);
        mCbLoginRegister = findViewById(R.id.login_register_cb);
        mTvLoginCodePrivateLink = findViewById(R.id.login_code_tv_private_link);
        mTvLoginCodeUserLink = findViewById(R.id.login_code_tv_user_agreement_link);
        mTvLoginPasswordPrivateLink = findViewById(R.id.login_password_tv_private_link);
        mTvLoginPasswordUserLink = findViewById(R.id.login_password_tv_user_agreement_link);
        mTvLoginRegisterPrivateLink = findViewById(R.id.login_register_tv_private_link);
        mEtRegisterUserName = findViewById(R.id.login_register_et_username);
        mEtRegisterPhoneNumber = findViewById(R.id.login_register_et_phone);
        mEtRegisterCode = findViewById(R.id.login_register_et_code);
        mEtRegisterPassword = findViewById(R.id.login_register_et_password);
        mEtRegisterConfirmPassword = findViewById(R.id.login_register_et_confirm_password);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_register_tv_change_team:
                if (popupwindowRegister == null) {
                    initPopupWindowView();
                    popupwindowRegister.showAsDropDown(view, 0, dip2px(LoginActivity.this, 3));

                    mIvRegisterDropDownPopView.setImageResource(R.mipmap.login_arrow_right_up_icon);
                } else {
                    popupwindowRegister = null;
                }

                break;
            case R.id.login_register_tv_getcode:
                hideSoftKeyboard(view);
                String registerAccount = mEtRegisterPhoneNumber.getText().toString().trim();
                if (registerAccount.length() == 11) {
                    messagePost(registerAccount);
                    registerTime.start();
                } else if (registerAccount.length() < 11 && registerAccount.length() > 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请输入正确手机号");
                } else if (registerAccount.length() == 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请输入手机号");
                }

                break;
            case R.id.login_code_btn_catch_information:
                hideSoftKeyboard(view);
                String codeAccount = mEtCodeAccount.getText().toString().trim();
                if (codeAccount.length() == 11) {
                    messagePost(codeAccount);
                    signInTime.start();
                } else if (codeAccount.length() < 11 && codeAccount.length() > 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请输入正确手机号");
                } else if (codeAccount.length() == 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请输入手机号");
                }

                break;
            case R.id.login_tv_register:
                hideSoftKeyboard(view);
                mCvRegister.setVisibility(View.VISIBLE);
                mCvCode.setVisibility(View.GONE);
                mCvPassword.setVisibility(View.GONE);
                mTvSignIn.setTextColor(0x60ffffff);
                mTvRegister.setTextColor(0xffffffff);
                signInLine.setVisibility(View.GONE);
                registerLine.setVisibility(View.VISIBLE);
                mBtnCodeSign.setVisibility(View.GONE);
                mBtnPasswordSign.setVisibility(View.GONE);
                mBtnRegister.setVisibility(View.VISIBLE);
                mTvRegisterToast.setVisibility(View.VISIBLE);
                registerView.setVisibility(View.VISIBLE);
                break;
            case R.id.login_tv_change_type_code:
                hideSoftKeyboard(view);
                if (isShowPassWordCard == 0) {
                    mCvCode.setVisibility(View.GONE);
                    mCvPassword.setVisibility(View.VISIBLE);
                    mBtnCodeSign.setVisibility(View.GONE);
                    mBtnPasswordSign.setVisibility(View.VISIBLE);
                    mBtnRegister.setVisibility(View.GONE);
                    mTvChangeSignType.setText("使用验证码登陆");
                    mTvRegisterToast.setVisibility(View.GONE);
                    isShowPassWordCard = 1;
                    registerView.setVisibility(View.GONE);
                } else if (isShowPassWordCard == 1) {
                    mCvCode.setVisibility(View.VISIBLE);
                    mCvPassword.setVisibility(View.GONE);
                    mBtnCodeSign.setVisibility(View.VISIBLE);
                    mBtnPasswordSign.setVisibility(View.GONE);
                    mBtnRegister.setVisibility(View.GONE);
                    mTvChangeSignType.setText("使用密码登陆");
                    mTvRegisterToast.setVisibility(View.GONE);
                    registerView.setVisibility(View.GONE);
                    isShowPassWordCard = 0;
                }
                break;
            case R.id.login_tv_sign:
                hideSoftKeyboard(view);
                mTvSignIn.setTextColor(0xffffffff);
                mTvRegister.setTextColor(0x60ffffff);
                if (isShowPassWordCard == 0) {
                    mCvCode.setVisibility(View.VISIBLE);
                    mCvRegister.setVisibility(View.GONE);
                    signInLine.setVisibility(View.VISIBLE);
                    registerLine.setVisibility(View.GONE);
                    mBtnCodeSign.setVisibility(View.VISIBLE);
                    mBtnRegister.setVisibility(View.GONE);
                    mBtnPasswordSign.setVisibility(View.GONE);
                    mTvRegisterToast.setVisibility(View.GONE);
                    registerView.setVisibility(View.GONE);
                } else if (isShowPassWordCard == 1) {
                    mCvPassword.setVisibility(View.VISIBLE);
                    mCvRegister.setVisibility(View.GONE);
                    signInLine.setVisibility(View.VISIBLE);
                    registerLine.setVisibility(View.GONE);
                    mBtnCodeSign.setVisibility(View.GONE);
                    mBtnRegister.setVisibility(View.GONE);
                    mBtnPasswordSign.setVisibility(View.VISIBLE);
                    mTvRegisterToast.setVisibility(View.GONE);
                    registerView.setVisibility(View.GONE);
                }
                break;
            case R.id.login_code_cb:
                hideSoftKeyboard(view);
                break;
            case R.id.login_password_cb:
                hideSoftKeyboard(view);
                break;
            case R.id.login_register_cb:
                hideSoftKeyboard(view);
                break;
            case R.id.login_code_tv_private_link:
                hideSoftKeyboard(view);
                break;
            case R.id.login_code_tv_user_agreement_link:
                hideSoftKeyboard(view);
                break;
            case R.id.login_password_tv_private_link:
                hideSoftKeyboard(view);
                break;
            case R.id.login_password_tv_user_agreement_link:
                hideSoftKeyboard(view);
                break;
            case R.id.login_register_tv_private_link:
                hideSoftKeyboard(view);
                break;
            case R.id.login_code_btn_login:
                hideSoftKeyboard(view);
                if (mEtCodeAccount.length() <= 0 && mEtCodeCode.length() <= 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请输入手机号和验证码");

                } else if (mEtCodeAccount.length() <= 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请输入手机号");
                } else if (mEtCodeCode.length() <= 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请输入验证码");
                } else if (!mCbLoginCode.isChecked()) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请勾选同意并阅读《隐私声明》《用户协议》");
                } else if (mEtCodeAccount != null && mEtCodeCode != null) {
                    loginCodePost(mEtCodeAccount.getText().toString().trim(), mEtCodeCode.getText().toString().trim(), "100d855909e9468b7e4");

                }

                break;
            case R.id.login_password_btn_login:
                hideSoftKeyboard(view);
                if (mEtPasswordAccount.length() <= 0 && mEtPasswordCode.length() <= 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请输入手机号和验证码");

                } else if (mEtPasswordAccount.length() <= 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请输入手机号");
                } else if (mEtPasswordCode.length() <= 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请输入密码");
                } else if (!mCbLoginPassword.isChecked()) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请勾选同意并阅读《隐私声明》《用户协议》");
                } else if (mEtPasswordAccount != null && mEtPasswordCode != null) {
                    loginPasswordPost(mEtPasswordAccount.getText().toString().trim(), mEtPasswordCode.getText().toString().trim(), "mEtPasswordCode");
                }


                break;
            case R.id.login_btn_register:
                int userType = 0;
                if (mTvRegisterChangeTeam.getText().equals("星火合伙人")) {
                    userType = 1;
                } else if (mTvRegisterChangeTeam.getText().equals("直营团队")) {
                    userType = 2;
                } else if (mTvRegisterChangeTeam.getText().equals("生态链合伙人")) {
                    userType = 3;
                }
                if (mEtRegisterUserName.length() <= 0 && mEtRegisterPhoneNumber.length() <= 0 && mEtRegisterCode.length() <= 0 && mEtRegisterPassword.length() <= 0 && mEtRegisterConfirmPassword.length() <= 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请填写注册信息");

                } else if (mEtRegisterUserName.length() <= 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请输入姓名");
                } else if (mEtRegisterPhoneNumber.length() <= 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请输入手机号");
                } else if (mEtRegisterCode.length() <= 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请输入验证码");
                } else if (mEtRegisterPassword.length() <= 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请输入密码");
                } else if (mEtRegisterConfirmPassword.length() <= 0) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请确认密码");
                } else if (!String.valueOf(mEtRegisterPassword.getText()).equals(String.valueOf(mEtRegisterConfirmPassword.getText()))) {
                    ToastUtils.showTextToast2(LoginActivity.this, "两次密码输入不一致");
                } else if (!mCbLoginRegister.isChecked()) {
                    ToastUtils.showTextToast2(LoginActivity.this, "请勾选阅读并同意《睡眠直营团队》");
                } else if (mEtRegisterUserName != null && mEtRegisterPhoneNumber != null && mEtRegisterCode != null && mEtRegisterPassword != null && mEtRegisterConfirmPassword != null) {
                    loginRegisterPost(String.valueOf(userType), mEtRegisterUserName.getText().toString().trim(), mEtRegisterPassword.getText().toString().trim(),
                            mEtRegisterPhoneNumber.getText().toString().trim(), mEtRegisterCode.getText().toString().trim(), "100d855909e9468b7e4");
                    Log.d("TAG", mEtRegisterPhoneNumber.getText().toString().trim());
                    Log.d("TAG", mEtRegisterCode.getText().toString().trim());
                    Log.d("TAG", String.valueOf(userType));
                    Log.d("TAG", mEtRegisterUserName.getText().toString().trim());
                    Log.d("TAG", mEtRegisterPassword.getText().toString().trim());
                }


                break;


        }
    }


    public void initPopupWindowView() {
        // // 获取自定义布局文件pop.xml的视图
        customViewRegister = getLayoutInflater().inflate(R.layout.login_popview_item,
                null, false);

        // 创建PopupWindow实例,280,160分别是宽度和高度
        popupwindowRegister = new PopupWindow(customViewRegister, dip2px(this, 280), dip2px(this, 160));


        mTvRegisterStarFire = customViewRegister.findViewById(R.id.popwindow_tv_starfire);
        mTvRegisterEcology = customViewRegister.findViewById(R.id.popwindow_tv_ecology);
        mTvRegisterManufacturer = customViewRegister.findViewById(R.id.popwindow_tv_manufacturer);
        mTvRegisterStarFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mTvRegisterChangeTeam.setText(mTvRegisterStarFire.getText());

                if (popupwindowRegister != null && popupwindowRegister.isShowing()) {
                    popupwindowRegister.dismiss();
                    popupwindowRegister = null;

                }
            }
        });
        mTvRegisterEcology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvRegisterChangeTeam.setText(mTvRegisterEcology.getText());

                if (popupwindowRegister != null && popupwindowRegister.isShowing()) {
                    popupwindowRegister.dismiss();
                    popupwindowRegister = null;

                }
            }
        });
        mTvRegisterManufacturer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvRegisterChangeTeam.setText(mTvRegisterManufacturer.getText());

                if (popupwindowRegister != null && popupwindowRegister.isShowing()) {
                    popupwindowRegister.dismiss();
                    popupwindowRegister = null;
                }
            }
        });


        popupwindowRegister.setOutsideTouchable(true);
        popupwindowRegister.setFocusable(true);
        customViewRegister.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (popupwindowRegister != null && popupwindowRegister.isShowing()) {
                    popupwindowRegister.dismiss();
                    popupwindowRegister = null;


                }


                return true;
            }
        });

        popupwindowRegister.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupwindowRegister = null;
                Log.d("TAG", "onDismiss: ");
                mIvRegisterDropDownPopView.setImageResource(R.mipmap.login_arrow_right_down_icon);

            }
        });

    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    public void hideSoftKeyboard(View view) {
        //这里获取view为参数 之前试过用context,LoginActivity.this会造成闪退
        //view不会闪退
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

    }

    /**
     * 设置状态栏背景颜色
     */
    public void setBarBackGround() {
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);//这里设置顶部bar为透明色
        }
    }

    /**
     * 实现点击获取验证码按钮倒计时效果
     */
    class RegisterTimeCount extends CountDownTimer {

        public RegisterTimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            mTvRegisterGetCode.setClickable(false);
            mTvRegisterGetCode.setText("" + millisUntilFinished / 1000 + "s");

        }

        @Override
        public void onFinish() {
            mTvRegisterGetCode.setText("重新获取");
            mTvRegisterGetCode.setClickable(true);
        }
    }

    class SignInTimeCount extends CountDownTimer {

        public SignInTimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            mBtnTimeCode.setClickable(false);
            mBtnTimeCode.setText("" + millisUntilFinished / 1000 + "s");
            mBtnTimeCode.setBackgroundResource(R.drawable.catch_information_button_background_catch);
        }

        @Override
        public void onFinish() {
            mBtnTimeCode.setText("重新获取");
            mBtnTimeCode.setClickable(true);
            mBtnTimeCode.setBackgroundResource(R.drawable.catch_information_button_background_normal);
        }
    }

    private void loginCodePost(String account, String code, String regId) {


        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", account);//18158188052
        map.put("code", code);//111
        map.put("regId", regId);
//        String url = (ApiConfig.BASE_URl+ApiConfig.LOGIN);
        String url = Api.URL + "/v1/user/login";
        postResLogin(url, map);


    }

    private void loginPasswordPost(String account, String password, String regId) {


        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", account);//18158188052
        map.put("password", password);//111
        map.put("regId", regId);
//        String url = (ApiConfig.BASE_URl+ApiConfig.LOGIN);
        String url = Api.URL + "/v1/user/login";
        postResLogin(url, map);


    }

    private void loginRegisterPost(String userType, String truename, String password, String mobile, String code, String regId) {


        HashMap<String, String> map = new HashMap<>();
        map.put("userType", userType);
        map.put("truename", truename);//18158188052
        map.put("password", password);//111
        map.put("mobile", mobile);
        map.put("code", code);
        map.put("regId", regId);


//        String url = (ApiConfig.BASE_URl+ApiConfig.LOGIN);
        String url = Api.URL + "/v1/user/register";
        postResRegister(url, map);


    }

    protected void postResLogin(String url, HashMap<String, String> map) {
        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.构造request
        //2.1构造requestbody

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
                .post(requestBodyJson)
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
                        ToastUtils.showTextToast2(LoginActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        L.e(res);

                        Gson gson = new Gson();

                        LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);

                        if (loginResponse.getCode() == 0) {

                            final String data = loginResponse.getData().toString();
                            LoginDataResponse loginDataResponse = gson.fromJson(data, LoginDataResponse.class);
                            String token = loginDataResponse.getToken();
                            int uid = loginDataResponse.getUid();
                            String type  = String.valueOf(loginDataResponse.getUserType());


                            saveStringToSp("token", token);
                            saveStringToSp("uid", String.valueOf(uid));
                            saveStringToSp("type", type);
                            if (loginDataResponse.getUserType() == 1 || loginDataResponse.getUserType() == 2 || loginDataResponse.getUserType() == 3){
                                saveStringToSp("usertype","1");
                            }else if (loginDataResponse.getUserType() == 4 || loginDataResponse.getUserType() == 5 || loginDataResponse.getUserType() == 6 || loginDataResponse.getUserType() == 7 || loginDataResponse.getUserType() == 8) {
                                saveStringToSp("usertype","2");
                            }


                            if (loginDataResponse.getUserType() == 3) {
                                if (loginDataResponse.isSupply() == false) {
                                    Intent intent = new Intent(LoginActivity.this, EcologyCompleteInformationActivity.class);
                                    startActivity(intent);
                                    ActivityCollector.removeActivity(LoginActivity.this);
                                    finish();
                                } else {
                                    if (loginDataResponse.isPayment() == 1) {
                                        Intent intent = new Intent(LoginActivity.this, PayJoinActivity.class);
                                        startActivity(intent);
                                        ActivityCollector.removeActivity(LoginActivity.this);
                                        finish();
                                    } else if (loginDataResponse.isPayment() == 2) {
                                        Intent intent = new Intent(LoginActivity.this, PayJoinExamineActivity.class);
                                        startActivity(intent);
                                        ActivityCollector.removeActivity(LoginActivity.this);
                                        finish();
                                    } else if (loginDataResponse.isPayment() == 3) {
                                        Intent intent = new Intent(LoginActivity.this, UserInterFaceActivity.class);
                                        startActivity(intent);
                                        ActivityCollector.removeActivity(LoginActivity.this);
                                        finish();
                                    }
                                }
                            } else if (loginDataResponse.getUserType() == 2 || loginDataResponse.getUserType() == 1) {
                                if (loginDataResponse.isSupply() == false) {
                                    Intent intent = new Intent(LoginActivity.this, StarFireCompleteInformationActivity.class);
                                    startActivity(intent);
                                    ActivityCollector.removeActivity(LoginActivity.this);
                                    finish();
                                } else {
                                    if (loginDataResponse.isPayment() == 3) {
                                        Intent intent = new Intent(LoginActivity.this, UserInterFaceActivity.class);
                                        startActivity(intent);
                                        ActivityCollector.removeActivity(LoginActivity.this);
                                        finish();
                                    }

                                }
                            }else if (loginDataResponse.getUserType() == 4 || loginDataResponse.getUserType() == 5 || loginDataResponse.getUserType() == 6 || loginDataResponse.getUserType() == 7 || loginDataResponse.getUserType() == 8){
                                if (loginDataResponse.isPayment() == 3) {
                                    Intent intent = new Intent(LoginActivity.this, UserInterFaceActivity.class);
                                    startActivity(intent);
                                    ActivityCollector.removeActivity(LoginActivity.this);
                                    finish();
                                }
                            }

                        } else if (loginResponse.getCode() == 10001) {
                            ToastUtils.showTextToast2(LoginActivity.this, "请先注册账户");
                        } else {
                            String msg = loginResponse.getMsg();
                            ToastUtils.showTextToast2(LoginActivity.this, msg);
                        }
                    }
                });

            }
        });

    }

    protected void postResRegister(String url, HashMap<String, String> map) {
        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.构造request
        //2.1构造requestbody

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
                .post(requestBodyJson)
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
                        ToastUtils.showTextToast2(LoginActivity.this, "网络请求失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
//                        L.e(res);

                        Gson gson = new Gson();

                        RegisterResponse registerResponse = gson.fromJson(res, RegisterResponse.class);

                        if (registerResponse.getCode() == 0) {


                            final String data = registerResponse.getData().toString();
                            RegisterDateResponse registerDateResponse = gson.fromJson(data, RegisterDateResponse.class);
                            String token = registerDateResponse.getToken();

                            int uid = registerDateResponse.getUid();

                            String type = registerDateResponse.getUserType();



                            saveStringToSp("uid", String.valueOf(uid));
                            saveStringToSp("token", token);
                            saveStringToSp("type", type);

                            if (registerDateResponse.getUserType().equals("1") || registerDateResponse.getUserType().equals("2") || registerDateResponse.equals("3")){
                                saveStringToSp("usertype","1");
                            }else if (registerDateResponse.getUserType().equals("4") || registerDateResponse.getUserType().equals("5") || registerDateResponse.getUserType().equals("6") || registerDateResponse.getUserType().equals("7") || registerDateResponse.getUserType().equals("8")) {
                                saveStringToSp("usertype","2");
                            }
                            saveStringToSp("uid", String.valueOf(uid));


                            if (registerDateResponse.getUserType().equals("3")) {
                                Intent intent = new Intent(LoginActivity.this, EcologyCompleteInformationActivity.class);
                                startActivity(intent);
                                ActivityCollector.removeActivity(LoginActivity.this);
                                finish();
                            } else if (registerDateResponse.getUserType().equals("2") || registerDateResponse.getUserType().equals("1")) {
                                Intent intent = new Intent(LoginActivity.this, StarFireCompleteInformationActivity.class);
                                startActivity(intent);
                                ActivityCollector.removeActivity(LoginActivity.this);
                                finish();
                            }


                        } else {
                            String msg = registerResponse.getMsg();
                            ToastUtils.showTextToast2(LoginActivity.this, msg);
                        }
                    }
                });

            }
        });

    }

    private void messagePost(String account) {
        HashMap<String, String> map = new HashMap<>();
        map.put("mobile", account);//18158188
        String url = Api.URL + "/v1/Jms";
        postResMessage(url, map);
    }

    protected void postResMessage(String url, HashMap<String, String> map) {
        //1.拿到okhttp对象
//        OkHttpClient okHttpClient = new OkHttpClient();


        //2.构造request
        //2.1构造requestbody

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
                .post(requestBodyJson)
                .build();
        //3.将request封装为call
        Call call = Api.ok().newCall(request);
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
                        ToastUtils.showTextToast2(LoginActivity.this, "短信发送失败");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                L.e("OnResponse");
                final String res = response.body().string();
                L.e(res);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        SendMessageResponse sendMessageResponse = gson.fromJson(res, SendMessageResponse.class);
                        String msg = sendMessageResponse.getMsg();


                        ToastUtils.showTextToast2(LoginActivity.this, msg);

                    }
                });


            }
        });


    }


    protected void saveStringToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.putString(key, val);
        editor.putString(key, val);
        editor.commit();
    }

    public String getTokenToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String token = sp.getString("token", "");
        return token;
    }

    public String getUidToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String uid = sp.getString("uid", "");
        return uid;
    }

    public String getUserTypeToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("token_uid_usertype", MODE_PRIVATE);
        String userType = sp.getString("usertype", "");
        return userType;
    }
}
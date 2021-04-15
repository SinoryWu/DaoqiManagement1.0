package com.example.daoqimanagement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.utils.ToastUtils;

public class DelayProtectDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private String cancel,confirm;
    private DelayProtectDialog.IOnCancelListener cancelListener;
    private DelayProtectDialog.IOnConfirmListener confirmListener;

    private TextView mTvOneMonth,mTvThreeMonth,mTvSixMonth;
    private Button mBtnConfirm,mBtnCancel;
    String month = "0";
    public DelayProtectDialog(@NonNull Context context) {
        super(context);
    }

    public DelayProtectDialog(@NonNull Context context, int themeResId) {

        super(context, themeResId);
        this.context = context;
    }

    protected DelayProtectDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

    }

    public DelayProtectDialog setCancel(String cancel, DelayProtectDialog.IOnCancelListener listener) {
        this.cancel = cancel;
        this.cancelListener = listener;
        return this;
    }

    public DelayProtectDialog setConfirm(String confirm, DelayProtectDialog.IOnConfirmListener listener) {
        this.confirm = confirm;
        this.confirmListener=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delay_protect_layout);
        mTvOneMonth= findViewById(R.id.dialog_delay_protect_tv_oneMonth);
        mTvThreeMonth= findViewById(R.id.dialog_delay_protect_tv_threeMonth);
        mTvSixMonth= findViewById(R.id.dialog_delay_protect_tv_sixMonth);
        mBtnConfirm = findViewById(R.id.dialog_delay_protect_btn_confirm);
        mBtnCancel = findViewById(R.id.dialog_delay_protect_btn_cancel);

        mTvOneMonth.setOnClickListener(this);
        mTvThreeMonth.setOnClickListener(this);
        mTvSixMonth.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        mTvOneMonth.performClick();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_delay_protect_btn_confirm:
                if (confirmListener != null){
                    if (month.equals("0")){
                        ToastUtils.showTextToast2(context,"请选择保护期");
                    }else {
                        confirmListener.onConfirm(DelayProtectDialog.this,month);

                        dismiss();
                    }


                }
                break;
            case R.id.dialog_delay_protect_btn_cancel:
                dismiss();
                break;
            case R.id.dialog_delay_protect_tv_oneMonth:
                mTvOneMonth.setBackgroundResource(R.drawable.delay_protect_btn_left_backgound_true);
                mTvOneMonth.setTextColor(Color.parseColor("#FFFFFF"));
                mTvThreeMonth.setBackgroundResource(R.drawable.delay_protect_btn_mid_backgound_false);
                mTvThreeMonth.setTextColor(Color.parseColor("#BCC5D3"));
                mTvSixMonth.setBackgroundResource(R.drawable.delay_protect_btn_right_backgound_false);
                mTvSixMonth.setTextColor(Color.parseColor("#BCC5D3"));
                month = mTvOneMonth.getText().toString();
                break;
            case R.id.dialog_delay_protect_tv_threeMonth:
                mTvOneMonth.setBackgroundResource(R.drawable.delay_protect_btn_left_backgound_false);
                mTvOneMonth.setTextColor(Color.parseColor("#BCC5D3"));
                mTvThreeMonth.setBackgroundResource(R.drawable.delay_protect_btn_mid_backgound_true);
                mTvThreeMonth.setTextColor(Color.parseColor("#FFFFFF"));
                mTvSixMonth.setBackgroundResource(R.drawable.delay_protect_btn_right_backgound_false);
                mTvSixMonth.setTextColor(Color.parseColor("#BCC5D3"));
                month = mTvThreeMonth.getText().toString();
                break;

            case R.id.dialog_delay_protect_tv_sixMonth:
                mTvOneMonth.setBackgroundResource(R.drawable.delay_protect_btn_left_backgound_false);
                mTvOneMonth.setTextColor(Color.parseColor("#BCC5D3"));
                mTvThreeMonth.setBackgroundResource(R.drawable.delay_protect_btn_mid_backgound_false);
                mTvThreeMonth.setTextColor(Color.parseColor("#BCC5D3"));
                mTvSixMonth.setBackgroundResource(R.drawable.delay_protect_btn_right_backgound_true);
                mTvSixMonth.setTextColor(Color.parseColor("#FFFFFF"));
                month = mTvSixMonth.getText().toString();
                break;




        }

    }


    public interface IOnConfirmListener{
        void onConfirm(DelayProtectDialog dialog,String month);
    }

    public interface IOnCancelListener{
        void onCancel(DelayProtectDialog dialog);
    }

}

package com.example.daoqimanagement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.utils.ToastUtils;

public class FormalReportDialog extends Dialog implements View.OnClickListener {
    private Context context;
    private String cancel,confirm;
    private FormalReportDialog.IOnCancelListener cancelListener;
    private FormalReportDialog.IOnConfirmListener confirmListener;
    String yesOrNo;
    int payProtect;
    String payAmount;


    public interface IOnConfirmListener{
        void onConfirm(FormalReportDialog dialog,String yesOrNo);
    }

    public interface IOnCancelListener{
        void onCancel(FormalReportDialog dialog);
    }


    public FormalReportDialog(@NonNull Context context) {
        super(context);
    }

    public FormalReportDialog(@NonNull Context context, int themeResId,int payProtect,String payAmount) {
        super(context, themeResId);
        this.context = context;
        this.payProtect = payProtect;
        this.payAmount = payAmount;
    }

    protected FormalReportDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public FormalReportDialog setCancel(String cancel, FormalReportDialog.IOnCancelListener listener) {
        this.cancel = cancel;
        this.cancelListener = listener;
        return this;
    }

    public FormalReportDialog setConfirm(String confirm, FormalReportDialog.IOnConfirmListener listener) {
        this.confirm = confirm;
        this.confirmListener=listener;
        return this;
    }

    private Button mBtnConfirm,mBtnCancel;
    private TextView mTvProtect,mTvAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_formal_report_layout);
        mBtnConfirm= findViewById(R.id.dialog_formal_report_btn_confirm);
        mBtnCancel= findViewById(R.id.dialog_formal_report_btn_cancel);
        mTvProtect = findViewById(R.id.formal_dialog_tv_payProtect);
        mTvAmount = findViewById(R.id.formal_dialog_tv_payAmount);

        mTvProtect.setText("报备保护期："+String.valueOf(payProtect)+"个月");
        mTvAmount.setText("报备保证金："+payAmount+"元");

        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_formal_report_btn_confirm:
                if (confirmListener != null){

                        confirmListener.onConfirm(FormalReportDialog.this,mBtnConfirm.getText().toString());

                        dismiss();

                }
                break;
            case R.id.dialog_formal_report_btn_cancel:
                dismiss();
                break;
        }
    }


}

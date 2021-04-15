package com.example.daoqimanagement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.daoqimanagement.R;

public class LoginOutDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private String cancel,confirm;
    private LoginOutDialog.IOnCancelListener cancelListener;
    private LoginOutDialog.IOnConfirmListener confirmListener;
    private Button mBtnConfirm,mBtnCancel;
    public LoginOutDialog(@NonNull Context context) {
        super(context);

    }

    public LoginOutDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }

    public LoginOutDialog setCancel(String cancel, LoginOutDialog.IOnCancelListener listener) {
        this.cancel = cancel;
        this.cancelListener = listener;
        return this;
    }

    public LoginOutDialog setConfirm(String confirm, LoginOutDialog.IOnConfirmListener listener) {
        this.confirm = confirm;
        this.confirmListener=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login_out_layout);

        mBtnConfirm = findViewById(R.id.dialog_login_out_btn_confirm);
        mBtnCancel = findViewById(R.id.dialog_login_out_btn_cancel);
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_login_out_btn_confirm:

                if (confirmListener != null){
                    confirmListener.onConfirm(LoginOutDialog.this);

                    dismiss();

                }

                break;
            case R.id.dialog_login_out_btn_cancel:
                dismiss();
                break;

        }
    }


    public interface IOnConfirmListener{
        void onConfirm(LoginOutDialog dialog);
    }

    public interface IOnCancelListener{
        void onCancel(LoginOutDialog dialog);
    }
}

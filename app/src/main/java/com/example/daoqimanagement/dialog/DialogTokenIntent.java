package com.example.daoqimanagement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.daoqimanagement.R;

public class DialogTokenIntent extends Dialog implements View.OnClickListener {


    private TextView mTvTitle,mTvMessage,mTvConfirm;

    private IOnConfirmListener confirmListener;

    private String title;
    private String message;
    private String confirm;



    public DialogTokenIntent setConfirm(String confirm, IOnConfirmListener listener) {
        this.confirm = confirm;
        this.confirmListener = listener;
        return this;
    }


    public DialogTokenIntent setTitle(String title) {
        this.title = title;
        return this;
    }

    public DialogTokenIntent setMessage(String message) {
        this.message = message;
        return this;
    }

    public DialogTokenIntent(@NonNull Context context) {
        super(context);
    }

    public DialogTokenIntent(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_token_intent);
        mTvTitle = findViewById(R.id.tv_token_title);
        mTvMessage = findViewById(R.id.tv_token_message);
        mTvConfirm = findViewById(R.id.tv_token_confirm);



       if (!TextUtils.isEmpty(title)){
           mTvTitle.setText(title);
       }
        if (!TextUtils.isEmpty(message)){
            mTvMessage.setText(message);
        }
        if (!TextUtils.isEmpty(confirm)){
            mTvConfirm.setText(confirm);
        }



        mTvConfirm.setOnClickListener(this);




    }

    public interface IOnConfirmListener{
        void OnConfirm(DialogTokenIntent dialog);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_token_confirm:
                if (confirmListener != null){
                    confirmListener.OnConfirm(this);
                    dismiss();
                }
                break;

        }
    }
}

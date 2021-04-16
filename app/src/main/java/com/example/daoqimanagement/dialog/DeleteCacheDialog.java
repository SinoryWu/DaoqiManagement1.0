package com.example.daoqimanagement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.example.daoqimanagement.R;

public class DeleteCacheDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private String cancel,confirm;
    private DeleteCacheDialog.IOnCancelListener cancelListener;
    private DeleteCacheDialog.IOnConfirmListener confirmListener;
    private Button mBtnConfirm,mBtnCancel;
    public DeleteCacheDialog(@NonNull Context context) {
        super(context);

    }

    public DeleteCacheDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }

    public DeleteCacheDialog setCancel(String cancel, DeleteCacheDialog.IOnCancelListener listener) {
        this.cancel = cancel;
        this.cancelListener = listener;
        return this;
    }

    public DeleteCacheDialog setConfirm(String confirm, DeleteCacheDialog.IOnConfirmListener listener) {
        this.confirm = confirm;
        this.confirmListener=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_cache_layout);

        mBtnConfirm = findViewById(R.id.dialog_delete_cache_btn_confirm);
        mBtnCancel = findViewById(R.id.dialog_delete_cache_btn_cancel);
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_delete_cache_btn_confirm:

                if (confirmListener != null){
                    confirmListener.onConfirm(DeleteCacheDialog.this);

                    dismiss();

                }

                break;
            case R.id.dialog_delete_cache_btn_cancel:
                dismiss();
                break;

        }
    }


    public interface IOnConfirmListener{
        void onConfirm(DeleteCacheDialog dialog);
    }

    public interface IOnCancelListener{
        void onCancel(DeleteCacheDialog dialog);
    }
}

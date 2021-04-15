package com.example.daoqimanagement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.utils.ToastUtils;

public class DeletePartnerDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private String cancel,confirm;
    private DeletePartnerDialog.IOnCancelListener cancelListener;
    private DeletePartnerDialog.IOnConfirmListener confirmListener;
    private Button mBtnConfirm,mBtnCancel;
    public DeletePartnerDialog(@NonNull Context context) {
        super(context);

    }

    public DeletePartnerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }

    public DeletePartnerDialog setCancel(String cancel, DeletePartnerDialog.IOnCancelListener listener) {
        this.cancel = cancel;
        this.cancelListener = listener;
        return this;
    }

    public DeletePartnerDialog setConfirm(String confirm, DeletePartnerDialog.IOnConfirmListener listener) {
        this.confirm = confirm;
        this.confirmListener=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_partner_layout);

        mBtnConfirm = findViewById(R.id.dialog_delete_partner_btn_confirm);
        mBtnCancel = findViewById(R.id.dialog_delete_partner_btn_cancel);
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_delete_partner_btn_confirm:

                if (confirmListener != null){
                    confirmListener.onConfirm(DeletePartnerDialog.this);

                    dismiss();

                }

                break;
            case R.id.dialog_delete_partner_btn_cancel:
                dismiss();
                break;

        }
    }


    public interface IOnConfirmListener{
        void onConfirm(DeletePartnerDialog dialog);
    }

    public interface IOnCancelListener{
        void onCancel(DeletePartnerDialog dialog);
    }
}

package com.example.daoqimanagement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.utils.ToastUtils;

public class DelayApplyDialog extends Dialog implements View.OnClickListener {
    private Context context;

    private String cancel,confirm;
    private DelayApplyDialog.IOnCancelListener cancelListener;
    private DelayApplyDialog.IOnConfirmListener confirmListener;
    private EditText mEtReason;
    private Button mBtnConfirm,mBtnCancel;
    public DelayApplyDialog(@NonNull Context context) {
        super(context);

    }

    public DelayApplyDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }

    public DelayApplyDialog setCancel(String cancel, DelayApplyDialog.IOnCancelListener listener) {
        this.cancel = cancel;
        this.cancelListener = listener;
        return this;
    }

    public DelayApplyDialog setConfirm(String confirm, DelayApplyDialog.IOnConfirmListener listener) {
        this.confirm = confirm;
        this.confirmListener=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delay_apply_layout);
        mEtReason = findViewById(R.id.dialog_delay_apply_et_content);
        mBtnConfirm = findViewById(R.id.dialog_delay_apply_btn_confirm);
        mBtnCancel = findViewById(R.id.dialog_delay_apply_btn_cancel);
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_delay_apply_btn_confirm:
                String reason = mEtReason.getText().toString();
                if (confirmListener != null){
                    if (reason.length() > 0 || !TextUtils.isEmpty(reason)){
                        confirmListener.onConfirm(DelayApplyDialog.this,reason);

                        dismiss();
                    }else {
                        ToastUtils.showTextToast2(context,"请输入申请理由");
                    }
                }

                break;
            case R.id.dialog_delay_apply_btn_cancel:
                dismiss();
                break;

        }
    }


    public interface IOnConfirmListener{
        void onConfirm(DelayApplyDialog dialog,String reason);
    }

    public interface IOnCancelListener{
        void onCancel(DelayApplyDialog dialog);
    }

    @Override
    public void dismiss() {
        View view = getCurrentFocus();
        if(view instanceof TextView){
            InputMethodManager mInputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            mInputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }

        super.dismiss();
    }




}

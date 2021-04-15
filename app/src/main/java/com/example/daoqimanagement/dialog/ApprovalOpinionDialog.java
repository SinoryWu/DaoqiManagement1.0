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

public class ApprovalOpinionDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private String cancel,confirm;
    private ApprovalOpinionDialog.IOnCancelListener cancelListener;
    private ApprovalOpinionDialog.IOnConfirmListener confirmListener;
    private EditText mEtApprovalOpinion;
    private Button mBtnConfirm,mBtnCancel;
    public ApprovalOpinionDialog(@NonNull Context context) {
        super(context);

    }

    public ApprovalOpinionDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }

    public ApprovalOpinionDialog setCancel(String cancel, ApprovalOpinionDialog.IOnCancelListener listener) {
        this.cancel = cancel;
        this.cancelListener = listener;
        return this;
    }

    public ApprovalOpinionDialog setConfirm(String confirm, ApprovalOpinionDialog.IOnConfirmListener listener) {
        this.confirm = confirm;
        this.confirmListener=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_approval_opinion_layout);
        mEtApprovalOpinion = findViewById(R.id.dialog_approval_opinion_et_content);
        mBtnConfirm = findViewById(R.id.dialog_approval_opinion_btn_confirm);
        mBtnCancel = findViewById(R.id.dialog_approval_opinion_btn_cancel);
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_approval_opinion_btn_confirm:
                String opinionContent = mEtApprovalOpinion.getText().toString();
                if (confirmListener != null){
                    if (opinionContent.length() > 0 || !TextUtils.isEmpty(opinionContent)){
                        confirmListener.onConfirm(ApprovalOpinionDialog.this,opinionContent);

                        dismiss();
                    }else {
                        ToastUtils.showTextToast2(context,"请输入内容");
                    }
                }

                break;
            case R.id.dialog_approval_opinion_btn_cancel:

                dismiss();
                break;

        }
    }


    public interface IOnConfirmListener{
        void onConfirm(ApprovalOpinionDialog dialog, String opinion);
    }

    public interface IOnCancelListener{
        void onCancel(ApprovalOpinionDialog dialog);
    }


    /**
     * 解决dialog消失时不消失软键盘
     */
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

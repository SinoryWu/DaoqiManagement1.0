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
import androidx.annotation.Nullable;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.utils.ToastUtils;

public class SubmitFeedbackDialog extends Dialog implements View.OnClickListener {

    private Context context;
    String feedBack;
    private String cancel,confirm;
    private SubmitFeedbackDialog.IOnCancelListener cancelListener;
    private SubmitFeedbackDialog.IOnConfirmListener confirmListener;
    private EditText mEtFeedBack;
    private Button mBtnConfirm,mBtnCancel;
    public SubmitFeedbackDialog(@NonNull Context context) {
        super(context);

    }

    public SubmitFeedbackDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }

    public SubmitFeedbackDialog setCancel(String cancel, SubmitFeedbackDialog.IOnCancelListener listener) {
        this.cancel = cancel;
        this.cancelListener = listener;
        return this;
    }

    public SubmitFeedbackDialog setConfirm(String confirm, SubmitFeedbackDialog.IOnConfirmListener listener) {
        this.confirm = confirm;
        this.confirmListener=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_submit_feedback_layout);
        mEtFeedBack= findViewById(R.id.dialog_feed_back_et_content);
        mBtnConfirm = findViewById(R.id.dialog_feed_back_btn_confirm);
        mBtnCancel = findViewById(R.id.dialog_feed_back_btn_cancel);
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_feed_back_btn_confirm:
                String feedContent = mEtFeedBack.getText().toString();
                if (confirmListener != null){
                    if (feedContent.length() > 0 || !TextUtils.isEmpty(feedContent)){
                        confirmListener.onConfirm(SubmitFeedbackDialog.this,feedContent);

                        dismiss();
                    }else {
                        ToastUtils.showTextToast2(context,"请输入反馈内容");
                    }
                }

                break;
            case R.id.dialog_feed_back_btn_cancel:
                dismiss();
                break;

        }
    }


    public interface IOnConfirmListener{
        void onConfirm(SubmitFeedbackDialog dialog,String feedBack);
    }

    public interface IOnCancelListener{
        void onCancel(SubmitFeedbackDialog dialog);
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

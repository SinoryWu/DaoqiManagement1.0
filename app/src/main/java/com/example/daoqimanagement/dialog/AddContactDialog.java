package com.example.daoqimanagement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.daoqimanagement.R;
import com.example.daoqimanagement.utils.ToastUtils;

public class AddContactDialog extends Dialog implements View.OnClickListener {

    private Context context;
    String feedBack;
    private String cancel,confirm;
    private AddContactDialog.IOnCancelListener cancelListener;
    private AddContactDialog.IOnConfirmListener confirmListener;
    private EditText mEtName,mEtMobile,mEtDepartment,mEtPosition;
    private Button mBtnConfirm,mBtnCancel;
    public AddContactDialog(@NonNull Context context) {
        super(context);

    }

    public AddContactDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;

    }

    public AddContactDialog setCancel(String cancel, AddContactDialog.IOnCancelListener listener) {
        this.cancel = cancel;
        this.cancelListener = listener;
        return this;
    }

    public AddContactDialog setConfirm(String confirm, AddContactDialog.IOnConfirmListener listener) {
        this.confirm = confirm;
        this.confirmListener=listener;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_contact_layout);
        mEtName= findViewById(R.id.dialog_add_contact_et_name);
        mEtMobile= findViewById(R.id.dialog_add_contact_et_mobile);
        mEtDepartment= findViewById(R.id.dialog_add_contact_et_department);
        mEtPosition= findViewById(R.id.dialog_add_contact_et_position);
        mBtnConfirm = findViewById(R.id.dialog_add_contact_btn_confirm);
        mBtnCancel = findViewById(R.id.dialog_add_contact_btn_cancel);
        mBtnConfirm.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_add_contact_btn_confirm:

                if (confirmListener != null){
                    if (mEtName.getText().toString().replace(" ","").length()==0){
                        ToastUtils.showTextToast2(context,"请输入姓名");
                    } else if (mEtMobile.getText().toString().replace(" ","").length()<11 && mEtMobile.getText().toString().replace(" ","").length()>0){
                        ToastUtils.showTextToast2(context,"请输入正确的手机号");
                    }else if (mEtMobile.getText().toString().replace(" ","").length()==0){
                        ToastUtils.showTextToast2(context,"请输入手机号");
                    }else if (mEtDepartment.getText().toString().replace(" ","").length()==0){
                        ToastUtils.showTextToast2(context,"请输入科室");
                    }else if (mEtPosition.getText().toString().replace(" ","").length()==0){
                        ToastUtils.showTextToast2(context,"请输入职位");
                    }else if (mEtMobile.getText().toString().replace(" ","").length()==11 && mEtName.getText().toString().replace(" ","").length() > 0 && mEtDepartment.getText().toString().replace(" ","").length() > 0 && mEtPosition.getText().toString().replace(" ","").length() > 0){
                        confirmListener.onConfirm(AddContactDialog.this,mEtName.getText().toString(),mEtMobile.getText().toString().replace(" ",""),mEtDepartment.getText().toString(),mEtPosition.getText().toString());

                        dismiss();
                    }



                }

                break;
            case R.id.dialog_add_contact_btn_cancel:
                dismiss();
                break;

        }
    }


    public interface IOnConfirmListener{
        void onConfirm(AddContactDialog dialog, String name,String mobile,String department,String position);
    }

    public interface IOnCancelListener{
        void onCancel(AddContactDialog dialog);
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

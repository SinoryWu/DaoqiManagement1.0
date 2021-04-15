package com.example.daoqimanagement.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.daoqimanagement.LoginActivity;
import com.example.daoqimanagement.R;
import com.example.daoqimanagement.utils.OnMultiClickListener;
import com.example.daoqimanagement.utils.ToastUtils;

public class ModifyStatusDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private String cancel,confirm;
    private ModifyStatusDialog.IOnCancelListener cancelListener;
    private ModifyStatusDialog.IOnConfirmListener confirmListener;

    public ModifyStatusDialog(@NonNull Context context) {
        super(context);
    }

    public ModifyStatusDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected ModifyStatusDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public ModifyStatusDialog setCancel(String cancel, ModifyStatusDialog.IOnCancelListener listener) {
        this.cancel = cancel;
        this.cancelListener = listener;
        return this;
    }

    public ModifyStatusDialog setConfirm(String confirm, ModifyStatusDialog.IOnConfirmListener listener) {
        this.confirm = confirm;
        this.confirmListener=listener;
        return this;
    }

    private TextView mTvModify;
    private Button mBtnConfirm,mBtnCancel;
    String status;
    private PopupWindow popupWindow;
    private View customView;
    private TextView mTvCancel,mTvFormal;
    private ImageView mIvModify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_modify_status_layout);
        mTvModify = findViewById(R.id.dialog_modify_status_tv_content);
        mBtnConfirm = findViewById(R.id.dialog_modify_status_btn_confirm);
        mBtnCancel = findViewById(R.id.dialog_modify_status_btn_cancel);
        mIvModify = findViewById(R.id.dialog_modify_status_iv_modify);

        status = mTvModify.getText().toString();
        mTvModify.setOnClickListener(this);
        mBtnConfirm.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                if (confirmListener != null){

                    confirmListener.onConfirm(ModifyStatusDialog.this,status);

                    dismiss();

                }
            }
        });
        mBtnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.dialog_modify_status_tv_content:
                initPopupWindowView();
                popupWindow.showAsDropDown(view, 0, dip2px(context, 3));
                mIvModify.setImageResource(R.mipmap.login_arrow_right_up_icon);
                break;



            case R.id.dialog_modify_status_btn_cancel:
                dismiss();
        }
    }

    public void initPopupWindowView() {
        // // 获取自定义布局文件pop.xml的视图
        customView = getLayoutInflater().inflate(R.layout.modify_status_popview_item,
                null, false);

        // 创建PopupWindow实例,280,160分别是宽度和高度
        popupWindow = new PopupWindow(customView, dip2px(context, 265), LinearLayout.LayoutParams.WRAP_CONTENT);


        mTvCancel = customView.findViewById(R.id.modify_popwindow_tv_cancel);
        mTvFormal = customView.findViewById(R.id.modify_popwindow_tv_formal);

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mTvModify.setText(mTvCancel.getText());
                status = mTvModify.getText().toString();
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;

                }
            }
        });
        mTvFormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvModify.setText(mTvFormal.getText());
                status = mTvModify.getText().toString();
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;

                }
            }
        });



        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        customView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    popupWindow = null;


                }


                return true;
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                popupWindow = null;
                Log.d("TAG", "onDismiss: ");
                mIvModify.setImageResource(R.mipmap.login_arrow_right_down_icon);

            }
        });

    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface IOnConfirmListener{
        void onConfirm(ModifyStatusDialog dialog,String status);
    }

    public interface IOnCancelListener{
        void onCancel(ModifyStatusDialog dialog);
    }
}

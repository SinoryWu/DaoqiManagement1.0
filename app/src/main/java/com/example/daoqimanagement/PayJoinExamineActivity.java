package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.daoqimanagement.dialog.LoginOutDialog;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.ActivityCollectorLogin;
import com.example.daoqimanagement.utils.OnMultiClickListener;

public class PayJoinExamineActivity extends AppCompatActivity {
    private LinearLayout mLlArrowLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollectorLogin.addActivity(this);
        ActivityCollector.addActivity(this);
        setContentView(R.layout.activity_pay_join_examine);
        mLlArrowLeft = findViewById(R.id.pay_join_examine_ll_arrowleft);
        mLlArrowLeft.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View view) {
                LoginOutDialog loginOutDialog = new LoginOutDialog(PayJoinExamineActivity.this,R.style.CustomDialog);
                loginOutDialog.setConfirm("是的", new LoginOutDialog.IOnConfirmListener() {
                    @Override
                    public void onConfirm(LoginOutDialog dialog) {
                        Intent intent = new Intent(PayJoinExamineActivity.this,LoginActivity.class);
                        startActivity(intent);
                        ActivityCollector.removeActivity(PayJoinExamineActivity.this);
                        finish();
                    }
                }).show();
            }
        });
    }
}
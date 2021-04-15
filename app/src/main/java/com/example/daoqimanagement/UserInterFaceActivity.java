package com.example.daoqimanagement;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.daoqimanagement.fragment.HomeFragment;
import com.example.daoqimanagement.fragment.MentorsFragment;
import com.example.daoqimanagement.fragment.PersonFragment;
import com.example.daoqimanagement.fragment.ApprovalFragment;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.utils.GetSharePerfenceSP;

import java.util.ArrayList;
import java.util.List;

public class UserInterFaceActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mRlTabHome,mRlTabMentors,mRlTabToast,mRlTabPerson;
    private ImageView mIvTabHome,mIvTabMentors,mIvTabToast,mIvTabPerson;
    private Fragment mHomeFragment,mMentorsFragment, mApprovalFragment,mPersonFragment,currentFragment;

    private List<Fragment> fragmentList = new ArrayList<>();
    View statusBarView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        if (TextUtils.isEmpty(GetSharePerfenceSP.getToken(UserInterFaceActivity.this))){
            Intent intent = new Intent(UserInterFaceActivity.this,LoginActivity.class);
            startActivity(intent);
            ActivityCollector.removeActivity(UserInterFaceActivity.this);
            finish();
        }
        //延时加载数据.
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                if (isStatusBar()) {
                    initStatusBar();
                    getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            initStatusBar();
                        }
                    });
                }
                statusBarView.setVisibility(View.GONE);
                //只走一次
                return false;
            }
        });

        initView();
        if (savedInstanceState != null) {
            //不为空说明缓存视图中有fragment实例，通过tag取出来
            /*获取保存的fragment  没有的话返回null*/
            mHomeFragment = (HomeFragment) getSupportFragmentManager().getFragment(savedInstanceState, "HomeFragment");
            mApprovalFragment = (ApprovalFragment) getSupportFragmentManager().getFragment(savedInstanceState, "ApprovalFragment");
            mMentorsFragment = (MentorsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "MentorsFragment");
            mPersonFragment = (PersonFragment)getSupportFragmentManager().getFragment(savedInstanceState, "PersonFragment");
            addToList(mHomeFragment);
            addToList(mApprovalFragment);
            addToList(mMentorsFragment);
            addToList(mPersonFragment);


        }else{
            initFragment();
        }
//        initTab();
        mRlTabHome.setOnClickListener(this);
        mRlTabMentors.setOnClickListener(this);
        mRlTabToast.setOnClickListener(this);
        mRlTabPerson.setOnClickListener(this);

    }

    public void initView(){
        setContentView(R.layout.activity_user_inter_face);
        mRlTabHome = findViewById(R.id.user_interface_rl_tab_home);
        mRlTabMentors = findViewById(R.id.user_interface_rl_tab_mentors);
        mRlTabToast = findViewById(R.id.user_interface_rl_tab_toast);
        mRlTabPerson = findViewById(R.id.user_interface_rl_tab_person);
        mIvTabHome = findViewById(R.id.user_interface_iv_tab_home);
        mIvTabMentors = findViewById(R.id.user_interface_iv_tab_mentors);
        mIvTabToast = findViewById(R.id.user_interface_iv_tab_toast);
        mIvTabPerson = findViewById(R.id.user_interface_iv_tab_person);

    }

    private void addToList(Fragment fragment) {
        if (fragment != null) {
            fragmentList.add(fragment);
        }


    }

    private void initFragment() {
        /* 默认显示home  fragment*/
        mHomeFragment = new HomeFragment();
        addFragment(mHomeFragment);
        showFragment(mHomeFragment);
        mIvTabHome.setImageResource(R.mipmap.tab_home_true_icon);
        mIvTabMentors.setImageResource(R.mipmap.tab_mentors_false_icon);
        mIvTabToast.setImageResource(R.mipmap.tab_toast_false_icon);
        mIvTabPerson.setImageResource(R.mipmap.tab_person_false_icon);

    }

    /*添加fragment*/
    private void addFragment(Fragment fragment) {

        /*判断该fragment是否已经被添加过  如果没有被添加  则添加*/
        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction().add(R.id.user_interface_content_layout, fragment).commit();
            /*添加到 fragmentList*/
            fragmentList.add(fragment);
        }


    }

    /*显示fragment*/
    private void showFragment(Fragment fragment) {



        for (Fragment frag : fragmentList) {

            if (frag != fragment) {
                /*先隐藏其他fragment*/

                getSupportFragmentManager().beginTransaction().hide(frag).commit();
            }
        }
        getSupportFragmentManager().beginTransaction().show(fragment).commit();

    }
    private void initTab(){
        if (mHomeFragment == null){
            mHomeFragment = new HomeFragment();
        }
        if (!mHomeFragment.isAdded()){
            // 提交事务
            getSupportFragmentManager().beginTransaction().add(R.id.user_interface_content_layout, mHomeFragment).commit();
            // 记录当前Fragment
            currentFragment = mHomeFragment;
            // 设置图片的变化
            mIvTabHome.setImageResource(R.mipmap.tab_home_true_icon);
            mIvTabMentors.setImageResource(R.mipmap.tab_mentors_false_icon);
            mIvTabToast.setImageResource(R.mipmap.tab_toast_false_icon);
            mIvTabPerson.setImageResource(R.mipmap.tab_person_false_icon);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.user_interface_rl_tab_home:

                if (mHomeFragment == null){
                    mHomeFragment = new HomeFragment();
                }
                addFragment(mHomeFragment);
                showFragment(mHomeFragment);
                statusBarView.setVisibility(View.GONE);
//                clickTabHomeLayout();
                mIvTabHome.setImageResource(R.mipmap.tab_home_true_icon);
                mIvTabMentors.setImageResource(R.mipmap.tab_mentors_false_icon);
                mIvTabToast.setImageResource(R.mipmap.tab_toast_false_icon);
                mIvTabPerson.setImageResource(R.mipmap.tab_person_false_icon);
                break;
            case R.id.user_interface_rl_tab_mentors:
                if (mMentorsFragment == null){
                    mMentorsFragment = new MentorsFragment();
                }
                addFragment(mMentorsFragment);
                showFragment(mMentorsFragment);
                statusBarView.setVisibility(View.GONE);
//                clickTabMentorsLayout();
                mIvTabHome.setImageResource(R.mipmap.tab_home_false_icon);
                mIvTabMentors.setImageResource(R.mipmap.tab_mentors_true_icon);
                mIvTabToast.setImageResource(R.mipmap.tab_toast_false_icon);
                mIvTabPerson.setImageResource(R.mipmap.tab_person_false_icon);
                break;
            case R.id.user_interface_rl_tab_toast:
                if (mApprovalFragment == null){
                    mApprovalFragment = new ApprovalFragment();
                }
                addFragment(mApprovalFragment);
                showFragment(mApprovalFragment);
                statusBarView.setVisibility(View.GONE);
                initStatusBar() ;
//                clickTabToastLayout();
                mIvTabHome.setImageResource(R.mipmap.tab_home_false_icon);
                mIvTabMentors.setImageResource(R.mipmap.tab_mentors_false_icon);
                mIvTabToast.setImageResource(R.mipmap.tab_toast_true_icon);
                mIvTabPerson.setImageResource(R.mipmap.tab_person_false_icon);
                break;
            case R.id.user_interface_rl_tab_person:
                if (mPersonFragment == null){
                    mPersonFragment = new PersonFragment();
                }
                addFragment(mPersonFragment);
                showFragment(mPersonFragment);
                statusBarView.setVisibility(View.GONE);
                statusBarView.setVisibility(View.VISIBLE);
                mIvTabHome.setImageResource(R.mipmap.tab_home_false_icon);
                mIvTabMentors.setImageResource(R.mipmap.tab_mentors_false_icon);
                mIvTabToast.setImageResource(R.mipmap.tab_toast_false_icon);
                mIvTabPerson.setImageResource(R.mipmap.tab_person_true_icon);
//                clickTabPersonLayout();
                break;

            default:
                break;
        }
    }

    private void initStatusBar() {
        if (statusBarView == null) {
            int identifier = getResources().getIdentifier("statusBarBackground", "id", "android");
            statusBarView = getWindow().findViewById(identifier);

        }
        if (statusBarView != null) {
            statusBarView.setBackgroundResource(R.drawable.person_tab_background);

        }
    }

    protected boolean isStatusBar() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        currentFragment.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 点击第一个tabHome
     */
    private void clickTabHomeLayout(){
        if (mHomeFragment == null){
            mHomeFragment = new HomeFragment();

        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(),mHomeFragment);
        //设置底部tab变化
        mIvTabHome.setImageResource(R.mipmap.tab_home_true_icon);
        mIvTabMentors.setImageResource(R.mipmap.tab_mentors_false_icon);
        mIvTabToast.setImageResource(R.mipmap.tab_toast_false_icon);
        mIvTabPerson.setImageResource(R.mipmap.tab_person_false_icon);
    }

    /**
     * 点击第二个tabMentors
     */
    private void clickTabMentorsLayout(){
        if (mMentorsFragment == null){
            mMentorsFragment = new MentorsFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(),mMentorsFragment);
        //设置底部tab变化
        mIvTabHome.setImageResource(R.mipmap.tab_home_false_icon);
        mIvTabMentors.setImageResource(R.mipmap.tab_mentors_true_icon);
        mIvTabToast.setImageResource(R.mipmap.tab_toast_false_icon);
        mIvTabPerson.setImageResource(R.mipmap.tab_person_false_icon);
    }

    /**
     * 点击第三个tabToast
     */
    private void clickTabToastLayout(){
        if (mApprovalFragment == null){
            mApprovalFragment = new ApprovalFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(), mApprovalFragment);
        //设置底部tab变化
        mIvTabHome.setImageResource(R.mipmap.tab_home_false_icon);
        mIvTabMentors.setImageResource(R.mipmap.tab_mentors_false_icon);
        mIvTabToast.setImageResource(R.mipmap.tab_toast_true_icon);
        mIvTabPerson.setImageResource(R.mipmap.tab_person_false_icon);
    }

    /**
     * 点击第四个tabPerson
     */
    private void clickTabPersonLayout(){
        if (mPersonFragment == null){
            mPersonFragment = new PersonFragment();
        }
        addOrShowFragment(getSupportFragmentManager().beginTransaction(),mPersonFragment);
        //设置底部tab变化
        mIvTabHome.setImageResource(R.mipmap.tab_home_false_icon);
        mIvTabMentors.setImageResource(R.mipmap.tab_mentors_false_icon);
        mIvTabToast.setImageResource(R.mipmap.tab_toast_false_icon);
        mIvTabPerson.setImageResource(R.mipmap.tab_person_true_icon);
    }

    /**
     * 添加或者显示碎片
     * @param transaction
     * @param fragment
     */
    private void addOrShowFragment(FragmentTransaction transaction, Fragment fragment){
        if (currentFragment == fragment){
            return ;
        }
        if (!fragment.isAdded()){// 如果当前fragment未被添加，则添加到Fragment管理器中
            transaction.hide(currentFragment).add(R.id.user_interface_content_layout, fragment).commit();

        }else {
            transaction.hide(currentFragment).show(fragment).commit();
//           getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
        }

        currentFragment = fragment;
    }

    /**
     * 按两次退出杀死进程
     */
    //声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private long mExitTime;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                System.exit(0);

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {

        /*fragment不为空时 保存*/
        if (mHomeFragment != null) {
            getSupportFragmentManager().putFragment(outState, "HomeFragment", mHomeFragment);
        }
        if (mApprovalFragment != null) {
            getSupportFragmentManager().putFragment(outState, "ApprovalFragment", mApprovalFragment);
        }
        if (mMentorsFragment != null) {
            getSupportFragmentManager().putFragment(outState, "MentorsFragment", mMentorsFragment);
        }

        if (mPersonFragment != null) {
            getSupportFragmentManager().putFragment(outState, "PersonFragment", mPersonFragment);
        }


        super.onSaveInstanceState(outState);
    }


}
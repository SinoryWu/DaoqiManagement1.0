package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.daoqimanagement.databinding.ActivityUserLinkBinding;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.viewmodel.TextViewModel;

public class UserLinkActivity extends AppCompatActivity {
    TextViewModel viewModel;
    ActivityUserLinkBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_link);
        ActivityCollector.addActivity(this);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_user_link);
        viewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(TextViewModel.class);
        viewModel.application = getApplication();
        viewModel.activity = UserLinkActivity.this;
        binding.setData(viewModel);
        binding.setLifecycleOwner(this);
    }
}
package com.example.daoqimanagement;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.daoqimanagement.databinding.ActivityPrivateLinkBinding;
import com.example.daoqimanagement.utils.ActivityCollector;
import com.example.daoqimanagement.viewmodel.TextViewModel;

public class PrivateLinkActivity extends AppCompatActivity {
    ActivityPrivateLinkBinding binding;
    TextViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);

//        viewModel.context  = getApplicationContext();
        binding = DataBindingUtil.setContentView(this,R.layout.activity_private_link);

        viewModel = new ViewModelProvider(this,new ViewModelProvider.NewInstanceFactory()).get(TextViewModel.class);
        viewModel.application = getApplication();
        viewModel.activity = PrivateLinkActivity.this;
        binding.setData(viewModel);
        binding.setLifecycleOwner(this);




    }
}
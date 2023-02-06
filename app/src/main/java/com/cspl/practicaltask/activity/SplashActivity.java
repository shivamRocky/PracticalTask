package com.cspl.practicaltask.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cspl.practicaltask.R;
import com.cspl.practicaltask.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private final int SPLASH_DISPLAY_LENGTH = 1500;
    Handler obj = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        obj.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashActivity.this, StartActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_DISPLAY_LENGTH);
    }
}
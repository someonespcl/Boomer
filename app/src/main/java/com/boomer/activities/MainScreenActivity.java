package com.boomer.activities;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.boomer.databinding.ActivityMainScreenBinding;

public class MainScreenActivity extends AppCompatActivity {

    private ActivityMainScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}

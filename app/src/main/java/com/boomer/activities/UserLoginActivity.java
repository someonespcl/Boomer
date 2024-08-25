package com.boomer.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.boomer.databinding.ActivityUserLoginBinding;

public class UserLoginActivity extends AppCompatActivity {
    
    private ActivityUserLoginBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
    
}

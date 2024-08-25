package com.boomer.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import com.boomer.R;
import androidx.appcompat.app.AppCompatActivity;
import com.boomer.databinding.ActivityUserRegisterBinding;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class UserRegisterActivity extends AppCompatActivity {

    private ActivityUserRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.passwordInput.setOnTouchListener(
                (v, event) -> {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX()
                                >= (binding.passwordInput.getRight()
                                        - binding.passwordInput
                                                .getCompoundDrawables()[2]
                                                .getBounds()
                                                .width())) {
                            boolean isPasswordVisible =
                                    !(binding.passwordInput.getTransformationMethod()
                                            instanceof PasswordTransformationMethod);
                            binding.passwordInput.setTransformationMethod(
                                    isPasswordVisible
                                            ? PasswordTransformationMethod.getInstance()
                                            : HideReturnsTransformationMethod.getInstance());
                            binding.passwordInput.setCompoundDrawablesRelativeWithIntrinsicBounds(
                                    0,
                                    0,
                                    isPasswordVisible ? R.drawable.eye_off : R.drawable.eye,
                                    0);
                            binding.passwordInput.setSelection(binding.passwordInput.length());
                            return true;
                        }
                    }
                    return false;
                });

        binding.usernameInput.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(
                            CharSequence arg0, int arg1, int arg2, int arg3) {}

                    @Override
                    public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                        String username = binding.usernameInput.getText().toString().trim();
                    
                        if(!username.isEmpty()) {
                            binding.usernameInput.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0, 0, R.drawable.checkmark, 0);
                            if(username.length() < 3) {
                            	binding.usernameInput.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0, 0, R.drawable.cross, 0);
                            }
                        } else {
                            binding.usernameInput.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    0, 0, R.drawable.cross, 0);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable arg0) {}
                });
    }

    public void validateDetails(View vDetails) {

        String username = binding.usernameInput.getText().toString().trim();
        String email = binding.emailInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();

        if (username.isEmpty()) {
            vibrateNShareOnError(binding.usernameInput);
            if (username.length() < 3) {
                vibrateNShareOnError(binding.usernameInput);
                binding.usernameInput.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0, 0, R.drawable.cross, 0);
            } else {
                binding.usernameInput.setCompoundDrawablesRelativeWithIntrinsicBounds(
                        0, 0, R.drawable.checkmark, 0);
            }
        } else {
            
        }
    }

    public void loginActivity(View loginActivity) {
        startActivity(new Intent(UserRegisterActivity.this, UserLoginActivity.class));
    }

    private void vibrateNShareOnError(View obj) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(VibrationEffect.createOneShot(256, VibrationEffect.DEFAULT_AMPLITUDE));
        YoYo.with(Techniques.Shake).duration(700).playOn(obj);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
}

package com.boomer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.MotionEvent;
import android.text.method.PasswordTransformationMethod;
import android.text.method.HideReturnsTransformationMethod;
import android.view.View;
import android.os.Vibrator;
import android.content.Context;
import android.os.VibrationEffect;
import com.boomer.model.User;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.boomer.databinding.ActivitySignUpWithEmailBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.boomer.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpWithEmailActivity extends AppCompatActivity {

	private ActivitySignUpWithEmailBinding binding;
	private FirebaseAuth mAuth;
	private FirebaseDatabase mDatabase;
	private DatabaseReference mDatabaseReference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		binding = ActivitySignUpWithEmailBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());

		mAuth = FirebaseAuth.getInstance();
		mDatabase = FirebaseDatabase.getInstance();
		mDatabaseReference = mDatabase.getReference("users");
		binding.passwordInput.setOnTouchListener((v, event) -> {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (event.getRawX() >= (binding.passwordInput.getRight()
						- binding.passwordInput.getCompoundDrawables()[2].getBounds().width())) {
					boolean isPasswordVisible = !(binding.passwordInput
							.getTransformationMethod() instanceof PasswordTransformationMethod);
					binding.passwordInput
							.setTransformationMethod(isPasswordVisible ? PasswordTransformationMethod.getInstance()
									: HideReturnsTransformationMethod.getInstance());
					binding.passwordInput.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0,
							isPasswordVisible ? R.drawable.eye_off : R.drawable.eye, 0);
					binding.passwordInput.setSelection(binding.passwordInput.length());
					return true;
				}
			}
			return false;
		});

		binding.emailInput.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int end, int count) {
				String email = s.toString().trim();

				if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
					binding.emailInput.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.cross, 0);
				} else {
					binding.emailInput.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.checkmark, 0);
				}
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		binding.createAcWithEmail.setOnClickListener(v -> {
			final String username = binding.usernameInput.getText().toString().trim();
			final String email = binding.emailInput.getText().toString().trim();
			final String password = binding.passwordInput.getText().toString().trim();

			if (username.isEmpty()) {
				vibrateNShareOnError(binding.usernameInput);
				binding.usernameInput.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.cross, 0);
				return;
			}
			if (username.length() < 3) {
				vibrateNShareOnError(binding.usernameInput);
				binding.usernameInput.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.cross, 0);
				return;
			}
			if (email.isEmpty()) {
				vibrateNShareOnError(binding.emailInput);
				binding.emailInput.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.cross, 0);
				return;
			}
			if (password.isEmpty()) {
				vibrateNShareOnError(binding.passwordInput);
				return;
			}
			if (password.length() < 6) {
				vibrateNShareOnError(binding.passwordInput);
				return;
			}
			createAccount(username, email, password);
		});
	}

	private void createAccount(String username, String email, String password) {
		mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpWithEmailActivity.this,
				new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {

							FirebaseUser currentUser = mAuth.getCurrentUser();
							if (currentUser != null) {

								String userId = currentUser.getUid();
                                long currentTimeStamp = System.currentTimeMillis();
								User user = new User(username, email, password, currentTimeStamp);
								mDatabaseReference.child(userId).setValue(user);
							}
                            
                            startActivity(new Intent(SignUpWithEmailActivity.this, MainScreenActivity.class));
                            finishAffinity();
                            
						} else {
							Toast.makeText(SignUpWithEmailActivity.this, task.getException().toString(),
									Toast.LENGTH_LONG).show();
						}
					}
				});
	}

	private void vibrateNShareOnError(View obj) {
		Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(VibrationEffect.createOneShot(256, VibrationEffect.DEFAULT_AMPLITUDE));
		YoYo.with(Techniques.Shake).duration(700).playOn(obj);
	}

	@Override
	protected void onStart() {
		super.onStart();
		final FirebaseUser currentUser = mAuth.getCurrentUser();
		if (currentUser != null) {
			startActivity(new Intent(SignUpWithEmailActivity.this, MainScreenActivity.class));
			finishAffinity();
		} else {

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.binding = null;
	}
}

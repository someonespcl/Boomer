package com.boomer.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.boomer.databinding.ActivityWelcomeBinding;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.boomer.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class WelcomeActivity extends AppCompatActivity {
    
    private ActivityWelcomeBinding binding;
    private FirebaseAuth mAuth;
    private SignInClient oneTapClient;
    private static final int REQ_ONE_TAP = 1;
    private boolean showOneTapUI = true;
    private BeginSignInRequest signInRequest;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        oneTapClient = Identity.getSignInClient(WelcomeActivity.this);
        signInRequest =
                BeginSignInRequest.builder()
                        .setGoogleIdTokenRequestOptions(
                                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                        .setSupported(true)
                                        .setServerClientId(
                                                getString(R.string.default_web_client_id))
                                        .setFilterByAuthorizedAccounts(false)
                                        .build())
                        .build();
        
        binding.continueWithGoogle.setOnClickListener(v -> {
            startOneTapSignIn();
        });
        
        binding.getStarted.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, SignUpWithEmailActivity.class));
        });
    }
    
    private void startOneTapSignIn() {
        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener(WelcomeActivity.this, result -> {
            try {
            	startIntentSenderForResult(result.getPendingIntent().getIntentSender(), REQ_ONE_TAP, null, 0, 0, 0);
            } catch(Exception e) {
            	
            }
        }).addOnFailureListener(WelcomeActivity.this, e -> {
            
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @NonNull Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // TODO: Implement this method
        if(requestCode == REQ_ONE_TAP) {
        	try {
        		SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                if(idToken != null) {
                	AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                    mAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(WelcomeActivity.this, task -> {
                        if(task.isSuccessful()) {
                        	Toast.makeText(WelcomeActivity.this, "Success Full", Toast.LENGTH_LONG).show();
                        } else {
                            
                        }
                    });
                }
        	} catch(Exception err) {
        		Toast.makeText(WelcomeActivity.this, err.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        	}
        }
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
        	startActivity(new Intent(WelcomeActivity.this, MainScreenActivity.class));
            finishAffinity();
        } else {
            
        }
    }
    
    
}
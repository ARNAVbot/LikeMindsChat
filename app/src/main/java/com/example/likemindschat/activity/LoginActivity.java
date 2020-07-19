package com.example.likemindschat.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.likemindschat.Prefs;
import com.example.likemindschat.R;
import com.example.likemindschat.databinding.ActivityLoginBinding;
import com.example.likemindschat.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private ActivityLoginBinding binding;
    private final String TAG = "LOGIN_ACTIVITY";
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        binding.acbSubmit.setOnClickListener(this);
    }

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading...");
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }

    public void hideProgressDialog() {
        if ((progressDialog != null) && (progressDialog.isShowing())) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.acb_submit:
                // check if email is not empty
                if (Utils.isEmpty(binding.acetUserEmail.getText().toString())) {
                    Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                // check if password is not empty
                if (Utils.isEmpty(binding.acetUserPassword.getText().toString())) {
                    Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                showProgressDialog();
                Utils.hideKeyboard(this);
                // call firebase to login this user. Get auth object and store user-id in prefs
                mAuth.signInWithEmailAndPassword(binding.acetUserEmail.getText().toString(), binding.acetUserPassword.getText().toString())
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                hideProgressDialog();
                                if (task.isSuccessful()) {
                                    // Sign in is done
                                    Log.d(TAG, "signIn = success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    setDataAndOpenChatScreen(user);

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signIn = failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                break;
        }
    }

    private void setDataAndOpenChatScreen(FirebaseUser user) {
        Prefs.getInstance().setAuthToken(user.getUid());
        Prefs.getInstance().setUserName(user.getEmail());
        Intent intent = new Intent(this, ChatScreenActivity.class);
        startActivity(intent);
        finish();
    }
}

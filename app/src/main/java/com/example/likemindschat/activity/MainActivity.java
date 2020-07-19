package com.example.likemindschat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.likemindschat.Prefs;
import com.example.likemindschat.databinding.ActivityMainBinding;
import com.example.likemindschat.utils.Utils;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent;
        if (Utils.isEmpty(Prefs.getInstance().getAuthToken())) {
            // start login activity
            intent = new Intent(this, LoginActivity.class);
        } else {
            // start home chat activity
            intent = new Intent(this, ChatScreenActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
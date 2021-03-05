package com.kozlovskiy.mostocks.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.kozlovskiy.mostocks.ui.main.MainActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(
                SplashActivity.this,
                MainActivity.class)
        );
    }
}
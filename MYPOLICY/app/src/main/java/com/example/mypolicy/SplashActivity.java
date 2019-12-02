package com.example.mypolicy;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        setContentView(R.layout.activity_splash);
        try
        {
            Thread.sleep(1700);
        }catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        startActivity(new Intent(SplashActivity.this,LoginActivity.class));
        finish();
    }
}

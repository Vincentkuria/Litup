package com.vinikuria.the20first;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Splashscreen extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        mRunnable=new Runnable() {
            @Override
            public void run() {
                Intent goToLoginActivity=new Intent(Splashscreen.this,Login.class);
                goToLoginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToLoginActivity);

            }
        };

        mHandler=new Handler();
        mHandler.postDelayed(mRunnable,2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHandler!=null & mRunnable!=null)
            mHandler.removeCallbacks(mRunnable);

    }
}
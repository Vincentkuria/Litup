package com.vinikuria.the20first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import java.util.LinkedList;

public class Splashscreen extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;
    Boolean isLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        if (ActivityCompat.checkSelfPermission(Splashscreen.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(Splashscreen.this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            /***CALL THE PROGRAMM TO LOAD DATA*****/
            LinkedList<String> linkedList =new FetchData(new GetLocation(Splashscreen.this).userLocation()).trimData();
            isLoaded=true;
            goToMainActivity();
        }else{

            mRunnable=new Runnable() {
                @Override
                public void run() {
                    goToMainActivity();
                }
            };

            mHandler=new Handler();
            mHandler.postDelayed(mRunnable,2000);

        }

    }

    private void goToMainActivity() {
        Intent goToMainActivity=new Intent(Splashscreen.this,MainActivity.class);
        goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        goToMainActivity.putExtra("isloaded",isLoaded);
        startActivity(goToMainActivity);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mHandler!=null & mRunnable!=null)
            mHandler.removeCallbacks(mRunnable);

    }
}
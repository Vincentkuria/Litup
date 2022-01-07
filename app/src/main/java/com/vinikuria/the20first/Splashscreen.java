package com.vinikuria.the20first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import java.util.LinkedList;

public class Splashscreen extends AppCompatActivity {

    private Handler mHandler;
    private Runnable mRunnable;
    Boolean isLoaded;
    static Object object=new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        if (ActivityCompat.checkSelfPermission(Splashscreen.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED &&
        ActivityCompat.checkSelfPermission(Splashscreen.this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            /***CALL THE PROGRAMM TO LOAD DATA*****/
            Log.d("LitupDebug","permitted1");

            Runnable runnable1=new Runnable() {
                @Override
                public void run() {
                    synchronized (object){
                        try {
                            object.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    isLoaded=true;
                    goToMainActivity();
                }
            };



            Runnable runnable=new Runnable() {
                @Override
                public synchronized void run() {
                    new GetLocation(Splashscreen.this).userLocation();
                }
            };

            Thread thread1=new Thread(runnable1);
            Thread thread=new Thread(runnable);
            thread1.start();
            thread.start();


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
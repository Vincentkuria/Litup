package com.vinikuria.the20first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    static Boolean in_mainpage=false;
    static Boolean search_open=false;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        Boolean isDataLoaded=new Intent().getBooleanExtra("isloaded",false);
        if (currentUser==null){
            Intent GoToLoginActivity=new Intent(MainActivity.this,Login.class);
            GoToLoginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(GoToLoginActivity);
        }else if (true){
            /**check if user details have been received***/
        }else if (!isDataLoaded){
            askPermissionforLocation();
        }

        viewPager2=findViewById(R.id.viewpager);
        viewPager2.setAdapter(new Viewpager_adapter(this));
    }

    private void askPermissionforLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission_group.LOCATION},1);
        }

    }

    @Override
    public void onBackPressed() {
        if (in_mainpage && search_open){
            viewPager2.setAdapter(new Viewpager_adapter(this));
        }else if (!in_mainpage){
            viewPager2.setAdapter(new Viewpager_adapter(this));
        }else{
            super.onBackPressed();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                /***CALL THE PROGRAMM TO LOAD DATA*****/
            }else if (grantResults[0]==PackageManager.PERMISSION_DENIED){
                Dialog dialog=new Dialog(this);
                dialog.setContentView(R.layout.location_permission_explain);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_animation;
                Button accept=dialog.findViewById(R.id.accept_permission);

                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        askPermissionforLocation();
                    }
                });

                /**call the programe with null location if still denied**/

            }

        }
    }
}
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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    static Boolean search_open=false;
    FirebaseAuth mAuth;
    boolean askedPermissionAgain=false;
    DatabaseReference databaseReference;
    String emailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        emailId = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail().replace(".", "").trim();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        Boolean isDataLoaded=new Intent().getBooleanExtra("isloaded",false);
        if (currentUser==null) {
            Intent GoToLoginActivity = new Intent(MainActivity.this, Login.class);
            GoToLoginActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(GoToLoginActivity);
        }else{
            databaseReference
                    .child("USERS")
                    .child(emailId.toUpperCase())
                    .child("PROFILE").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean detailsReceived=snapshot.hasChild("Username");
                    if (!detailsReceived){
                        Log.d("cool1","watt");
                        Intent GoToGetdetails = new Intent(MainActivity.this, Getdetails.class);
                        GoToGetdetails.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(GoToGetdetails);
                        Log.d("cool2","wattt");
                    }else if (!isDataLoaded && detailsReceived){
                        askPermissionforLocation();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        if (currentUser!=null){
            DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
            connectedRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean connected = snapshot.getValue(Boolean.class);
                    if (connected) {
                        databaseReference
                                .child("USERS")
                                .child(emailId.toUpperCase())
                                .child("PROFILE")
                                .child("Online Status").setValue("true");

                        databaseReference
                                .child("USERS")
                                .child(emailId.toUpperCase())
                                .child("PROFILE")
                                .child("Online Status").onDisconnect().setValue("false");

                        databaseReference
                                .child("USERS")
                                .child(emailId.toUpperCase())
                                .child("PROFILE")
                                .child("Last Online").onDisconnect().setValue(ServerValue.TIMESTAMP);
                    } else {
                        databaseReference
                                .child("USERS")
                                .child(emailId.toUpperCase())
                                .child("PROFILE")
                                .child("Online Status").setValue("false");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        viewPager2=findViewById(R.id.viewpager);
        viewPager2.setAdapter(new Viewpager_adapter(this,viewPager2));
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_DENIED &&
                ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_DENIED){
            /***CALL THE PROGRAMM TO LOAD DATA with null location *****/
            StoreLoadedData.null_loc_linkedList=new FetchData(null).trimData();
        }
    }

    private void askPermissionforLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},1);
        }

    }

    @Override
    public void onBackPressed() {
        int currentItem=viewPager2.getCurrentItem();
        switch (currentItem) {
            case 1:
                viewPager2.setCurrentItem(0);
                break;
            case 0:
                if (Mainpage.menu_dropdown.getVisibility()==View.VISIBLE){
                    Mainpage.menu_dropdown.setVisibility(View.INVISIBLE);
                }else if (search_open){
                    viewPager2.setAdapter(new Viewpager_adapter(this,viewPager2));
                }else {
                    super.onBackPressed();
                }

                break;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                /***CALL THE PROGRAMM TO LOAD DATA*****/
                //new GetLocation(MainActivity.this).userLocation();
            }else if (!askedPermissionAgain && grantResults[0]==PackageManager.PERMISSION_DENIED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(permissions[0])){
                        displayDialog();
                    }
                }else {
                    displayDialog();
                }


            }

        }
    }

    private void displayDialog() {
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.location_permission_explain);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_animation;
        Button accept=dialog.findViewById(R.id.accept_permission);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                askPermissionforLocation();
            }
        });
        dialog.show();
        askedPermissionAgain=true;
    }
}
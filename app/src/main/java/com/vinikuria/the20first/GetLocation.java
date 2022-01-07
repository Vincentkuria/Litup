package com.vinikuria.the20first;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class GetLocation {
    Context context;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location location;

    public GetLocation(Context context){
        this.context=context;
    }

    @SuppressLint("MissingPermission")
    public void userLocation(){
        Log.d("LitupDebug","permitted3");
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(context);
        LocationManager locationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            Log.d("LitupDebug","permitted4");
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Log.d("LitupDebug","permitted5");
                    Location mLocation=task.getResult();
                    if (mLocation!=null){
                        location =mLocation;
                    }else{
                        LocationRequest locationRequest=new LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);

                        LocationCallback locationCallback=new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                location=locationResult.getLastLocation();
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                    }
                    FirebaseAuth mAuth=FirebaseAuth.getInstance();
                    String emailId = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail().replace(".", "").trim().toUpperCase();
                    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
                    databaseReference
                            .child("USERS")
                            .child(emailId)
                            .child("PROFILE")
                            .child("Location")
                            .setValue(location);
                    for (int i=1;i<6;i++){
                        String a="VINVINCENT254@GMAILCOM"+i;
                        StoreLoadedData.linkedList.add(a);
                    }
                    Log.d("LitupDebug","permitted6");
                    synchronized (Splashscreen.object){
                        Splashscreen.object.notify();
                    }

                    //StoreLoadedData.linkedList=new FetchData(location).trimData();
                }
            });
        }
    }

}

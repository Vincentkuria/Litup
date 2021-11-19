package com.vinikuria.the20first;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.Objects;

public class FetchData {
    Location location;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    String emailId = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail().replace(".", "").trim();

    public FetchData(Location location) {
        this.location = location;
    }

    public LinkedList<String> trimData (){
        LinkedList<String> linkedList=new LinkedList<>();
        trimByGender();
        return linkedList;
    }

    private void trimByGender() {
        final String[] userGender = new String[1];
        LinkedList<String> trimmed=new LinkedList<>();
        databaseReference.child("USERS").child(emailId.toUpperCase()).child("PROFILE").child("Gender")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                            userGender[0] = (String) snapshot.getValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        databaseReference.child("USERS").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dss:snapshot.getChildren()){
                    String string=(String) snapshot
                            .child(Objects.requireNonNull(dss.getKey()))
                            .child("PROFILE")
                            .child("Gender")
                            .getValue();

                    if (!userGender[0].equals(string)){
                        trimmed.add(dss.getKey());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("coool",error.toString());
            }
        });
        return;
    }
}

package com.vinikuria.the20first;

import android.location.Location;
import android.util.Log;

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
        return addCloseRelations(sortByLocation(trimByAge(trimByGender())));
    }

    private LinkedList<String> trimByGender() {
        final String[] userGender = new String[1];
        LinkedList<String> genderTrimmed=new LinkedList<>();
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
                        genderTrimmed.add(dss.getKey());
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("coool",error.toString());
            }
        });
        return genderTrimmed;
    }


    private LinkedList <String> trimByAge(LinkedList<String> strings) {
        LinkedList <String> ageTrimmed=new LinkedList<>();

        return ageTrimmed;
    }

    private LinkedList<String> sortByLocation(LinkedList<String> trimByGender) {
        if (location==null){
            return trimByGender;
        }else {
            LinkedList <String> locationSorted=new LinkedList<>();
            for (String a:trimByGender) {
                databaseReference
                        .child("USERS")
                        .child(a)
                        .child("PROFILE")
                        .child("Location")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                /***compare locations and sort***/
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

            }
            return locationSorted;
        }


    }

    private LinkedList<String> addCloseRelations(LinkedList<String> sortByLocation) {
        LinkedList<String> linkedList=new LinkedList<>();
        /****add those he previously liked or liked him
         * and those showed interest
         * those invited him to a groupdate
         *
         ****/
        return linkedList;
    }
}

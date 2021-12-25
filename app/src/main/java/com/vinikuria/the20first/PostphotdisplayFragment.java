package com.vinikuria.the20first;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

public class PostphotdisplayFragment extends Fragment {

    ImageView displayPostPhoto;
    TextView postPhoto2;
    static String emailId;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    static DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    static FirebaseStorage storage=FirebaseStorage.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_postphotdisplay, container, false);
        displayPostPhoto=view.findViewById(R.id.displayPostPhoto);
        postPhoto2=view.findViewById(R.id.postPhoto2);
        emailId = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail().replace(".", "").trim();

        Glide.with(getContext()).load(DisplaystorageRecycler.files.get(DisplaystorageRecycler.clicked-1)).placeholder(R.drawable.error).into(displayPostPhoto);
        postPhoto2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference
                        .child("USERS")
                        .child(emailId.toUpperCase())
                        .child("postTime").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        databaseReference
                                .child("USERS")
                                .child(emailId.toUpperCase())
                                .child("postTime").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String time=String.valueOf(snapshot.getValue());
                                final StorageReference postsFolder=storage.getReference().child("Posts").child(emailId).child(time);
                                if (DisplaystorageRecycler.clicked!=0){
                                    postsFolder.putFile(Uri.fromFile(DisplaystorageRecycler.files.get(DisplaystorageRecycler.clicked-1))).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            postsFolder.getDownloadUrl()
                                                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Uri> task) {
                                                            String postUrl=task.getResult().toString();
                                                            databaseReference
                                                                    .child("USERS")
                                                                    .child(emailId.toUpperCase())
                                                                    .child("POSTS")
                                                                    .push().setValue(postUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    StoreLoadedData.linkedList.add(emailId.toUpperCase());
                                                                    Intent intent=new Intent(getContext(),MainActivity.class);
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                        }
                                                    });
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });

            }
        });

        return view;
    }
}
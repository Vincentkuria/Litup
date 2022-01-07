package com.vinikuria.the20first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class DisplaystorageActivity extends AppCompatActivity {
    Toolbar toolbar;
    static RecyclerView display_storeRecycler;
    String fileType="PHOTO";
    ArrayList<File> files=new ArrayList<>();
    static ShapeableImageView selectedItem;
    static TextView postPhoto1;
    static RelativeLayout postRelativeLayout;
    static String emailId;
    FirebaseAuth mAuth;
    static DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    static FirebaseStorage storage=FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaystorage);
        toolbar=findViewById(R.id.toolbar);
        selectedItem=findViewById(R.id.selectedItem);
        display_storeRecycler=findViewById(R.id.display_storeRecycler);
        postPhoto1=findViewById(R.id.postPhoto1);
        postRelativeLayout=findViewById(R.id.postRelativeLayout);
        mAuth=FirebaseAuth.getInstance();
        emailId = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail().replace(".", "").trim();

        toolbar.setTitle("Photos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        postPhoto1.setOnClickListener(new View.OnClickListener() {
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
                                                                    task.addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            StoreLoadedData.linkedList.add(0,emailId.toUpperCase());
                                                                            Intent intent=new Intent(DisplaystorageActivity.this,MainActivity.class);
                                                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                            startActivity(intent);
                                                                        }
                                                                    });
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

        selectedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FragmentManager manager=getSupportFragmentManager();
               manager.beginTransaction().add(R.id.frame,new PostphotdisplayFragment()).commit();
               display_storeRecycler.setVisibility(View.INVISIBLE);
               toolbar.setVisibility(View.INVISIBLE);
               postRelativeLayout.setVisibility(View.INVISIBLE);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        String[] allPath=StorageUtil.getStorageDirectories(this);
        for (String path:allPath){
            File storage=new File(path);
            files.addAll(Filefinder.loadFile(storage,fileType));
        }
        Log.d("Errvin", String.valueOf(files.size()));

        DisplaystorageRecycler displaystorageRecycler=new DisplaystorageRecycler(files,this);
        display_storeRecycler.setLayoutManager(new GridLayoutManager(this,4));
        display_storeRecycler.setAdapter(displaystorageRecycler);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.frame);

        if (fragment!=null) {
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().remove(fragment).commit();
            display_storeRecycler.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            postRelativeLayout.setVisibility(View.VISIBLE);
        }else{
            super.onBackPressed();
        }


    }
}
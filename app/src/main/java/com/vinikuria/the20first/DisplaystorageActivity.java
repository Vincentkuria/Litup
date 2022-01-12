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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
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
    SpinKitView spinKitView;
    static boolean posting=false;
    static DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    static FirebaseStorage storage=FirebaseStorage.getInstance();
    LinearLayout showPosting1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displaystorage);
        spinKitView=findViewById(R.id.storageSpinKit);
        toolbar=findViewById(R.id.toolbar);
        selectedItem=findViewById(R.id.selectedItem);
        display_storeRecycler=findViewById(R.id.display_storeRecycler);
        postPhoto1=findViewById(R.id.postPhoto1);
        postRelativeLayout=findViewById(R.id.postRelativeLayout);
        mAuth=FirebaseAuth.getInstance();
        showPosting1=findViewById(R.id.showPosting1);
        emailId = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail().replace(".", "").trim();

        toolbar.setTitle("Photos");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spinKitView.setVisibility(View.VISIBLE);

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                String[] allPath=StorageUtil.getStorageDirectories(DisplaystorageActivity.this);
                for (String path:allPath){
                    File storage=new File(path);
                    files.addAll(Filefinder.loadFile(storage,fileType));
                }

            }
        });
        thread.start();
        boolean b=true;
        while (b){
            if (!thread.isAlive()){
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                spinKitView.setVisibility(View.GONE);
                Log.d("Errvin", String.valueOf(files.size()));

                DisplaystorageRecycler displaystorageRecycler=new DisplaystorageRecycler(files,this);
                display_storeRecycler.setLayoutManager(new GridLayoutManager(this,4));
                display_storeRecycler.setAdapter(displaystorageRecycler);
                b=false;
            }

        }


        postPhoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPosting1.setVisibility(View.VISIBLE);
                postPhoto1.setVisibility(View.GONE);
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
                                                                    .child("POSTED")
                                                                    .push()
                                                                    .child("Url").setValue(postUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    task.addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void unused) {
                                                                            StoreLoadedData.linkedList.add(0,emailId.toUpperCase());
                                                                            Toast.makeText(DisplaystorageActivity.this, "Image posted successfully", Toast.LENGTH_SHORT).show();
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
    public void onBackPressed() {
        Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.frame);

        if (fragment!=null && posting) {
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().remove(fragment).commit();
            display_storeRecycler.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            postRelativeLayout.setVisibility(View.GONE);
        }else if (fragment!=null){
            FragmentManager manager=getSupportFragmentManager();
            manager.beginTransaction().remove(fragment).commit();
            display_storeRecycler.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.VISIBLE);
            postRelativeLayout.setVisibility(View.VISIBLE);
        }else {
            super.onBackPressed();
        }


    }
}
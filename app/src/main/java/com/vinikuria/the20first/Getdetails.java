package com.vinikuria.the20first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Objects;

public class Getdetails extends AppCompatActivity {
    ShapeableImageView mProf_image;
    ImageView mAdd_image;
    EditText mUsername_input;
    TextView mHobbies_input,mUsername_error_alt;
    Button mBtn_next;
    static Button hobbies_save;
    FirebaseAuth mAuth;
    RadioGroup mRadiogender,mRadioage,mRadioheight;
    private FirebaseStorage storage=FirebaseStorage.getInstance();
    private StorageReference profileFOLDER;
    private DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();
    String emailId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdetails);
        mProf_image=findViewById(R.id.prof_image);
        mAdd_image=findViewById(R.id.add_image);
        mUsername_input=findViewById(R.id.username_input);
        mHobbies_input=findViewById(R.id.hobbies_input);
        mBtn_next=findViewById(R.id.btn_next);
        mUsername_error_alt=findViewById(R.id.username_error_alt);
        mRadiogender=findViewById(R.id.radiogender);
        mRadioage=findViewById(R.id.radioage);
        mRadioheight=findViewById(R.id.radioheight);
        mAuth=FirebaseAuth.getInstance();
        emailId = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail().replace(".", "").trim();
        profileFOLDER=storage.getReference().child("PROFILE PICTURES").child(emailId);

        databaseReference
                .child("USERS")
                .child(emailId.toUpperCase())
                .child("PROFILE")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChild("Profile image url")){
                    String prfImgURL=snapshot.child("Profile image url").getValue().toString();
                    Glide.with(Getdetails.this)
                            .load(prfImgURL)
                            .centerCrop()
                            .placeholder(R.drawable.ic_account_circle)
                            .into(mProf_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mAdd_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1,1)
                        .start(Getdetails.this);
            }
        });


        mBtn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gender = null,age = null,height = null,username = null;

                String name=mUsername_input.getText().toString().trim();
                if (!TextUtils.isEmpty(name)){
                    username=mUsername_input.getText().toString();
                }else{
                    mUsername_error_alt.setVisibility(View.VISIBLE);
                }

                if(mRadiogender.getCheckedRadioButtonId()!=-1){
                    RadioButton radioButton=findViewById(mRadiogender.getCheckedRadioButtonId());
                    gender= (String) radioButton.getText();
                }

                if(mRadioage.getCheckedRadioButtonId()!=-1){
                    RadioButton radioButton=findViewById(mRadioage.getCheckedRadioButtonId());
                    age= (String) radioButton.getText();
                }

                if(mRadioheight.getCheckedRadioButtonId()!=-1){
                    RadioButton radioButton=findViewById(mRadioheight.getCheckedRadioButtonId());
                    height= (String) radioButton.getText();
                }

                if (username!=null && gender!=null && age!=null && height!=null){
                    databaseReference
                            .child("USERS")
                            .child(emailId.toUpperCase())
                            .child("PROFILE")
                            .child("Username").setValue(username);

                    databaseReference
                            .child("USERS")
                            .child(emailId.toUpperCase())
                            .child("PROFILE")
                            .child("Gender").setValue(gender);

                    databaseReference
                            .child("USERS")
                            .child(emailId.toUpperCase())
                            .child("PROFILE")
                            .child("Age").setValue(age);

                    databaseReference
                            .child("USERS")
                            .child(emailId.toUpperCase())
                            .child("PROFILE")
                            .child("Height").setValue(height);

                    Intent intent=new Intent(Getdetails.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }else{
                    Toast.makeText(Getdetails.this,"NOTE All sections are required",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mUsername_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUsername_error_alt.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mHobbies_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(Getdetails.this);
                dialog.setContentView(R.layout.hobbiesview);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.getWindow().getAttributes().windowAnimations=R.style.dialog_animation;

                Button cancle_hobiDialog=dialog.findViewById(R.id.cancle_hobiDialog);
                hobbies_save=dialog.findViewById(R.id.hobbies_save);
                RecyclerView recyclerView=dialog.findViewById(R.id.hobbies_recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(Getdetails.this));
                HobbiesrecyclerAdapter hobbiesrecyclerAdapter = new HobbiesrecyclerAdapter();
                recyclerView.setAdapter(hobbiesrecyclerAdapter);

                hobbies_save.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onClick(View v) {
                        databaseReference
                                .child("USERS")
                                .child(emailId.toUpperCase())
                                .child("PROFILE").child("Hobbies")
                                .setValue(HobbiesrecyclerAdapter.Myhobbies)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                mHobbies_input.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_painting,0,R.drawable.ic_done_24,0);
                            }
                        });
                        dialog.dismiss();
                    }
                });

                cancle_hobiDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });


    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                profileFOLDER.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        profileFOLDER.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String prfImgURL=task.getResult().toString();
                                databaseReference
                                        .child("USERS")
                                        .child(emailId.toUpperCase())
                                        .child("PROFILE")
                                        .child("Profile image url")
                                        .setValue(prfImgURL).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Glide.with(Getdetails.this)
                                                        .load(prfImgURL)
                                                        .centerCrop()
                                                        .placeholder(R.drawable.ic_account_circle)
                                                        .into(mProf_image);
                                            }
                                        });
                            }
                        });
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }
}

package com.vinikuria.the20first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

public class Getdetails extends AppCompatActivity {
    ShapeableImageView mProf_image;
    ImageView mAdd_image;
    EditText mUsername_input;
    TextView mHobbies_input;
    Button mBtn_next;
    static Button hobbies_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdetails);
        mProf_image=findViewById(R.id.prof_image);
        mAdd_image=findViewById(R.id.add_image);
        mUsername_input=findViewById(R.id.username_input);
        mHobbies_input=findViewById(R.id.hobbies_input);
        mBtn_next=findViewById(R.id.btn_next);


        mBtn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**to next activity and save the inputs**/
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
                    @Override
                    public void onClick(View v) {
                        /**save and dismiss*/
                    }
                });

                cancle_hobiDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }
}
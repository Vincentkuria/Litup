package com.vinikuria.the20first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {
    EditText mSignupemail,mSignuppass,mSignuppass_confirm;
    TextView mSignup_email_error_mess,mSignup_error_message;
    Button mSignupnextbtn;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mSignupemail=findViewById(R.id.signupemail);
        mSignuppass=findViewById(R.id.signuppass);
        mSignuppass_confirm=findViewById(R.id.signuppass_confirm);
        mSignup_email_error_mess=findViewById(R.id.signup_email_error_mess);
        mSignup_error_message=findViewById(R.id.signup_error_message);
        mSignupnextbtn=findViewById(R.id.signupnextbtn);
        mAuth= FirebaseAuth.getInstance();

        mSignupnextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=mSignupemail.getText().toString().trim().toLowerCase();
                String pass=mSignuppass.getText().toString().trim();
                String confirmPass=mSignuppass_confirm.getText().toString().trim();

                if ((TextUtils.isEmpty(email) && TextUtils.isEmpty(pass) && TextUtils.isEmpty(confirmPass)) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(confirmPass)){
                    String error="All sections required";
                    mSignup_error_message.setText(error);
                    mSignup_error_message.setVisibility(View.VISIBLE);
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    String error = "Invalid email address";
                    mSignup_email_error_mess.setText(error);
                    mSignup_email_error_mess.setVisibility(View.VISIBLE);
                }else if (!(pass.equals(confirmPass))){
                    String error="please confirm password correctly";
                    mSignup_error_message.setText(error);
                    mSignup_error_message.setVisibility(View.VISIBLE);
                }else if (!(pass.toCharArray().length >=6)){
                    String error="password should be at least 6 characters";
                    mSignup_error_message.setText(error);
                    mSignup_error_message.setVisibility(View.VISIBLE);
                }else {

                    mAuth.createUserWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){

                                        Intent GotoGetDetails=new Intent(Signup.this,Getdetails.class);
                                        GotoGetDetails.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(GotoGetDetails);

                                    }else {

                                        String exe=task.getException().toString();
                                        Toast.makeText(Signup.this,exe,Toast.LENGTH_LONG).show();
                                        /*****specify the error***/

                                    }

                                }
                            });

                }

            }

        });

        mSignupemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mSignup_email_error_mess.setVisibility(View.INVISIBLE);
                mSignup_error_message.setVisibility(View.INVISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSignuppass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mSignup_email_error_mess.setVisibility(View.INVISIBLE);
                mSignup_error_message.setVisibility(View.INVISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mSignuppass_confirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mSignup_email_error_mess.setVisibility(View.INVISIBLE);
                mSignup_error_message.setVisibility(View.INVISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
}
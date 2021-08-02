package com.vinikuria.the20first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText mLogemail,mLogpass;
    Button mLoginbtn,mSignupbtn;
    TextView mForgotpassword,mLogin_error_message,mEmail_error_mess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        mLogemail=findViewById(R.id.logemail);
        mLogpass=findViewById(R.id.logpass);
        mLoginbtn=findViewById(R.id.loginbtn);
        mSignupbtn=findViewById(R.id.signupbtn);
        mForgotpassword=findViewById(R.id.forgotpassword);
        mLogin_error_message=findViewById(R.id.login_error_message);
        mEmail_error_mess=findViewById(R.id.email_error_mess);



        mLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail=mLogemail.getText().toString().trim().toLowerCase();
                String txtPass=mLogpass.getText().toString().trim();

                if ((TextUtils.isEmpty(txtEmail) && TextUtils.isEmpty(txtPass)) || (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPass)) ) {

                    String error1="all sections required";
                    mLogin_error_message.setText(error1);
                    mLogin_error_message.setVisibility(View.VISIBLE);

                }else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()){

                    mEmail_error_mess.setVisibility(View.VISIBLE);

                }else{

                        mAuth.signInWithEmailAndPassword(txtEmail, txtPass)
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(Login.this,"WELCOM",Toast.LENGTH_SHORT);
                                            Intent goToMainActivity=new Intent(getApplicationContext(),MainActivity.class);
                                            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(goToMainActivity);

                                        }else{
                                            String error="filed to login";
                                            mLogin_error_message.setText(error);
                                            mLogin_error_message.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });

                    }


            }
        });


        mLogemail.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mEmail_error_mess.setVisibility(View.INVISIBLE);
                mLogin_error_message.setVisibility(View.INVISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mLogpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                mEmail_error_mess.setVisibility(View.INVISIBLE);
                mLogin_error_message.setVisibility(View.INVISIBLE);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mSignupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent GoToSignup=new Intent(Login.this,Signup.class);
                startActivity(GoToSignup);

            }
        });


        mForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

}
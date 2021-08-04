package com.vinikuria.the20first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgottenPassReset extends AppCompatActivity {
    Button mSendresetemail;
    EditText mRecovery_email;
    TextView mRecovemail_er_mess;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_pass_reset);
        mSendresetemail=findViewById(R.id.sendresetemail);
        mRecovery_email=findViewById(R.id.recovery_email);
        mRecovemail_er_mess=findViewById(R.id.recovemail_er_mess);
        mAuth=FirebaseAuth.getInstance();

        mSendresetemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email=mRecovery_email.getText().toString().trim().toLowerCase();
                if (TextUtils.isEmpty(email)){
                    String error="please enter email";
                    mRecovemail_er_mess.setText(error);
                    mRecovemail_er_mess.setVisibility(View.VISIBLE);
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    String error="Invalid email";
                    mRecovemail_er_mess.setText(error);
                    mRecovemail_er_mess.setVisibility(View.VISIBLE);
                }else{
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        String message="An email have been sent ";
                                        Toast.makeText(ForgottenPassReset.this,message,Toast.LENGTH_LONG);

                                        Intent GotoLogin=new Intent(ForgottenPassReset.this,Login.class);
                                        GotoLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(GotoLogin);
                                    }else {

                                        /**specify error**/

                                    }

                                }
                            });
                }

            }
        });
    }
}
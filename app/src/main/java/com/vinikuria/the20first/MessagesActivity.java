package com.vinikuria.the20first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class MessagesActivity extends AppCompatActivity {

    Toolbar messagesToolbar;
    RecyclerView messagesRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        messagesToolbar=findViewById(R.id.messagesToolbar);
        messagesRecycler=findViewById(R.id.messagesRecycler);

        messagesToolbar.setTitle("Messages");
        setSupportActionBar(messagesToolbar);

    }
}
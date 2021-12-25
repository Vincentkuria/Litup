package com.vinikuria.the20first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class NotificationsActivity extends AppCompatActivity {

    Toolbar notificationsToolbar;
    RecyclerView notificationsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationsToolbar=findViewById(R.id.notificationsToolbar);
        notificationsRecycler=findViewById(R.id.notificationsRecycler);

        notificationsToolbar.setTitle("Notifications");
        setSupportActionBar(notificationsToolbar);

    }
}
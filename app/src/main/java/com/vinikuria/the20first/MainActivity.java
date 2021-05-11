package com.vinikuria.the20first;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    Boolean in_mainpage=false;
    Boolean search_open=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager2=findViewById(R.id.viewpager);
        viewPager2.setAdapter(new Viewpager_adapter(this));
    }

    @Override
    public void onBackPressed() {
        if (in_mainpage && search_open){
            viewPager2.setAdapter(new Viewpager_adapter(this));
        }else if (!in_mainpage){
            viewPager2.setAdapter(new Viewpager_adapter(this));
        }else{
            super.onBackPressed();
        }

    }
}
package com.vinikuria.the20first;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class Viewpager_adapter extends FragmentStateAdapter {
    private ViewPager2 viewPager2;
    public Viewpager_adapter(@NonNull FragmentActivity fragmentActivity,ViewPager2 viewPager2) {
        super(fragmentActivity);
        this.viewPager2=viewPager2;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new Groupdate();
        }else{
            return new Mainpage(viewPager2);
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

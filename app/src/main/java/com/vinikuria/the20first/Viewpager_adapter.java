package com.vinikuria.the20first;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class Viewpager_adapter extends FragmentStateAdapter {
    public Viewpager_adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return new Groupdate();
        }else{
            return new Mainpage();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

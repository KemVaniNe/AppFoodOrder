package com.example.foodorderapp.NewAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.foodorderapp.View.Admin.AdminAccountManagerFragment;
import com.example.foodorderapp.View.Admin.AdminCartManagerFragment;
import com.example.foodorderapp.View.Admin.AdminFoodManagerFragment;
import com.example.foodorderapp.View.Admin.AdminUserManagerFragment;

public class AdminViewPagerAdapter extends FragmentPagerAdapter {
    public AdminViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new AdminFoodManagerFragment();
            case 1:
                return new AdminUserManagerFragment();
            case 2:
                return new AdminCartManagerFragment();
            case 3:
                return new AdminAccountManagerFragment();
            default:
                return new AdminFoodManagerFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}


package com.example.foodorderapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.foodorderapp.View.User.UserAccountFragment;
import com.example.foodorderapp.View.User.UserCartFragment;
import com.example.foodorderapp.View.User.UserMainFragment;

public class UserViewPagerAdapter extends FragmentPagerAdapter {
    public UserViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new UserMainFragment();
            case 1:
                return new UserCartFragment();
            case 2:
                return new UserAccountFragment();
            default:
                return new UserMainFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}

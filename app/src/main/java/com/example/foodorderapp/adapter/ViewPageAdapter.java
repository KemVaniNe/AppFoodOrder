package com.example.foodorderapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.foodorderapp.activities.user.AccountUserFragment;
import com.example.foodorderapp.activities.user.CartUserFragment;
import com.example.foodorderapp.activities.user.UserMainFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ViewPageAdapter extends FragmentPagerAdapter{

    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new UserMainFragment();
            case 1:
                return new CartUserFragment();
            case 2:
                return new AccountUserFragment();
            default:
                return new UserMainFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}

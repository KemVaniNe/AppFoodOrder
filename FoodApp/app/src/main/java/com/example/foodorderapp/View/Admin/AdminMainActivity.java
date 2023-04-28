package com.example.foodorderapp.View.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.foodorderapp.Adapter.AdminViewPagerAdapter;
import com.example.foodorderapp.Adapter.UserViewPagerAdapter;
import com.example.foodorderapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AdminMainActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        mViewPager = findViewById(R.id.vp_main);
        mBottomNavigationView = findViewById(R.id.bottom_navigation);

        AdminViewPagerAdapter adapter = new AdminViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_admin_food).setChecked(true);
                        break;
                    case 1:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_admin_user).setChecked(true);
                        break;
                    case 2:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_admin_cart).setChecked(true);
                        break;
                    case 3:
                        mBottomNavigationView.getMenu().findItem(R.id.menu_admin_account).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_admin_food:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_admin_user:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_admin_cart:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.menu_admin_account:
                        mViewPager.setCurrentItem(3);
                        break;
                }
                return true;
            }
        });
    }
}
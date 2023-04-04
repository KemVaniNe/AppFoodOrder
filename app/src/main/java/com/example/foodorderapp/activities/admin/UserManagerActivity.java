package com.example.foodorderapp.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.foodorderapp.databinding.ActivityAdminBinding;
import com.example.foodorderapp.databinding.ActivityOrderManagerBinding;
import com.example.foodorderapp.databinding.ActivityUserManagerBinding;
import com.example.foodorderapp.utilities.PreferenceManeger;

public class UserManagerActivity extends AppCompatActivity {
    private ActivityUserManagerBinding binding;
    private PreferenceManeger preferenceManeger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserManagerBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        preferenceManeger = new PreferenceManeger(getApplicationContext());
    }
}
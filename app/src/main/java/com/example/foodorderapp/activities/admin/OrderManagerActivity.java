package com.example.foodorderapp.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.foodorderapp.databinding.ActivityAdminBinding;
import com.example.foodorderapp.databinding.ActivityFoodManagerBinding;
import com.example.foodorderapp.databinding.ActivityOrderManagerBinding;
import com.example.foodorderapp.utilities.PreferenceManeger;

public class OrderManagerActivity extends AppCompatActivity {
    private ActivityOrderManagerBinding binding;
    private PreferenceManeger preferenceManeger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderManagerBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        preferenceManeger = new PreferenceManeger(getApplicationContext());

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderManagerActivity.this, AdminActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }
}
package com.example.foodorderapp.activities.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.foodorderapp.activities.shared.AccountManagerActivity;
import com.example.foodorderapp.activities.shared.LoginActivity;
import com.example.foodorderapp.databinding.ActivityAdminBinding;
import com.example.foodorderapp.utilities.PreferenceManeger;

public class AdminActivity extends AppCompatActivity {
    private ActivityAdminBinding binding;
    private PreferenceManeger preferenceManeger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        preferenceManeger = new PreferenceManeger(getApplicationContext());

        binding.btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        binding.btnQuanLyMonAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, FoodManagerActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        binding.btnQuanLyHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, OrderManagerActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        binding.btnQuanLyTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AccountManagerActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        binding.btnQuanLyNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, UserManagerActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }
}
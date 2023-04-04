package com.example.foodorderapp.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ActivityUserBinding;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;

public class UserActivity extends AppCompatActivity {
    private ActivityUserBinding binding;

    private PreferenceManeger preferenceManeger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        preferenceManeger = new PreferenceManeger(getApplicationContext());
        loadUserDetails();

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, MainActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        binding.btnHoTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, SupportActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        binding.btnDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, AddressActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        binding.btnQuanLyTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, AccountActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        binding.btnDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, OrderActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void loadUserDetails(){
        binding.txtuser.setText(preferenceManeger.getSrting(Contants.KEY_USERNAME));

    }

}
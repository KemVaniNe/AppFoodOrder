package com.example.foodorderapp.View.User;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodorderapp.R;

public class NavUserAccountFragment extends Fragment {

    private ConstraintLayout btnDonHang, btnQuanLyTaiKhoan, btnSupport;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nav_user_account, container, false);

        btnDonHang = view.findViewById(R.id.btn_donHang);
        btnQuanLyTaiKhoan = view.findViewById(R.id.btn_quanLyTaiKhoan);
        btnSupport = view.findViewById(R.id.btn_hoTro);

        btnDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navUserAccountFragment_to_navUserCartFragment);
            }
        });

        btnQuanLyTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navUserAccountFragment_to_navUserAccountManagerFragment);
            }
        });

        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navUserAccountFragment_to_navUserSupportFragment);
            }
        });

        return view;
    }
}
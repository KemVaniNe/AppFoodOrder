package com.example.foodorderapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ActivityAccountBinding;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AccountActivity extends AppCompatActivity {
    private ActivityAccountBinding binding;

    private PreferenceManeger preferenceManeger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        preferenceManeger = new PreferenceManeger(getApplicationContext());
        loadUser();
    }

    private void updateUser(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        String user_id = preferenceManeger.getSrting(Contants.KEY_USER_ID);
        user.put(Contants.KEY_USERNAME, binding.etUsername3.getText().toString());
        user.put(Contants.KEY_PHONE, binding.etUsername4.getText().toString());
        user.put(Contants.KEY_PASSWORD, binding.etPassword5.getText().toString());
        database.collection(Contants.KEY_COLEECTION_USERS)
                .document(user_id)
                .update(user)
                .addOnSuccessListener(aVoid -> {
                    showToast("Cập nhật thành công");
                })
                .addOnFailureListener(e -> {
                    showToast("Cập nhật thất bại");
                });

    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }
    private Boolean isValidRegisterDetail(){
        if(binding.etUsername3.getText().toString().trim().isEmpty()){
            showToast("Nhập username");
            return false;
        }else if (binding.etUsername4.getText().toString().trim().isEmpty()){
            showToast("Nhập number phone");
            return false;
        }
        else if (binding.etPassword4.getText().toString().equals(preferenceManeger.getSrting(Contants.KEY_PASSWORD))
        ){
            showToast("Nhập mật khẩu cũ sai");
            return false;
        }
        else if (binding.etPassword5.getText().toString().trim().isEmpty()){
            showToast("Nhập mật khẩu mới");
            return false;
        }else if(!binding.etPassword6.getText().toString().equals(binding.etPassword5.getText().toString())){
            showToast("Xác nhận mật khẩu mới sai");
            return false;
        }else{
            return true;
        }
    }

    private void loadUser(){
        binding.etUsername3.setText(preferenceManeger.getSrting(Contants.KEY_USERNAME));
        binding.etUsername4.setText(preferenceManeger.getSrting(Contants.KEY_PHONE));
        binding.textView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidRegisterDetail()){
                    updateUser();
                }
            }
        });
    }
}
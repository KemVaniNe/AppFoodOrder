package com.example.foodorderapp.activities.shared;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.foodorderapp.activities.user.MainActivity;
import com.example.foodorderapp.databinding.ActivityRegisterBinding;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    private PreferenceManeger preferenceManeger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManeger = new PreferenceManeger(getApplicationContext());
        SetListener();
    }
    private void SetListener(){
        binding.tvSignin.setOnClickListener(v->onBackPressed());
        binding.tvRegiter.setOnClickListener(v->{
            if(isValidRegisterDetail()){
                Register();
            }

        });
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }
    private void Register(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String , Object> user = new HashMap<>();
        user.put(Contants.KEY_USERNAME, binding.etUsername.getText().toString());
        user.put(Contants.KEY_PHONE, binding.etUsername2.getText().toString());
        user.put(Contants.KEY_PASSWORD, binding.etPassword.getText().toString());
        database.collection((Contants.KEY_COLEECTION_USERS))
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManeger.putBoolean(Contants.KEY_IS_LOGINED_IN, true);
                    preferenceManeger.putString(Contants.KEY_USER_ID,documentReference.getId() );
                    preferenceManeger.putString(Contants.KEY_USERNAME , binding.etUsername.getText().toString());
                    preferenceManeger.putString(Contants.KEY_PHONE , binding.etUsername2.getText().toString());
                    preferenceManeger.putString(Contants.KEY_PASSWORD, binding.etPassword.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception->{
                    showToast("Đăng ký thất bại");
                });
    }
//
    private Boolean isValidRegisterDetail(){
        if(binding.etUsername.getText().toString().trim().isEmpty()){
            showToast("Nhập username");
            return false;
        }else if (binding.etUsername2.getText().toString().trim().isEmpty()){
            showToast("Nhập number phone");
            return false;
        }
        else if (binding.etPassword.getText().toString().trim().isEmpty()){
            showToast("Nhập mật khẩu");
            return false;
        }else if(!binding.etPassword2.getText().toString().equals(binding.etPassword.getText().toString())){
            showToast("Xác nhận mật khẩu sai");
            return false;
        }else{
            return true;
        }
    }
}
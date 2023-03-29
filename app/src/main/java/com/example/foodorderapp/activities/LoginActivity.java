package com.example.foodorderapp.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.example.foodorderapp.databinding.ActivityLoginBinding;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private PreferenceManeger preferenceManeger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        preferenceManeger = new PreferenceManeger(getApplicationContext());
        if(preferenceManeger.getBoolean(Contants.KEY_IS_LOGINED_IN)){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(binding.getRoot());
        SetListener();
    }
    private void SetListener(){
        binding.btnLogin.setOnClickListener(v->{
            if(isValidLoginDetails()){
                Login();
            }
        });

        binding.btnRegister.setOnClickListener(v->
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class))
            );
    }
    private void Login(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Contants.KEY_COLEECTION_USERS)
                .whereEqualTo(Contants.KEY_USERNAME, binding.etUsername.getText().toString())
                .whereEqualTo(Contants.KEY_PASSWORD, binding.etPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocumentChanges().size()> 0){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManeger.putBoolean(Contants.KEY_IS_LOGINED_IN, true);
                        preferenceManeger.putString(Contants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManeger.putString(Contants.KEY_USERNAME, documentSnapshot.getString(Contants.KEY_USERNAME));
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        showToast("Sai username , mật khẩu!");
                    }
                });
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

    }
    private Boolean isValidLoginDetails(){
        if(binding.etUsername.getText().toString().isEmpty()){
            showToast("Chưa nhập username");
            return false;
        }else if(binding.etPassword.getText().toString().isEmpty()){
            showToast("Chưa nhập password");
            return false;
        }else{
            return true;
        }
    }
}
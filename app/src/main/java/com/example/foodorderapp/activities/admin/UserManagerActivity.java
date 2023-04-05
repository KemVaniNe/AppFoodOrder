package com.example.foodorderapp.activities.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.foodorderapp.adapter.FoodAdminAdapter;
import com.example.foodorderapp.adapter.UserAdminAdapter;
import com.example.foodorderapp.databinding.ActivityAdminBinding;
import com.example.foodorderapp.databinding.ActivityOrderManagerBinding;
import com.example.foodorderapp.databinding.ActivityUserManagerBinding;
import com.example.foodorderapp.model.FoodModel;
import com.example.foodorderapp.model.UserModel;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserManagerActivity extends AppCompatActivity {
    private ActivityUserManagerBinding binding;
    private PreferenceManeger preferenceManeger;

    private UserAdminAdapter userAdminAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserManagerBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        preferenceManeger = new PreferenceManeger(getApplicationContext());

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserManagerActivity.this, AdminActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        loadUser();
    }

    void loadUser(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Contants.KEY_COLEECTION_USERS)
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        List<UserModel> userModels = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            UserModel userModel = new UserModel();
                            userModel.setName(queryDocumentSnapshot.getString(Contants.KEY_USERNAME));
                            userModel.setPass(queryDocumentSnapshot.getString(Contants.KEY_PASSWORD));
                            userModels.add(userModel);
                        }
                        if(userModels.size() >0){
                            userAdminAdapter = new UserAdminAdapter(userModels);
                            binding.recyclerview.setAdapter(userAdminAdapter);
                            binding.recyclerview.setVisibility(View.VISIBLE);
                        }else{
                            Toast.makeText(getApplicationContext(), "recyclerviewfood1", Toast.LENGTH_LONG);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "recyclerviewfood2", Toast.LENGTH_LONG);
                    }
                });
    }
}
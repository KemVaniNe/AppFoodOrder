package com.example.foodorderapp.activities.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodorderapp.adapter.CategoryAdapter;
import com.example.foodorderapp.adapter.FoodAdapter;
import com.example.foodorderapp.adapter.FoodAdminAdapter;
import com.example.foodorderapp.databinding.ActivityAdminBinding;
import com.example.foodorderapp.databinding.ActivityFoodManagerBinding;
import com.example.foodorderapp.model.FoodModel;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FoodManagerActivity extends AppCompatActivity {
    private ActivityFoodManagerBinding binding;
    private PreferenceManeger preferenceManeger;

    private CategoryAdapter adapter;
    private FoodAdminAdapter foodAdminAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodManagerBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        preferenceManeger = new PreferenceManeger(getApplicationContext());

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodManagerActivity.this, AdminActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        binding.btnNewCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });

        binding.btnNewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFood();
            }
        });
        loadfood();
    }

    void loadfood(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerviewfood.setLayoutManager(linearLayoutManager);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Contants.KEY_COLEECTION_FOODS)
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        List<FoodModel> foodModels = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            FoodModel foodModel = new FoodModel();
                            foodModel.setName(queryDocumentSnapshot.getString(Contants.KEY_NAME_FOOD));
                            foodModel.setPrice(queryDocumentSnapshot.getString(Contants.KEY_PRICE_FOOD));
                            foodModel.setImage(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_FOOD));
                            foodModel.setDetail(queryDocumentSnapshot.getString(Contants.KEY_DETAIL_FOOD));
                            foodModels.add(foodModel);
                        }
                        if(foodModels.size() >0){
                            foodAdminAdapter = new FoodAdminAdapter(foodModels);
                            binding.recyclerviewfood.setAdapter(foodAdminAdapter);
                            binding.recyclerviewfood.setVisibility(View.VISIBLE);
                        }else{
                            Toast.makeText(getApplicationContext(), "recyclerviewfood1", Toast.LENGTH_LONG);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "recyclerviewfood2", Toast.LENGTH_LONG);
                    }
                });
    }

    public void addFood(){
   /*     AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        binding = ActivityFoodManagerBinding.inflate(getLayoutInflater());
        View mView = inflater.inflate(R.layout.add_note, null);
        mDialog.setView(mView);

        AlertDialog dialog = mDialog.create();
        dialog.setCancelable(true);

        Button save = mView.findViewById(R.id.btn_save);
        EditText edtTitle = mView.findViewById(R.id.edt_title);
        EditText edtContent = mView.findViewById(R.id.edt_content);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = myRef.push().getKey();
                String title = edtTitle.getText().toString();
                String content = edtContent.getText().toString();
                myRef.child(id).setValue(new Post(id,title,content, getRandomColor()))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Add note successful!",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Add note fail!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                dialog.dismiss();
            }
        });

        dialog.show();*/
    }

    public void addCategory(){

    }
}
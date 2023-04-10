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

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.CatetoryAdapter;
import com.example.foodorderapp.adapter.FoodAdapter;
import com.example.foodorderapp.adapter.FoodAdminAdapter;
import com.example.foodorderapp.databinding.ActivityAdminBinding;
import com.example.foodorderapp.databinding.ActivityFoodManagerBinding;
import com.example.foodorderapp.model.CategoryModel;
import com.example.foodorderapp.model.FoodModel;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FoodManagerActivity extends AppCompatActivity {
    private ActivityFoodManagerBinding binding;
    private PreferenceManeger preferenceManeger;

    private CatetoryAdapter adapter;
    private FoodAdminAdapter foodAdminAdapter;
    private FirebaseAuth mAuth;
 //   private DatabaseReference myRef;

    private CatetoryAdapter catetoryAdapter;
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
                            foodModel.setId_food(queryDocumentSnapshot.getId());
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

    private void recyclerViewCategory(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Contants.KEY_COLEECTION_CATEGORY)
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        List<CategoryModel> categoryModels = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            CategoryModel categoryModel = new CategoryModel();
                            categoryModel.setId_category(Integer.parseInt(queryDocumentSnapshot.getString(Contants.KEY_ID_CATEGORY)));
                            categoryModel.setName_category(queryDocumentSnapshot.getString(Contants.KEY_NAME_CATEGORY));
                            categoryModel.setImage_category(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_CATEGORY));
                            categoryModels.add(categoryModel);
                        }
                        if(categoryModels.size() >0){
                       //     catetoryAdapter = new CatetoryAdapter(categoryModels,this);
                            binding.recyclerview.setAdapter(catetoryAdapter);
                        }else{
                            Toast.makeText(getApplicationContext(), "recyclerviewfood1", Toast.LENGTH_LONG);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "recyclerviewfood2", Toast.LENGTH_LONG);
                    }
                });
    }

    public void addFood(){
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        binding = ActivityFoodManagerBinding.inflate(getLayoutInflater());
        View mView = inflater.inflate(R.layout.add_food, null);
        mDialog.setView(mView);

        AlertDialog dialog = mDialog.create();
        dialog.setCancelable(true);

        Button save = mView.findViewById(R.id.btn_save);
        EditText edtName = mView.findViewById(R.id.edt_name);
        EditText edtPrice = mView.findViewById(R.id.edt_price);
        EditText edtDetail = mView.findViewById(R.id.edt_detail);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   String id = myRef.push().getKey();
                String name = edtDetail.getText().toString();
                String price = edtPrice.getText().toString();
                String detail = edtDetail.getText().toString();
            /*    myRef.child(id).setValue(new Post(id,title,content, getRandomColor()))
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
                        });*/

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void addCategory(){
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        binding = ActivityFoodManagerBinding.inflate(getLayoutInflater());
        View mView = inflater.inflate(R.layout.add_category, null);
        mDialog.setView(mView);

        AlertDialog dialog = mDialog.create();
        dialog.setCancelable(true);

        Button save = mView.findViewById(R.id.btn_save);
        EditText edtName = mView.findViewById(R.id.edt_name);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    String id = myRef.push().getKey();
                String name = edtName.getText().toString();
            /*    myRef.child(id).setValue(new Post(id,title,content, getRandomColor()))
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
                        });*/

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
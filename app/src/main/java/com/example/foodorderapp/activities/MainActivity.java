package com.example.foodorderapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.CategoryAdapter;
import com.example.foodorderapp.adapter.FoodAdapter;
import com.example.foodorderapp.databinding.ActivityMainBinding;
import com.example.foodorderapp.domain.CategoryDomain;
import com.example.foodorderapp.model.FoodModel;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    String categoryId="";

    private CategoryAdapter adapter;
    private FoodAdapter foodAdapter;
    private PreferenceManeger preferenceManeger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManeger = new PreferenceManeger(getApplicationContext());
        loadUserDetails();
        getToken();
        setListener();
        loadfood();
        recyclerViewCategory();

    }

    void loadfood(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerviewfood.setLayoutManager(linearLayoutManager);
        System.out.println("test load food");
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
                            foodAdapter = new FoodAdapter(foodModels);
                            binding.recyclerviewfood.setAdapter(foodAdapter);
                            binding.recyclerviewfood.setVisibility(View.VISIBLE);
                        }else{
                            showToast("Error recyclerviewfood1");
                        }
                    }else{
                        showToast("Error recyclerviewfood2");
                    }
                });
  }
    private void recyclerViewCategory(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerview.setLayoutManager(linearLayoutManager);

        ArrayList<CategoryDomain> category = new ArrayList<>();
        category.add(new CategoryDomain("Cơm", "com"));
        category.add(new CategoryDomain("Mì", "mi"));
        category.add(new CategoryDomain("Nước uống", "nuoc_uong"));
        category.add(new CategoryDomain("Thức ăn", "thuc_an"));
        category.add(new CategoryDomain("Tokbokki", "tokbokki"));

        adapter = new CategoryAdapter(category);
        binding.recyclerview.setAdapter(adapter);
    }
    private void setListener(){
        binding.imageLogOut.setOnClickListener(v->Logout());
        binding.btnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }
    private void loadUserDetails(){
        binding.tvUsername.setText(preferenceManeger.getSrting(Contants.KEY_USERNAME));

    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void updateToken(String token){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Contants.KEY_COLEECTION_USERS).document(
                        preferenceManeger.getSrting(Contants.KEY_USER_ID)
                );
        documentReference.update(Contants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(unused->showToast("Cập nhật Token thành công!"))
                .addOnFailureListener(e->showToast("Cập nhật Token thất bại!"));
    }

    private void Logout(){
        showToast("Logout..");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection(Contants.KEY_COLEECTION_USERS)
                .document(
                        preferenceManeger.getSrting(Contants.KEY_USER_ID)
                );
        HashMap<String , Object> updates = new HashMap<>();
        updates.put(Contants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused->{
                    preferenceManeger.clear();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                })
                .addOnFailureListener(e->showToast("Đăng xuất thất bại!"));
    }

}
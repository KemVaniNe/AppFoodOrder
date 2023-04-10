package com.example.foodorderapp.activities.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.foodorderapp.activities.shared.LoginActivity;
import com.example.foodorderapp.adapter.CatetoryAdapter;

import com.example.foodorderapp.adapter.FoodAdapter;
import com.example.foodorderapp.databinding.ActivityMainBinding;
import com.example.foodorderapp.listener.CategoryListener;
import com.example.foodorderapp.listener.FoodListener;
import com.example.foodorderapp.model.CategoryModel;
import com.example.foodorderapp.model.FoodModel;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryListener, FoodListener {
    private ActivityMainBinding binding;
    String categoryId="";
    private CatetoryAdapter catetoryAdapter;
    private FoodAdapter foodAdapter;
    private PreferenceManeger preferenceManeger;
    FirebaseFirestore database ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManeger = new PreferenceManeger(getApplicationContext());
        database = FirebaseFirestore.getInstance();
        loadUserDetails();
        getToken();
        setListener();
        loadfood();
        LoadidOrder();
        recyclerViewCategory();
    }

    void loadfood(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerviewfood.setLayoutManager(linearLayoutManager);

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
                            foodModels.add(foodModel);}
                        if(foodModels.size() >0){
                            foodAdapter = new FoodAdapter(foodModels,this);
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
                            catetoryAdapter = new CatetoryAdapter(categoryModels,this);
                            binding.recyclerview.setAdapter(catetoryAdapter);
                        }else{
                            showToast("Error recyclerview1");
                        }
                    }else{
                        showToast("Error recyclerview2");
                    }
                });
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
        binding.btnCart.setOnClickListener(v->{
            Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
            startActivity(intent);
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


    @Override
    public void CategoryCLick(CategoryModel categoryModel) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerviewfood.setLayoutManager(linearLayoutManager);
        database.collection(Contants.KEY_COLEECTION_FOODS)
                .whereEqualTo(Contants.KEY_ID_CATEGORY, String.valueOf(categoryModel.getId_category()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<FoodModel> foodModels = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            FoodModel foodModel = new FoodModel();
                            foodModel.setId_food(queryDocumentSnapshot.getId());
                            foodModel.setName(queryDocumentSnapshot.getString(Contants.KEY_NAME_FOOD));
                            foodModel.setPrice(queryDocumentSnapshot.getString(Contants.KEY_PRICE_FOOD));
                            foodModel.setImage(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_FOOD));
                            foodModel.setDetail(queryDocumentSnapshot.getString(Contants.KEY_DETAIL_FOOD));
                            foodModels.add(foodModel);
                        }
                        if (foodModels.size() > 0) {

                            foodAdapter = new FoodAdapter(foodModels,this);
                            binding.recyclerviewfood.setAdapter(foodAdapter);
                            binding.recyclerviewfood.setVisibility(View.VISIBLE);
                        } else {
                            showToast("Error recyclerviewfood1");
                        }
                    } else {
                        showToast("Error recyclerviewfood2");
                    }
                });

    }
    public void LoadidOrder(){
        database.collection(Contants.KEY_COLEECTION_ORDER)
                .whereEqualTo(Contants.KEY_USER_ID, preferenceManeger.getSrting(Contants.KEY_USER_ID))
                .get()
                .addOnCompleteListener(Task -> {
                    if(Task.isSuccessful() && Task.getResult() != null){
                        int i = 0 ;
                        boolean add = true;
                        for(QueryDocumentSnapshot queryDocumentSnapshot: Task.getResult()){

                            if(!queryDocumentSnapshot.getBoolean(Contants.KEY_STATUS_ORDER)){
                                preferenceManeger.putString(Contants.KEY_ID_ORDER, queryDocumentSnapshot.getId());
                                add = false;
                                break;
                            }
                            i++;
                        }
                        if(add){
                            addNewOrder();

                        }
                    }
                });
    }
    public void addNewOrder(){
        HashMap<String , Object> neworder = new HashMap<>();
        neworder.put(Contants.KEY_STATUS_ORDER, false);
        neworder.put(Contants.KEY_USER_ID, preferenceManeger.getSrting(Contants.KEY_USER_ID));
        neworder.put("CreateAt" , "");
        neworder.put("Total" , "");
        database.collection(Contants.KEY_COLEECTION_ORDER)
                .add(neworder)
                .addOnSuccessListener(documentReference -> {
                    preferenceManeger.putString(Contants.KEY_ID_ORDER, documentReference.getId());
                    showToast("Đi đến gọi món nào");
                });
    }
    @Override
    public void FoodItemCLick(FoodModel foodModel) {
        if(preferenceManeger.getSrting(Contants.KEY_ID_ORDER) == null){
            addNewOrder();
        }
        HashMap<String , Object> order_detail = new HashMap<>();
        order_detail.put(Contants.KEY_ID_ORDER, preferenceManeger.getSrting(Contants.KEY_ID_ORDER));
        order_detail.put(Contants.KEY_ID_FOOD,foodModel.getId_food());
        database.collection(Contants.KEY_COLEECTION_ORDER_DETAIL)
                .whereEqualTo(Contants.KEY_ID_ORDER , preferenceManeger.getSrting(Contants.KEY_ID_ORDER))
                .get()
                .addOnCompleteListener(Task -> {
                    if(Task.isSuccessful() && Task.getResult() != null){
                        boolean add = true;
                        String id_detailorder = "" ;
                        int soluong = 0 ;
                        for(QueryDocumentSnapshot queryDocumentSnapshot: Task.getResult()){

                            if(queryDocumentSnapshot.getString(Contants.KEY_ID_FOOD).equals(foodModel.getId_food())){
                                add = false;
                                id_detailorder = queryDocumentSnapshot.getId();
                                soluong = Integer.parseInt(queryDocumentSnapshot.getString("Number"));
                                break;
                            }
                        }
                        if(add){
                            HashMap<String , Object> newfood = new HashMap<>();
                            newfood.put("Number", "1");
                            newfood.put(Contants.KEY_ID_FOOD,foodModel.getId_food());
                            newfood.put(Contants.KEY_ID_ORDER , preferenceManeger.getSrting(Contants.KEY_ID_ORDER));
                            database.collection(Contants.KEY_COLEECTION_ORDER_DETAIL)
                                    .add(newfood)
                                    .addOnSuccessListener(documentReference -> {
                                        showToast("đã thêm thành công");
                                    });
                        }else{
                            DocumentReference documentReference =
                                    database.collection(Contants.KEY_COLEECTION_ORDER_DETAIL).document(
                                            id_detailorder
                                    );
                            documentReference.update("Number",String.valueOf(soluong + 1) )
                                    .addOnSuccessListener(unused->showToast("Đặt món thành công!"))
                                    .addOnFailureListener(e->showToast("Đặt món thất bại!"));
                        }
                    }
                });
    }
        @Override
    public void FoodItemDetailClick(FoodModel food) {
        Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
        intent.putExtra("FOOD" ,(Serializable) food);
        startActivity(intent);
    }
}
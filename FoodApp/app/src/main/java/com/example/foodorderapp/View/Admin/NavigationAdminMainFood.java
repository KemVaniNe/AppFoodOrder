package com.example.foodorderapp.View.Admin;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodorderapp.Adapter.FoodAdminAdapter;
import com.example.foodorderapp.R;
import com.example.foodorderapp.Adapter.CatetoryAdapter;
import com.example.foodorderapp.databinding.FragmentNavigationAdminMainFoodBinding;
import com.example.foodorderapp.listener.CategoryListener;
import com.example.foodorderapp.Model.CategoryModel;
import com.example.foodorderapp.Model.FoodModel;
import com.example.foodorderapp.listener.FoodAdminListener;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class NavigationAdminMainFood extends Fragment implements CategoryListener, FoodAdminListener {

    private CatetoryAdapter catetoryAdapter;
    private FoodAdminAdapter foodAdapter;
    FirebaseFirestore database ;
    private FragmentNavigationAdminMainFoodBinding binding ;
    private PreferenceManeger preferenceManeger;

    private int sizeCategory = 0;

    private ProgressDialog pd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavigationAdminMainFoodBinding.inflate(inflater,container,false);
        preferenceManeger = new PreferenceManeger(getActivity());
        pd = new ProgressDialog(getContext());

        View view = binding.getRoot();
        database = FirebaseFirestore.getInstance();
        loadUserDetails();
        loadfood();
        recyclerViewCategory();
        binding.btnNewCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigationAdminMainFood_to_navigationAdminNewCategory);
            }
        });

        binding.btnNewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigationAdminMainFood_to_navigationAdminNewFood);
            }
        });

        binding.btnSearchFoodAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFood();
            }
        });

        return view;
    }

    private void searchFood()
    {
        String textSearch = binding.edtSearchFoodAdmin.getText().toString().trim().toLowerCase();
        pd.setTitle("Searching food...");
        pd.show();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvFood.setLayoutManager(linearLayoutManager);
        database.collection(Contants.KEY_COLEECTION_FOODS)
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        List<FoodModel> foodModels = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            FoodModel foodModel = new FoodModel("" ,"","","","","");
                            foodModel.setId(queryDocumentSnapshot.getId());
                            foodModel.setCategory_id(queryDocumentSnapshot.getString(Contants.KEY_ID_CATEGORY));
                            foodModel.setName(queryDocumentSnapshot.getString(Contants.KEY_NAME_FOOD));
                            foodModel.setPrice(queryDocumentSnapshot.getString(Contants.KEY_PRICE_FOOD));
                            foodModel.setImage(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_FOOD));
                            foodModel.setDetail(queryDocumentSnapshot.getString(Contants.KEY_DETAIL_FOOD));
                            foodModels.add(foodModel);}
                        if(foodModels.size() >0){
                            List<FoodModel> newList = new ArrayList<>();
                            for (FoodModel food : foodModels) {
                                if (food.getName().toLowerCase().contains(textSearch)) {
                                    newList.add(food);
                                }
                            }
                            foodAdapter = new FoodAdminAdapter(newList,this);
                            binding.rvFood.setAdapter(foodAdapter);
                            pd.dismiss();
                            binding.rvFood.setVisibility(View.VISIBLE);
                        }else{
                            pd.dismiss();
                        }
                    }else{
                        pd.dismiss();
                        Toast.makeText(getContext(), "Không tìm thấy!" , Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println(e);
                });
    }

 /*   private void searchFood()
    {
        pd.setTitle("Searching food...");
        pd.show();
        String text = binding.edtSearchFoodAdmin.getText().toString().trim();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvFood.setLayoutManager(linearLayoutManager);
        database.collection(Contants.KEY_COLEECTION_FOODS)
                .whereGreaterThanOrEqualTo("name", text)
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        List<FoodModel> foodModels = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            FoodModel foodModel = new FoodModel("" ,"","","","","");
                            foodModel.setId(queryDocumentSnapshot.getId());
                            foodModel.setCategory_id(queryDocumentSnapshot.getString(Contants.KEY_ID_CATEGORY));
                            foodModel.setName(queryDocumentSnapshot.getString(Contants.KEY_NAME_FOOD));
                            foodModel.setPrice(queryDocumentSnapshot.getString(Contants.KEY_PRICE_FOOD));
                            foodModel.setImage(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_FOOD));
                            foodModel.setDetail(queryDocumentSnapshot.getString(Contants.KEY_DETAIL_FOOD));
                            foodModels.add(foodModel);}
                        if(foodModels.size() >0){
                            foodAdapter = new FoodAdminAdapter(foodModels,this);
                            binding.rvFood.setAdapter(foodAdapter);
                            binding.rvFood.setVisibility(View.VISIBLE);
                            pd.dismiss();
                        }else{
                            pd.dismiss();
                            Toast.makeText(getContext(), "Không tìm thấy!" , Toast.LENGTH_LONG).show();
                        }
                    }else{
                        pd.dismiss();
                        showToast("Error recyclerviewfood2");
                    }
                })
                .addOnFailureListener(e -> {
                    pd.dismiss();
                    System.out.println(e);
                });
    }*/
    private void loadUserDetails(){
        binding.tvUsername.setText(preferenceManeger.getSrting(Contants.KEY_USERNAME));
        binding.imgAvatar.setImageBitmap(getAvatarImage(preferenceManeger.getSrting(Contants.KEY_IMAGE_USER)));
    }
    void loadfood(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvFood.setLayoutManager(linearLayoutManager);
        database.collection(Contants.KEY_COLEECTION_FOODS)
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        List<FoodModel> foodModels = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            FoodModel foodModel = new FoodModel("" ,"","","","","");
                            foodModel.setId(queryDocumentSnapshot.getId());
                            foodModel.setCategory_id(queryDocumentSnapshot.getString(Contants.KEY_ID_CATEGORY));
                            foodModel.setName(queryDocumentSnapshot.getString(Contants.KEY_NAME_FOOD));
                            foodModel.setPrice(queryDocumentSnapshot.getString(Contants.KEY_PRICE_FOOD));
                            foodModel.setImage(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_FOOD));
                            foodModel.setDetail(queryDocumentSnapshot.getString(Contants.KEY_DETAIL_FOOD));
                            foodModels.add(foodModel);}
                        if(foodModels.size() >0){
                            foodAdapter = new FoodAdminAdapter(foodModels,this);
                            binding.rvFood.setAdapter(foodAdapter);
                            binding.rvFood.setVisibility(View.VISIBLE);
                        }else{
                            showToast("Error recyclerviewfood1");
                        }
                    }else{
                        showToast("Error recyclerviewfood2");
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println(e);
                });
    }

    private void recyclerViewCategory(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvCategories.setLayoutManager(linearLayoutManager);
        database.collection(Contants.KEY_COLEECTION_CATEGORY)
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        List<CategoryModel> categoryModels = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            CategoryModel categoryModel = new CategoryModel("","");
                            categoryModel.setId_category(queryDocumentSnapshot.getId());
                            categoryModel.setName_category(queryDocumentSnapshot.getString(Contants.KEY_NAME_CATEGORY));
                            categoryModel.setImage_category(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_CATEGORY));
                            categoryModels.add(categoryModel);
                        }
                        if(categoryModels.size() >0){
                            catetoryAdapter = new CatetoryAdapter(categoryModels,this);
                            binding.rvCategories.setAdapter(catetoryAdapter);
                        }else{
                            showToast("Error recyclerview1");
                        }
                    }else{
                        showToast("Error recyclerview2");
                    }
                });
    }
    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
    }
    @Override
    public void CategoryCLick(CategoryModel categoryModel) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvFood.setLayoutManager(linearLayoutManager);
        database.collection(Contants.KEY_COLEECTION_FOODS)
                .whereEqualTo(Contants.KEY_ID_CATEGORY, String.valueOf(categoryModel.getId_category()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<FoodModel> foodModels = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            FoodModel foodModel = new FoodModel("","","","","","");
                            foodModel.setId(queryDocumentSnapshot.getId());
                            foodModel.setCategory_id(queryDocumentSnapshot.getString(Contants.KEY_ID_CATEGORY));
                            foodModel.setName(queryDocumentSnapshot.getString(Contants.KEY_NAME_FOOD));
                            foodModel.setPrice(queryDocumentSnapshot.getString(Contants.KEY_PRICE_FOOD));
                            foodModel.setImage(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_FOOD));
                            foodModel.setDetail(queryDocumentSnapshot.getString(Contants.KEY_DETAIL_FOOD));
                            foodModels.add(foodModel);
                        }
                        if (foodModels.size() > 0) {

                            foodAdapter = new FoodAdminAdapter(foodModels,this);
                            binding.rvFood.setAdapter(foodAdapter);
                            binding.rvFood.setVisibility(View.VISIBLE);
                        } else {
                            showToast("Error recyclerviewfood1");
                        }
                    } else {
                        showToast("Error recyclerviewfood2");
                    }
                });

    }
    private Bitmap getAvatarImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }

    @Override
    public void FoodAdminClick(FoodModel food) {
        Bundle bundle = new Bundle();
        String foodId = food.getId();
        bundle.putString("FoodID",foodId);
        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigationAdminMainFood_to_navigationAdminDetailFood, bundle);
    }
}
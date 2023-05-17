package com.example.foodorderapp.View.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodorderapp.NewAdapter.FoodAdminAdapter;
import com.example.foodorderapp.NewListener.FoodAdminListener;
import com.example.foodorderapp.NewModel.Food;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.CatetoryAdapter;
import com.example.foodorderapp.databinding.ActivityFoodManagerBinding;
import com.example.foodorderapp.listener.CategoryListener;
import com.example.foodorderapp.model.CategoryModel;
import com.example.foodorderapp.model.FoodModel;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class NavigationAdminMainFood extends Fragment implements CategoryListener, FoodAdminListener {

    private CatetoryAdapter catetoryAdapter;
    private FoodAdminAdapter foodAdapter;
    FirebaseFirestore database ;

    private RecyclerView rvFood, rvCategory;

    private AppCompatButton btnNewCategory, btnNewFood;

    private View view;

    private int sizeCategory = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_navigation_admin_main_food, container, false);

        database = FirebaseFirestore.getInstance();

        rvFood = view.findViewById(R.id.rv_food);
        rvCategory = view.findViewById(R.id.rv_categories);
        btnNewCategory = view.findViewById(R.id.btn_newCategories);
        btnNewFood = view.findViewById(R.id.btn_newFood);

        btnNewCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("SizeCategory", sizeCategory);
                Navigation.findNavController(view).navigate(R.id.action_navigationAdminMainFood_to_navigationAdminNewCategory, bundle);
            }
        });

        btnNewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigationAdminMainFood_to_navigationAdminNewFood);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadfood();
        recyclerViewCategory();
    }

    void loadfood(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvFood.setLayoutManager(linearLayoutManager);
        database.collection(Contants.KEY_COLEECTION_FOODS)
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        List<Food> foodModels = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            Food foodModel = new Food();
                            foodModel.setId(queryDocumentSnapshot.getId());
                            foodModel.setCategoryid(queryDocumentSnapshot.getString(Contants.KEY_ID_CATEGORY));
                            foodModel.setName(queryDocumentSnapshot.getString(Contants.KEY_NAME_FOOD));
                            foodModel.setPrice(queryDocumentSnapshot.getString(Contants.KEY_PRICE_FOOD));
                            foodModel.setImage(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_FOOD));
                            foodModel.setDetail(queryDocumentSnapshot.getString(Contants.KEY_DETAIL_FOOD));
                            foodModels.add(foodModel);}
                        if(foodModels.size() >0){
                            foodAdapter = new FoodAdminAdapter(foodModels,this);
                            rvFood.setAdapter(foodAdapter);
                            rvFood.setVisibility(View.VISIBLE);
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
        rvCategory.setLayoutManager(linearLayoutManager);
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
                            sizeCategory = categoryModels.size();
                            catetoryAdapter = new CatetoryAdapter(categoryModels,this);
                            rvCategory.setAdapter(catetoryAdapter);
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
        rvFood.setLayoutManager(linearLayoutManager);
        database.collection(Contants.KEY_COLEECTION_FOODS)
                .whereEqualTo(Contants.KEY_ID_CATEGORY, String.valueOf(categoryModel.getId_category()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Food> foodModels = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            Food foodModel = new Food();
                            foodModel.setId(queryDocumentSnapshot.getId());
                            foodModel.setCategoryid(queryDocumentSnapshot.getString(Contants.KEY_ID_CATEGORY));
                            foodModel.setName(queryDocumentSnapshot.getString(Contants.KEY_NAME_FOOD));
                            foodModel.setPrice(queryDocumentSnapshot.getString(Contants.KEY_PRICE_FOOD));
                            foodModel.setImage(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_FOOD));
                            foodModel.setDetail(queryDocumentSnapshot.getString(Contants.KEY_DETAIL_FOOD));
                            foodModels.add(foodModel);
                        }
                        if (foodModels.size() > 0) {

                            foodAdapter = new FoodAdminAdapter(foodModels,this);
                            rvFood.setAdapter(foodAdapter);
                            rvFood.setVisibility(View.VISIBLE);
                        } else {
                            showToast("Error recyclerviewfood1");
                        }
                    } else {
                        showToast("Error recyclerviewfood2");
                    }
                });

    }


    @Override
    public void FoodAdminClick(Food food) {
        Bundle bundle = new Bundle();
        String foodId = food.getId();
        bundle.putString("FoodID",foodId);
        Navigation.findNavController(view).navigate(R.id.action_navigationAdminMainFood_to_navigationAdminDetailFood, bundle);
    }
}
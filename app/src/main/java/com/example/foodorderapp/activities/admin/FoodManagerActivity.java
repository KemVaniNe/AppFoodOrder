package com.example.foodorderapp.activities.admin;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foodorderapp.NewAdapter.FoodAdminAdapter;
import com.example.foodorderapp.R;
import com.example.foodorderapp.activities.user.MainActivity;
import com.example.foodorderapp.adapter.CatetoryAdapter;
import com.example.foodorderapp.databinding.ActivityFoodManagerBinding;
import com.example.foodorderapp.model.CategoryModel;
import com.example.foodorderapp.model.FoodModel;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FoodManagerActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 10;
    private static final String TAG = MainActivity.class.getName();
    private ActivityFoodManagerBinding binding;
    private PreferenceManeger preferenceManeger;

    private CatetoryAdapter adapter;
    private FoodAdminAdapter foodAdminAdapter;

    private StorageReference storageRef;

    private CatetoryAdapter catetoryAdapter;

    private FirebaseFirestore database;

    private ImageView imgUpload;

    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Log.e(TAG, "onActivityResult");
                if(result.getResultCode() == Activity.RESULT_OK)
                {
                    Intent data = result.getData();
                    if(data == null)
                    {
                        return;
                    }
                    Uri uri = data.getData();
                    try
                    {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                        imgUpload.setImageBitmap(bitmap);
                    }catch(IOException e)
                    {
                        e.printStackTrace();
                    }
            }
        }
    );

    public FoodManagerActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodManagerBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        database = FirebaseFirestore.getInstance();
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


        loadFood();
        loadCategory();
    }

    void loadFood(){
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
                            foodModels.add(foodModel);
                        }
                        if(foodModels.size() >0){
                         /*   foodAdminAdapter = new FoodAdminAdapter(foodModels,this);
                            binding.recyclerviewfood.setAdapter(foodAdminAdapter);
                            binding.recyclerviewfood.setVisibility(View.VISIBLE);*/
                        }else{
                            Toast.makeText(getApplicationContext(), "recyclerviewfood1", Toast.LENGTH_LONG);
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "recyclerviewfood2", Toast.LENGTH_LONG);
                    }
                });
    }

    private void loadCategory(){
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
                            catetoryAdapter = new CatetoryAdapter(categoryModels);
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
        imgUpload = mView.findViewById(R.id.btn_addPicFood);

        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickRequestPermission();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             //   String id = myRef.push().getKey();
                String id = "";
                String name = edtName.getText().toString();
                String price = edtPrice.getText().toString();
                String detail = edtDetail.getText().toString();
                String anh = "";
                addFoodToDB(id,name,price,detail,anh);

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void onClickRequestPermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return;
        }
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
        }
        else {
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission,MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MY_REQUEST_CODE)
        {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select picture"));
    }

    public void addFoodToDB(String id, String name, String price, String detail, String anh )
    {
        HashMap<String , Object> food = new HashMap<>();
        food.put(Contants.KEY_NAME_FOOD,name);
        food.put(Contants.KEY_PRICE_FOOD, price );
        food.put(Contants.KEY_DETAIL_FOOD,detail );
        food.put(Contants.KEY_ID_FOOD, id);
        food.put(Contants.KEY_IMAGE_FOOD,anh );
        database.collection((Contants.KEY_COLEECTION_FOODS))
                .add(food)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getApplicationContext(),"Thêm món ăn thành công",Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(exception->{
                    Toast.makeText(getApplicationContext(),"Thêm món ăn thất bại",Toast.LENGTH_SHORT).show();
                });
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

                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
package com.example.foodorderapp.View.Admin;

import static android.app.Activity.RESULT_OK;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.foodorderapp.Adapter.AddFoodCategory;
import com.example.foodorderapp.Adapter.ComboAdapter;
import com.example.foodorderapp.R;
import com.example.foodorderapp.Adapter.CatetoryAdapter;
import com.example.foodorderapp.Model.CategoryModel;
import com.example.foodorderapp.databinding.FragmentNavigationAdminNewFoodBinding;
import com.example.foodorderapp.utilities.Contants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NavigationAdminNewFood extends Fragment {
    private String encodedImage;
    private FragmentNavigationAdminNewFoodBinding binding;
    private String idCate;
    private AddFoodCategory categoryAdapter;
    private List<CategoryModel> categoryList;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNavigationAdminNewFoodBinding.inflate(inflater , container , false);

        categoryList = new ArrayList<>();
        categoryAdapter = new AddFoodCategory(getContext(), R.layout.item_selected, getListCategory());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spnCategory.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        binding.spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryModel selectedCategory = (CategoryModel) adapterView.getItemAtPosition(i);
                idCate = selectedCategory.getId_category();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigationAdminNewFood_to_navigationAdminMainFood);
            }
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveFood();
            }
        });

        binding.btnAddPicFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        return binding.getRoot();
    }

    private List<CategoryModel> getListCategory()
    {
        List<CategoryModel> list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("categorys")
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            String nameCategory = queryDocumentSnapshot.getString(Contants.KEY_NAME_CATEGORY);
                            String idCategory = queryDocumentSnapshot.getId();
                            CategoryModel categoryModel = new CategoryModel(idCategory,nameCategory);
                            list.add(categoryModel);
                        }
                        categoryAdapter.notifyDataSetChanged();
                    }else{
                        Log.d("TAG", "Error getting categories: ", task.getException());
                    }
                });
        return list;
    }
    private void SaveFood()
    {
        Map<String, Object> doc = new HashMap<>();
        doc.put("categoryid", idCate);
        doc.put("image", encodedImage);
        doc.put("detail",binding.edtName.getText().toString());
        doc.put("name",binding.edtDetail.getText().toString());
        doc.put("price",binding.edtPrice.getText().toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("foods").add(doc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Thêm món ăn thành công!" , Toast.LENGTH_LONG).show();
                        Navigation.findNavController(binding.getRoot()).navigate(R.id.action_navigationAdminNewFood_to_navigationAdminMainFood);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Thêm món ăn thất bại thất bại!" , Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            binding.btnAddPicFood.setImageURI(imageUri);

            try {
                InputStream inputStream = getContext().getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
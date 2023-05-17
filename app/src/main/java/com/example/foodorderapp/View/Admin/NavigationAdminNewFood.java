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

import com.example.foodorderapp.NewAdapter.AddFoodCategory;
import com.example.foodorderapp.NewAdapter.ComboAdapter;
import com.example.foodorderapp.NewModel.Category;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.CatetoryAdapter;
import com.example.foodorderapp.model.CategoryModel;
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
    private ImageView btnBack, btnAddPicFood;

    private AppCompatButton btnSave;

    private EditText edtName, edtPrice, edtDetail;

    private String encodedImage;
    private View view;

    private String idCate;
    private Spinner spinnerCategory;
    private AddFoodCategory categoryAdapter;
    private List<Category> categoryList;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view =  inflater.inflate(R.layout.fragment_navigation_admin_new_food, container, false);

        btnBack = view.findViewById(R.id.btn_back);
        btnSave = view.findViewById(R.id.btn_save);
        btnAddPicFood = view.findViewById(R.id.btn_addPicFood);
        edtDetail = view.findViewById(R.id.edt_detail);
        edtPrice = view.findViewById(R.id.edt_price);
        edtName = view.findViewById(R.id.edt_name);
        spinnerCategory = view.findViewById(R.id.spn_category);

        categoryList = new ArrayList<>();
        categoryAdapter = new AddFoodCategory(getContext(), R.layout.item_selected, getListCategory());
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category selectedCategory = (Category) adapterView.getItemAtPosition(i);
                idCate = selectedCategory.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigationAdminNewFood_to_navigationAdminMainFood);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveFood();
            }
        });

        btnAddPicFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        return view;
    }

    private List<Category> getListCategory()
    {
        List<Category> list = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("categorys")
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            String nameCategory = queryDocumentSnapshot.getString(Contants.KEY_NAME_CATEGORY);
                            String idCategory = queryDocumentSnapshot.getString(Contants.KEY_ID_CATEGORY);
                            Category categoryModel = new Category(nameCategory,idCategory);
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
        doc.put("detail",edtName.getText().toString());
        doc.put("name",edtDetail.getText().toString());
        doc.put("price",edtPrice.getText().toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("foods").add(doc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getContext(), "Thêm món ăn thành công!" , Toast.LENGTH_LONG).show();
                        Navigation.findNavController(view).navigate(R.id.action_navigationAdminNewFood_to_navigationAdminMainFood);
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
            btnAddPicFood.setImageURI(imageUri);

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
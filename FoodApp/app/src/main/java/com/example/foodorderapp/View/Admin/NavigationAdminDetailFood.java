package com.example.foodorderapp.View.Admin;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foodorderapp.Model.FoodModel;
import com.example.foodorderapp.R;
import com.example.foodorderapp.Model.CategoryModel;
import com.example.foodorderapp.utilities.Contants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NavigationAdminDetailFood extends Fragment {
    private ImageView btnBack;

    private String foodId;
    private EditText etNameFood, etPriceFood, etDetailFood;
    private ImageView imgFood;
    private FirebaseFirestore database;
    private AppCompatButton btnUpdate, btnDelete;
    private String encodedImage;
    private View view;
    private ProgressDialog pd;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_navigation_admin_detail_food, container, false);
        pd = new ProgressDialog(getContext());

        btnBack = view.findViewById(R.id.btn_back);
        btnUpdate = view.findViewById(R.id.btn_updateFood);
        btnDelete = view.findViewById(R.id.btn_deleteFood);
        etNameFood = view.findViewById(R.id.et_nameFood);
        etPriceFood = view.findViewById(R.id.et_priceFood);
        etDetailFood = view.findViewById(R.id.et_detailFood);
        imgFood = view.findViewById(R.id.img_food);

        database = FirebaseFirestore.getInstance();

        foodId = getArguments().getString("FoodID");



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigationAdminDetailFood_to_navigationAdminMainFood);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateFood();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteFood();
            }
        });

        imgFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findFood();
    }


    private void findFood()
    {
        database.collection("foods").document(foodId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            FoodModel foodModel = new FoodModel("","","","","","");
                            foodModel.setId(documentSnapshot.getId());
                            foodModel.setCategory_id(documentSnapshot.getString(Contants.KEY_ID_CATEGORY));
                            foodModel.setName(documentSnapshot.getString(Contants.KEY_NAME_FOOD));
                            foodModel.setPrice(documentSnapshot.getString(Contants.KEY_PRICE_FOOD));
                            foodModel.setImage(documentSnapshot.getString(Contants.KEY_IMAGE_FOOD));
                            foodModel.setDetail(documentSnapshot.getString(Contants.KEY_DETAIL_FOOD));

                            etNameFood.setText(foodModel.getName());
                            etPriceFood.setText(foodModel.getPrice());
                            etDetailFood.setText(foodModel.getDetail());
                            if (foodModel.getImage() != null) {
                                byte[] bytes = Base64.decode(foodModel.getImage(), Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imgFood.setImageBitmap(bitmap);
                                encodedImage = foodModel.getImage();
                            }
                        } else {
                            Log.d("DEBUG", "Document not found");
                        }
                    } else {
                        Log.d("DEBUG", "Fail admin detail food show");
                    }
                });


    }

    private void UpdateFood() {
        String name = etNameFood.getText().toString();
        String price = etPriceFood.getText().toString();
        String detail = etDetailFood.getText().toString();

        if (name.isEmpty() || price.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin!", Toast.LENGTH_LONG).show();
        } else {
            pd.setTitle("Updating food...");
            pd.show();
            database.collection("foods").document(foodId)
                    .update("name", name, "price", price, "detail", detail, "image", encodedImage)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Cập nhật thông tin món ăn thành công!", Toast.LENGTH_LONG).show();
                            Navigation.findNavController(view).navigate(R.id.action_navigationAdminDetailFood_to_navigationAdminMainFood);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getContext(), "Cập nhật thông tin món ăn thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void DeleteFood() {
        database.collection("foods").document(foodId)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Xóa món ăn thành công!", Toast.LENGTH_LONG).show();
                        Navigation.findNavController(view).navigate(R.id.action_navigationAdminDetailFood_to_navigationAdminMainFood);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Xóa món ăn thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
            imgFood.setImageURI(imageUri);

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



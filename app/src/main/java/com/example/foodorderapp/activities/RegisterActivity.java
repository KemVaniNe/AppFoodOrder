package com.example.foodorderapp.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import com.example.foodorderapp.databinding.ActivityRegisterBinding;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    private PreferenceManeger preferenceManeger;
    private  String encodedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManeger = new PreferenceManeger(getApplicationContext());
        SetListener();
    }
    private void SetListener(){
        binding.tvSignin.setOnClickListener(v->onBackPressed());
//        binding.tvSignin.setOnClickListener(v->{
//            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            pickImage.launch(intent);
//        });
        binding.tvRegiter.setOnClickListener(v->{
            if(isValidRegisterDetail()){
                Register();
            }
           // addfood();
        });
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }
    private void Register(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String , Object> user = new HashMap<>();
        user.put(Contants.KEY_USERNAME, binding.etUsername.getText().toString());
        user.put(Contants.KEY_PASSWORD, binding.etPassword.getText().toString());
        database.collection((Contants.KEY_COLEECTION_USERS))
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManeger.putBoolean(Contants.KEY_IS_LOGINED_IN, true);
                    preferenceManeger.putString(Contants.KEY_USER_ID,documentReference.getId() );
                    preferenceManeger.putString(Contants.KEY_USERNAME , binding.etUsername.getText().toString());
                    preferenceManeger.putString(Contants.KEY_PASSWORD, binding.etPassword.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                })
                .addOnFailureListener(exception->{
                    showToast("Đăng ký thất bại");
                });
    }

    private Boolean isValidRegisterDetail(){
        if(binding.etUsername.getText().toString().trim().isEmpty()){
            showToast("Nhập username");
            return false;
        }else if (!binding.etUsername2.getText().toString().equals(binding.etUsername.getText().toString())){
            showToast("Xác nhận username sai");
            return false;
        }
        else if (binding.etPassword.getText().toString().trim().isEmpty()){
            showToast("Nhập mật khẩu");
            return false;
        }else if(!binding.etPassword2.getText().toString().equals(binding.etPassword.getText().toString())){
            showToast("Xác nhận mật khẩu sai");
            return false;
        }else{
            return true;
        }
    }

//    private void addfood(){
//
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        HashMap<String , Object> food = new HashMap<>();
//        food.put(Contants.KEY_NAME_FOOD,binding.etUsername.getText().toString());
//        food.put(Contants.KEY_PRICE_FOOD,binding.etUsername2.getText().toString() );
//        food.put(Contants.KEY_DETAIL_FOOD,binding.etPassword.getText().toString() );
//        food.put(Contants.KEY_ID_FOOD, binding.etPassword2.getText().toString());
//        food.put(Contants.KEY_IMAGE_FOOD,encodedImage );
//        database.collection((Contants.KEY_COLEECTION_FOODS))
//                .add(food)
//                .addOnSuccessListener(documentReference -> {
//                    showToast("Đăng ký thành công");
//
//                })
//                .addOnFailureListener(exception->{
//                    showToast("Đăng ký thất bại");
//                });
//    }
//
//
//    private void addCategory(){
//
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        HashMap<String , Object> food = new HashMap<>();
//        food.put(Contants.KEY_NAME_CATEGORY,binding.etUsername.getText().toString());
//        food.put(Contants.KEY_ID_CATEGORY,binding.etUsername2.getText().toString() );
//        food.put(Contants.KEY_IMAGE_CATEGORY,encodedImage );
//        database.collection((Contants.KEY_COLEECTION_CATEGORY))
//                .add(food)
//                .addOnSuccessListener(documentReference -> {
//                    showToast("Đăng ký thành công");
//
//                })
//                .addOnFailureListener(exception->{
//                    showToast("Đăng ký thất bại");
//                });
//    }
//
//    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if(result.getResultCode() == RESULT_OK) {
//                    if(result.getData() != null) {
//                        Uri imageUri = result.getData().getData();
//                        try{
//                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
//                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                            encodedImage = encodedImage(bitmap);
//                        }catch (FileNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//    );
//
//    private  String encodedImage(Bitmap bitmap){
//        int previewWidth = 150;
//        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
//        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
//        byte[] bytes = byteArrayOutputStream.toByteArray();
//        return Base64.encodeToString(bytes, Base64.DEFAULT);
//    }
}
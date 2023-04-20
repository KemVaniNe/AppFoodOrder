package com.example.foodorderapp.activities.shared;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ActivityAccountBinding;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class AccountActivity extends AppCompatActivity {

    private ActivityAccountBinding binding;
    private  FirebaseFirestore database ;
    private PreferenceManeger preferenceManeger;
    private  String encodedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        preferenceManeger = new PreferenceManeger(getApplicationContext());
        database = FirebaseFirestore.getInstance();
        loadUser();

    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK) {
                    if(result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try{
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.imgAvatar.setImageBitmap(bitmap);
                            encodedImage = encodedImage(bitmap);
                        }catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private  String encodedImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private void updateUser(){

        HashMap<String, Object> user = new HashMap<>();
        String user_id = preferenceManeger.getSrting(Contants.KEY_USER_ID);
        user.put(Contants.KEY_USERNAME, binding.etUsername.getText().toString());
        user.put(Contants.KEY_PHONE, binding.etPhone.getText().toString());
        user.put(Contants.KEY_IMAGE_USER, encodedImage);
        database.collection(Contants.KEY_COLEECTION_USERS)
                .document(user_id)
                .update(user)
                .addOnSuccessListener(aVoid -> {
                    showToast("Cập nhật thành công");
                })
                .addOnFailureListener(e -> {
                    showToast("Cập nhật thất bại");
                });

    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }
    private Boolean isValidRegisterDetail(){
        if(binding.etUsername.getText().toString().trim().isEmpty()){
            showToast("Nhập username");
            return false;
        }else if (binding.etPhone.getText().toString().trim().isEmpty()){
            showToast("Nhập number phone");
            return false;
        }else{
            return true;
        }
    }
    private Bitmap getAvatarImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
    private void loadUser(){
        binding.etUsername.setText(preferenceManeger.getSrting(Contants.KEY_USERNAME));
        binding.etPhone.setText(preferenceManeger.getSrting(Contants.KEY_PHONE));
        binding.imgAvatar.setImageBitmap(getAvatarImage(preferenceManeger.getSrting(Contants.KEY_IMAGE_USER)));
        binding.btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidRegisterDetail()){
                    updateUser();
                }
            }
        });
        binding.imgAvatar.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
        binding.btUpdatepass.setOnClickListener(v->openChangePassword(Gravity.BOTTOM));
    }
    private void openChangePassword(int gravity){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_pass);
        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.BOTTOM == gravity){
            dialog.setCancelable(true);
        }else{
            dialog.setCancelable(false);
        }
        EditText edoldpass = dialog.findViewById(R.id.ed_oldpass);
        EditText newpass = dialog.findViewById(R.id.ed_newpass);
        EditText cofpass = dialog.findViewById(R.id.edt_confirmPass);
        AppCompatButton bt_cancle = dialog.findViewById(R.id.bt_cancle);
        AppCompatButton bt_update = dialog.findViewById(R.id.bt_update);

        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = true;
                if (edoldpass.getText().toString().equals(preferenceManeger.getSrting(Contants.KEY_PASSWORD))){
                    showToast("Nhập mật khẩu cũ sai");
                    check= false;
                }
                else if (newpass.getText().toString().trim().isEmpty()){
                    showToast("Nhập mật khẩu mới");
                    check = false;
                }else if(!cofpass.getText().toString().equals(newpass.getText().toString())){
                    showToast("Xác nhận mật khẩu mới sai");
                    check = false;
                }
                if(check){
                    HashMap<String, Object> user = new HashMap<>();
                    String user_id = preferenceManeger.getSrting(Contants.KEY_USER_ID);
                    user.put(Contants.KEY_PASSWORD, newpass.getText().toString());
                    database.collection(Contants.KEY_COLEECTION_USERS)
                            .document(user_id)
                            .update(user)
                            .addOnSuccessListener(aVoid -> {
                                showToast("Cập nhật thành công");
                            })
                            .addOnFailureListener(e -> {
                                showToast("Cập nhật thất bại");
                            });

                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
}
package com.example.foodorderapp.activities.user;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import com.example.foodorderapp.activities.shared.AccountActivity;
import com.example.foodorderapp.databinding.ActivityUserBinding;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserActivity extends AppCompatActivity {
    private ActivityUserBinding binding;
    private FirebaseFirestore database ;
    private PreferenceManeger preferenceManeger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        preferenceManeger = new PreferenceManeger(getApplicationContext());
        database = FirebaseFirestore.getInstance();
        loadUserDetails();
        listenModifyUser();
        binding.btnBack.setOnClickListener(v->onBackPressed());

        binding.btnHoTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, SupportActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        binding.btnDiaChi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, AddressActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        binding.btnQuanLyTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, AccountActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        binding.btnDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, OrderActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void loadUserDetails(){
        binding.txtuser.setText(preferenceManeger.getSrting(Contants.KEY_USERNAME));
        binding.imgAvatar.setImageBitmap(getAvatarImage(preferenceManeger.getSrting(Contants.KEY_IMAGE_USER)));
    }
    private Bitmap getAvatarImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
    private void listenModifyUser(){
        database.collection(Contants.KEY_COLEECTION_USERS)
                .addSnapshotListener(eventListener);
    }
    private final EventListener<QuerySnapshot> eventListener = (value, error) ->{
        if(error !=null){
            return;
        }
        if(value != null){

            for(DocumentChange documentChange : value.getDocumentChanges()){
                System.out.println(documentChange.getDocument().getString(Contants.KEY_USERNAME));
                if(documentChange.getType() == DocumentChange.Type.MODIFIED){
                    String newname = documentChange.getDocument().getString(Contants.KEY_USERNAME);
                    String image = documentChange.getDocument().getString(Contants.KEY_IMAGE_USER);
                    binding.imgAvatar.setImageBitmap(getAvatarImage(image));
                    binding.txtuser.setText(newname);
                    preferenceManeger.Remove(Contants.KEY_USERNAME);
                    preferenceManeger.Remove(Contants.KEY_IMAGE_USER);
                    preferenceManeger.putString(Contants.KEY_USERNAME , newname);
                    preferenceManeger.putString(Contants.KEY_IMAGE_USER, image);
                }
            }
        }
    };
}
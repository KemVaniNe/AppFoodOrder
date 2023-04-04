package com.example.foodorderapp.activities;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {
    ActivityDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        loadDetail();
    }
    private void loadDetail(){
        Intent intent = getIntent();
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.tvNameFood.setText(intent.getStringExtra("name"));
        binding.tvPriceFood.setText(intent.getStringExtra("price"));
        binding.tvDetail.setText(intent.getStringExtra("detail"));
        String encodedImage = intent.getStringExtra("image");
        Bitmap bitmap = getFoodImage(encodedImage);
        binding.imageView.setImageBitmap(bitmap);
    }

    private Bitmap getFoodImage(String encodedImage){
        byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

}
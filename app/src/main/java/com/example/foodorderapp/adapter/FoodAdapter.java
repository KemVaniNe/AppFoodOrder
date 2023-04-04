package com.example.foodorderapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.activities.DetailActivity;
import com.example.foodorderapp.databinding.ActivityDetailBinding;
import com.example.foodorderapp.databinding.ViewholderFoodBinding;
import com.example.foodorderapp.model.FoodModel;

import java.util.List;


public class FoodAdapter extends  RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{
    private  final List<FoodModel> foodModels;

    ActivityDetailBinding binding;
    public FoodAdapter(List<FoodModel> foodModels) {
        this.foodModels = foodModels;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderFoodBinding viewholderFoodBinding = ViewholderFoodBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new FoodViewHolder(viewholderFoodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.setFooddata(foodModels.get(position));
        FoodModel food = foodModels.get(position);
        holder.itemView.setOnClickListener(v ->{
            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("name", food.getName());
            intent.putExtra("price", food.getPrice());
            intent.putExtra("detail", food.getDetail());
            intent.putExtra("image", food.getImage());
            v.getContext().startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return foodModels.size();
    }

    class  FoodViewHolder extends RecyclerView.ViewHolder{
        ViewholderFoodBinding binding;
        FoodViewHolder(@NonNull ViewholderFoodBinding viewholderFoodBinding) {
            super(viewholderFoodBinding.getRoot());
            binding = viewholderFoodBinding;

        }
        void setFooddata(FoodModel food){
            binding.name.setText(food.getName());
            binding.price.setText(food.getPrice());
            binding.imagefood.setImageBitmap(getFoodImage(food.getImage()));
        }

    }
    private Bitmap getFoodImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }

}

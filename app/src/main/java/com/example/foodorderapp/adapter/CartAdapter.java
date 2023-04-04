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
import com.example.foodorderapp.databinding.ViewholderCartBinding;
import com.example.foodorderapp.model.FoodModel;

import java.util.List;

public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private  final List<FoodModel> foodModel;
    private Context mContext;

    public CartAdapter(List<FoodModel> foodModel) {
        this.foodModel = foodModel;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding viewholderCartBinding = ViewholderCartBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new CartAdapter.CartViewHolder(viewholderCartBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        holder.setFooddata(foodModel.get(position));
    }

    @Override
    public int getItemCount() {
        return foodModel.size();
    }

    ViewholderCartBinding binding;
    public class CartViewHolder extends RecyclerView.ViewHolder {
        public CartViewHolder(@NonNull ViewholderCartBinding viewholderCartBinding) {
            super(viewholderCartBinding.getRoot());
            binding = viewholderCartBinding;
        }

        void setFooddata(FoodModel food){
            binding.nameCk.setText(food.getName());
            binding.priceCk.setText(food.getPrice());
            binding.imagefoodCk.setImageBitmap(getCartImage(food.getImage()));
        }
    }
    private Bitmap getCartImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
}

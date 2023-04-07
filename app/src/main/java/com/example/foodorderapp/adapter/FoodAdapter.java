package com.example.foodorderapp.adapter;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.activities.user.DetailActivity;

import com.example.foodorderapp.databinding.ViewholderFoodBinding;
import com.example.foodorderapp.listener.FoodListener;
import com.example.foodorderapp.model.FoodModel;

import java.util.List;


public class FoodAdapter extends  RecyclerView.Adapter<FoodAdapter.FoodViewHolder>{
    private  final List<FoodModel> foodModels;
    private final FoodListener listener;


    public FoodAdapter(List<FoodModel> foodModels, FoodListener listener) {

        this.foodModels = foodModels;
        this.listener = listener;
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
            binding.btAdd.setOnClickListener(v->listener.FoodItemCLick(food));
            binding.imagefood.setOnClickListener(v->listener.FoodItemDetailClick(food));
        }

    }


    
    private Bitmap getFoodImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }

}

package com.example.foodorderapp.Adapter;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foodorderapp.Model.FoodModel;
import com.example.foodorderapp.databinding.ViewholderUserFoodBinding;
import com.example.foodorderapp.listener.FoodListener;

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
        ViewholderUserFoodBinding viewholderFoodBinding = ViewholderUserFoodBinding.inflate(
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
        ViewholderUserFoodBinding binding;
        FoodViewHolder(@NonNull ViewholderUserFoodBinding viewholderFoodBinding) {
            super(viewholderFoodBinding.getRoot());
            binding = viewholderFoodBinding;

        }
        void setFooddata(FoodModel food){
            binding.name.setText(food.getName());
            binding.price.setText(food.getPrice());
            binding.imagefood.setImageBitmap(getFoodImage(food.getImage()));
            binding.btnNewFood.setOnClickListener(v->listener.FoodItemCLick(food));
            binding.imagefood.setOnClickListener(v->listener.FoodItemDetailClick(food));
        }

    }


    
    private Bitmap getFoodImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }

}

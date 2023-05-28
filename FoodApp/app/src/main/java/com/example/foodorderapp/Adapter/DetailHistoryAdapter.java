package com.example.foodorderapp.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.Model.FoodOrderModel;
import com.example.foodorderapp.databinding.ViewholderUserOrderBinding;
import com.example.foodorderapp.listener.DetailHistoryListener;

import java.util.List;

public class DetailHistoryAdapter extends RecyclerView.Adapter<DetailHistoryAdapter.DetailHistoryViewHolder> {

    private  final List<FoodOrderModel> foodOrderModels;
    private final DetailHistoryListener listener;
    private final String Role;
    public DetailHistoryAdapter(List<FoodOrderModel> foodOrderModels, DetailHistoryListener listener , String Role) {
        this.foodOrderModels = foodOrderModels;
        this.listener = listener;
        this.Role = Role;
    }

    @NonNull
    @Override
    public DetailHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderUserOrderBinding activityDetailBinding = ViewholderUserOrderBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new DetailHistoryViewHolder(activityDetailBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailHistoryViewHolder holder, int position) {
        holder.setFooddata(foodOrderModels.get(position), Role);
    }

    @Override
    public int getItemCount() {
        return foodOrderModels.size();
    }

    public class DetailHistoryViewHolder extends RecyclerView.ViewHolder {
        ViewholderUserOrderBinding binding;
        public DetailHistoryViewHolder(@NonNull ViewholderUserOrderBinding activityDetailBinding) {
            super(activityDetailBinding.getRoot());
            binding = activityDetailBinding;
        }

        void setFooddata(FoodOrderModel foodOrderModel, String Role){
            binding.tvNameFood.setText(foodOrderModel.getNameFood());
            binding.totalFood.setText(String.valueOf(foodOrderModel.getPrice()*foodOrderModel.getNumber()));
            binding.imageOder.setImageBitmap(getCartImage(foodOrderModel.getImageFood()));
            binding.tvNumber.setText(String.valueOf(foodOrderModel.getNumber()));
            if(Role.equals("User")){
                binding.imgEvaluation.setOnClickListener(v->listener.evaluationClick(foodOrderModel.getId()));
            }else{
                binding.imgEvaluation.setVisibility(View.GONE);
            }
        }
    }
    private Bitmap getCartImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
}

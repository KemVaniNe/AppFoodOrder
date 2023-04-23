package com.example.foodorderapp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.databinding.ViewholderCartBinding;
import com.example.foodorderapp.databinding.ViewholderOdersBinding;
import com.example.foodorderapp.listener.DetailHistoryListener;
import com.example.foodorderapp.model.FoodOrderModel;

import java.util.List;

public class DetailHistoryAdapter extends RecyclerView.Adapter<DetailHistoryAdapter.DetailHistoryViewHolder> {

    private  final List<FoodOrderModel> foodOrderModels;
    private final DetailHistoryListener listener;

    public DetailHistoryAdapter(List<FoodOrderModel> foodOrderModels, DetailHistoryListener listener) {
        this.foodOrderModels = foodOrderModels;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DetailHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderOdersBinding activityDetailBinding = ViewholderOdersBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new DetailHistoryViewHolder(activityDetailBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailHistoryViewHolder holder, int position) {
        holder.setFooddata(foodOrderModels.get(position));
    }

    @Override
    public int getItemCount() {
        return foodOrderModels.size();
    }

    public class DetailHistoryViewHolder extends RecyclerView.ViewHolder {
        ViewholderOdersBinding binding;
        public DetailHistoryViewHolder(@NonNull ViewholderOdersBinding activityDetailBinding) {
            super(activityDetailBinding.getRoot());
            binding = activityDetailBinding;
        }

        void setFooddata(FoodOrderModel foodOrderModel){
            binding.tvNameFood.setText(foodOrderModel.getNameFood());
            binding.totalFood.setText(String.valueOf(foodOrderModel.getPrice()*foodOrderModel.getNumber()));
            binding.imageOder.setImageBitmap(getCartImage(foodOrderModel.getImageFood()));
            binding.tvNumber.setText(String.valueOf(foodOrderModel.getNumber()));
            binding.imgEvaluation.setOnClickListener(v->listener.evaluationClick(foodOrderModel.getId()));
        }
    }
    private Bitmap getCartImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
}

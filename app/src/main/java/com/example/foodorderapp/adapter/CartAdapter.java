package com.example.foodorderapp.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.activities.user.CheckoutActivity;
import com.example.foodorderapp.activities.user.DetailActivity;
import com.example.foodorderapp.activities.user.UserActivity;
import com.example.foodorderapp.databinding.ActivityCheckoutBinding;
import com.example.foodorderapp.databinding.ActivityDetailBinding;
import com.example.foodorderapp.databinding.ViewholderCartBinding;
import com.example.foodorderapp.listener.OrderAddorSubListener;
import com.example.foodorderapp.model.FoodModel;
import com.example.foodorderapp.model.FoodOrderModel;

import java.util.List;

public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.CartViewHolder>{
    private  final List<FoodOrderModel> foodOrderModels;
    private  final OrderAddorSubListener listener ;
    private int totalprice;


    public CartAdapter(List<FoodOrderModel> foodOrderModels, OrderAddorSubListener listener, int totalprice) {
        this.foodOrderModels = foodOrderModels;
        this.listener = listener;
        this.totalprice = totalprice;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCartBinding activityDetailBinding = ViewholderCartBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new CartViewHolder(activityDetailBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        holder.setFooddata(foodOrderModels.get(position));
    }

    @Override
    public int getItemCount() {
        return foodOrderModels.size();
    }
    public void setTotalprice(int totalprice){
        this.totalprice = totalprice;
    }
    public class CartViewHolder extends RecyclerView.ViewHolder {
        ViewholderCartBinding binding;
        public CartViewHolder(@NonNull ViewholderCartBinding activityDetailBinding) {
            super(activityDetailBinding.getRoot());
            binding = activityDetailBinding;
        }

        void setFooddata(FoodOrderModel foodOrderModel){
            binding.nameFood.setText(foodOrderModel.getNameFood());
            binding.totalPrice.setText(String.valueOf(foodOrderModel.getPrice()*foodOrderModel.getNumber()));
            binding.imagefoodCk.setImageBitmap(getCartImage(foodOrderModel.getImageFood()));
            binding.tvNumber.setText(String.valueOf(foodOrderModel.getNumber()));
            binding.btAdd.setOnClickListener(v->{

                int price = 0;
                int number = Integer.parseInt(binding.tvNumber.getText().toString());
                number += 1;
                totalprice -= Integer.parseInt(binding.totalPrice.getText().toString());
                price = number*foodOrderModel.getPrice();
                binding.totalPrice.setText(String.valueOf(price));
                binding.tvNumber.setText(String.valueOf(number));
                listener.AddorSubClick(totalprice + price,number,foodOrderModel.getId());
            });
            binding.btSub.setOnClickListener(v->{
                int number = Integer.parseInt(binding.tvNumber.getText().toString());
                int price = 0;
                if(number>0){
                    number -= 1;
                    totalprice -= Integer.parseInt(binding.totalPrice.getText().toString());
                    price = number*foodOrderModel.getPrice();
                    binding.totalPrice.setText(String.valueOf(price));
                }
                binding.tvNumber.setText(String.valueOf(number));
                listener.AddorSubClick(totalprice + price , number,foodOrderModel.getId());
            });
        }
    }
    private Bitmap getCartImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
}

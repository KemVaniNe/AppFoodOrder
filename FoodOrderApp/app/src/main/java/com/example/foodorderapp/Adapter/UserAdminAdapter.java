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
import com.example.foodorderapp.Model.UserModel;
import com.example.foodorderapp.databinding.ViewholderAdminUserBinding;

import java.util.List;

public class UserAdminAdapter extends  RecyclerView.Adapter<UserAdminAdapter.UserAdminViewHolder>{
    private List<UserModel> userModels;

//    ActivityDetailBinding binding;
    public UserAdminAdapter(List<UserModel> foodModels) {
        this.userModels = foodModels;
    }

    @NonNull
    @Override
    public UserAdminAdapter.UserAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderAdminUserBinding viewholderFoodBinding = ViewholderAdminUserBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new UserAdminAdapter.UserAdminViewHolder(viewholderFoodBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdminAdapter.UserAdminViewHolder holder, int position) {
        holder.setUserdata(userModels.get(position));
        UserModel user = userModels.get(position);
        holder.itemView.setOnClickListener(v ->{
//            Intent intent = new Intent(v.getContext(), DetailActivity.class);
//            intent.putExtra("name",user.getName());
//            intent.putExtra("pass",user.getPass());
//            v.getContext().startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return userModels.size();
    }

    class  UserAdminViewHolder extends RecyclerView.ViewHolder{

        ViewholderAdminUserBinding binding;
        UserAdminViewHolder(@NonNull ViewholderAdminUserBinding viewholderFoodBinding) {
            super(viewholderFoodBinding.getRoot());
            binding = viewholderFoodBinding;

        }
        void setUserdata(UserModel user){
            binding.tvNameUser.setText(user.getName());
            binding.tvPassUser.setText(user.getPass());
        }

    }
    private Bitmap getFoodImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
}

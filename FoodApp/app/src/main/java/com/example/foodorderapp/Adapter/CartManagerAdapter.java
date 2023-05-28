package com.example.foodorderapp.Adapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.Model.UserModel;
import com.example.foodorderapp.databinding.ViewholderAdminCartmanagerBinding;
import com.example.foodorderapp.listener.HistoryListener;
import com.example.foodorderapp.listener.UserHistoryListener;

import java.util.List;

public class CartManagerAdapter extends RecyclerView.Adapter<CartManagerAdapter.CartManagerViewHolder> {
    private final List<UserModel> list ;
    private final UserHistoryListener listener;
    public CartManagerAdapter(List<UserModel> list, UserHistoryListener listener) {
        this.list = list;
        this.listener = listener;
    }



    @NonNull
    @Override
    public CartManagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderAdminCartmanagerBinding binding = ViewholderAdminCartmanagerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new CartManagerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartManagerViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CartManagerViewHolder extends RecyclerView.ViewHolder{
        ViewholderAdminCartmanagerBinding binding;
        public CartManagerViewHolder(@NonNull ViewholderAdminCartmanagerBinding viewholderHistoryBinding) {
            super(viewholderHistoryBinding.getRoot());
            binding = viewholderHistoryBinding;
        }
        void setData(UserModel userModel){
            binding.tvNameUser.setText(userModel.getName());
            binding.btnView.setOnClickListener(v->listener.DetailUserHistoryClick(userModel.getId()));
        }
    }
}

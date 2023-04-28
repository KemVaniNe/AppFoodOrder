package com.example.foodorderapp.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.Model.CategoryModel;
import com.example.foodorderapp.databinding.ViewholderShareCategoryBinding;
import com.example.foodorderapp.listener.CategoryListener;

import java.util.List;

public class CatetoryAdapter extends RecyclerView.Adapter<CatetoryAdapter.CatetoryViewHolder> {
    private List<CategoryModel> list;
    private CategoryListener listener;

    public CatetoryAdapter(List<CategoryModel> list, CategoryListener listener) {
        this.list = list;
        this.listener = listener;
    }

    public CatetoryAdapter(List<CategoryModel> list){
        this.list = list;
    }

    @NonNull
    @Override
    public CatetoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderShareCategoryBinding viewholderCategoryBinding = ViewholderShareCategoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new CatetoryViewHolder(viewholderCategoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CatetoryViewHolder holder, int position) {
        holder.setCatetoryData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CatetoryViewHolder extends RecyclerView.ViewHolder{
        ViewholderShareCategoryBinding binding;
        public CatetoryViewHolder(@NonNull ViewholderShareCategoryBinding viewholderCategoryBinding) {
            super(viewholderCategoryBinding.getRoot());
            binding = viewholderCategoryBinding;
        }
        void setCatetoryData(CategoryModel categoryModel){
            binding.categoryName.setText(categoryModel.getName_category());
            binding.categoryPic.setImageBitmap(getCatetoryImage(categoryModel.getImage_category()));
            binding.getRoot().setOnClickListener(v->listener.CategoryCLick(categoryModel));
        }
    }
    private Bitmap getCatetoryImage(String encodedImage){
        byte[] bytes =Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
}

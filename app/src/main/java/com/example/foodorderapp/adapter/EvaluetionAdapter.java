package com.example.foodorderapp.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.databinding.ViewholderCommentBinding;
import com.example.foodorderapp.model.CommentModel;

import java.util.List;

public class EvaluetionAdapter extends RecyclerView.Adapter<EvaluetionAdapter.EvaluetionViewHolder> {
    private final List<CommentModel> list ;

    public EvaluetionAdapter(List<CommentModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public EvaluetionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderCommentBinding binding = ViewholderCommentBinding.inflate(
                LayoutInflater.from(parent.getContext())
                ,parent,
                false
        );
        return new EvaluetionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EvaluetionViewHolder holder, int position) {
        holder.setCommentData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class EvaluetionViewHolder extends RecyclerView.ViewHolder{
        ViewholderCommentBinding binding;

        public EvaluetionViewHolder(@NonNull ViewholderCommentBinding viewholderCommentBinding) {
            super(viewholderCommentBinding.getRoot());
            binding= viewholderCommentBinding;
        }
        void setCommentData(CommentModel commentModel){
               binding.imgAvatar.setImageBitmap(getAvatarUser(commentModel.getAvatar_user()));
               binding.tvNameUser.setText(commentModel.getName_user());
               binding.tvComment.setText(commentModel.getComment());
               binding.RatingBar.setRating(commentModel.getRate());
        }
    }
    private Bitmap getAvatarUser(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
}

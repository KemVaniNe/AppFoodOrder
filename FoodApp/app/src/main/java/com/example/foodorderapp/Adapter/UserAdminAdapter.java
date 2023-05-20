package com.example.foodorderapp.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.Model.UserModel;
import com.example.foodorderapp.R;

import java.util.List;

public class UserAdminAdapter extends RecyclerView.Adapter<UserAdminAdapter.NotificationViewHolder>{
    private final List<UserModel> listUser;

    public UserAdminAdapter(List<UserModel> listUser) {
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_admin_user,parent,false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        UserModel user = listUser.get(position);
        if(user == null)
        {
            return;
        }
        holder.tvName.setText(user.getName());

        if (user.getAvatar() != null) {

            holder.imgImage.setImageBitmap(getAvatarImage(user.getAvatar()));
        }
    }

    @Override
    public int getItemCount() {
        if(listUser != null)
        {
            return listUser.size();
        }
        return 0;
    }
    private Bitmap getAvatarImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
    public static class NotificationViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvName;
        private final ImageView imgImage;
        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_nameUser);
            imgImage = itemView.findViewById(R.id.img_avatar);
        }
    }


}
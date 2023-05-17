package com.example.foodorderapp.NewAdapter;

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

import com.example.foodorderapp.NewModel.User;
import com.example.foodorderapp.R;

import java.util.List;

public class UserAdminAdapter extends RecyclerView.Adapter<UserAdminAdapter.NotificationViewHolder>{
    private final List<User> listUser;

    public UserAdminAdapter(List<User> listUser) {
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
        User user = listUser.get(position);
        if(user == null)
        {
            return;
        }
        holder.tvName.setText(user.getName());

        if (user.getAvatar() != null) {
            byte[] bytes = Base64.decode(user.getAvatar(),Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            holder.imgImage.setImageBitmap(bitmap);
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
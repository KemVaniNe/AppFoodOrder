package com.example.foodorderapp.NewAdapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.NewListener.FoodAdminListener;
import com.example.foodorderapp.NewModel.Food;
import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.FoodAdapter;
import com.example.foodorderapp.databinding.ViewholderFoodBinding;
import com.example.foodorderapp.listener.FoodListener;
import com.example.foodorderapp.model.FoodModel;

import java.util.List;

public class FoodAdminAdapter extends  RecyclerView.Adapter<FoodAdminAdapter.FoodAdminViewHolder>{
    private  final List<Food> foodModels;
    private final FoodAdminListener listener;


    public FoodAdminAdapter(List<Food> foodModels, FoodAdminListener listener) {

        this.foodModels = foodModels;
        this.listener = listener;
    }


    @NonNull
    @Override
    public FoodAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_admin_food,parent,false);
        return new FoodAdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodAdminAdapter.FoodAdminViewHolder holder, int position) {
        holder.setListener(foodModels.get(position));
        Food food = foodModels.get(position);
        if (food == null) {
            return;
        }
        holder.tvName.setText(food.getName());
        holder.tvPrice.setText(food.getPrice());
        if (food.getImage() != null) {
            byte[] bytes = Base64.decode(food.getImage(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            holder.imgImage.setImageBitmap(bitmap);

        }

    }
    @Override
    public int getItemCount() {
        if(foodModels != null)
        {
            return foodModels.size();
        }
        return 0;
    }

    class  FoodAdminViewHolder extends RecyclerView.ViewHolder{
        private final TextView tvName;
        private final ImageView imgImage;
        private final TextView tvPrice;
        public FoodAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_nameFood);
            imgImage = itemView.findViewById(R.id.img_food);
            tvPrice = itemView.findViewById(R.id.tv_priceFood);

        }
        void setListener(Food food){
            imgImage.setOnClickListener(v->listener.FoodAdminClick(food));
        }

    }

}

package com.example.foodorderapp.NewAdapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.foodorderapp.NewModel.Category;
import com.example.foodorderapp.R;
import com.example.foodorderapp.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class AddFoodCategory extends ArrayAdapter<Category> {
    public AddFoodCategory(@NonNull Context context, int resource, @NonNull List<Category> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected,parent,false);
        TextView tvSelected = convertView.findViewById(R.id.tv_selected);
        Category category = this.getItem(position);
        if(category != null)
        {
            tvSelected.setText(category.getName());
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        TextView tvCategory = convertView.findViewById(R.id.tv_category);
        Category category = this.getItem(position);
        if(category != null)
        {
            tvCategory.setText(category.getName());
        }
        return convertView;
    }
}

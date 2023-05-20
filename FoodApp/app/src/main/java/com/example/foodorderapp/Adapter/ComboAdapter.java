package com.example.foodorderapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.foodorderapp.Model.CategoryModel;

import java.util.List;

public class ComboAdapter extends ArrayAdapter<CategoryModel> {

    public ComboAdapter(Context context, List<CategoryModel> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        CategoryModel item = getItem(position);

        if (item != null) {
            textView.setText(item.getName_category());
        }

        return convertView;
    }
}


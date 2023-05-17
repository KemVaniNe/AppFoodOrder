package com.example.foodorderapp.NewAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.foodorderapp.NewModel.Category;

import java.util.List;

public class ComboAdapter extends ArrayAdapter<Category> {

    public ComboAdapter(Context context, List<Category> items) {
        super(context, 0, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        Category item = getItem(position);

        if (item != null) {
            textView.setText(item.getName());
        }

        return convertView;
    }
}


package com.example.foodorderapp.View.Admin;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodorderapp.R;


public class navAdminFoodFragment extends Fragment {

  // private AppCompatButton btnTest;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_nav_admin_food, container, false);

     /*   btnTest = view.findViewById(R.id.btn_newCategories);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navAdminFoodFragment_to_navAdminDetailFoodFragment);
            }
        });*/
        return view;
    }
}
package com.example.foodorderapp.View.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodorderapp.Adapter.CatetoryAdapter;
import com.example.foodorderapp.Adapter.FoodAdapter;
import com.example.foodorderapp.Adapter.PosterAdapter;

import com.example.foodorderapp.Model.CategoryModel;
import com.example.foodorderapp.Model.FoodModel;
import com.example.foodorderapp.Model.Poster;
import com.example.foodorderapp.R;
import com.example.foodorderapp.View.Admin.AdminFoodManagerFragment;
import com.example.foodorderapp.View.Share.LoginActivity;
import com.example.foodorderapp.databinding.FragmentUserMainBinding;
import com.example.foodorderapp.listener.CategoryListener;
import com.example.foodorderapp.listener.FoodListener;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class UserMainFragment extends Fragment  {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public UserMainFragment() {
    }

    public static UserMainFragment newInstance(String param1, String param2) {
        UserMainFragment fragment = new UserMainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_main, container, false);
    }
}
package com.example.foodorderapp.View.Admin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodorderapp.Adapter.UserAdminAdapter;
import com.example.foodorderapp.Model.UserModel;

import com.example.foodorderapp.databinding.FragmentAdminUserManagerBinding;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminUserManagerFragment extends Fragment {
    private FragmentAdminUserManagerBinding binding;
    private PreferenceManeger preferenceManeger;
    FirebaseFirestore database;
    private UserAdminAdapter userAdminAdapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminUserManagerFragment() {
    }

    public static AdminUserManagerFragment newInstance(String param1, String param2) {
        AdminUserManagerFragment fragment = new AdminUserManagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        binding = FragmentAdminUserManagerBinding.inflate(inflater, container, false);
        preferenceManeger = new PreferenceManeger(getActivity());
        database = FirebaseFirestore.getInstance();

        loadUserDetails();
        loadUser();
        return binding.getRoot();
    }
    private void loadUserDetails(){
        binding.tvUsername.setText(preferenceManeger.getSrting(Contants.KEY_USERNAME));
        binding.imgAvatar.setImageBitmap(getAvatarImage(preferenceManeger.getSrting(Contants.KEY_IMAGE_USER)));
    }
    private Bitmap getAvatarImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
    void loadUser(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Contants.KEY_COLEECTION_USERS)
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        List<UserModel> userModels = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            UserModel userModel = new UserModel();
                            userModel.setName(queryDocumentSnapshot.getString(Contants.KEY_USERNAME));
                            userModel.setPass(queryDocumentSnapshot.getString(Contants.KEY_PASSWORD));
                            userModels.add(userModel);
                        }
                        if(userModels.size() >0){
                            userAdminAdapter = new UserAdminAdapter(userModels);
                            binding.recyclerview.setAdapter(userAdminAdapter);
                            binding.recyclerview.setVisibility(View.VISIBLE);
                        }else{
                            Toast.makeText(getActivity(), "recyclerviewfood1", Toast.LENGTH_LONG);
                        }
                    }else{
                        Toast.makeText(getActivity(), "recyclerviewfood2", Toast.LENGTH_LONG);
                    }
                });
    }
}
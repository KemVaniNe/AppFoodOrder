package com.example.foodorderapp.View.Admin;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodorderapp.Adapter.FoodAdminAdapter;
import com.example.foodorderapp.Adapter.UserAdminAdapter;
import com.example.foodorderapp.Model.FoodModel;
import com.example.foodorderapp.R;
import com.example.foodorderapp.Model.UserModel;
import com.example.foodorderapp.databinding.FragmentAdminUserManagerBinding;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminUserManagerFragment extends Fragment {
    private FragmentAdminUserManagerBinding binding ;
    private PreferenceManeger preferenceManeger ;
    private List<UserModel> listUser;
    private UserAdminAdapter adapter;
    private ProgressDialog pd;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminUserManagerBinding.inflate(inflater, container, false );
        preferenceManeger = new PreferenceManeger(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        pd = new ProgressDialog(getContext());
        loadUserDetails();
        loadUser();
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        binding.recyclerview.addItemDecoration(itemDecoration);

        listUser = new ArrayList<>();

        binding.btnSearchUserAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchUser();
            }
        });

        return binding.getRoot();
    }

    private void searchUser()
    {
        String textSearch = binding.edtSearchUserAdmin.getText().toString().trim().toLowerCase();
        pd.setTitle("Searching user...");
        pd.show();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listUser = new ArrayList<>();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null){
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            UserModel userModel = new UserModel();
                            userModel.setName(queryDocumentSnapshot.getString(Contants.KEY_USERNAME));
                            userModel.setAvatar(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_USER));
                            listUser.add(userModel);
                        }
                        if(listUser.size() >0){
                            List<UserModel> newList = new ArrayList<>();
                            for (UserModel food : listUser) {
                                if (food.getName().toLowerCase().contains(textSearch)) {
                                    newList.add(food);
                                }
                            }
                            pd.dismiss();
                            adapter = new UserAdminAdapter(newList);
                            binding.recyclerview.setAdapter(adapter);
                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG","fail user list: " + e.getMessage());
                    }
                });
    }
    private void loadUserDetails(){
        binding.tvUsername.setText(preferenceManeger.getSrting(Contants.KEY_USERNAME));
        binding.imgAvatar.setImageBitmap(getAvatarImage(preferenceManeger.getSrting(Contants.KEY_IMAGE_USER)));
    }
    private Bitmap getAvatarImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
    public void loadUser()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listUser = new ArrayList<>();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful() && task.getResult() != null){
                       for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                           UserModel userModel = new UserModel();
                           userModel.setName(queryDocumentSnapshot.getString(Contants.KEY_USERNAME));
                           userModel.setAvatar(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_USER));
                           listUser.add(userModel);
                       }
                       if(listUser.size() >0){
                           adapter = new UserAdminAdapter(listUser);
                           binding.recyclerview.setAdapter(adapter);
                       }
                   }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("DEBUG","fail user list: " + e.getMessage());
                    }
                });
    }
}
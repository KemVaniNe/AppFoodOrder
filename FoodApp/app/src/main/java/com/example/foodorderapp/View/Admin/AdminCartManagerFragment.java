package com.example.foodorderapp.View.Admin;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.foodorderapp.Adapter.CartManagerAdapter;
import com.example.foodorderapp.Adapter.DetailHistoryAdapter;
import com.example.foodorderapp.Adapter.HistoryAdapter;
import com.example.foodorderapp.Adapter.UserAdminAdapter;
import com.example.foodorderapp.Model.FoodModel;
import com.example.foodorderapp.Model.FoodOrderModel;
import com.example.foodorderapp.Model.HistoryModel;
import com.example.foodorderapp.Model.UserModel;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentAdminAccountManagerBinding;
import com.example.foodorderapp.databinding.FragmentAdminCartManagerBinding;
import com.example.foodorderapp.listener.DetailHistoryListener;
import com.example.foodorderapp.listener.HistoryListener;
import com.example.foodorderapp.listener.UserHistoryListener;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminCartManagerFragment extends Fragment implements HistoryListener , UserHistoryListener {

    private List<UserModel> listUser;
    private CartManagerAdapter adapter;
    private HistoryAdapter historyAdapter ;
    private DetailHistoryAdapter detailHistoryAdapter;
    private FragmentAdminCartManagerBinding binding ;
    private FirebaseFirestore database ;
    private PreferenceManeger preferenceManeger;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminCartManagerBinding.inflate(inflater, container, false);
        preferenceManeger = new PreferenceManeger(getActivity());
        database = FirebaseFirestore.getInstance();
        loadUserDetails();
        loadUser();

        binding.btnSearchCartAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchCart();
            }
        });
        return binding.getRoot();
    }

    private void searchCart()
    {
        String textSearch = binding.edtSearchCartAdmin.getText().toString().trim().toLowerCase();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listUser = new ArrayList<>();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null){
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            UserModel userModel = new UserModel();
                            userModel.setId(queryDocumentSnapshot.getId());
                            userModel.setName(queryDocumentSnapshot.getString(Contants.KEY_USERNAME));
                            listUser.add(userModel);
                        }
                        if(listUser.size() >0){
                            List<UserModel> newList = new ArrayList<>();
                            for (UserModel food : listUser) {
                                if (food.getName().toLowerCase().contains(textSearch)) {
                                    newList.add(food);
                                }
                            }
                            adapter = new CartManagerAdapter(newList, this);
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
    public void loadUser()
    {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        listUser = new ArrayList<>();
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null){
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            UserModel userModel = new UserModel();
                            userModel.setId(queryDocumentSnapshot.getId());
                            userModel.setName(queryDocumentSnapshot.getString(Contants.KEY_USERNAME));
                            listUser.add(userModel);
                        }
                        if(listUser.size() >0){
                            adapter = new CartManagerAdapter(listUser, this);
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
    private Bitmap getAvatarImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }

    @Override
    public void ViewClick(String id) {
        View viewdialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet,null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(viewdialog);
        bottomSheetDialog.show();
        database.collection(Contants.KEY_COLEECTION_ORDER_DETAIL)
                .whereEqualTo(Contants.KEY_ID_ORDER, id)
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        List<FoodOrderModel> orderModels = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            FoodOrderModel orderModel = new FoodOrderModel();
                            orderModel.setId(queryDocumentSnapshot.getId());
                            orderModel.setId_foodorder(queryDocumentSnapshot.getString(Contants.KEY_ID_FOOD));
                            orderModel.setNumber(Integer.parseInt(queryDocumentSnapshot.getString("Number")));
                            orderModels.add(orderModel);
                        }
                        if(orderModels.size() >0){
                            database.collection(Contants.KEY_COLEECTION_FOODS)
                                    .get()
                                    .addOnCompleteListener(Task->{
                                        if(Task.isSuccessful() && Task.getResult() != null){
                                            List<FoodOrderModel> foodOrderModels = new ArrayList<>();
                                            for(FoodOrderModel i : orderModels){
                                                for(QueryDocumentSnapshot queryDocumentSnapshot : Task.getResult()){
                                                    if(queryDocumentSnapshot.getId().equals(i.getId_foodorder())){
                                                        FoodOrderModel foodOrderModel = new FoodOrderModel();
                                                        foodOrderModel.setId(i.getId_foodorder());
                                                        foodOrderModel.setNameFood(queryDocumentSnapshot.getString(Contants.KEY_NAME_FOOD));
                                                        foodOrderModel.setPrice(Integer.parseInt(queryDocumentSnapshot.getString(Contants.KEY_PRICE_FOOD)));
                                                        foodOrderModel.setImageFood(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_FOOD));
                                                        foodOrderModel.setNumber(i.getNumber());
                                                        foodOrderModels.add(foodOrderModel);
                                                    }
                                                }
                                            }
                                            if(foodOrderModels.size() >0){
                                                detailHistoryAdapter = new DetailHistoryAdapter(foodOrderModels,null , preferenceManeger.getSrting(Contants.KEY_ROLE_USER));
                                                RecyclerView recyclerView = viewdialog.findViewById(R.id.rcv_detailhistories);
                                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                                                recyclerView.setNestedScrollingEnabled(true);
                                                recyclerView.setLayoutManager(linearLayoutManager);
                                                recyclerView.setAdapter(detailHistoryAdapter);
                                            }else{
                                                showToast("Error recyclerviewfood1");
                                            }
                                        }else{
                                            showToast("Error recyclerviewfood2");
                                        }
                                    });
                        }else{
                            showToast("Error recyclerviewfood1");
                        }
                    }else{
                        showToast("Error recyclerviewfood2");
                    }
                });
    }

    @Override
    public void DelClick(String id) {

    }
    @Override
    public void DetailUserHistoryClick(String id) {
        View viewdialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet,null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(viewdialog);
        bottomSheetDialog.show();
        database.collection(Contants.KEY_COLEECTION_ORDER)
                .whereEqualTo(Contants.KEY_USER_ID, id)
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null){
                        List<HistoryModel> historyModels = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(queryDocumentSnapshot.getBoolean(Contants.KEY_STATUS_ORDER)){
                                HistoryModel historyModel = new HistoryModel();
                                historyModel.setStatus(queryDocumentSnapshot.getBoolean(Contants.KEY_STATUS_ORDER));
                                historyModel.setId(queryDocumentSnapshot.getId());
                                historyModel.setCreateAt(queryDocumentSnapshot.getString("CreateAt"));
                                historyModel.setTotalPrice(Integer.parseInt(queryDocumentSnapshot.getString("Total")));
                                historyModels.add(historyModel);
                            }
                        }
                        if(historyModels.size() > 0){
                            historyAdapter = new HistoryAdapter(historyModels, this , preferenceManeger.getSrting(Contants.KEY_ROLE_USER));
                            RecyclerView recyclerView = viewdialog.findViewById(R.id.rcv_detailhistories);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                            recyclerView.setNestedScrollingEnabled(true);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(historyAdapter);
                        }
                    }
                }).addOnFailureListener(exception ->{
                    showToast("Error");
                });
    }
    private void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
    }

}
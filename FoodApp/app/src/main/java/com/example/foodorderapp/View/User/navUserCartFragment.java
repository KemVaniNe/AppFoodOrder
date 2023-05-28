package com.example.foodorderapp.View.User;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foodorderapp.Adapter.DetailHistoryAdapter;
import com.example.foodorderapp.Adapter.HistoryAdapter;
import com.example.foodorderapp.Model.FoodOrderModel;
import com.example.foodorderapp.Model.HistoryModel;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentNavUserAccountBinding;
import com.example.foodorderapp.databinding.FragmentNavUserCartBinding;
import com.example.foodorderapp.listener.DetailHistoryListener;
import com.example.foodorderapp.listener.HistoryListener;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class navUserCartFragment extends Fragment  implements HistoryListener, DetailHistoryListener {

    FragmentNavUserCartBinding binding;
    private HistoryAdapter adapter;
    private PreferenceManeger preferenceManeger;
    FirebaseFirestore database ;
    private DetailHistoryAdapter detailHistoryAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentNavUserCartBinding.inflate(inflater, container, false);
        preferenceManeger = new PreferenceManeger(getActivity());
        database = FirebaseFirestore.getInstance();
        loadUser();
        loadHistory();
        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();}
        });

        return binding.getRoot();
    }
    private void loadUser(){
        binding.imgAvatar.setImageBitmap(getAvatarImage(preferenceManeger.getSrting(Contants.KEY_IMAGE_USER)));
    }

    private void loadHistory(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerview.setLayoutManager(linearLayoutManager);

        database.collection(Contants.KEY_COLEECTION_ORDER)
                .whereEqualTo(Contants.KEY_USER_ID, preferenceManeger.getSrting(Contants.KEY_USER_ID))
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
                            adapter = new HistoryAdapter(historyModels, this , preferenceManeger.getSrting(Contants.KEY_ROLE_USER));
                            binding.recyclerview.setAdapter(adapter);
                        }
                    }
                }).addOnFailureListener(exception ->{
                    showToast("Error");
                });
    }
    private void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
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
                                                detailHistoryAdapter = new DetailHistoryAdapter(foodOrderModels,this, preferenceManeger.getSrting(Contants.KEY_ROLE_USER));
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
        database.collection(Contants.KEY_COLEECTION_ORDER_DETAIL)
                .whereEqualTo(Contants.KEY_ID_ORDER, id)
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            database.collection(Contants.KEY_COLEECTION_ORDER_DETAIL)
                                    .document(queryDocumentSnapshot.getId())
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            showToast("Deleted!");
                                        }
                                    });
                        }

                    }else{
                        showToast("Error recyclerviewfood2");
                    }
                });
        database.collection(Contants.KEY_COLEECTION_ORDER)
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                          @Override
                                          public void onSuccess(Void unused) {
                                              showToast("Đã xoá thành công");
                                          }
                                      }

                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast("Xoá thất bại");
                    }
                });
        loadHistory();
    }
    private Bitmap getAvatarImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
    @Override
    public void evaluationClick(String idfood) {
        String iduser = preferenceManeger.getSrting(Contants.KEY_USER_ID);
        RateUsDialog rateUsDialog = new RateUsDialog(getContext() , idfood ,iduser);
        rateUsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(com.google.android.material.R.color.abc_decor_view_status_guard)));
        rateUsDialog.setCancelable(false);
        rateUsDialog.show();

    }
}
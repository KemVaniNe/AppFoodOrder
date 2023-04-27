package com.example.foodorderapp.View.User;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.foodorderapp.Adapter.CartAdapter;
import com.example.foodorderapp.Model.FoodOrderModel;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentUserCartBinding;
import com.example.foodorderapp.listener.OrderAddorSubListener;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserCartFragment extends Fragment implements OrderAddorSubListener {
    FragmentUserCartBinding binding;
    private CartAdapter cartAdapter;
    private PreferenceManeger preferenceManeger;
    FirebaseFirestore database ;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public UserCartFragment() {

    }

    public static UserCartFragment newInstance(String param1, String param2) {
        UserCartFragment fragment = new UserCartFragment();
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
        binding = FragmentUserCartBinding.inflate(inflater, container, false);
        preferenceManeger = new PreferenceManeger(getActivity());
        database = FirebaseFirestore.getInstance();
        loadCart();
        Listener();
        return binding.getRoot();
    }


    public  void Listener(){
        Date dnow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy.MM.dd ");

        binding.tvOrder.setOnClickListener(v->{
            Map<String, Object> updates = new HashMap<>();
            updates.put(Contants.KEY_STATUS_ORDER, true );
            updates.put("CreateAt" , ft.format(dnow) );
            updates.put("Total" , binding.tvTotal.getText());
            DocumentReference documentReference =
                    database.collection(Contants.KEY_COLEECTION_ORDER).document(
                            preferenceManeger.getSrting(Contants.KEY_ID_ORDER)
                    );
            documentReference.update(updates)
                    .addOnSuccessListener(unused->showToast("Đặt món thành công!"))
                    .addOnFailureListener(e->showToast("Đặt món thất bại!"));
            preferenceManeger.Remove(Contants.KEY_ID_ORDER);
            openDialog(Gravity.CENTER);

        });
    }
    public void openDialog(int gravity){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_checkout_layout);
        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        WindowManager.LayoutParams windowatrributes = window.getAttributes();
        windowatrributes.gravity = gravity;
        window.setAttributes(windowatrributes);

        if(Gravity.BOTTOM == gravity){
            dialog.setCancelable(true);
        }else{
            dialog.setCancelable(false);
        }
        dialog.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserMainActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        dialog.show();
    }
    public void loadCart(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        binding.rclFoodOrdered.setLayoutManager(linearLayoutManager);

        database.collection(Contants.KEY_COLEECTION_ORDER_DETAIL)
                .whereEqualTo(Contants.KEY_ID_ORDER, preferenceManeger.getSrting(Contants.KEY_ID_ORDER))
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
                            loadFoodOrder(orderModels);
                        }else{
                            showToast("Error recyclerviewfood1");
                        }
                    }else{
                        showToast("Error recyclerviewfood2");
                    }
                });
    }

    private void loadFoodOrder(List<FoodOrderModel> orderModels){

        database.collection(Contants.KEY_COLEECTION_FOODS)
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        List<FoodOrderModel> foodOrderModels = new ArrayList<>();
                        int totalprice= 0 ;
                        for(FoodOrderModel i : orderModels){
                            for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                if(queryDocumentSnapshot.getId().equals(i.getId_foodorder())){
                                    FoodOrderModel foodOrderModel = new FoodOrderModel();
                                    foodOrderModel.setId(i.getId());
                                    foodOrderModel.setNameFood(queryDocumentSnapshot.getString(Contants.KEY_NAME_FOOD));
                                    foodOrderModel.setPrice(Integer.parseInt(queryDocumentSnapshot.getString(Contants.KEY_PRICE_FOOD)));
                                    foodOrderModel.setImageFood(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_FOOD));
                                    foodOrderModel.setNumber(i.getNumber());
                                    foodOrderModels.add(foodOrderModel);
                                    totalprice += foodOrderModel.getPrice()*foodOrderModel.getNumber();
                                }
                            }
                        }
                        if(foodOrderModels.size() >0){
                            cartAdapter = new CartAdapter(foodOrderModels, this , totalprice);
                            binding.rclFoodOrdered.setAdapter(cartAdapter);
                            binding.tvTotal.setText(String.valueOf(totalprice));
                            binding.tvItem.setText(String.valueOf(orderModels.size()));
                            binding.rclFoodOrdered.setVisibility(View.VISIBLE);
                            binding.tvMessage.setVisibility(View.GONE);
                        }else{
                            showToast("Error recyclerviewfood1");
                        }
                    }else{
                        showToast("Error recyclerviewfood2");
                    }
                });

    }

    private void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
    }

    @Override
    public void AddorSubClick( int totalprice , int number,String id_orderdetail) {
        binding.tvTotal.setText(String.valueOf(totalprice));
        cartAdapter.setTotalprice(totalprice);
        DocumentReference documentReference =
                database.collection(Contants.KEY_COLEECTION_ORDER_DETAIL).document(
                        id_orderdetail
                );
        documentReference.update("Number",String.valueOf(number) )
                .addOnSuccessListener(unused->showToast("Thêm số lượng thành công"))
                .addOnFailureListener(e->showToast("Thêm số lượng thất bại!"));
    }


}
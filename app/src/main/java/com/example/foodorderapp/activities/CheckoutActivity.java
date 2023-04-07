package com.example.foodorderapp.activities;

 import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

 import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

 import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.CartAdapter;
import com.example.foodorderapp.adapter.FoodAdapter;
import com.example.foodorderapp.databinding.ActivityCheckoutBinding;
import com.example.foodorderapp.listener.OrderAddorSubListener;
import com.example.foodorderapp.model.FoodModel;
import com.example.foodorderapp.model.FoodOrderModel;
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

public class CheckoutActivity extends AppCompatActivity implements OrderAddorSubListener {
    ActivityCheckoutBinding binding;
    private CartAdapter cartAdapter;
    private PreferenceManeger preferenceManeger;
    FirebaseFirestore database ;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManeger = new PreferenceManeger(getApplicationContext());
        database = FirebaseFirestore.getInstance();
        loadCart();
        Listener();
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
        });
    }
    public void loadCart(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
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
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
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
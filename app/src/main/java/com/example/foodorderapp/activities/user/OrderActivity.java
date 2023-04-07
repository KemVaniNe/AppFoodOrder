 package com.example.foodorderapp.activities.user;
import androidx.annotation.NonNull;
 import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

 import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

 import com.example.foodorderapp.R;
import com.example.foodorderapp.adapter.DetailHistoryAdapter;
import com.example.foodorderapp.adapter.HistoryAdapter;
import com.example.foodorderapp.databinding.ActivityOrderBinding;
import com.example.foodorderapp.listener.HistoryListener;
import com.example.foodorderapp.model.FoodOrderModel;
import com.example.foodorderapp.model.HistoryModel;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;
import java.util.List;

public class OrderActivity extends AppCompatActivity implements HistoryListener {
 ActivityOrderBinding binding;
 private HistoryAdapter adapter;
 private PreferenceManeger preferenceManeger;
 FirebaseFirestore database ;
 private DetailHistoryAdapter detailHistoryAdapter;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
     binding = ActivityOrderBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());
     preferenceManeger = new PreferenceManeger(getApplicationContext());
     database = FirebaseFirestore.getInstance();
     loadHistory();
 }

 private void loadHistory(){
     LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
     binding.rcvHistory.setLayoutManager(linearLayoutManager);

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
                         adapter = new HistoryAdapter(historyModels, this);
                         binding.rcvHistory.setAdapter(adapter);
                     }
                 }
             }).addOnFailureListener(exception ->{
                 showToast("Error");
             });
 }
 private void showToast(String message){
     Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
 }

 @Override
 public void ViewClick(String id) {
     View viewdialog = getLayoutInflater().inflate(R.layout.layout_bottom_sheet,null);
     BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
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
                                         List<FoodOrderModel> foodOrderModels = new ArrayList<>();                                         for(FoodOrderModel i : orderModels){
                                             for(QueryDocumentSnapshot queryDocumentSnapshot : Task.getResult()){
                                                 if(queryDocumentSnapshot.getId().equals(i.getId_foodorder())){
                                                     FoodOrderModel foodOrderModel = new FoodOrderModel();
                                                     foodOrderModel.setId(i.getId());
                                                     foodOrderModel.setNameFood(queryDocumentSnapshot.getString(Contants.KEY_NAME_FOOD));
                                                     foodOrderModel.setPrice(Integer.parseInt(queryDocumentSnapshot.getString(Contants.KEY_PRICE_FOOD)));
                                                     foodOrderModel.setImageFood(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_FOOD));
                                                     foodOrderModel.setNumber(i.getNumber());     
                                                     foodOrderModels.add(foodOrderModel);
                                                 }
                                             }
                                         }
                                         if(foodOrderModels.size() >0){
                                             detailHistoryAdapter = new DetailHistoryAdapter(foodOrderModels);
                                             RecyclerView recyclerView = viewdialog.findViewById(R.id.rcv_detailhistories);
                                             LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
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
 }
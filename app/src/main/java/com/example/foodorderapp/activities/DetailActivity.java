 package com.example.foodorderapp.activities;
 import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
 import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

 import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ActivityDetailBinding;
import com.example.foodorderapp.model.FoodModel;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
  ActivityDetailBinding binding;
  private FoodModel foodModel;
  private PreferenceManeger preferenceManeger;
  FirebaseFirestore database ;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);

      binding = ActivityDetailBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());
      preferenceManeger = new PreferenceManeger(getApplicationContext());
      database = FirebaseFirestore.getInstance();
      loadDetail();
      Listener();
  }
  private void loadDetail(){
      foodModel = (FoodModel) getIntent().getSerializableExtra("FOOD");
      binding.tvNameFood.setText(foodModel.getName());
      binding.tvPriceFood.setText(foodModel.getPrice());
      binding.tvDetail.setText(foodModel.getDetail());
      String encodedImage = foodModel.getImage();
      Bitmap bitmap = getFoodImage(encodedImage);
      binding.imageView.setImageBitmap(bitmap);
  }
  private void Listener(){
      binding.btAdd.setOnClickListener(v->{
          int price = 0;
          int number = Integer.parseInt(binding.tvNumber.getText().toString());
          number += 1;
          price = number*Integer.parseInt(foodModel.getPrice());
          binding.tvPriceFood.setText(String.valueOf(price));
          binding.tvNumber.setText(String.valueOf(number));
      });
      binding.btSub.setOnClickListener(v->{
          int number = Integer.parseInt(binding.tvNumber.getText().toString());
          int price = 0;
          if(number>0){
              number -= 1;
              price = number*Integer.parseInt(foodModel.getPrice());
              binding.tvPriceFood.setText(String.valueOf(price));
          }
          binding.tvNumber.setText(String.valueOf(number));
      });
      binding.tvAddnewOrder.setOnClickListener(v->addNewOrderFood());
  }

  private Bitmap getFoodImage(String encodedImage){
      byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
      return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
  }

  public void addNewOrder(){
      HashMap<String , Object> neworder = new HashMap<>();
      neworder.put(Contants.KEY_STATUS_ORDER, false);
      neworder.put(Contants.KEY_USER_ID, preferenceManeger.getSrting(Contants.KEY_USER_ID));       
      neworder.put("CreateAt" , "");
      neworder.put("Total" , "");
      database.collection(Contants.KEY_COLEECTION_ORDER)
              .add(neworder)
              .addOnSuccessListener(documentReference -> {
                  preferenceManeger.putString(Contants.KEY_ID_ORDER, documentReference.getId());   

              });
  }

  private void addNewOrderFood() {
      if(preferenceManeger.getSrting(Contants.KEY_ID_ORDER) == null){
          addNewOrder();
      }
      HashMap<String , Object> order_detail = new HashMap<>();
      order_detail.put(Contants.KEY_ID_ORDER, preferenceManeger.getSrting(Contants.KEY_ID_ORDER)); 
      order_detail.put(Contants.KEY_ID_FOOD,foodModel.getId_food());
      database.collection(Contants.KEY_COLEECTION_ORDER_DETAIL)
              .whereEqualTo(Contants.KEY_ID_ORDER , preferenceManeger.getSrting(Contants.KEY_ID_ORDER))
              .get()
              .addOnCompleteListener(Task -> {
                  if(Task.isSuccessful() && Task.getResult() != null){
                      boolean add = true;
                      String id_detailorder = "" ;
                      int soluong = 0 ;
                      for(QueryDocumentSnapshot queryDocumentSnapshot: Task.getResult()){

                          if(queryDocumentSnapshot.getString(Contants.KEY_ID_FOOD).equals(foodModel.getId_food())){
                              add = false;
                              id_detailorder = queryDocumentSnapshot.getId();
                              soluong = Integer.parseInt(queryDocumentSnapshot.getString("Number"));
                              break;
                          }
                      }
                      if(add){
                          HashMap<String , Object> newfood = new HashMap<>();
                          newfood.put("Number", binding.tvNumber.getText());
                          newfood.put(Contants.KEY_ID_FOOD,foodModel.getId_food());
                          newfood.put(Contants.KEY_ID_ORDER , preferenceManeger.getSrting(Contants.KEY_ID_ORDER));
                          database.collection(Contants.KEY_COLEECTION_ORDER_DETAIL)
                                  .add(newfood)
                                  .addOnSuccessListener(documentReference -> {
                                  });
                      }else{
                          DocumentReference documentReference =
                                  database.collection(Contants.KEY_COLEECTION_ORDER_DETAIL).document(
                                          id_detailorder
                                  );
                          documentReference.update("Number",String.valueOf(soluong + Integer.parseInt((String) binding.tvNumber.getText())) )
                                  .addOnSuccessListener(unused->showToast("Đặt món thành công!"))  
                                  .addOnFailureListener(e->showToast("Đặt món thất bại!"));
                      }
                  }
              });
  }
  private void showToast(String message){
      Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
     }
 }
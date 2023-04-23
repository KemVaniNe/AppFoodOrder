package com.example.foodorderapp.activities.user;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.foodorderapp.adapter.EvaluetionAdapter;
import com.example.foodorderapp.adapter.FoodAdapter;
import com.example.foodorderapp.databinding.ActivityDetailBinding;
import com.example.foodorderapp.model.CommentModel;
import com.example.foodorderapp.model.FoodModel;
import com.example.foodorderapp.model.FoodOrderModel;
import com.example.foodorderapp.model.UserModel;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
  ActivityDetailBinding binding;
  private FoodModel foodModel;
  private PreferenceManeger preferenceManeger;
  FirebaseFirestore database ;
  private EvaluetionAdapter adapter;
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
      LoadComment();
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

  public void LoadComment(){
      LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
      binding.rcvEvaluetion.setLayoutManager(linearLayoutManager);
         database.collection(Contants.KEY_COLEECTION_USERS)
                 .get()
                 .addOnCompleteListener(task->{
                     if(task.isSuccessful() && task.getResult() != null){
                     List<UserModel> userModels = new ArrayList<>();
                     for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                         UserModel userModel = new UserModel();
                         userModel.setName(queryDocumentSnapshot.getString(Contants.KEY_USERNAME));
                         userModel.setAvatar(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_USER));
                         userModel.setId(queryDocumentSnapshot.getId());
                         userModels.add(userModel);
                     }
                     if(userModels.size() >0){
                         database.collection(Contants.KEY_COLEECTION_EVALUETION)
                                 .whereEqualTo(Contants.KEY_ID_FOOD , foodModel.getId_food())
                                 .get()
                                 .addOnCompleteListener(Task -> {
                                     if (Task.isSuccessful() && Task.getResult() != null) {
                                         List<CommentModel> commentModels = new ArrayList<>();
                                         for(UserModel i : userModels){
                                         for (QueryDocumentSnapshot queryDocumentSnapshot : Task.getResult()) {
                                             if(queryDocumentSnapshot.getString(Contants.KEY_USER_ID).equals(i.getId())){
                                                 CommentModel commentModel = new CommentModel();
                                                 commentModel.setId(queryDocumentSnapshot.getId());
                                                 commentModel.setRate(Float.parseFloat(queryDocumentSnapshot.getString(Contants.KEY_RATE_EVALUETION)));
                                                 commentModel.setComment(queryDocumentSnapshot.getString(Contants.KEY_COMMENT_EVALUETION));
                                                 commentModel.setAvatar_user(i.getAvatar());
                                                 commentModel.setName_user(i.getName());
                                                 commentModels.add(commentModel);
                                             }
                                         }
                                         }
                                         if (commentModels.size() > 0) {
                                             float rate = 0 ;
                                             float total = 0;
                                             for ( CommentModel comment: commentModels) {
                                                 rate+= comment.getRate();
                                                 total +=1;
                                             }
                                             float rateus = rate/total;
                                             binding.RatingBar.setRating(rateus);
                                             adapter = new EvaluetionAdapter(commentModels);
                                             binding.rcvEvaluetion.setAdapter(adapter);
                                         } else {
                                             showToast("Error recyclerviewfood1");
                                         }

                                     } else {
                                         showToast("Error recyclerviewfood2");
                                     }
                                 });
                     }
                     }


                 });
  }
  private void showToast(String message){
      Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
     }
 }
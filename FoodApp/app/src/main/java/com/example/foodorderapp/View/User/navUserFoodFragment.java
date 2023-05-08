package com.example.foodorderapp.View.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
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
import com.example.foodorderapp.View.Share.LoginActivity;
import com.example.foodorderapp.databinding.FragmentNavUserFoodBinding;
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

public class navUserFoodFragment extends Fragment implements CategoryListener, FoodListener {
    private FragmentNavUserFoodBinding binding;
    String categoryId="";
    private CatetoryAdapter catetoryAdapter;
    private FoodAdapter foodAdapter;
    private PreferenceManeger preferenceManeger;
    FirebaseFirestore database ;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PosterAdapter posterAdapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public navUserFoodFragment() {
    }


    public static navUserFoodFragment newInstance(String param1, String param2) {
        navUserFoodFragment fragment = new navUserFoodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        binding = FragmentNavUserFoodBinding.inflate(inflater,container,false);
        preferenceManeger = new PreferenceManeger(getActivity());
        database = FirebaseFirestore.getInstance();
        loadUserDetails();
        getToken();
        setListener();
        loadfood();
        LoadidOrder();
        recyclerViewCategory();
        listenModifyUser();
        View view = binding.getRoot();
        viewPager = view.findViewById(R.id.viewpager);
        circleIndicator = view.findViewById(R.id.circle_indicator);

        posterAdapter = new PosterAdapter(getActivity(), getListPoster());
        viewPager.setAdapter(posterAdapter);

        circleIndicator.setViewPager(viewPager);
        posterAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        return view;
    }
    private List<Poster> getListPoster(){
        List<Poster> list = new ArrayList<>();
        list.add(new Poster(R.drawable.poster1));
        list.add(new Poster(R.drawable.poster2));
        list.add(new Poster(R.drawable.poster3));
        list.add(new Poster(R.drawable.poster4));
        list.add(new Poster(R.drawable.poster5));
        return list;
    }
    void loadfood(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerviewfood.setLayoutManager(linearLayoutManager);

        database.collection(Contants.KEY_COLEECTION_FOODS)
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        List<FoodModel> foodModels = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            FoodModel foodModel = new FoodModel();
                            foodModel.setId_food(queryDocumentSnapshot.getId());
                            foodModel.setName(queryDocumentSnapshot.getString(Contants.KEY_NAME_FOOD));
                            foodModel.setPrice(queryDocumentSnapshot.getString(Contants.KEY_PRICE_FOOD));
                            foodModel.setImage(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_FOOD));
                            foodModel.setDetail(queryDocumentSnapshot.getString(Contants.KEY_DETAIL_FOOD));
                            foodModels.add(foodModel);}
                        if(foodModels.size() >0){
                            foodAdapter = new FoodAdapter(foodModels,this);
                            binding.recyclerviewfood.setAdapter(foodAdapter);
                            binding.recyclerviewfood.setVisibility(View.VISIBLE);
                        }else{
                            showToast("Error recyclerviewfood1");
                        }
                    }else{
                        showToast("Error recyclerviewfood2");
                    }
                })
                .addOnFailureListener(e -> {
                    System.out.println(e);
                });
    }

    private void recyclerViewCategory(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerview.setLayoutManager(linearLayoutManager);
        database.collection(Contants.KEY_COLEECTION_CATEGORY)
                .get()
                .addOnCompleteListener(task->{
                    if(task.isSuccessful() && task.getResult() != null){
                        List<CategoryModel> categoryModels = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            CategoryModel categoryModel = new CategoryModel();
                            categoryModel.setId_category(Integer.parseInt(queryDocumentSnapshot.getString(Contants.KEY_ID_CATEGORY)));
                            categoryModel.setName_category(queryDocumentSnapshot.getString(Contants.KEY_NAME_CATEGORY));
                            categoryModel.setImage_category(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_CATEGORY));
                            categoryModels.add(categoryModel);
                        }
                        if(categoryModels.size() >0){
                            catetoryAdapter = new CatetoryAdapter(categoryModels,this);
                            binding.recyclerview.setAdapter(catetoryAdapter);
                        }else{
                            showToast("Error recyclerview1");
                        }
                    }else{
                        showToast("Error recyclerview2");
                    }
                });
    }

    private void setListener(){
        binding.imgAvatar.setOnClickListener(v->Logout());
    }
    private void loadUserDetails(){
        binding.tvUsername.setText(preferenceManeger.getSrting(Contants.KEY_USERNAME));
        binding.imgAvatar.setImageBitmap(getAvatarImage(preferenceManeger.getSrting(Contants.KEY_IMAGE_USER)));
    }
    private void showToast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
    }
    private void getToken(){
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    private void updateToken(String token){
        DocumentReference documentReference =
                database.collection(Contants.KEY_COLEECTION_USERS).document(
                        preferenceManeger.getSrting(Contants.KEY_USER_ID)
                );
        documentReference.update(Contants.KEY_FCM_TOKEN, token)
                .addOnSuccessListener(unused->showToast("Cập nhật Token thành công!"))
                .addOnFailureListener(e->showToast("Cập nhật Token thất bại!"));
    }

    private void Logout(){
        showToast("Logout..");
        DocumentReference documentReference = database.collection(Contants.KEY_COLEECTION_USERS)
                .document(
                        preferenceManeger.getSrting(Contants.KEY_USER_ID)
                );
        HashMap<String , Object> updates = new HashMap<>();
        updates.put(Contants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused->{
                    preferenceManeger.clear();
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                })
                .addOnFailureListener(e->showToast("Đăng xuất thất bại!"));
    }


    @Override
    public void CategoryCLick(CategoryModel categoryModel) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerviewfood.setLayoutManager(linearLayoutManager);
        database.collection(Contants.KEY_COLEECTION_FOODS)
                .whereEqualTo(Contants.KEY_ID_CATEGORY, String.valueOf(categoryModel.getId_category()))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<FoodModel> foodModels = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            FoodModel foodModel = new FoodModel();
                            foodModel.setId_food(queryDocumentSnapshot.getId());
                            foodModel.setName(queryDocumentSnapshot.getString(Contants.KEY_NAME_FOOD));
                            foodModel.setPrice(queryDocumentSnapshot.getString(Contants.KEY_PRICE_FOOD));
                            foodModel.setImage(queryDocumentSnapshot.getString(Contants.KEY_IMAGE_FOOD));
                            foodModel.setDetail(queryDocumentSnapshot.getString(Contants.KEY_DETAIL_FOOD));
                            foodModels.add(foodModel);
                        }
                        if (foodModels.size() > 0) {

                            foodAdapter = new FoodAdapter(foodModels,this);
                            binding.recyclerviewfood.setAdapter(foodAdapter);
                            binding.recyclerviewfood.setVisibility(View.VISIBLE);
                        } else {
                            showToast("Error recyclerviewfood1");
                        }
                    } else {
                        showToast("Error recyclerviewfood2");
                    }
                });

    }
    public void LoadidOrder(){
        database.collection(Contants.KEY_COLEECTION_ORDER)
                .whereEqualTo(Contants.KEY_USER_ID, preferenceManeger.getSrting(Contants.KEY_USER_ID))
                .get()
                .addOnCompleteListener(Task -> {
                    if(Task.isSuccessful() && Task.getResult() != null){
                        int i = 0 ;
                        boolean add = true;
                        for(QueryDocumentSnapshot queryDocumentSnapshot: Task.getResult()){

                            if(!queryDocumentSnapshot.getBoolean(Contants.KEY_STATUS_ORDER)){
                                preferenceManeger.putString(Contants.KEY_ID_ORDER, queryDocumentSnapshot.getId());
                                add = false;
                                break;
                            }
                            i++;
                        }
                        if(add){
                            addNewOrder();

                        }
                    }
                });
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
                    showToast("Đi đến gọi món nào");
                });
    }
    @Override
    public void FoodItemCLick(FoodModel foodModel) {
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
                            newfood.put("Number", "1");
                            newfood.put(Contants.KEY_ID_FOOD,foodModel.getId_food());
                            newfood.put(Contants.KEY_ID_ORDER , preferenceManeger.getSrting(Contants.KEY_ID_ORDER));
                            database.collection(Contants.KEY_COLEECTION_ORDER_DETAIL)
                                    .add(newfood)
                                    .addOnSuccessListener(documentReference -> {
                                        showToast("đã thêm thành công");
                                    });
                        }else{
                            DocumentReference documentReference =
                                    database.collection(Contants.KEY_COLEECTION_ORDER_DETAIL).document(
                                            id_detailorder
                                    );
                            documentReference.update("Number",String.valueOf(soluong + 1) )
                                    .addOnSuccessListener(unused->showToast("Đặt món thành công!"))
                                    .addOnFailureListener(e->showToast("Đặt món thất bại!"));
                        }
                    }
                });
    }
    private void listenModifyUser(){
        database.collection(Contants.KEY_COLEECTION_USERS)
                .addSnapshotListener(eventListener);
    }
    private final EventListener<QuerySnapshot> eventListener = (value, error) ->{
        if(error !=null){
            return;
        }
        if(value != null){

            for(DocumentChange documentChange : value.getDocumentChanges()){
                   if(documentChange.getType() == DocumentChange.Type.MODIFIED){
                       if(documentChange.getDocument().getId().equals(preferenceManeger.getSrting(Contants.KEY_USER_ID))){
                           String newname = documentChange.getDocument().getString(Contants.KEY_USERNAME);
                           String image = documentChange.getDocument().getString(Contants.KEY_IMAGE_USER);
                           binding.imgAvatar.setImageBitmap(getAvatarImage(image));
                           binding.tvUsername.setText(newname);
                           preferenceManeger.Remove(Contants.KEY_USERNAME);
                           preferenceManeger.Remove(Contants.KEY_IMAGE_USER);
                           preferenceManeger.putString(Contants.KEY_USERNAME , newname);
                           preferenceManeger.putString(Contants.KEY_IMAGE_USER, image);
                       }
                   }
            }
        }
    };
    private Bitmap getAvatarImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
    @Override
    public void FoodItemDetailClick(FoodModel food) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("FOOD",(Serializable) food);
        NavController navController = Navigation.findNavController(binding.getRoot());
        navController.navigate(R.id.navUserFoodDetailFragment, bundle);
    }
}
package com.example.foodorderapp.View.Admin;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodorderapp.Adapter.CatetoryAdapter;
import com.example.foodorderapp.Adapter.FoodAdminAdapter;
import com.example.foodorderapp.Model.CategoryModel;
import com.example.foodorderapp.Model.FoodModel;
import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentAdminFoodManagerBinding;
import com.example.foodorderapp.databinding.FragmentUserAccountBinding;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminFoodManagerFragment extends Fragment {
    private FragmentAdminFoodManagerBinding binding;
    private PreferenceManeger preferenceManeger;

    private CatetoryAdapter adapter;
    private FoodAdminAdapter foodAdminAdapter;
    private FirebaseAuth mAuth;
    private CatetoryAdapter catetoryAdapter;

    FirebaseFirestore database;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AdminFoodManagerFragment() {
    }

    public static AdminFoodManagerFragment newInstance(String param1, String param2) {
        AdminFoodManagerFragment fragment = new AdminFoodManagerFragment();
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
        binding = FragmentAdminFoodManagerBinding.inflate(inflater, container, false);
        preferenceManeger = new PreferenceManeger(getActivity());
        database = FirebaseFirestore.getInstance();


        binding.btnNewCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addCategory();
            }
        });

        binding.btnNewFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFood();
            }
        });
        loadUserDetails();
        loadFood();
        loadCategory();
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
    void loadFood(){
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
                            foodModels.add(foodModel);
                        }
                        if(foodModels.size() >0){
                            foodAdminAdapter = new FoodAdminAdapter(foodModels);
                            binding.recyclerviewfood.setAdapter(foodAdminAdapter);
                            binding.recyclerviewfood.setVisibility(View.VISIBLE);
                        }else{
                            Toast.makeText(getActivity(), "recyclerviewfood1", Toast.LENGTH_LONG);
                        }
                    }else{
                        Toast.makeText(getActivity(), "recyclerviewfood2", Toast.LENGTH_LONG);
                    }
                });
    }

    private void loadCategory(){
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
                            catetoryAdapter = new CatetoryAdapter(categoryModels);
                            binding.recyclerview.setAdapter(catetoryAdapter);
                        }else{
                            Toast.makeText(getActivity(), "recyclerviewfood1", Toast.LENGTH_LONG);
                        }
                    }else{
                        Toast.makeText(getActivity(), "recyclerviewfood2", Toast.LENGTH_LONG);
                    }
                });
    }

    public void addFood(){
        AlertDialog.Builder mDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        binding = FragmentAdminFoodManagerBinding.inflate(getLayoutInflater());
        View mView = inflater.inflate(R.layout.add_food, null);
        mDialog.setView(mView);

        AlertDialog dialog = mDialog.create();
        dialog.setCancelable(true);

        Button save = mView.findViewById(R.id.btn_save);
        EditText edtName = mView.findViewById(R.id.edt_name);
        EditText edtPrice = mView.findViewById(R.id.edt_price);
        EditText edtDetail = mView.findViewById(R.id.edt_detail);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   String id = myRef.push().getKey();
                String id = "";
                String name = edtDetail.getText().toString();
                String price = edtPrice.getText().toString();
                String detail = edtDetail.getText().toString();
                String anh = "";
                addFoodToDB(id,name,price,detail,anh);

                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public void addFoodToDB(String id, String name, String price, String detail, String anh )
    {
        HashMap<String , Object> food = new HashMap<>();
        food.put(Contants.KEY_NAME_FOOD,name);
        food.put(Contants.KEY_PRICE_FOOD, price );
        food.put(Contants.KEY_DETAIL_FOOD,detail );
        food.put(Contants.KEY_ID_FOOD, id);
        food.put(Contants.KEY_IMAGE_FOOD,anh );
        database.collection((Contants.KEY_COLEECTION_FOODS))
                .add(food)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(),"Thêm món ăn thành công",Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(exception->{
                    Toast.makeText(getActivity(),"Thêm món ăn thất bại",Toast.LENGTH_SHORT).show();
                });
    }

    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK) {
                    if(result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        try{
                            ContentResolver resolver = requireContext().getContentResolver();
                            InputStream inputStream = resolver.openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            //  encodedImage = encodedImage(bitmap);
                        }catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    private  String encodedImage(Bitmap bitmap){
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    public void addCategory(){
        AlertDialog.Builder mDialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        binding = FragmentAdminFoodManagerBinding.inflate(getLayoutInflater());
        View mView = inflater.inflate(R.layout.add_category, null);
        mDialog.setView(mView);

        AlertDialog dialog = mDialog.create();
        dialog.setCancelable(true);

        Button save = mView.findViewById(R.id.btn_save);
        EditText edtName = mView.findViewById(R.id.edt_name);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //    String id = myRef.push().getKey();
                String name = edtName.getText().toString();
            /*    myRef.child(id).setValue(new Post(id,title,content, getRandomColor()))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Add note successful!",Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Add note fail!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });*/

                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
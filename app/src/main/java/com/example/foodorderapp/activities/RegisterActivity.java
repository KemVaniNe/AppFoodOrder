package com.example.foodorderapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.foodorderapp.databinding.ActivityRegisterBinding;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    private PreferenceManeger preferenceManeger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManeger = new PreferenceManeger(getApplicationContext());
        SetListener();
    }
    private void SetListener(){
        binding.tvSignin.setOnClickListener(v->onBackPressed());
        binding.tvRegiter.setOnClickListener(v->{
            if(isValidRegisterDetail()){
                Register();
            }

        });
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message , Toast.LENGTH_LONG).show();
    }
    private void Register(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        HashMap<String , Object> user = new HashMap<>();
        user.put(Contants.KEY_USERNAME, binding.etUsername.getText().toString());
        user.put(Contants.KEY_PASSWORD, binding.etPassword.getText().toString());
        database.collection((Contants.KEY_COLEECTION_USERS))
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    preferenceManeger.putBoolean(Contants.KEY_IS_LOGINED_IN, true);
                    preferenceManeger.putString(Contants.KEY_USER_ID,documentReference.getId() );
                    preferenceManeger.putString(Contants.KEY_USERNAME , binding.etUsername.getText().toString());
                    preferenceManeger.putString(Contants.KEY_PASSWORD, binding.etPassword.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                })
                .addOnFailureListener(exception->{
                    showToast("Đăng ký thất bại");
                });
    }
//    private void addfood(){
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        HashMap<String , Object> food = new HashMap<>();
//        food.put(Contants.KEY_NAME_FOOD,"Cơm cuộn chiên xù" );
//        food.put(Contants.KEY_PRICE_FOOD,"35.000" );
//        food.put(Contants.KEY_DETAIL_FOOD,"Cơm, đồ trộn" );
//        food.put(Contants.KEY_IMAGE_FOOD, "https://lh3.googleusercontent.com/pw/AMWts8DMvxewerN9npJq8Mteyb9yY5RFpODuhy45wVeiQbEhUeRAwXeHzCpl6Zo9ACCPxPARkKIN9xSGVzjOszVnND9Sqcl0j-YoysScAIKhZAWPI6NEKHvRuh8m8dv8NSGRsZxz8o0xv2tzAQyh0K-zIzqXd7XRuA6ko0eSpY1ymFhuziWf-v2PljQ5wvXjl9YfUccwcTMFssUbdHPnq3OH2_GmyUlSZajSX9kYEW1pRfXcfuAxpXfthEcyJt0vPGQ8JupUbJLkENj-yBtSk9vQBjcM45hOUh6NDaTFEReyE7fZVYnPipZTCfsBJB_APc3bwfVHk9PrpiBtZmwRRpcpIz65ZPSIOAGpUdQflwBy77N4_9h9VzNgtQesmVeXuyQGvBLW_LckXRxYtmQigTL2ySbIYL_OSnV9t-_DvX7oHNBXrKZQYHfNL7i_sirTkLzWpmBNhziFV_dsglCG815O2pmGf_0eDK8uT4r81Mg6v3s5aT3wYgGkOyMJxGeh-kmtBxTKpcFNZsIMoyepJlfNFDvBVqv-uTLtOwr4j9VLHxJlllCMpvLLGkZF6o4NA8sgd9fyBvjPNDkpbO211kMiJBNFBWO69d19uLQ7AV6CvYomxQrpAlAyid9dxJftUt-XABMLfWP1DtvWEAMIrIbC4meSyBtobLfJIbwmbkcNcRVHbWPOfli22EXIg-oDjFnwIKqskzgBLcizIuoQVd2rVYLWBTFKek2S4qtH0IS9_IFLmvotZfOEwDWOzJLIIAvnIRV0lHGCx5jVf_8UoysrB5778pca9NmS5iCYeImKnp6eEFiryKa4ZhLFXI_uq3mpbvYuzluXuuo9uyWb7aNcLTsg0natDPnlTllEbEdqtBot3HmZzslesAw0NrMbeaa6_1XBGBQfSLOrYhkn4rFjDA=w730-h520-s-no?authuser=0");
//        database.collection((Contants.KEY_COLEECTION_FOODS))
//                .add(food)
//                .addOnSuccessListener(documentReference -> {
//
//
//                })
//                .addOnFailureListener(exception->{
//                    showToast("Đăng ký thất bại");
//                });
//    }
    private Boolean isValidRegisterDetail(){
        if(binding.etUsername.getText().toString().trim().isEmpty()){
            showToast("Nhập username");
            return false;
        }else if (!binding.etUsername2.getText().toString().equals(binding.etUsername.getText().toString())){
            showToast("Xác nhận username sai");
            return false;
        }
        else if (binding.etPassword.getText().toString().trim().isEmpty()){
            showToast("Nhập mật khẩu");
            return false;
        }else if(!binding.etPassword2.getText().toString().equals(binding.etPassword.getText().toString())){
            showToast("Xác nhận mật khẩu sai");
            return false;
        }else{
            return true;
        }
    }
}
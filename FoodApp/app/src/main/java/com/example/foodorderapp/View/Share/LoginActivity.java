package com.example.foodorderapp.View.Share;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.foodorderapp.R;
import com.example.foodorderapp.View.Admin.AdminMainActivity;
import com.example.foodorderapp.View.User.UserMainActivity;
import com.example.foodorderapp.databinding.ActivityLoginBinding;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private PreferenceManeger preferenceManeger;
    private FirebaseFirestore database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseFirestore.getInstance();
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        preferenceManeger = new PreferenceManeger(getApplicationContext());
        if(preferenceManeger.getBoolean(Contants.KEY_IS_LOGINED_IN)){
            if(preferenceManeger.getSrting(Contants.KEY_ROLE_USER).equals("User")){
                Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                startActivity(intent);
                finish();
            }else{
                Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                startActivity(intent);
                finish();
            }
        }
        setContentView(binding.getRoot());
        SetListener();
    }
    private void SetListener(){
        binding.btnLogin.setOnClickListener(v->{
            if(isValidLoginDetails()){
                Login();
            }
        });

        binding.btnRegister.setOnClickListener(v->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );
    }
    private void Login(){

        database.collection(Contants.KEY_COLEECTION_USERS)
                .whereEqualTo(Contants.KEY_USERNAME, binding.etUsername.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocumentChanges().size()> 0){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        String hashPassword = documentSnapshot.getString(Contants.KEY_PASSWORD);
                        BCrypt.Result result = BCrypt.verifyer().verify(binding.etPassword.getText().toString().toCharArray() , hashPassword);
                        if(result.verified){
                            preferenceManeger.putBoolean(Contants.KEY_IS_LOGINED_IN, true);
                            preferenceManeger.putString(Contants.KEY_USER_ID, documentSnapshot.getId());
                            preferenceManeger.putString(Contants.KEY_USERNAME, documentSnapshot.getString(Contants.KEY_USERNAME));
                            preferenceManeger.putString(Contants.KEY_PHONE, documentSnapshot.getString(Contants.KEY_PHONE));
                            preferenceManeger.putString(Contants.KEY_IMAGE_USER, documentSnapshot.getString(Contants.KEY_IMAGE_USER));
                            preferenceManeger.putString(Contants.KEY_ROLE_USER , documentSnapshot.getString(Contants.KEY_ROLE_USER));
                            preferenceManeger.putString(Contants.KEY_PASSWORD , binding.etPassword.getText().toString());
                            if(documentSnapshot.getString(Contants.KEY_ROLE_USER).equals("Admin")){
                                Intent intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }else{
                                LoadidOrder();
                                Intent intent = new Intent(getApplicationContext(), UserMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        }else{
                            showToast("Sai mật khẩu!");
                        }
                    }else{
                        showToast("Sai username , mật khẩu!");
                    }
                });
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();

    }
    private Boolean isValidLoginDetails(){
        if(binding.etUsername.getText().toString().isEmpty()){
            showToast("Chưa nhập username");
            return false;
        }else if(binding.etPassword.getText().toString().isEmpty()){
            showToast("Chưa nhập password");
            return false;
        }else{
            return true;
        }
    }
    public void LoadidOrder(){
        database.collection(Contants.KEY_COLEECTION_ORDER)
                .whereEqualTo(Contants.KEY_USER_ID, preferenceManeger.getSrting(Contants.KEY_USER_ID))
                .get()
                .addOnCompleteListener(Task -> {
                    if(Task.isSuccessful() && Task.getResult() != null){
                        for(QueryDocumentSnapshot queryDocumentSnapshot: Task.getResult()){

                            if(!queryDocumentSnapshot.getBoolean(Contants.KEY_STATUS_ORDER)){
                                preferenceManeger.putString(Contants.KEY_ID_ORDER, queryDocumentSnapshot.getId());

                                break;
                            }

                        }

                    }
                });
    }
}
package com.example.foodorderapp.activities.shared;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.ActivityAccountManagerBinding;
import com.example.foodorderapp.databinding.ActivityUserManagerBinding;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class AccountManagerActivity extends AppCompatActivity {
    private ActivityAccountManagerBinding binding;
    private PreferenceManeger preferenceManeger;

    private FirebaseAuth mAuth;
    private DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountManagerBinding.inflate(getLayoutInflater());
        View viewRoot = binding.getRoot();
        setContentView(viewRoot);
        preferenceManeger = new PreferenceManeger(getApplicationContext());

        binding.btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePass();
            }
        });

        binding.btnUpdateInfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateInfor();
            }
        });
    }
    public void UpdatePass()
    {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View mView = inflater.inflate(R.layout.update_pass, null);
        mDialog.setView(mView);

        AlertDialog dialog = mDialog.create();
        dialog.setCancelable(true);

        Button save = mView.findViewById(R.id.btn_save);
        EditText edtNewPass = mView.findViewById(R.id.edt_newPass);
        EditText edtOldPass = mView.findViewById(R.id.edt_oldPass);
        EditText edtConfirmPass = mView.findViewById(R.id.edt_confirmPass);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = myRef.push().getKey();
                String oldPass = edtOldPass.getText().toString();
                String newPass = edtNewPass.getText().toString();
                String confirmPass = edtConfirmPass.getText().toString();
                if(newPass == confirmPass){
                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    HashMap<String, Object> user = new HashMap<>();
                    String user_id = preferenceManeger.getSrting(Contants.KEY_USER_ID);
                //    user.put(Contants.KEY_USERNAME, binding.etUsername3.getText().toString());
                //    user.put(Contants.KEY_PHONE, binding.etUsername4.getText().toString());
                    user.put(Contants.KEY_PASSWORD, newPass);
                    database.collection(Contants.KEY_COLEECTION_USERS)
                            .document(user_id)
                            .update(user)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getApplicationContext(),"Update pass successful!",Toast.LENGTH_SHORT).show();

                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(),"Update pass successful!",Toast.LENGTH_SHORT).show();
                            });
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"New pass và Confirm pass khác nhau!",Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void UpdateInfor()
    {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View mView = inflater.inflate(R.layout.update_infor, null);
        mDialog.setView(mView);

        AlertDialog dialog = mDialog.create();
        dialog.setCancelable(true);

        Button save = mView.findViewById(R.id.btn_save);
        EditText edtUser = mView.findViewById(R.id.edt_userName);
        EditText edtMail = mView.findViewById(R.id.edt_email);
        EditText edtPhone = mView.findViewById(R.id.edt_phone);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = myRef.push().getKey();
                String userName = edtUser.getText().toString();
                String mail = edtMail.getText().toString();
                String phone = edtPhone.getText().toString();
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                HashMap<String, Object> user = new HashMap<>();
                String user_id = preferenceManeger.getSrting(Contants.KEY_USER_ID);
                user.put(Contants.KEY_USERNAME, userName);
                user.put(Contants.KEY_EMAIL, mail);
                user.put(Contants.KEY_PHONE, phone);
                database.collection(Contants.KEY_COLEECTION_USERS)
                        .document(user_id)
                        .update(user)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(getApplicationContext(),"Update infor successful!",Toast.LENGTH_SHORT).show();

                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getApplicationContext(),"Update infor successful!",Toast.LENGTH_SHORT).show();
                        });

                dialog.dismiss();
            }
        });

        dialog.show();
}
}
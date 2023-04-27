package com.example.foodorderapp.View.User;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodorderapp.R;
import com.example.foodorderapp.databinding.FragmentNavUserAccountBinding;
import com.example.foodorderapp.databinding.FragmentUserAccountBinding;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class NavUserAccountFragment extends Fragment {
    private FragmentNavUserAccountBinding binding;
    private FirebaseFirestore database ;
    private PreferenceManeger preferenceManeger;
    private ConstraintLayout btnDonHang, btnQuanLyTaiKhoan, btnSupport;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavUserAccountBinding.inflate(inflater, container, false);
        preferenceManeger = new PreferenceManeger(getActivity());
        database = FirebaseFirestore.getInstance();
        loadUserDetails();
        listenModifyUser();

        binding.btnHoTro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(UserActivity.this, SupportActivity.class);
//                startActivityForResult(intent, 1);
            }
        });

        binding.btnQuanLyTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(UserActivity.this, AccountActivity.class);
//                startActivityForResult(intent, 1);
            }
        });
        binding.btnDonHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(UserActivity.this, OrderActivity.class);
//                startActivityForResult(intent, 1);
            }
        });
        return binding.getRoot();
    }
    private void loadUserDetails(){
        binding.txtuser.setText(preferenceManeger.getSrting(Contants.KEY_USERNAME));
        binding.imgAvatar.setImageBitmap(getAvatarImage(preferenceManeger.getSrting(Contants.KEY_IMAGE_USER)));
    }
    private Bitmap getAvatarImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
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
                System.out.println(documentChange.getDocument().getString(Contants.KEY_USERNAME));
                if(documentChange.getType() == DocumentChange.Type.MODIFIED){
                    String newname = documentChange.getDocument().getString(Contants.KEY_USERNAME);
                    String image = documentChange.getDocument().getString(Contants.KEY_IMAGE_USER);
                    binding.imgAvatar.setImageBitmap(getAvatarImage(image));
                    binding.txtuser.setText(newname);
                    preferenceManeger.Remove(Contants.KEY_USERNAME);
                    preferenceManeger.Remove(Contants.KEY_IMAGE_USER);
                    preferenceManeger.putString(Contants.KEY_USERNAME , newname);
                    preferenceManeger.putString(Contants.KEY_IMAGE_USER, image);
                }
            }
        }
    };
}
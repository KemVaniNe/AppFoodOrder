package com.example.foodorderapp.View.Admin;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.foodorderapp.R;
import com.example.foodorderapp.View.Share.LoginActivity;
import com.example.foodorderapp.databinding.FragmentAdminAccountManagerBinding;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminAccountManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAccountManagerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AdminAccountManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AdminAccountManagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AdminAccountManagerFragment newInstance(String param1, String param2) {
        AdminAccountManagerFragment fragment = new AdminAccountManagerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private FragmentAdminAccountManagerBinding binding;
    private FirebaseFirestore database ;
    private PreferenceManeger preferenceManeger;
    private  String encodedImage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminAccountManagerBinding.inflate(inflater, container, false);
        preferenceManeger = new PreferenceManeger(getActivity());
        database = FirebaseFirestore.getInstance();
        loadUser();
        return binding.getRoot();
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
                            binding.imgAvatar.setImageBitmap(bitmap);
                            encodedImage = encodedImage(bitmap);
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
    private void updateUser(){

        HashMap<String, Object> user = new HashMap<>();
        String user_id = preferenceManeger.getSrting(Contants.KEY_USER_ID);
        user.put(Contants.KEY_USERNAME, binding.etUsername.getText().toString());
        user.put(Contants.KEY_PHONE, binding.etPhone.getText().toString());
        user.put(Contants.KEY_IMAGE_USER, encodedImage);
        database.collection(Contants.KEY_COLEECTION_USERS)
                .document(user_id)
                .update(user)
                .addOnSuccessListener(aVoid -> {
                    showToast("Cập nhật thành công");
                })
                .addOnFailureListener(e -> {
                    showToast("Cập nhật thất bại");
                });

    }
    private void showToast(String message){
        Toast.makeText(getContext(), message , Toast.LENGTH_LONG).show();
    }
    private Boolean isValidRegisterDetail(){
        if(binding.etUsername.getText().toString().trim().isEmpty()){
            showToast("Nhập username");
            return false;
        }else if (binding.etPhone.getText().toString().trim().isEmpty()){
            showToast("Nhập number phone");
            return false;
        }else{
            return true;
        }
    }
    private Bitmap getAvatarImage(String encodeImage){
        byte[] bytes = Base64.decode(encodeImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0 , bytes.length);
    }
    private void loadUser(){
        binding.etUsername.setText(preferenceManeger.getSrting(Contants.KEY_USERNAME));
        binding.etPhone.setText(preferenceManeger.getSrting(Contants.KEY_PHONE));
        binding.imgAvatar.setImageBitmap(getAvatarImage(preferenceManeger.getSrting(Contants.KEY_IMAGE_USER)));
        encodedImage = preferenceManeger.getSrting(Contants.KEY_IMAGE_USER);
        binding.btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidRegisterDetail()){
                    updateUser();
                }
            }
        });
        binding.imgAvatar.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
        binding.btLogout.setOnClickListener(v->Logout());
        binding.btUpdatepass.setOnClickListener(v->openChangePassword(Gravity.BOTTOM));
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
    private void openChangePassword(int gravity){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.change_pass);
        Window window = dialog.getWindow();
        if(window == null){
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.BOTTOM == gravity){
            dialog.setCancelable(true);
        }else{
            dialog.setCancelable(false);
        }
        EditText edoldpass = dialog.findViewById(R.id.ed_oldpass);
        EditText newpass = dialog.findViewById(R.id.ed_newpass);
        EditText cofpass = dialog.findViewById(R.id.edt_confirmPass);
        AppCompatButton bt_cancle = dialog.findViewById(R.id.bt_cancle);
        AppCompatButton bt_update = dialog.findViewById(R.id.bt_update);

        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = true;
                if (!edoldpass.getText().toString().equals(preferenceManeger.getSrting(Contants.KEY_PASSWORD))){
                    showToast("Nhập mật khẩu cũ sai");
                    check= false;
                }
                else if (newpass.getText().toString().trim().isEmpty()){
                    showToast("Nhập mật khẩu mới");
                    check = false;
                }else if(!cofpass.getText().toString().equals(newpass.getText().toString())){
                    showToast("Xác nhận mật khẩu mới sai");
                    check = false;
                }
                if(check){
                    HashMap<String, Object> user = new HashMap<>();
                    String user_id = preferenceManeger.getSrting(Contants.KEY_USER_ID);
                    String hashPassword = BCrypt.withDefaults().hashToString(12,newpass.getText().toString().toCharArray() );

                    user.put(Contants.KEY_PASSWORD, hashPassword);
                    database.collection(Contants.KEY_COLEECTION_USERS)
                            .document(user_id)
                            .update(user)
                            .addOnSuccessListener(aVoid -> {
                                preferenceManeger.Remove(Contants.KEY_PASSWORD);
                                preferenceManeger.putString(Contants.KEY_PASSWORD , newpass.getText().toString());
                                showToast("Cập nhật thành công");
                            })
                            .addOnFailureListener(e -> {
                                showToast("Cập nhật thất bại");
                            });

                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
}
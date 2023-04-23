package com.example.foodorderapp.activities.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.foodorderapp.R;
import com.example.foodorderapp.utilities.Contants;
import com.example.foodorderapp.utilities.PreferenceManeger;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class RateUsDialog extends Dialog {
    private  float userRate = 0;
    private String idfood ;
    private String iduser ;
    public RateUsDialog(@NonNull Context context, String idfood, String iduser) {
        super(context);
        this.idfood = idfood;
        this.iduser = iduser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_us_dialog_layout);
        final AppCompatButton rateNowBtn = findViewById(R.id.btn_rate);
        final AppCompatButton BackBtn = findViewById(R.id.btn_back);
        final RatingBar ratingBar = findViewById(R.id.RatingBar);
        final ImageView imgicon = findViewById(R.id.img_icon);
        final EditText ed_evaluate = findViewById(R.id.ed_evalute);
        rateNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                HashMap<String , Object> evaluetion = new HashMap<>();
                evaluetion.put(Contants.KEY_ID_FOOD , idfood);
                evaluetion.put(Contants.KEY_USER_ID, iduser);
                evaluetion.put(Contants.KEY_RATE_EVALUETION, String.valueOf(ratingBar.getRating()));
                evaluetion.put(Contants.KEY_COMMENT_EVALUETION, ed_evaluate.getText().toString());
                database.collection((Contants.KEY_COLEECTION_EVALUETION))
                        .add(evaluetion)
                        .addOnSuccessListener(documentReference -> {
                           dismiss();
                        })
                        .addOnFailureListener(exception->{
                            System.out.println(exception);
                });
            }
        });
        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(rating <= 1){
                    imgicon.setImageResource(R.drawable.one_star);
                } else if (rating <= 2){

                    imgicon.setImageResource(R.drawable.two_star);
                }else if (rating <= 3){
                    imgicon.setImageResource(R.drawable.three_star);

                }else if( rating <= 4){

                    imgicon.setImageResource(R.drawable.four_star);
                }else{

                    imgicon.setImageResource(R.drawable.five_star);
                }
                userRate = rating;
            }
        });
        animateImage(imgicon);
    }
    private  void animateImage(ImageView ratingImage){
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f,0,1f,
                Animation.RELATIVE_TO_SELF , 0.f,Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ratingImage.startAnimation(scaleAnimation);
    }

}

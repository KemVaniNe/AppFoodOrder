package com.example.foodorderapp.Adapter;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.foodorderapp.Model.Poster;
import com.example.foodorderapp.R;

import java.util.List;

public class PosterAdapter extends PagerAdapter {

    private Context mContext;
    private List<Poster> mListPoster;

    public PosterAdapter(Context mContext, List<Poster> mListPoster)
    {
        this.mContext = mContext;
        this.mListPoster = mListPoster;
    }
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_poster,container,false);
        ImageView imgPhoto = view.findViewById(R.id.img_photo);
        Poster poster = mListPoster.get(position);
        if(poster!=null)
        {
            Glide.with(mContext).load(poster.getResourceId()).into(imgPhoto);
        }

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if(mListPoster!=null)
        {
            return mListPoster.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}

package com.example.foodorderapp.View.User;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodorderapp.Adapter.PosterAdapter;
import com.example.foodorderapp.Model.Poster;
import com.example.foodorderapp.R;
import com.example.foodorderapp.View.Admin.AdminUserManagerFragment;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class UserMainFragment extends Fragment {



    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PosterAdapter posterAdapter;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_main, container, false);

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

}
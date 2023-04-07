package com.example.foodorderapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.databinding.ViewholderHistoryBinding;
import com.example.foodorderapp.listener.HistoryListener;
import com.example.foodorderapp.model.HistoryModel;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private final List<HistoryModel> list ;

    public HistoryAdapter(List<HistoryModel> list, HistoryListener listener) {
        this.list = list;
        this.listener = listener;
    }

    private final HistoryListener listener;

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewholderHistoryBinding binding = ViewholderHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new HistoryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class HistoryViewHolder extends RecyclerView.ViewHolder{
        ViewholderHistoryBinding binding;
        public HistoryViewHolder(@NonNull ViewholderHistoryBinding viewholderHistoryBinding) {
            super(viewholderHistoryBinding.getRoot());
            binding = viewholderHistoryBinding;
        }
        void setData(HistoryModel historyModel){
            if(historyModel.isStatus()){
                binding.stutas.setText("Đã thanh toán");
            }else{
                binding.stutas.setText("Chưa thanh toán");
            }
            binding.tvCreateAt.setText(historyModel.getCreateAt());
            binding.tvTotal.setText(String.valueOf(historyModel.getTotalPrice()));
            binding.btView.setOnClickListener(v->listener.ViewClick(historyModel.getId()));
            binding.btDel.setOnClickListener(v-> listener.DelClick(historyModel.getId()));
        }
    }
}

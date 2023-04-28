package com.example.foodorderapp.Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.Model.HistoryModel;
import com.example.foodorderapp.listener.HistoryListener;
import java.util.List;
import com.example.foodorderapp.databinding.ViewholderUserHistoryBinding;
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
        ViewholderUserHistoryBinding binding = ViewholderUserHistoryBinding.inflate(
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
        ViewholderUserHistoryBinding binding;
        public HistoryViewHolder(@NonNull ViewholderUserHistoryBinding viewholderHistoryBinding) {
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
            binding.btnView.setOnClickListener(v->listener.ViewClick(historyModel.getId()));
            binding.btnDel.setOnClickListener(v-> listener.DelClick(historyModel.getId()));
        }
    }
}

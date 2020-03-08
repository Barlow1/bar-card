package com.nexuscmarketing.bar_card.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nexuscmarketing.bar_card.R;
import com.nexuscmarketing.bar_card.model.Bar;

import java.util.ArrayList;

public class BarListAdapter extends RecyclerView.Adapter<BarListAdapter.BarViewHolder> {
    ArrayList<Bar> barList = new ArrayList<>();
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public BarListAdapter(ArrayList<Bar> barList) {
        this.barList = barList;
    }

    @NonNull
    @Override
    public BarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bar_list_item, parent, false);
        BarViewHolder barViewHolder = new BarViewHolder(view);
        return barViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BarViewHolder holder, int position) {
        TextView barName = holder.barName;
        barName.setText(barList.get(position).getBarName());
    }

    @Override
    public int getItemCount() {
        return barList.size();
    }

    public class BarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView barName;
        public BarViewHolder(@NonNull View itemView) {
            super(itemView);
            this.barName = itemView.findViewById(R.id.bar_list_item_name);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                listener.onItemClick(view, position);
            }
        }
    }
}

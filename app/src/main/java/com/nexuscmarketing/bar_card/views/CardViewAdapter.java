package com.nexuscmarketing.bar_card.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nexuscmarketing.bar_card.R;
import com.nexuscmarketing.bar_card.model.BarCard;

import java.util.ArrayList;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {
    private ArrayList<BarCard> barCardArrayList;

    CardViewAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public void setOnItemClickListener(CardViewAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public CardViewAdapter(ArrayList<BarCard> data) {
        this.barCardArrayList = data;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ImageView barImage = holder.barImage;
        TextView barName = holder.barName;
        TextView punchesRemaining = holder.punchesRemaining;
        TextView reward = holder.reward;
        ImageView deleteButton = holder.deleteButton;

        barImage.setImageResource(barCardArrayList.get(position).getImage());
        barName.setText(barCardArrayList.get(position).getBarName());
        punchesRemaining.setText(barCardArrayList.get(position).getPunchesRemaining().toString());
        reward.setText(barCardArrayList.get(position).getReward());
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return barCardArrayList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        ImageView barImage;
        TextView barName;
        TextView punchesRemaining;
        TextView reward;
        ImageButton deleteButton;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.barImage = itemView.findViewById(R.id.bar_image);
            this.barName = itemView.findViewById(R.id.bar_name);
            this.punchesRemaining = itemView.findViewById(R.id.punches_remaining);
            this.reward = itemView.findViewById(R.id.reward);
            this.deleteButton = itemView.findViewById(R.id.bar_card_delete_button);
        }
    }
}

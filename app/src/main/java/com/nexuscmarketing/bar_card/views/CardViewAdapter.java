package com.nexuscmarketing.bar_card.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nexuscmarketing.bar_card.R;
import com.nexuscmarketing.bar_card.model.BarCard;

import java.util.ArrayList;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {
    private ArrayList<BarCard> barCardArrayList;

    private Context context;

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
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        ArrayList<Integer> punches = new ArrayList<>();
        for (int i = 1; i <= barCardArrayList.get(position).getRewardPunches(); i++) {
            punches.add(i);
        }
        ImageView barImage = holder.barImage;
        ImageView deleteButton = holder.deleteButton;
        RecyclerView punchView = holder.punchView;

        PunchAdapter punchAdapter = new PunchAdapter(punches, barCardArrayList.get(position).getPunches());
        punchView.setHasFixedSize(true);
        punchView.setAdapter(punchAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        punchView.setLayoutManager(layoutManager);
        punchView.setItemAnimator(new DefaultItemAnimator());


        barImage.setImageResource(barCardArrayList.get(position).getImage());
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
        RecyclerView punchView;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.barImage = itemView.findViewById(R.id.bar_image);
            this.deleteButton = itemView.findViewById(R.id.bar_card_delete_button);
            this.punchView = itemView.findViewById(R.id.punches_view);
        }
    }
}

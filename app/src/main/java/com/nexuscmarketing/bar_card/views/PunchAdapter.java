package com.nexuscmarketing.bar_card.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nexuscmarketing.bar_card.R;

import java.util.ArrayList;

public class PunchAdapter extends RecyclerView.Adapter<PunchAdapter.PunchViewHolder> {
    public PunchAdapter(ArrayList<Integer> punches, int punchesRecieved) {
        this.punches = punches;
        this.punchesRecieved = punchesRecieved;
    }

    private ArrayList<Integer> punches;
    private int punchesRecieved;

    @NonNull
    @Override
    public PunchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_punch, parent, false);
        PunchViewHolder punchViewHolder = new PunchViewHolder(view);
        return punchViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PunchViewHolder holder, int position) {
        TextView punch = holder.punch;
        punch.setText(String.valueOf(punches.get(position)));
        if (punches.get(position) <= punchesRecieved) {
            punch.setBackgroundResource(R.drawable.punch_circle_punched);
        }
    }

    @Override
    public int getItemCount() {
        return punches.size();
    }

    public class PunchViewHolder extends RecyclerView.ViewHolder {
        TextView punch;

        public PunchViewHolder(@NonNull View itemView) {
            super(itemView);
            this.punch = itemView.findViewById(R.id.punches);
        }
    }
}

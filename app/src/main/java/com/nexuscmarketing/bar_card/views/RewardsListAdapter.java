package com.nexuscmarketing.bar_card.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nexuscmarketing.bar_card.R;
import com.nexuscmarketing.bar_card.model.Reward;

import java.util.ArrayList;

public class RewardsListAdapter extends RecyclerView.Adapter<RewardsListAdapter.RewardViewHolder> {
    private ArrayList<Reward> rewardArrayList;

    RewardsListAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(RewardsListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public RewardsListAdapter(ArrayList<Reward> data) {
        this.rewardArrayList = data;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reward_layout, parent, false);
        RewardViewHolder rewardViewHolder = new RewardViewHolder(view);
        return rewardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        ImageView barImage = holder.barImage;
        TextView title = holder.title;
        TextView description = holder.description;
        Button claim = holder.claim;


        barImage.setImageResource(rewardArrayList.get(position).getImage());
        title.setText(rewardArrayList.get(position).getTitle());
        description.setText(rewardArrayList.get(position).getDescription());

        claim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return rewardArrayList.size();
    }

    public static class RewardViewHolder extends RecyclerView.ViewHolder {
        ImageView barImage;
        TextView title;
        TextView description;
        Button claim;

        public RewardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.barImage = itemView.findViewById(R.id.reward_bar_image);
            this.title = itemView.findViewById(R.id.reward_title);
            this.description = itemView.findViewById(R.id.reward_description);
            this.claim = itemView.findViewById(R.id.claim_reward);
        }
    }
}


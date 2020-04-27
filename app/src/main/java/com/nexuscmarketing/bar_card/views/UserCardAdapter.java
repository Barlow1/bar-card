package com.nexuscmarketing.bar_card.views;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nexuscmarketing.bar_card.R;
import com.nexuscmarketing.bar_card.model.UserBarCard;

import java.util.ArrayList;

public class UserCardAdapter extends RecyclerView.Adapter<UserCardAdapter.UserCardViewHolder> {
    private ArrayList<UserBarCard> barCardUsers;
    private UserCardAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public void setOnItemClickListener(UserCardAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public UserCardAdapter(ArrayList<UserBarCard> barCardUsers) {
        this.barCardUsers = barCardUsers;
    }

    @NonNull
    @Override
    public UserCardAdapter.UserCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_bar_card_list_item, parent, false);
        UserCardAdapter.UserCardViewHolder userCardViewHolder = new UserCardAdapter.UserCardViewHolder(view);
        return userCardViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserCardAdapter.UserCardViewHolder holder, int position) {
        TextView email = holder.email;
        TextView phone = holder.phone;
        TextView firstName = holder.firstName;
        TextView lastName = holder.lastName;
        TextView punches = holder.punches;
        email.setText(barCardUsers.get(position).getEmail());
        phone.setText(barCardUsers.get(position).getPhone());
        firstName.setText(barCardUsers.get(position).getFirstName());
        lastName.setText(barCardUsers.get(position).getLastName());
        punches.setText((String.valueOf(barCardUsers.get(position).getPunches())));
    }

    @Override
    public int getItemCount() {
        return barCardUsers.size();
    }

    public class UserCardViewHolder extends RecyclerView.ViewHolder {
        TextView email;
        TextView phone;
        TextView firstName;
        TextView lastName;
        TextView punches;
        Button punchButton;

        private UserCardViewHolder(@NonNull View itemView) {
            super(itemView);
            this.email = itemView.findViewById(R.id.user_email);
            this.phone = itemView.findViewById(R.id.user_phone);
            this.firstName = itemView.findViewById(R.id.user_first_name);
            this.lastName = itemView.findViewById(R.id.user_last_name);
            this.punches = itemView.findViewById(R.id.user_punches);
            this.punchButton = itemView.findViewById(R.id.punch_button);
            punchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                        listener.onItemClick(v, position);
                    }
                }
            });
        }
    }
}

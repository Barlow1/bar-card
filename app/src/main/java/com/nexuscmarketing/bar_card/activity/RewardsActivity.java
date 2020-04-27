package com.nexuscmarketing.bar_card.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nexuscmarketing.bar_card.R;
import com.nexuscmarketing.bar_card.model.Reward;
import com.nexuscmarketing.bar_card.model.User;
import com.nexuscmarketing.bar_card.sql.DatabaseHelper;
import com.nexuscmarketing.bar_card.views.RewardsListAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class RewardsActivity extends AppCompatActivity {
    ArrayList<Reward> rewardArrayList;
    SharedPreferences preferences;
    User loggedInUser;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rewards_activity);

        TextView noRewards = findViewById(R.id.no_rewards);
        noRewards.setVisibility(View.INVISIBLE);

        preferences = getApplicationContext().getSharedPreferences("UserId", MODE_PRIVATE);


        RecyclerView rewardsListView = findViewById(R.id.rewards_view);
        rewardsListView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rewardsListView.setLayoutManager(layoutManager);
        rewardsListView.setItemAnimator(new DefaultItemAnimator());

        if (savedInstanceState != null) {
            loggedInUser = (User) savedInstanceState.get("User");
        }
        else {
            loggedInUser = (User) getIntent().getSerializableExtra("User");
        }

        if (loggedInUser == null) {
            loggedInUser = new User();
            loggedInUser.setId(UUID.fromString(preferences.getString("User", null)));
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    setRewardArrayList(new DatabaseHelper().getUserRewards(loggedInUser.getId(), getApplicationContext()));
                }
                catch (SQLException e) {
                    Log.e("getUserBarCards","Failed to get UsersCards due to SQL Exception", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                if (rewardArrayList.size() == 0) {
                    noRewards.setVisibility(View.VISIBLE);
                }
                RewardsListAdapter rewardsListAdapter = new RewardsListAdapter(rewardArrayList);
                rewardsListAdapter.setOnItemClickListener(new  RewardsListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                Reward reward = rewardArrayList.get(position);
                                try {
                                    new DatabaseHelper().claimReward(reward.getId());
                                } catch (SQLException e) {
                                    Log.e("getUserRewards", "Failed to get UserRewards due to SQL Exception", e);
                                }
                                return null;
                            }
                        }.execute();
                    }

                });
                rewardsListView.setAdapter(rewardsListAdapter);
            }
        }.execute();
    }

    public void setRewardArrayList(ArrayList<Reward> rewardArrayList) {
        this.rewardArrayList = rewardArrayList;
    }
}

package com.nexuscmarketing.bar_card.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nexuscmarketing.bar_card.R;
import com.nexuscmarketing.bar_card.model.Bar;
import com.nexuscmarketing.bar_card.model.User;
import com.nexuscmarketing.bar_card.sql.DatabaseHelper;
import com.nexuscmarketing.bar_card.views.BarListAdapter;
import com.nexuscmarketing.bar_card.views.CardViewAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class AddCardActivity extends AppCompatActivity {
    public ArrayList<Bar> getBarArrayList() {
        return barArrayList;
    }

    public void setBarArrayList(ArrayList<Bar> barArrayList) {
        this.barArrayList = barArrayList;
    }

    ArrayList<Bar> barArrayList;
    User loggedInUser;
    SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_card_activity);

        preferences = getApplicationContext().getSharedPreferences("UserId", MODE_PRIVATE);


        RecyclerView barListView = findViewById(R.id.bar_list);
        barListView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        barListView.setLayoutManager(layoutManager);
        barListView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration barDivider = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        barListView.addItemDecoration(barDivider);

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
                    setBarArrayList(new DatabaseHelper().getBarList());
                }
                catch (SQLException e) {
                    Log.e("getUserBarCards","Failed to get UsersCards do to SQL Exception", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                BarListAdapter barListAdapter = new BarListAdapter(barArrayList);
                barListAdapter.setOnItemClickListener(new  BarListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                Bar bar = barArrayList.get(position);
                                try {
                                    new DatabaseHelper().insertBarCard(bar, loggedInUser);
                                } catch (SQLException e) {
                                    Log.e("getUserBarCards", "Failed to get UsersCards do to SQL Exception", e);
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void aVoid) {
                                super.onPostExecute(aVoid);
                                finish();

                                Intent barCardIntent = getParentActivityIntent();
                                navigateUpTo(barCardIntent);
                            }
                        }.execute();
                    }

                });
                barListView.setAdapter(barListAdapter);
            }
        }.execute();
    }
}

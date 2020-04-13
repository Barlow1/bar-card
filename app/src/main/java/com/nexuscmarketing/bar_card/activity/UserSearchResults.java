package com.nexuscmarketing.bar_card.activity;

import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nexuscmarketing.bar_card.R;
import com.nexuscmarketing.bar_card.model.Bar;
import com.nexuscmarketing.bar_card.model.UserBarCard;
import com.nexuscmarketing.bar_card.sql.DatabaseHelper;
import com.nexuscmarketing.bar_card.views.UserCardAdapter;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserSearchResults extends AppCompatActivity {
    ArrayList<UserBarCard> barCardUsers;
    Bar adminBar;
    RecyclerView barCarsUsersList;
    TextView noCards;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_search_results);
        adminBar = (Bar) getIntent().getBundleExtra(SearchManager.APP_DATA).getSerializable("adminBar");
        barCarsUsersList = findViewById(R.id.bar_card_users);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        barCarsUsersList.setLayoutManager(layoutManager);
        barCarsUsersList.setItemAnimator(new DefaultItemAnimator());
        noCards = findViewById(R.id.no_user_cards);
        noCards.setVisibility(View.INVISIBLE);
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    try {
                        barCardUsers = new DatabaseHelper().getUsersWithBarCards(adminBar.getId(), query);
                    } catch (SQLException e) {
                        Log.e("getUsersWithBarCards", "Failed to get barCardUsers do to SQL Exception", e);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    if (barCardUsers.size() == 0) {
                        noCards.setVisibility(View.VISIBLE);
                    }
                    UserCardAdapter userCardAdapter = new UserCardAdapter(barCardUsers);
                    userCardAdapter.setOnItemClickListener(new UserCardAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View itemView, int position) {
                            new AsyncTask<Void, Void, Void>() {

                                @Override
                                protected Void doInBackground(Void... voids) {
                                    try {
                                        UserBarCard userBarCard = barCardUsers.get(position);
                                        new DatabaseHelper().punchBarCard(userBarCard.getBarCardId(), userBarCard.getPunches());
                                    } catch (SQLException e) {
                                        Log.e("punchBarCard", "Failed to punch UserBarCard do to SQL Exception", e);
                                    }
                                    return null;
                                }

                                @Override
                                protected void onPostExecute(Void aVoid) {
                                    super.onPostExecute(aVoid);
                                    finish();
                                    overridePendingTransition(0, 0);
                                    startActivity(getIntent());
                                    overridePendingTransition(0, 0);
                                }
                            }.execute();
                        }
                    });
                    barCarsUsersList.setAdapter(userCardAdapter);
                }
            }.execute();
        }
    }
}

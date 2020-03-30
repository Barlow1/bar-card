package com.nexuscmarketing.bar_card.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nexuscmarketing.bar_card.R;
import com.nexuscmarketing.bar_card.model.BarCard;
import com.nexuscmarketing.bar_card.model.User;
import com.nexuscmarketing.bar_card.sql.DatabaseHelper;
import com.nexuscmarketing.bar_card.views.CardViewAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

public class CardActivity extends AppCompatActivity {
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("User", loggedInUser);
    }

    @Override
    protected void onPause() {
        super.onPause();
        preferences.edit().putString("User",loggedInUser.getId().toString()).apply();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        loggedInUser = (User) savedInstanceState.get("User");
    }

    public void setBarCardArrayList(ArrayList<BarCard> barCardArrayList) {
        this.barCardArrayList = barCardArrayList;
    }

    ArrayList<BarCard> barCardArrayList;
    User loggedInUser;
    SharedPreferences preferences;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.main_menu_add) {
                    Intent addCardIntent = new Intent(CardActivity.this, AddCardActivity.class);
            addCardIntent.putExtra("User", loggedInUser);
            startActivity(addCardIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity);
        barCardArrayList = new ArrayList<>();

        preferences = getApplicationContext().getSharedPreferences("UserId", MODE_PRIVATE);

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

        TextView noCards = findViewById(R.id.no_cards);
        noCards.setVisibility(View.INVISIBLE);
        RecyclerView cardView = findViewById(R.id.card_view);
        cardView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        cardView.setLayoutManager(layoutManager);
        cardView.setItemAnimator(new DefaultItemAnimator());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    setBarCardArrayList(new DatabaseHelper().getUserBarCards(getApplicationContext(), loggedInUser.getId()));
                }
                catch (SQLException e) {
                    Log.e("getUserBarCards","Failed to get UsersCards do to SQL Exception", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void v) {
                if (barCardArrayList.size() == 0) {
                    noCards.setVisibility(View.VISIBLE);
                }
                CardViewAdapter cardViewAdapter = new CardViewAdapter(barCardArrayList);
                cardViewAdapter.setOnItemClickListener(new CardViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View itemView, int position) {
                        new AsyncTask<Void, Void, Void>() {

                            @Override
                            protected Void doInBackground(Void... voids) {
                                try {
                                    BarCard barCard = barCardArrayList.get(position);
                                    new DatabaseHelper().deleteBarCard(barCard);
                                } catch (SQLException e) {
                                    Log.e("getUserBarCards", "Failed to get UsersCards do to SQL Exception", e);
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
                cardView.setAdapter(cardViewAdapter);
            }
        }.execute();
    }
}

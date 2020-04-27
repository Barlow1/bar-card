package com.nexuscmarketing.bar_card.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.nexuscmarketing.bar_card.R;
import com.nexuscmarketing.bar_card.model.Bar;
import com.nexuscmarketing.bar_card.model.User;
import com.nexuscmarketing.bar_card.model.UserBarCard;
import com.nexuscmarketing.bar_card.sql.DatabaseHelper;
import com.nexuscmarketing.bar_card.utils.ResourceUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class AdminPunchActivity extends AppCompatActivity {
    Bar adminBar;
    User adminUser;
    ArrayList<UserBarCard> barCardUsers;

    @Override
    public boolean onSearchRequested() {
        Bundle appData = new Bundle();
        appData.putSerializable("adminBar", adminBar);
        startSearch(null, false, appData, false);
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.admin_punch_activity);
        super.onCreate(savedInstanceState);
        adminUser = (User) getIntent().getSerializableExtra("AdminUser");
        ImageView adminBarImage = findViewById(R.id.admin_bar_img);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    adminBar = new DatabaseHelper().getAdminBar(adminUser.getId());
                } catch (SQLException e) {
                    Log.e("getAdminBar", "Failed to get AdminBar do to SQL Exception", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
            adminBarImage.setImageResource(ResourceUtil.getDrawableIdByResName(getApplicationContext(), adminBar.getImageName()));
                super.onPostExecute(aVoid);
            }
        }.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.admin_search) {
            onSearchRequested();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

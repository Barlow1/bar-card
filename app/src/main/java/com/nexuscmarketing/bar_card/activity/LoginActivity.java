package com.nexuscmarketing.bar_card.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nexuscmarketing.bar_card.R;
import com.nexuscmarketing.bar_card.sql.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {
    Boolean existingUser;

    protected void setExistingUser(Boolean exists) {
        this.existingUser = exists;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        Button login = findViewById(R.id.login);
        TextView loginEmail = findViewById(R.id.login_email);
        TextView loginPassword = findViewById(R.id.login_password);
        TextView registerLink = findViewById(R.id.link_register);

        AlertDialog loginSuccessAlert = new AlertDialog.Builder(this)
                .setTitle(R.string.login_success)
                .setMessage(R.string.welcome)
                .create();

        AlertDialog loginFailedAlert = new AlertDialog.Builder(this)
                .setTitle(R.string.login_fail)
                .setMessage(R.string.login_fail_msg)
                .create();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginEmail.length() > 0 && loginPassword.length() > 0) {
                    String email = loginEmail.getText().toString();
                    String password = loginPassword.getText().toString();

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            setExistingUser(new DatabaseHelper().isExistingUser(email, password));
                            Log.i("User Exists", existingUser.toString());
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void v) {
                            if (existingUser) {
                                loginSuccessAlert.show();
                            } else {
                                loginFailedAlert.show();
                            }
                        }

                    }.execute();
                }
            }
        });

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}
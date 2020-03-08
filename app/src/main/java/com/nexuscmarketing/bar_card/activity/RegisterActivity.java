package com.nexuscmarketing.bar_card.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nexuscmarketing.bar_card.R;
import com.nexuscmarketing.bar_card.model.User;
import com.nexuscmarketing.bar_card.sql.DatabaseHelper;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    public Boolean getRegistrationSuccessful() {
        return registrationSuccessful;
    }

    public void setRegistrationSuccessful(Boolean registrationSuccessful) {
        this.registrationSuccessful = registrationSuccessful;
    }

    Boolean registrationSuccessful;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        Button register = findViewById(R.id.register);
        TextView registerFirstName = findViewById(R.id.first_name_register);
        TextView registerLastName = findViewById(R.id.last_name_register);
        TextView registerEmail = findViewById(R.id.register_email);
        TextView registerPassword = findViewById(R.id.register_password);
        TextView loginLink = findViewById(R.id.link_login);
        CheckBox registerAdminCheck = findViewById(R.id.admin_check);

        AlertDialog registerSuccessAlert = new AlertDialog.Builder(this)
                .setTitle(R.string.register_success)
                .setMessage(R.string.register_welcome)
                .create();

        AlertDialog registerFailedAlert = new AlertDialog.Builder(this)
                .setTitle(R.string.register_fail)
                .setMessage(R.string.register_fail_msg)
                .create();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registerFirstName.length() > 0 && registerLastName.length() > 0
                        && registerEmail.length() > 0 && registerPassword.length() > 0) {
                    String firstName = registerFirstName.getText().toString();
                    String lastName = registerLastName.getText().toString();
                    String email = registerEmail.getText().toString();
                    String password = registerPassword.getText().toString();
                    Integer adminCheck = registerAdminCheck.isChecked() ? 1 : 0;
                    User newUser = new User();

                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected void onPreExecute() {
                            newUser.setId(UUID.randomUUID());
                            newUser.setFirstName(firstName);
                            newUser.setLastName(lastName);
                            newUser.setEmail(email);
                            newUser.setPassword(password);
                            newUser.setAdmin(adminCheck);
                        }

                        @Override
                        protected Void doInBackground(Void... voids) {
                            try {
                                new DatabaseHelper().addUser(newUser);
                                setRegistrationSuccessful(true);
                                Log.i("RegisterActivity", "User created successfully");
                            } catch (Exception e) {
                                setRegistrationSuccessful(false);
                                Log.d("RegisterActivity", e.toString());
                            }
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void v) {
                            if (registrationSuccessful) {
                                registerSuccessAlert.show();
                            } else {
                                registerFailedAlert.show();
                            }
                        }

                    }.execute();
                }
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(registerIntent);
            }
        });
    }
}

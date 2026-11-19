package com.example.studenttaskmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.*;

public class LoginActivity extends Activity {

    DatabaseHelper db;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        prefs = getSharedPreferences("session", MODE_PRIVATE);

        String saved = prefs.getString("username", null);
        if (saved != null) {
            openDashboard(saved);
            return;
        }

        setContentView(R.layout.activity_login);
        EditText edtUsername = findViewById(R.id.edtUsername);
        EditText edtPassword = findViewById(R.id.edtPassword);

        findViewById(R.id.btnLogin).setOnClickListener(v -> {
            String u = edtUsername.getText().toString().trim();
            String p = edtPassword.getText().toString().trim();
            if (u.isEmpty() || p.isEmpty()) {
                Toast.makeText(this, "Enter username and password!",
                        Toast.LENGTH_SHORT).show();
            } else if (db.checkUser(u, p)) {
                prefs.edit().putString("username", u).apply();
                openDashboard(u);
            } else {
                Toast.makeText(this, "Invalid credentials!",
                        Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btnRegister).setOnClickListener(v -> showRegister());
    }

    private void openDashboard(String user) {
        Intent i = new Intent(this, DashboardActivity.class);
        i.putExtra("username", user);
        startActivity(i);
        finish();
    }

    private void showRegister() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 20, 30, 10);

        EditText u = new EditText(this);
        u.setHint("Username");
        EditText p = new EditText(this);
        p.setHint("Password (min. 4 characters)");
        p.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        layout.addView(u);
        layout.addView(p);

        new AlertDialog.Builder(this)
                .setTitle("Create Account")
                .setView(layout)
                .setPositiveButton("REGISTER", (d, w) -> {
                    String user = u.getText().toString().trim();
                    String pass = p.getText().toString().trim();
                    if (user.isEmpty() || pass.length() < 4) {
                        Toast.makeText(this,
                                "Username required, password min. 4 chars",
                                Toast.LENGTH_SHORT).show();
                    } else if (db.registerUser(user, pass)) {
                        Toast.makeText(this,
                                "Account created! You can log in now.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Username already taken!",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }
}
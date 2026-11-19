package com.example.studenttaskmanager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DashboardActivity extends Activity {

    TextView txtWelcome, txtTotal, txtPending, txtCompleted, txtOverdue;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtWelcome = findViewById(R.id.txtWelcome);
        txtTotal = findViewById(R.id.txtTotal);
        txtPending = findViewById(R.id.txtPending);
        txtCompleted = findViewById(R.id.txtCompleted);
        txtOverdue = findViewById(R.id.txtOverdue);
        View btnManage = findViewById(R.id.btnManageTasks);
        View btnAbout = findViewById(R.id.btnAbout);
        View btnLogout = findViewById(R.id.btnLogout);

        databaseHelper = new DatabaseHelper(this);

        String user = getIntent().getStringExtra("username");
        txtWelcome.setText("Hello, " + (user != null ? user : "Student") + "!");

        btnManage.setOnClickListener(v ->
                startActivity(new Intent(this, MainActivity.class)));
        btnAbout.setOnClickListener(v ->
                startActivity(new Intent(this, AboutActivity.class)));
        btnLogout.setOnClickListener(v ->
                new AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("YES", (d, w) -> {
                            getSharedPreferences("session", MODE_PRIVATE)
                                    .edit().clear().apply();
                            startActivity(new Intent(this, LoginActivity.class));
                            finish();
                        })
                        .setNegativeButton("NO", null)
                        .show());
    }

    @Override
    protected void onResume() {
        super.onResume();
        txtTotal.setText(String.valueOf(databaseHelper.getCount(-1)));
        txtPending.setText(String.valueOf(databaseHelper.getCount(0)));
        txtCompleted.setText(String.valueOf(databaseHelper.getCount(1)));
        txtOverdue.setText(String.valueOf(databaseHelper.getOverdueCount()));
    }
}
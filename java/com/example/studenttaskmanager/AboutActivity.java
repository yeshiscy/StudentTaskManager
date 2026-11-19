package com.example.studenttaskmanager;

import android.app.Activity;
import android.os.Bundle;

public class AboutActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.btnBackBottom).setOnClickListener(v -> finish());
    }
}
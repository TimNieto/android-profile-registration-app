package com.timnieto.profileregistration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int FADE_IN = android.R.anim.fade_in;
    private static final int FADE_OUT = android.R.anim.fade_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View proceedButton = findViewById(R.id.btnProceed);
        proceedButton.setOnClickListener(view -> openRegistrationScreen());
    }

    private void openRegistrationScreen() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        overridePendingTransition(FADE_IN, FADE_OUT);
    }
}
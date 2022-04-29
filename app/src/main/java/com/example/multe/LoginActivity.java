package com.example.multe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

public class LoginActivity extends AppCompatActivity {
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setNavigationBarColor(Color.BLACK);

        View.OnClickListener login = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                finish();
            }
        };

        (findViewById(R.id.bin1)).setOnClickListener(login);
        (findViewById(R.id.bin2)).setOnClickListener(login);
    }
}
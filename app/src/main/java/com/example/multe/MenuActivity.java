package com.example.multe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getWindow().setNavigationBarColor(Color.BLACK);

        View.OnClickListener info = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                startActivity(new Intent(MenuActivity.this, InfoActivity.class));
            }
        };
        View.OnClickListener logout = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                startActivity(new Intent(MenuActivity.this, LoginActivity.class));
            }
        };

        (findViewById(R.id.binfo1)).setOnClickListener(info);
        (findViewById(R.id.binfo2)).setOnClickListener(info);

        (findViewById(R.id.bout1)).setOnClickListener(logout);
        (findViewById(R.id.bout2)).setOnClickListener(logout);
    }
}
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
                finish();
            }
        };
        View.OnClickListener nuovaMulta = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                startActivity(new Intent(MenuActivity.this, NuovaMultaActivity.class));
            }
        };
        View.OnClickListener vediMulte = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();
            }
        };

        (findViewById(R.id.binfo1)).setOnClickListener(info);
        (findViewById(R.id.binfo2)).setOnClickListener(info);

        (findViewById(R.id.bout1)).setOnClickListener(logout);
        (findViewById(R.id.bout2)).setOnClickListener(logout);

        (findViewById(R.id.bnuovo1)).setOnClickListener(nuovaMulta);
        (findViewById(R.id.bnuovo2)).setOnClickListener(nuovaMulta);

        (findViewById(R.id.bvedi1)).setOnClickListener(vediMulte);
        (findViewById(R.id.bvedi2)).setOnClickListener(vediMulte);
    }
}
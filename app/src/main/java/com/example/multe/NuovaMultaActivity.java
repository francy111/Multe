package com.example.multe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;

public class NuovaMultaActivity extends AppCompatActivity {
    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_multa);
        getWindow().setNavigationBarColor(Color.BLACK);

        View.OnClickListener listaEffrazioni = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                (findViewById(R.id.effrazioni)).setVisibility(View.VISIBLE);
            }
        };

        (findViewById(R.id.beffr1)).setOnClickListener(listaEffrazioni);
        (findViewById(R.id.beffr2)).setOnClickListener(listaEffrazioni);
        (findViewById(R.id.effrazioni)).setVisibility(View.GONE);
    }
}
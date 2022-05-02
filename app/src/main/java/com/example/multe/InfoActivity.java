package com.example.multe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        getWindow().setNavigationBarColor(Color.BLACK);

        SharedPreferences sp = getSharedPreferences("vigile", MODE_PRIVATE);

        ((TextView)findViewById(R.id.nome)).setText(sp.getString("nome", ""));
        ((TextView)findViewById(R.id.cognome)).setText(sp.getString("cognome", ""));
        ((TextView)findViewById(R.id.ruolo)).setText(sp.getString("ruolo", ""));
        ((TextView)findViewById(R.id.matricola)).setText(sp.getString("matricola", ""));
    }
}
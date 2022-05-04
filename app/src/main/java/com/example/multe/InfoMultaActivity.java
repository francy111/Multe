package com.example.multe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

public class InfoMultaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_multa);
        getWindow().setNavigationBarColor(Color.BLACK);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        String multa = b.getString("multa");

        JSONObject o;
        String targa, dataora, luogo, coordinate, effrazioni;
        try {
            o = new JSONObject(multa);
            targa = o.getString("targa");
            dataora = o.getString("dataora");
            luogo = o.getString("luogo");
            coordinate = o.getString("latitudine") + "," + o.getString("longitudine");
            effrazioni = o.getString("effrazioni");

            ((TextView)findViewById(R.id.targhetta)).setText("Targa: " + targa);
            ((TextView)findViewById(R.id.datoretta)).setText(dataora);
            ((TextView)findViewById(R.id.luoghetto)).setText(luogo);
            ((TextView)findViewById(R.id.coordinatine)).setText(coordinate);

            String[] effr = effrazioni.split(",");
            TextView t;
            for(String s : effr){
                t = new TextView(InfoMultaActivity.this);
                t.setTextColor(Color.rgb(43, 43, 43));
                t.setTextSize(16);
                t.setTypeface(ResourcesCompat.getFont(this, R.font.ock));
                t.setText(s);

                ((LinearLayout)findViewById(R.id.scrollerino)).addView(t);
            }
        }catch(Exception e){}
    }
}
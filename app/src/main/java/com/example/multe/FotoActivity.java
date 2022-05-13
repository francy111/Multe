package com.example.multe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class FotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
        getWindow().setNavigationBarColor(Color.BLACK);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        String immagine = b.getString("Immagine");
        Log.d("COCK", "FOTO ACTIVITY:"+ immagine);

        byte[] decodedString = Base64.decode(immagine, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        ImageView coc = findViewById(R.id.imageView);
        coc.setImageBitmap(decodedByte);
    }
}
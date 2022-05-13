package com.example.multe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

public class NuovaMultaActivity extends AppCompatActivity {
    private long mLastClickTime = 0;
    private FusedLocationProviderClient client;
    public Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_multa);
        getWindow().setNavigationBarColor(Color.BLACK);

        if (getSharedPreferences("theme", MODE_PRIVATE).getBoolean("dark", true)) {
            dark_theme();
        } else {
            light_theme();
        }

        View.OnClickListener listaEffrazioni = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                (findViewById(R.id.effrazioni)).setVisibility(View.VISIBLE);

                SharedPreferences.Editor e = getSharedPreferences("fragment", MODE_PRIVATE).edit();
                e.putInt("visibility", /*0*/8);
                e.commit();
            }
        };
        View.OnClickListener chiudiLista = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                (findViewById(R.id.effrazioni)).setVisibility(View.GONE);

                SharedPreferences.Editor e = getSharedPreferences("fragment", MODE_PRIVATE).edit();
                e.putInt("visibility", 8);
                e.commit();
            }
        };
        View.OnClickListener dataOra = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat ora = new SimpleDateFormat("HH:mm:ss");

                ((TextView) findViewById(R.id.date)).setText(data.format(new Date()));
                ((TextView) findViewById(R.id.time)).setText(ora.format(new Date()));
            }
        };
        View.OnClickListener coordinate = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();
                if (ActivityCompat.checkSelfPermission(NuovaMultaActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                client.getLastLocation().addOnSuccessListener(NuovaMultaActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            ((TextView) findViewById(R.id.lat)).setText("" + location.getLatitude());
                            ((TextView) findViewById(R.id.lon)).setText("" + location.getLongitude());
                        }
                    }
                });
            }
        };
        View.OnClickListener nuovaMulta = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                if (datiInseriti()) {
                    spedisciMulta();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NuovaMultaActivity.this);
                    builder.setTitle("Warning");
                    builder.setMessage("Inserire tutti i dati richiesti");
                    builder.setCancelable(false);
                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    AlertDialog alertdialog = builder.create();
                    alertdialog.show();
                }
            }
        };
        View.OnClickListener foto = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                AlertDialog.Builder builder = new AlertDialog.Builder(NuovaMultaActivity.this);
                builder.setTitle("Selezione");
                builder.setMessage("Vuoi scattare una foto o sceglierla dalla galleria?");
                builder.setCancelable(false);
                builder.setPositiveButton("Scatta", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(takePicture, 500);
                    }
                });
                builder.setNegativeButton("Scegli", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , 501);
                    }
                });
                AlertDialog alertdialog = builder.create();
                alertdialog.show();

            }
        };
        View.OnClickListener mostrafoto = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();

                if(photo == null);
                else{
                    Intent i = new Intent(NuovaMultaActivity.this, FotoActivity.class);
                    String s = MainActivity.encodeImage(photo);
                    i.putExtra("Immagine", s);
                    Log.d("COCK", s);
                    startActivity(i);
                }
            }
        };

        (findViewById(R.id.bdt1)).setOnClickListener(dataOra);
        (findViewById(R.id.bdt2)).setOnClickListener(dataOra);

        (findViewById(R.id.bc1)).setOnClickListener(coordinate);
        (findViewById(R.id.bc2)).setOnClickListener(coordinate);

        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(this);


        SharedPreferences s = getSharedPreferences("fragment", MODE_PRIVATE);

        int vis = s.getInt("visibility", 8);

        (findViewById(R.id.effrazioni)).setVisibility(vis);

        (findViewById(R.id.beffr1)).setOnClickListener(listaEffrazioni);
        (findViewById(R.id.beffr2)).setOnClickListener(listaEffrazioni);
        (findViewById(R.id.badge)).setOnClickListener(chiudiLista);

        (findViewById(R.id.bnuovamulta1)).setOnClickListener(nuovaMulta);
        (findViewById(R.id.bnuovamulta2)).setOnClickListener(nuovaMulta);

        (findViewById(R.id.bp1)).setOnClickListener(foto);
        (findViewById(R.id.bp2)).setOnClickListener(foto);
        (findViewById(R.id.fotinacarina)).setOnClickListener(mostrafoto);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private int importoMulta(ArrayList<Integer> s) {
        int a = 0;
        for (Integer b : s) a += b;
        return a;
    }

    private void spedisciMulta() {
        ((View) findViewById(R.id.bnuovamulta1)).setClickable(false);
        ((View) findViewById(R.id.bnuovamulta2)).setClickable(false);
        RequestQueue queue = Volley.newRequestQueue(NuovaMultaActivity.this);
        //for POST requests, only the following line should be changed to
        StringRequest sr = new StringRequest(Request.Method.POST, "http://" + MainActivity.IP + "/sito/API-PHP/api.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("HttpClient", "success! response: " + response.toString());

                        //MainActivity.effrazioniTotali.addAll(MainActivity.effrazioni);
                        MainActivity.effrazioniTotali.clear();
                        MainActivity.effrazioni.clear();
                        photo = null;

                        ((View) findViewById(R.id.bnuovamulta1)).setClickable(true);
                        ((View) findViewById(R.id.bnuovamulta2)).setClickable(true);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                try {
                    String s = "";
                    for (JSONObject obj : MainActivity.effrazioni) {
                        s = s + obj.getString("id") + ",";
                    }
                    s = s.substring(0, s.length() - 1);

                    params.put("vigile", getSharedPreferences("vigile", MODE_PRIVATE).getString("matricola", ""));
                    params.put("targa", ((TextView) findViewById(R.id.plate)).getText().toString());
                    params.put("luogo", ((TextView) findViewById(R.id.location)).getText().toString());
                    params.put("importo", importoMulta(MainActivity.importi) + "");
                    params.put("data", ((TextView) findViewById(R.id.date)).getText().toString());
                    params.put("ora", ((TextView) findViewById(R.id.time)).getText().toString());
                    params.put("latitudine", ((TextView) findViewById(R.id.lat)).getText().toString());
                    params.put("longitudine", ((TextView) findViewById(R.id.lon)).getText().toString());
                    params.put("foto", MainActivity.encodeImage(photo));

                    params.put("effrazioni", s);

                    params.put("token", getSharedPreferences("vigile", MODE_PRIVATE).getString("token", ""));
                    params.put("function", "app-nuovamulta");
                } catch (Exception e) {
                }
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(sr);
    }

    public void dark_theme() {
        findViewById(R.id.view12).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.bdt2).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.bc2).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.view20).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.viewewew).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.view15).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.view14).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.view18).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.bp1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.beffr1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.bnuovamulta1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio2));
        findViewById(R.id.view13).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite1));
        findViewById(R.id.view9).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio1));
    }

    public void light_theme() {
        findViewById(R.id.view12).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.bdt2).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.bc2).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.view20).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.viewewew).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.view15).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.view14).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.view18).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.bp1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.beffr1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.bnuovamulta1).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio4));
        findViewById(R.id.view13).setBackground(getResources().getDrawable(R.drawable.rettangolinoantracite2));
        findViewById(R.id.view9).setBackground(getResources().getDrawable(R.drawable.rettangolinogrigio3));
    }

    public boolean datiInseriti() {
        boolean check = true;
        if (((TextView) findViewById(R.id.plate)).getText().toString().equals(""))
            check = false;
        if (((TextView) findViewById(R.id.location)).getText().toString().equals(""))
            check = false;
        if (((TextView) findViewById(R.id.date)).getText().toString().equals(""))
            check = false;
        if (((TextView) findViewById(R.id.time)).getText().toString().equals(""))
            check = false;
        if (((TextView) findViewById(R.id.lat)).getText().toString().equals(""))
            check = false;
        if (((TextView) findViewById(R.id.lon)).getText().toString().equals(""))
            check = false;
        if (MainActivity.effrazioni.size() == 0)
            check = false;
        return check;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        try {
            switch (requestCode) {
                case 500:
                    if (resultCode == RESULT_OK) {
                        Bundle extra = imageReturnedIntent.getExtras();
                        Bitmap immagine = (Bitmap)extra.get("data");
                        photo = immagine;
                    }

                    break;
                case 501:
                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = imageReturnedIntent.getData();
                        InputStream imageStream = getContentResolver().openInputStream(selectedImage);
                        Bitmap iamge = BitmapFactory.decodeStream(imageStream);
                        photo = iamge;
                    }
                    break;
            }
        }catch(Exception e){
            Log.e("COCK", "ERRORE: " + e.getMessage());
        }
    }


}

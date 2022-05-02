package com.example.multe;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NuovaMultaActivity extends AppCompatActivity {
    private long mLastClickTime = 0;
    private FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuova_multa);
        getWindow().setNavigationBarColor(Color.BLACK);

        Log.d("COCK", ""+importoMulta(MainActivity.importi));

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

                ((TextView)findViewById(R.id.date)).setText(data.format(new Date()));
                ((TextView)findViewById(R.id.time)).setText(ora.format(new Date()));
            }
        };
        View.OnClickListener coordinate = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                mLastClickTime = SystemClock.elapsedRealtime();
                if (ActivityCompat.checkSelfPermission(NuovaMultaActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                    return;
                }
                client.getLastLocation().addOnSuccessListener(NuovaMultaActivity.this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            ((TextView)findViewById(R.id.lat)).setText(""+location.getLatitude());
                            ((TextView)findViewById(R.id.lon)).setText(""+location.getLongitude());
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
                RequestQueue queue = Volley.newRequestQueue(NuovaMultaActivity.this);
                //for POST requests, only the following line should be changed to
                StringRequest sr = new StringRequest(Request.Method.POST, "http://multe.ddns.net:8080/sito/API-PHP/api.php",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("HttpClient", "success! response: " + response.toString());
                                try {
                                    JSONObject rispostaJSON = new JSONObject(response);
                                } catch (JSONException jsonException) {
                                    jsonException.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("HttpClient", "error: " + error.toString());
                            }
                        })
                {
                    @Override
                    protected Map<String,String> getParams(){
                        Map<String,String> params = new HashMap<String, String>();

                        try {
                            String s = "";
                            for (JSONObject obj : MainActivity.effrazioni) {
                                s = s + obj.getString("id") + ",";
                            }
                            s = s.substring(0, s.length()-1);

                            params.put("vigile",getSharedPreferences("vigile", MODE_PRIVATE).getString("matricola", ""));
                            params.put("targa",((TextView)findViewById(R.id.plate)).getText().toString());
                            params.put("luogo",((TextView)findViewById(R.id.location)).getText().toString());
                            params.put("importo",importoMulta(MainActivity.importi)+ "");
                            params.put("data",((TextView)findViewById(R.id.date)).getText().toString());
                            params.put("ora",((TextView)findViewById(R.id.time)).getText().toString());
                            params.put("latitudine",((TextView)findViewById(R.id.lat)).getText().toString());
                            params.put("longitudine",((TextView)findViewById(R.id.lon)).getText().toString());
                            //params.put("foto",null);

                            params.put("effrazioni",s);
                            Log.d("COCK", params.toString());

                            params.put("token", getSharedPreferences("vigile", MODE_PRIVATE).getString("token", ""));
                            params.put("function","app-nuovamulta");
                        }catch(Exception e){}
                        return params;
                    }
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("Content-Type","application/x-www-form-urlencoded");
                        return params;
                    }
                };
                queue.add(sr);
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
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }

    private int importoMulta(ArrayList<Integer> s){
        int a = 0;
        for(Integer b : s) a += b;
        return a;
    }
}

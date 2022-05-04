package com.example.multe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VisualizzaMultaActivity extends AppCompatActivity {
    private long mLastClickTime = 0;
    private JSONArray array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_multa);
        getWindow().setNavigationBarColor(Color.BLACK);
        RequestQueue queue = Volley.newRequestQueue(VisualizzaMultaActivity.this);
        //for POST requests, only the following line should be changed to
        StringRequest sr = new StringRequest(Request.Method.POST, "http://multe.ddns.net:8080/sito/API-PHP/api.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("HttpClient", "success! response: " + response.toString());
                        try {
                            JSONObject rispostaJSON = new JSONObject(response);
                            Log.d("COCK", "RISPOSTA" + rispostaJSON.toString());

                            array = rispostaJSON.getJSONArray("multe");
                            ((TextView)findViewById(R.id.textView7)).setText("Multe totali effettuate: " + array.length());

                            JSONObject o;
                            Button effr, divider;
                            for(int i = 0; i < array.length(); i++) {
                                o = array.getJSONObject(i);

                                effr = new Button(VisualizzaMultaActivity.this);
                                divider = new Button(VisualizzaMultaActivity.this);


                                Drawable d = getResources().getDrawable(R.drawable.rettangolinogrigio2);
                                effr.setBackground(d);
                                effr.setTextColor(Color.rgb(43, 43, 43));
                                effr.setText("M" + o.getString("id") + " - " + o.getString("dataora"));
                                effr.setId(i);
                                effr.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) return;
                                        mLastClickTime = SystemClock.elapsedRealtime();

                                        try {
                                            String s = array.getJSONObject(view.getId()).toString();

                                            Intent i = new Intent(VisualizzaMultaActivity.this, InfoMultaActivity.class);
                                            i.putExtra("multa", s);
                                            startActivity(i);

                                        }catch(Exception e){}
                                    }
                                });

                                divider.setBackgroundColor(Color.rgb(67, 67, 67));

                                ((LinearLayout) findViewById(R.id.scrolly)).addView(effr, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 175));
                                ((LinearLayout) findViewById(R.id.scrolly)).addView(divider, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 35));
                            }


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
                params.put("token", getSharedPreferences("vigile", MODE_PRIVATE).getString("token", ""));
                params.put("function","app-visualizzamulte");
                Log.d("COCK", "token: "+getSharedPreferences("vigile", MODE_PRIVATE).getString("token", ""));
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
}
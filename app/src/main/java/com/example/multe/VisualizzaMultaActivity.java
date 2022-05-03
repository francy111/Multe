package com.example.multe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

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

                            JSONArray array = rispostaJSON.getJSONArray("multe");

                            JSONObject o;
                            for(int i = 0; i < array.length(); i++) {
                                o = array.getJSONObject(i);
                                Log.d("COCK", i+" " + o.toString());
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
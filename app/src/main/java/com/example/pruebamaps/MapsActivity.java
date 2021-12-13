package com.example.pruebamaps;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.pruebamaps.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private RequestQueue queue;
    private Marker BbtcUQ, InNUQ;
    String CuposSensores, CuposTotales;

    TextView titulo, info, cupos;

    int tam=20;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        titulo = findViewById(R.id.textView3);
        info = findViewById(R.id.textView4);
        cupos = findViewById(R.id.textView6);

        titulo.setText("LUGAR: SELECCIONE UN LUGAR");   titulo.setTextSize(tam);
        titulo.setGravity(Gravity.CENTER);
        info.setText("CUPOS DISPONIBLES");              info.setTextSize(tam);
        info.setGravity(Gravity.CENTER);
        cupos.setText("-");                             cupos.setTextSize(80);
        cupos.setGravity(Gravity.CENTER);

        queue = Volley.newRequestQueue(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Biblioteca UQ 4.556401, -75.659832
        // Entrada Norte UQ 4.556372, -75.658528

        LatLng bbtcUQ = new LatLng(4.556401, -75.659832);
        BbtcUQ= googleMap.addMarker(new MarkerOptions().position(bbtcUQ).title("BIBLIOTECA UQ").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));

        LatLng inNUQ = new LatLng(4.556372, -75.658528);
        InNUQ = googleMap.addMarker(new MarkerOptions().position(inNUQ).title("SECTOR NORTE UQ"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bbtcUQ, 17));
        googleMap.setOnMarkerClickListener(this);
//        googleMap.setOnInfoWindowClickListener(this);

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        if (marker.equals(BbtcUQ)){
            String urlsens = "https://api.thingspeak.com/channels/1524993/fields/1.json?api_key=DTJ2H6PCR7AKP38I&results=2";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlsens, null, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray feeds = response.getJSONArray("feeds");
                        JSONObject datos = feeds.getJSONObject(1);
                        CuposSensores = datos.getString("field1");
                        titulo.setText("LUGAR: "+marker.getTitle());
                        cupos.setText("   "+CuposSensores);
//                        marker.setSnippet("Cupos disponibles= "+CuposSensores);
//                        Toast.makeText(MapsActivity.this, "Cupos disponibles = " + CuposSensores, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }
            );
            queue.add(request);
        }


        if (marker.equals(InNUQ)){
            String urladmin = "https://api.thingspeak.com/channels/1530086/fields/1.json?api_key=7N2I61NM0G5W3BEE&results=2";

            JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, urladmin, null, new Response.Listener<JSONObject>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray feeds = response.getJSONArray("feeds");
                        JSONObject datos = feeds.getJSONObject(1);
                        CuposTotales = datos.getString("field1");
                        titulo.setText("LUGAR: "+marker.getTitle());
                        cupos.setText("   "+CuposTotales);
//                        marker.setSnippet("Cupos disponibles= "+CuposTotales);
//                        Toast.makeText(MapsActivity.this, "Cupos disponibles = " + CuposTotales, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }
            );
            queue.add(request2);
        }

        return false;
    }

//    @Override
//    public void onInfoWindowClick(@NonNull Marker marker) {
//        if (marker.equals(BbtcUQ)){
//                AlertDialog.Builder instrucciones = new AlertDialog.Builder(MapsActivity.this);
//                instrucciones.setMessage("Cupos disponibles= "+CuposSensores);
//                instrucciones.setCancelable(true);
//                instrucciones.setPositiveButton("Entiendo", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                AlertDialog titulo = instrucciones.create();
//                titulo.setTitle("INFORMACIÓN");
//                titulo.show();
//        }
//
//        if (marker.equals(InNUQ)){
//            AlertDialog.Builder instrucciones = new AlertDialog.Builder(MapsActivity.this);
//            instrucciones.setMessage("Cupos disponibles= "+CuposTotales);
//            instrucciones.setCancelable(true);
//            instrucciones.setPositiveButton("Entiendo", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            AlertDialog titulo = instrucciones.create();
//            titulo.setTitle("INFORMACIÓN");
//            titulo.show();
//        }
//
//    }

    public void salir (View view){ finish(); }

}
package com.example.pruebamaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

   Button mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mapa = findViewById(R.id.button);

//        mapa.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
//               startActivity(intent);
//            }
//        });
    }

    public void Mapa (View view){
        Intent mapa = new Intent(this, MapsActivity.class);
        startActivity(mapa);
    }

    public void salir (View view){
        finish();
    }

}
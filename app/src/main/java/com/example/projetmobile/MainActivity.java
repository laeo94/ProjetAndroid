package com.example.projetmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button go = findViewById(R.id.Information);
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si la connexion avec internet fonctionne
              if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    Intent i = new Intent(getApplicationContext(),
                            LoginActivity.class);
                    startActivity(i);
                } else {
                    //Display error message if not connected to internet
                    Toast.makeText(MainActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

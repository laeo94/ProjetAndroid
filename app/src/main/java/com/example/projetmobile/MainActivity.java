package com.example.projetmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String BASE_URL = "";
    public static final String KEY_SUCCESS = "success";
    public static final String KEY_DATA = "data";
    public static final String KEY_PERSON_ID= "uid";
    public static final String KEY_PSEUDO = "pseudo";
    public static final String KEY_MDP = "mdp";
    public static final String KEY_ACCOUNT_ID = "aid";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESC = "description";
    public static final String KEY_DEV = "device";
    public static final String KEY_SOMME = "somme";

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
                    Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(i);
                } else {
                    //Message d'erreur
                    Toast.makeText(MainActivity.this, "Unable to connect to internet", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

package com.example.projetmobile;


import android.content.Intent;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText user, password;
    Button login,signup;
    String pass, username, uid;
    private static  int success;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user =  findViewById(R.id.txtuser);
        password =  findViewById(R.id.txtmdp);
        login =  findViewById(R.id.userlogin);
        signup = findViewById(R.id.btnAdd);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = user.getText().toString();
                pass = password.getText().toString();
                if (username.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "fill details", Toast.LENGTH_SHORT).show();
                } else {
                    new FindUserAsynTask().execute();
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddUserActivity.class);
                startActivity(intent);
            }
        });
    }

    private class FindUserAsynTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            String pseudo = user.getText().toString();
            String pass = password.getText().toString();
            httpParams.put(MainActivity.KEY_PSEUDO, pseudo);
            httpParams.put(MainActivity.KEY_MDP, pass);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(MainActivity.BASE_URL + "login.php", "GET", httpParams);
            if (jsonObject == null) {
                System.out.println("JSON NULL");
            }
            try {
                success = jsonObject.getInt(MainActivity.KEY_SUCCESS);
                JSONArray person;
                if (success == 1) {
                    person = jsonObject.getJSONArray(MainActivity.KEY_DATA);
                    JSONObject accounts = person.getJSONObject(0);
                    uid = accounts.getString(MainActivity.KEY_PERSON_ID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                public void run() {
                   if(success ==1) login();
                   if(success ==2) {
                       Toast.makeText(LoginActivity.this, "mdp not valide retry", Toast.LENGTH_LONG).show();
                   }
                   if(success==3){
                       Toast.makeText(LoginActivity.this, "pseudo not valide retry", Toast.LENGTH_LONG).show();
                   }
                }
            });
        }

        private void login() {
            if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                Intent intent = new Intent(getApplicationContext(),AccountHomeActivity.class);
                intent.putExtra(MainActivity.KEY_PERSON_ID,uid);
                startActivityForResult(intent, 20);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Unable to connect to internet", Toast.LENGTH_LONG).show();

            }
        }

    }
}




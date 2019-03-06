package com.example.projetmobile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private static final String STRING_EMPTY = "";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_UID = "uid";
    private static final String KEY_PSEUDO = "pseudo";
    private static final String KEY_MDP = "mdp";
    private static final String BASE_URL = "https://pw.lacl.fr/~u21505006/ProjetAndroid/";
    EditText user, password;
    Button login;
    String pass, username, uid;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        user =  findViewById(R.id.txtuser);
        password =  findViewById(R.id.txtmdp);
        login =  findViewById(R.id.userlogin);


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

     /*   signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(MainActivity.this,Signup.class);
                startActivity(intent);
            }
        });*/
    }

    private class FindUserAsynTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            String pseudo = getIntent().getStringExtra(KEY_PSEUDO);
            String pass = getIntent().getStringExtra(KEY_MDP);
            httpParams.put(KEY_PSEUDO, pseudo);
            httpParams.put(KEY_MDP, pass);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "login.php", "GET", httpParams);
            System.out.println(jsonObject + "-------------------------------------------------");
            if (jsonObject == null) {
                System.out.println("JSON NULL");
            }
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray person;
                if (success == 1) {
                    person = jsonObject.getJSONArray(KEY_DATA);
                    JSONObject accounts = person.getJSONObject(0);
                    uid = accounts.getString(KEY_UID);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                public void run() {
                    login();
                }
            });
        }

        private void login() {

            if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                Intent intent = new Intent(getApplicationContext(), AccountUpdateOrDeleteActivity.class);
                intent.putExtra(KEY_UID, uid);
                startActivityForResult(intent, 20);

            } else {
                Toast.makeText(LoginActivity.this, "Unable to connect to internet", Toast.LENGTH_LONG).show();

            }
        }

    }
}




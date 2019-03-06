package com.example.projetmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success", KEY_PSEUDO ="pseudo",KEY_MDP = "mdp",STRING_EMPTY = "";
    private static final String BASE_URL = "https://pw.lacl.fr/~u21505006/ProjetAndroid/";
    private String pseudo, mdp;
    private EditText pseudoUser, mdpUser, mdp2User;
    private Button add;
    private int success;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        pseudoUser = findViewById(R.id.pseudo);
        mdpUser =findViewById(R.id.mdp);
        mdp2User = findViewById(R.id.mdp2);
        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    addUser();
                } else {
                    Toast.makeText(AddUserActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void addUser(){
        if (!STRING_EMPTY.equals(pseudoUser.getText().toString()) &&!STRING_EMPTY.equals(mdpUser.getText().toString())&&!STRING_EMPTY.equals(mdp2User.getText().toString())) {
            if(mdpUser.getText().toString().equals(mdp2User.getText().toString())) {
                pseudo = pseudoUser.getText().toString();
                mdp = mdpUser.getText().toString();
                new AddUserAsyncTask().execute();
            }else{
                Toast.makeText(AddUserActivity.this,
                        "password not the same !",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(AddUserActivity.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();

        }
    }
    private class AddUserAsyncTask extends AsyncTask<String,String,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddUserActivity.this);
            pDialog.setMessage("Add user.. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PSEUDO, pseudo);
            httpParams.put(KEY_MDP,mdp);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(BASE_URL + "add_user.php", "POST", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        Toast.makeText(AddUserActivity.this,
                                "User Added", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                        startActivityForResult(intent, 15);
                        finish();
                    }else if(success==2){
                        Toast.makeText(AddUserActivity.this,
                                "pseudo already taken, please change! ",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AddUserActivity.this,
                                "Some error occurred while adding person ",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

    }
}

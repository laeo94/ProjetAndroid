package com.example.projetmobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
//TODO probleme de recuperation du pseudo
public class DepenseActivity extends AppCompatActivity {
    private static final String STRING_EMPTY = "";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_ACCOUNT_ID = "aid";
    private static final String KEY_PSEUDO ="pseudo";
    private static final String KEY_PERSON_ID ="uid";
    private static final String BASE_URL = "https://pw.lacl.fr/~u21505006/ProjetAndroid/";
    private int success;
    private String personid,pseudostr, accountId;
    private TextView pseudo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depense);
        Button supp = findViewById(R.id.supp);
        Intent intent = getIntent();
        pseudo=findViewById(R.id.pseudo);
        personid = intent.getStringExtra(KEY_PERSON_ID);
        accountId=intent.getStringExtra(KEY_ACCOUNT_ID);
        pseudostr=intent.getStringExtra(KEY_PSEUDO);
        pseudo.setText(pseudostr);
        //new FetchPersonAsyncTask().execute();
        supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });
    }
    private void confirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DepenseActivity.this);
        alertDialogBuilder.setMessage("Are you sure, you want to delete "+pseudostr+" from this account?");
        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                            //If the user confirms deletion, execute DeleteAccountAsyncTask
                            new DepenseActivity.DeleteParticipateAsyncTask().execute();
                        } else {
                            Toast.makeText(DepenseActivity.this,
                                    "Unable to connect to internet",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private class DeleteParticipateAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PERSON_ID, personid);
            httpParams.put(KEY_ACCOUNT_ID,accountId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(BASE_URL + "delete_person_participate.php", "POST", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        //Display success message
                        Toast.makeText(DepenseActivity.this, "person  Deleted", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about account deletion
                        setResult(20, i);
                        finish();

                    } else {
                        Toast.makeText(DepenseActivity.this, "Some error occurred while deleting account", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }



}

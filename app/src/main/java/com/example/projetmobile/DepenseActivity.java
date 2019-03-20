package com.example.projetmobile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DepenseActivity extends AppCompatActivity {
    private static final String STRING_EMPTY = "";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_ACCOUNT_ID = "aid";
    private static final String KEY_PSEUDO ="pseudo";
    private static final String BASE_URL = "https://pw.lacl.fr/~u21505006/ProjetAndroid/";
    private int success;
    private String personfrom,pseudoto,personto, accountId,sommestr,detailstr;
    private TextView pseudo;
    private EditText somme, detail;
    private Button add;
    private ArrayList<HashMap<String, String>> depenseList;
    private RecyclerView depenseListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_depense);
        Button supp = findViewById(R.id.supp);
        Intent intent = getIntent();
        depenseListView = findViewById(R.id.recyclerView);
        pseudo=findViewById(R.id.pseudo);
        personfrom= intent.getStringExtra("idfrom");
        accountId=intent.getStringExtra(KEY_ACCOUNT_ID);
        pseudoto=intent.getStringExtra(KEY_PSEUDO);
        personto =intent.getStringExtra("idto");
        pseudo.setText(pseudoto);
        somme =findViewById(R.id.somme);
        detail =findViewById(R.id.detail);
        add =findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAdd();
            }
        });
        supp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });
        new AllSelectDepenseAsynTask().execute();
    }
    private class AllSelectDepenseAsynTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put("aid",accountId);
            httpParams.put("idto",personto);
            httpParams.put("idfrom",personfrom);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "select_all_depense_user.php", "GET",httpParams);
            if (jsonObject == null) {
                System.out.println("JSON NULL");
            }
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray depense;
                if (success == 1) {
                    depenseList = new ArrayList<>();
                    depense= jsonObject.getJSONArray(KEY_DATA);
                    for (int i = 0; i < depense.length(); i++) {
                        JSONObject depenses  = depense.getJSONObject(i);
                        String did = depenses.getString("did");
                        String somme =depenses.getString("somme");
                        String statut= depenses.getString("statut");
                        String detail =depenses.getString("detail");
                        String idto =depenses.getString("idto");
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("did",did);
                        map.put("statut",statut);
                        map.put("somme",somme);
                        map.put("detail",detail);
                        map.put("idto",idto);
                        depenseList.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                public void run() {
                    populateAccountList();
                }
            });
        }

        private void populateAccountList() {
            depenseListView.setLayoutManager((new LinearLayoutManager(getApplicationContext())));
            depenseListView.setAdapter(new MyAdapter(depenseList,personfrom));


        }

    }
    private void confirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DepenseActivity.this);
        alertDialogBuilder.setMessage("Are you sure, you want to delete "+pseudoto+" from this account?");
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
            httpParams.put("idto", personto);
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

    private void confirmAdd() {
        if (!STRING_EMPTY.equals(somme.getText().toString()) &&!STRING_EMPTY.equals(detail.getText().toString())) {
            sommestr=somme.getText().toString();
            detailstr = detail.getText().toString();
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DepenseActivity.this);
            alertDialogBuilder.setMessage("Are you sure, you want to add "+pseudoto+" ?");
            alertDialogBuilder.setPositiveButton("Add",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                                new DepenseActivity.AddDepenseAsyncTask().execute();
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
        } else {
            Toast.makeText(DepenseActivity.this, "One or more fields left empty!", Toast.LENGTH_LONG).show();

        }
    }
    private class AddDepenseAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_ACCOUNT_ID,accountId);
            httpParams.put("idfrom", personfrom);
            httpParams.put("idto", personto);
            httpParams.put("somme",sommestr);
            httpParams.put("detail",detailstr);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(BASE_URL + "add_depense.php", "POST", httpParams);
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
                        Toast.makeText(DepenseActivity.this, sommestr+" to "+pseudoto+" has been added", Toast.LENGTH_LONG).show();
                        somme.setText("");
                        detail.setText("");
                    } else {
                        Toast.makeText(DepenseActivity.this, "Some error occurred while add expenses to "+pseudoto, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}

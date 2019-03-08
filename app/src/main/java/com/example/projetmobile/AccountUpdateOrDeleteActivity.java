package com.example.projetmobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountUpdateOrDeleteActivity extends AppCompatActivity {
    //La base de donnée
    private static final String STRING_EMPTY = "";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_ACCOUNT_ID = "aid";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESC = "description";
    private static final String KEY_PERSON_ID ="uid";
    private static final String KEY_PSEUDO ="pseudo";
    private static final String KEY_DEV = "device";

    private static final String BASE_URL = "https://pw.lacl.fr/~u21505006/ProjetAndroid/";

    //les String utilise pour recupere et modifie
    private String accountId , pseudo ,titleaccount,desc,dev;
    //On recupere les donnée de la base de donnée et avec possiblite de modifie
    private EditText titleResult;
    private EditText descResult;
    private EditText devResult;

    //Partie Button
    private Button deleteButton;
    private Button updateButton;

    private int success;
    private ProgressDialog pDialog;
    private ArrayList<HashMap<String, String>> personList;
    private ListView personListView;
    private EditText rech ;
    private String rechstr;
    private String uid;
    private String userpseudo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_update_or_delete);
        Intent intent = getIntent();
        accountId = intent.getStringExtra(KEY_ACCOUNT_ID);
        // Partie Selection
        titleResult = findViewById(R.id.accountId);
        devResult =  findViewById(R.id.accountDev);
        descResult = findViewById(R.id.accountDesc);
        //Partie recherche
        personListView = findViewById(R.id.listperson);
        rech =findViewById(R.id.rech);
        new SelectAccountDetailAsynTask().execute();
        deleteButton = findViewById(R.id.btnDelete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });
        updateButton = findViewById(R.id.btnUpdate);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    updateAccount();
                } else {
                    Toast.makeText(AccountUpdateOrDeleteActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
        rech.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                        rechstr =rech.getText().toString();
                        new FoundUserAsynTask().execute();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    /**
     * Le but de AsynTask est de recupere la ligne correspondant dans la base et d'affiche sur les EditText.
     */
    private class SelectAccountDetailAsynTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(AccountUpdateOrDeleteActivity.this);
            pDialog.setMessage("Loading Account Details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_ACCOUNT_ID, accountId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(BASE_URL + "get_account.php", "GET", httpParams);
            System.out.println("accountId");
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONObject account;
                if (success == 1) {
                    account = jsonObject.getJSONObject(KEY_DATA);
                    titleaccount= account.getString(KEY_TITLE);
                    desc = account.getString(KEY_DESC);
                    dev = account.getString(KEY_DEV);
                }
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    //Populate the Edit Texts once the network activity is finished executing
                    titleResult.setText(titleaccount);
                    devResult.setText(dev);
                    descResult.setText(desc);


                }
            });
        }

    }
    private void updateAccount(){
        System.out.println("-------------------------------------------------ha");
        if(!STRING_EMPTY.equals(titleResult.getText().toString()) && !STRING_EMPTY.equals(devResult.getText().toString())) {
            System.out.println("-------------------------------------------------haa");
          titleaccount =titleResult.getText().toString();
          desc = descResult.getText().toString();
          dev = devResult.getText().toString();
          new UpdateAccountAsynTask().execute();
        }else{
            Toast.makeText(AccountUpdateOrDeleteActivity.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();
        }
    }
    /**
     * Asyn permet de modifie les donnée qui se trouve dans les Edit Texte
     */
    private class UpdateAccountAsynTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(AccountUpdateOrDeleteActivity.this);
            pDialog.setMessage("Updating Account. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_ACCOUNT_ID, accountId);
            httpParams.put(KEY_TITLE, titleaccount);
            httpParams.put(KEY_DESC, desc);
            httpParams.put(KEY_DEV,dev);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(BASE_URL + "update_account.php", "POST", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result) {
            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        Toast.makeText(AccountUpdateOrDeleteActivity.this, "Account Updated", Toast.LENGTH_LONG).show();
                        Intent intent = getIntent();
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AccountUpdateOrDeleteActivity.this, "Some error occurred while updating Account", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    /**
     * Confirmation d'efface
     */
    private void confirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccountUpdateOrDeleteActivity.this);
        alertDialogBuilder.setMessage("Are you sure, you want to delete this account?");
        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                            //If the user confirms deletion, execute DeleteAccountAsyncTask
                            new DeleteAccountAsyncTask().execute();
                        } else {
                            Toast.makeText(AccountUpdateOrDeleteActivity.this,
                                    "Unable to connect to internet",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private class DeleteAccountAsyncTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(AccountUpdateOrDeleteActivity.this);
            pDialog.setMessage("Deleting Account. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_ACCOUNT_ID, accountId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(BASE_URL + "delete_account.php", "POST", httpParams);
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
                        //Display success message
                        Toast.makeText(AccountUpdateOrDeleteActivity.this, "Account Deleted", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        //send result code 20 to notify about account deletion
                        setResult(20, i);
                        finish();

                    } else {
                        Toast.makeText(AccountUpdateOrDeleteActivity.this, "Some error occurred while deleting account", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    private class FoundUserAsynTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put("rech", rechstr);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(BASE_URL + "found_user.php", "GET", httpParams);
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray person;
                if (success == 1) {
                    personList = new ArrayList<>();
                    person= jsonObject.getJSONArray(KEY_DATA);
                    for (int i = 0; i < person.length(); i++) {
                        JSONObject users  = person.getJSONObject(i);
                        String userId = users.getString(KEY_PERSON_ID);
                        String pseudo= users.getString(KEY_PSEUDO);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_PERSON_ID, userId);
                        map.put(KEY_PSEUDO,pseudo);
                        personList.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result) {

            pDialog.dismiss();
            runOnUiThread(new Runnable() {
                public void run() {
                    populateRechearchList();
                }
            });
        }

        private void populateRechearchList() {
            ListAdapter adapter = new SimpleAdapter(
                    AccountUpdateOrDeleteActivity.this, personList, R.layout.list_item, new String[]{
                    KEY_PERSON_ID, KEY_PSEUDO}, new int[]{R.id.textView, R.id.textView1});
            personListView.setAdapter(adapter);

            personListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    uid = ((TextView) findViewById(R.id.textView)).getText().toString();
                    userpseudo = ((TextView) findViewById(R.id.textView1)).getText().toString();
                   confirmAdd();
                }
            });
        }

    }
    private void confirmAdd() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccountUpdateOrDeleteActivity.this);
        alertDialogBuilder.setMessage("Are you sure, you want to add "+userpseudo+" ?");
        alertDialogBuilder.setPositiveButton("Add",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                            //If the user confirms deletion, execute DeleteAccountAsyncTask
                            new AccountUpdateOrDeleteActivity.AddPersonAsyncTask().execute();
                        } else {
                            Toast.makeText(AccountUpdateOrDeleteActivity.this,
                                    "Unable to connect to internet",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private class AddPersonAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PERSON_ID, uid);
            httpParams.put(KEY_ACCOUNT_ID,accountId);
            System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+uid+" "+accountId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(BASE_URL + "add_user_participations.php", "POST", httpParams);
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
                        Toast.makeText(AccountUpdateOrDeleteActivity.this, userpseudo+" has been added", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(AccountUpdateOrDeleteActivity.this, "Some error occurred while deleting person", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

}
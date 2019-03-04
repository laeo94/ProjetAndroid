package com.example.projetmobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

public class AccountUpdateOrDeleteActivity extends AppCompatActivity {
    //La base de donnée
    private static final String STRING_EMPTY = "";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_ACCOUNT_ID = "aid";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DESC = "description";
    private static final String KEY_DEV = "device";
    private static final String KEY_PID = "pid";

    private static final String BASE_URL = "https://pw.lacl.fr/~u21402914/ProjetAndroid/";

    //les String utilise pour recupere et modifie
    private String accountId;
    private String titleaccount;
    private String desc;
    private String dev;
    private String pid;

    //On recupere les donnée de la base de donnée et avec possiblite de modifie
    private EditText titleResult;
    private EditText descResult;
    private EditText devResult;

    //Partie Button
    private Button deleteButton;
    private Button updateButton;

    private int success;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_update_or_delete);
        Intent intent = getIntent();
        accountId = intent.getStringExtra(KEY_ACCOUNT_ID);
        // Partie Selection
        titleResult = (EditText) findViewById(R.id.accountId);
        devResult = (EditText) findViewById(R.id.accountDev);
        descResult = (EditText) findViewById(R.id.accountDesc);

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
                    System.out.println("-------------------------------------------------h");
                    updateAccount();
                } else {
                    Toast.makeText(AccountUpdateOrDeleteActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();
                }

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
            System.out.println("-------------------------------------------------haaa");
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_ACCOUNT_ID, accountId);
            httpParams.put(KEY_TITLE, titleaccount);
            httpParams.put(KEY_DESC, desc);
            httpParams.put(KEY_DEV,dev);
            /*httpParams.put(KEY_PID,pid);*/
            System.out.println("ppppppppppppppppppppppppppppppppppp"+desc);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(BASE_URL + "update_account.php", "POST", httpParams);
            System.out.println("-------------------------------------------------haaaa");
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
                        System.out.println("-------------------------------------------------haaaaaa");
                        setResult(20, intent);
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
}
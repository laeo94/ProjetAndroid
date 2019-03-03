package com.example.projetmobile;

import android.annotation.SuppressLint;
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
//<<<<<<< HEAD
 import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountHomeActivity extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_PERSON_ID = "pid";
    private  static  final  String KEY_PSEUDO ="pseudo";
    private static final String BASE_URL = "https://pw.lacl.fr/~u21505006/ProjetAndroid/";
    int success;
    private String personId;
//d7f46b314d074129c33cc71ce3580cf1ee0d08f9
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_home);
        personId = getIntent().getStringExtra(KEY_PERSON_ID);
        Button viewAllBtn = (Button) findViewById(R.id.viewAllBtn);
        Button addNewBtn = (Button) findViewById(R.id.addNewBtn);
        final Button updatePerson = (Button) findViewById(R.id.updateperson);
        final EditText pseudo =findViewById(R.id.textView3);
        final Button ok = findViewById(R.id.ok);
        final Button delete = findViewById(R.id.button7);
        ok.setVisibility(View.INVISIBLE);
        pseudo.setVisibility(View.INVISIBLE);
        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext(),AccountListingActivity.class);
                    intent.putExtra(KEY_PERSON_ID,personId);
                    System.out.println("ACCOUNTHOMEACTIVITY PERSON ID ------------------"+personId);
                    startActivity(intent);
                }else{
                    Toast.makeText(AccountHomeActivity.this,"Unable to connect to internet",Toast.LENGTH_LONG).show();
                }
            }
        });
        addNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext(),AddAccountActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(AccountHomeActivity.this,"Unable to connect to internet",Toast.LENGTH_LONG).show();
                }
            }
        });

        updatePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePerson.setVisibility(View.INVISIBLE);
                pseudo.setVisibility(View.VISIBLE);
                ok.setVisibility(View.VISIBLE);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDelete();
            }
        });
    }


    private void confirmDelete() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AccountHomeActivity.this);
        alertDialogBuilder.setMessage("Are you sure, you want to delete this account?");
        alertDialogBuilder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                            //If the user confirms deletion, execute DeleteAccountAsyncTask
                            new AccountHomeActivity.DeletePersonAsyncTask().execute();
                        } else {
                            Toast.makeText(AccountHomeActivity.this,
                                    "Unable to connect to internet",
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancel", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private class DeletePersonAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PERSON_ID, personId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(BASE_URL + "delete_person.php", "POST", httpParams);
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
                        Toast.makeText(AccountHomeActivity.this, "Person Deleted", Toast.LENGTH_LONG).show();
                        finish();
                        Intent i = new Intent(getApplicationContext(),ChoicePerson.class);
                        startActivity(i);

                    } else {
                        Toast.makeText(AccountHomeActivity.this, "Some error occurred while deleting person", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}

package com.example.projetmobile;

import android.app.AlertDialog;
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

public class AccountHomeActivity extends AppCompatActivity {
    private static final String STRING_EMPTY = "";
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_PERSON_ID = "uid";
    private  static  final  String KEY_PSEUDO ="pseudo";
    private static final String BASE_URL = "https://pw.lacl.fr/~u21402914/ProjetAndroid/";
    int success;
    private Button updatePerson;
    private  Button ok;
    private  EditText pseudo;
    private String personId;
    private Button cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_home);
        personId = getIntent().getStringExtra(KEY_PERSON_ID);
        Button viewAllBtn = (Button) findViewById(R.id.viewAllBtn);
        Button addNewBtn = (Button) findViewById(R.id.addNewBtn);
        updatePerson = (Button) findViewById(R.id.updateperson);
        pseudo =findViewById(R.id.textView3);
        ok = findViewById(R.id.ok);
        cancel = findViewById(R.id.button2);
        cancel.setVisibility(View.INVISIBLE);
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
                cancel.setVisibility(View.VISIBLE);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatePerson.setVisibility(View.VISIBLE);
                        pseudo.setVisibility(View.INVISIBLE);
                        ok.setVisibility(View.INVISIBLE);
                        cancel.setVisibility(View.INVISIBLE);
                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updatePseudo();
                    }
                });

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
    private void updatePseudo(){
        if(!STRING_EMPTY.equals(pseudo.getText().toString())) {
            new AccountHomeActivity.UpdatePersonAsynTask().execute();
        }else{
            Toast.makeText(AccountHomeActivity.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();
        }
    }
    private class UpdatePersonAsynTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PERSON_ID,personId);
            httpParams.put(KEY_PSEUDO,pseudo.getText().toString());
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(BASE_URL + "update_person.php", "POST", httpParams);
            try {
                success = jsonObject.getInt(KEY_SUCCESS);
            }catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String result) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (success == 1) {
                        Toast.makeText(AccountHomeActivity.this, "Person Updated", Toast.LENGTH_LONG).show();
                        ok.setVisibility(View.INVISIBLE);
                        pseudo.setVisibility(View.INVISIBLE);
                        updatePerson.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(AccountHomeActivity.this, "Some error occurred while updating Person", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
}

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
    private Button updatePerson, ok, cancel,delete,viewAllBtn, addNewBtn;
    private EditText pseudo;
    private String personId;
    private int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_home);
        personId = getIntent().getStringExtra(MainActivity.KEY_PERSON_ID);
        viewAllBtn = findViewById(R.id.viewAllBtn);
        addNewBtn =findViewById(R.id.addNewBtn);
        updatePerson = findViewById(R.id.updateperson);
        pseudo =findViewById(R.id.textView3);
        ok = findViewById(R.id.ok);
        cancel = findViewById(R.id.button2);
        cancel.setVisibility(View.INVISIBLE);
        delete = findViewById(R.id.button7);
        ok.setVisibility(View.INVISIBLE);
        pseudo.setVisibility(View.INVISIBLE);
        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext(),AccountListingActivity.class);
                    intent.putExtra(MainActivity.KEY_PERSON_ID,personId);
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
                    intent.putExtra(MainActivity.KEY_PERSON_ID,personId);
                    startActivity(intent);
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
        alertDialogBuilder.setMessage("Are you sure, you want to delete you ?");
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
            httpParams.put(MainActivity.KEY_PERSON_ID, personId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(MainActivity.BASE_URL + "delete_user.php", "POST", httpParams);
            try {
                success = jsonObject.getInt(MainActivity.KEY_SUCCESS);
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
                        Intent i = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        Toast.makeText(AccountHomeActivity.this, "Some error occurred while deleting person", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    private void updatePseudo(){
        if(!pseudo.getText().toString().isEmpty()) {
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
            httpParams.put(MainActivity.KEY_PERSON_ID,personId);
            httpParams.put(MainActivity.KEY_PSEUDO,pseudo.getText().toString());
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(MainActivity.BASE_URL + "update_user.php", "POST", httpParams);
            try {
                success = jsonObject.getInt(MainActivity.KEY_SUCCESS);
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
                        cancel.setVisibility(View.INVISIBLE);
                    }else if(success == 2){
                        Toast.makeText(AccountHomeActivity.this, "Pseudo already taken please choose a nother", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(AccountHomeActivity.this, "Some error occurred while updating Person", Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }
}

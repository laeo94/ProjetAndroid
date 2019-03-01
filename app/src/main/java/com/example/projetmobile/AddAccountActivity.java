
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

public class AddAccountActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_TiTLE = "title";
    private static final String KEY_DESC = "description";
    private static final String KEY_DEV = "device";
    private static final String KEY_PID= "pid";
    private static final String BASE_URL = "https://pw.lacl.fr/~u21402914/ProjetAndroid/";
    private static String STRING_EMPTY = "";

    private EditText accountTitleEditText;
    private EditText accountDescEditText;
    private EditText accountDevEditText;
    private EditText accountPidEditText;

    private String accountTitle;
    private String accountDesc;
    private String accountDev;
    private String accountPid;
    private Button addButton;
    private int success;
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        accountTitleEditText = (EditText) findViewById(R.id.txtAccountTitleAdd);
        accountDescEditText = (EditText) findViewById(R.id.txtAccountDescAdd);
        accountDevEditText = (EditText) findViewById(R.id.txtAccountDevAdd);
        accountPidEditText = (EditText) findViewById(R.id.txtpidadd);
        addButton = (Button) findViewById(R.id.btnAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    addAccount();
                } else {
                    Toast.makeText(AddAccountActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    private void addAccount() {
        if (!STRING_EMPTY.equals(accountTitleEditText.getText().toString()) && !STRING_EMPTY.equals(accountDescEditText.getText().toString()) && !STRING_EMPTY.equals(accountDevEditText.getText().toString())) {
            accountTitle = accountTitleEditText.getText().toString();
            accountDesc = accountDescEditText.getText().toString();
            accountDev = accountDevEditText.getText().toString();
            accountPid = accountPidEditText.getText().toString();
            new AddAccountAsynTask().execute();
        } else {
            Toast.makeText(AddAccountActivity.this, "One or more fields left empty!", Toast.LENGTH_LONG).show();

        }
    }

    private class AddAccountAsynTask extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute(){
        super.onPreExecute();
        //Display progress bar
        pDialog = new ProgressDialog(AddAccountActivity.this);
        pDialog.setMessage("Adding Account. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
    }
        @Override
        protected String doInBackground(String... params) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_TiTLE, accountTitle);
            httpParams.put(KEY_DESC, accountDesc);
            httpParams.put(KEY_DEV, accountDev);
            httpParams.put(KEY_PID,accountPid);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "add_account.php", "POST", httpParams);
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
                        Toast.makeText(AddAccountActivity.this, "Account Added", Toast.LENGTH_LONG).show();
                        Intent i = getIntent();
                        setResult(20, i);
                        finish();
                    } else {
                        Toast.makeText(AddAccountActivity.this, "Some error occurred while adding account", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }
}


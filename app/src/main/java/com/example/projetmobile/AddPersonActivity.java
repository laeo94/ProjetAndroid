package com.example.projetmobile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddPersonActivity extends AppCompatActivity {

    private static final String KEY_SUCCESS = "success";
    private static final String KEY_PSEUDO = "pseudo";
    private static String STRING_EMPTY = "";
    private static final String BASE_URL = "https://pw.lacl.fr/~u21402914/ProjetAndroid/";
    private String strperson;
    private EditText pseudoPerson;
    private Button add;
    private int success;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        pseudoPerson =findViewById(R.id.textView2555);
        add =findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    addPerson();
                } else {
                    Toast.makeText(AddPersonActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    private void addPerson() {
        if (!STRING_EMPTY.equals(pseudoPerson.getText().toString())) {
            strperson = pseudoPerson.getText().toString();
            new AddPersonAsyncTask().execute();
        } else {
            Toast.makeText(AddPersonActivity.this,
                    "One or more fields left empty!",
                    Toast.LENGTH_LONG).show();

        }


    }

    private class AddPersonAsyncTask extends AsyncTask<String,String,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Display progress bar
            pDialog = new ProgressDialog(AddPersonActivity.this);
            pDialog.setMessage("Updating Account. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            //Populating request parameters
            httpParams.put(KEY_PSEUDO, strperson);

            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "add_person.php", "POST", httpParams);
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
                        Toast.makeText(AddPersonActivity.this,
                                "Person Added", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),  ListParticipateActivity.class);
                        startActivityForResult(intent, 15);
                        finish();

                    } else {
                        Toast.makeText(AddPersonActivity.this,
                                "Some error occurred while adding person",
                                Toast.LENGTH_LONG).show();

                    }
                }
            });
        }

    }
}

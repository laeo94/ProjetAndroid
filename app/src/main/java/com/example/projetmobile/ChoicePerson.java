package com.example.projetmobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
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

public class ChoicePerson extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_PERSON_ID = "pid";
    private static final String KEY_LASTNAME ="lastname";
    private static final String KEY_FIRSTNAME="firstname";
    private static final String BASE_URL = "https://pw.lacl.fr/~u21505006/ProjetAndroid/";
    private ArrayList<HashMap<String, String>> personList;
    private ListView personListView;
    private Button nouveau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_person);
        personListView = findViewById(R.id.personList);
        nouveau = findViewById(R.id.button);
        new FetchPersonAsyncTask().execute();
        nouveau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Cliker sur new --------------------------------------");
            }
        });
    }

    //Fetche the list of person from the server
    private class FetchPersonAsyncTask extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "select_all_person.php", "GET", null);
            if (jsonObject == null) {
                System.out.println("JSON NULL");
            }
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray person;
                if (success == 1) {
                    personList = new ArrayList<>();
                    person= jsonObject.getJSONArray(KEY_DATA);
                    for (int i = 0; i < person.length(); i++) {
                        JSONObject accounts  = person.getJSONObject(i);
                        String lastname = accounts.getString(KEY_LASTNAME );
                        String firstname = accounts.getString(KEY_FIRSTNAME );
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_LASTNAME , lastname);
                        map.put(KEY_FIRSTNAME, firstname);
                        personList.add(map);
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
    }

    //Updating parsed JSON data into ListView
    private void populateAccountList() {
        ListAdapter adapter = new SimpleAdapter(
                ChoicePerson.this, personList,
                R.layout.list_item, new String[]{KEY_LASTNAME,
                KEY_FIRSTNAME },
                new int[]{R.id.lastname, R.id.firstname});

        // updating listview
        personListView.setAdapter(adapter);
        //Call MovieUpdateDeleteActivity when a movie is clicked
        personListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    String accountId = ((TextView) view.findViewById(R.id.lastname))
                            .getText().toString();
                    /*Intent intent = new Intent(getApplicationContext(),
                            AccountUpdateDeleteActivity.class);
                    intent.putExtra(KEY_ACCOUNT_ID, accountId);
                    startActivityForResult(intent, 20);*/

                } else {
                    Toast.makeText(ChoicePerson.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }


            }
        });

    }
}

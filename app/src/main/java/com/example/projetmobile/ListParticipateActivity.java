package com.example.projetmobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.Map;

public class ListParticipateActivity extends AppCompatActivity {
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_ACCOUNT_ID = "aid";
    private static final String KEY_PERSON_ID = "uid";
    private static final String KEY_PSEUDO = "pseudo";
    private static final String BASE_URL = "https://pw.lacl.fr/~u21505006/ProjetAndroid/";
    private ArrayList<HashMap<String, String>> personList;
    private ListView personListView;
    private String accountId, personfrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_part);
        personListView = findViewById(R.id.personList);
        Intent intent = getIntent();
        accountId = intent.getStringExtra(KEY_ACCOUNT_ID);
        personfrom =intent.getStringExtra(KEY_PERSON_ID);
        Button info;
        info = findViewById(R.id.Information);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    Intent i = new Intent(getApplicationContext(),
                            AccountUpdateOrDeleteActivity.class);
                    i.putExtra(KEY_ACCOUNT_ID,accountId);
                    startActivity(i);
                } else {
                    //Display error message if not connected to internet
                    Toast.makeText(ListParticipateActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }

            }
        });
        new FetchPersonAsyncTask().execute();
    }

    private class FetchPersonAsyncTask extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_ACCOUNT_ID, accountId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "select_all_person_participate.php", "GET", httpParams);
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
                        String  pseudo = accounts.getString(KEY_PSEUDO);
                        String uid = accounts.getString(KEY_PERSON_ID );
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_PERSON_ID, uid);
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
            runOnUiThread(new Runnable() {
                public void run() {
                    populatePersonList();

                }
            });
        }
    }

    //Updating parsed JSON data into ListView
    private void populatePersonList() {
        final ListAdapter adapter = new SimpleAdapter(
                ListParticipateActivity.this, personList,
                R.layout.list_item, new String[]{KEY_PERSON_ID,
                KEY_PSEUDO},
                new int[]{R.id.textView, R.id.textView1});

        // updating listview
        personListView.setAdapter(adapter);
        //Call MovieUpdateDeleteActivity when a person is clicked
        personListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Check for network connectivity
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                    String personto = ((TextView) view.findViewById(R.id.textView)).getText().toString();
                    String pseudo = ((TextView) view.findViewById(R.id.textView1)).getText().toString();
                    Intent intent = new Intent(getApplicationContext(),DepenseActivity.class);
                    intent.putExtra("idto",personto);
                    intent.putExtra("idfrom",personfrom);
                    intent.putExtra(KEY_PSEUDO,pseudo);
                    intent.putExtra(KEY_ACCOUNT_ID, accountId);
                    startActivityForResult(intent, 20);
                    finish();
                } else {
                    Toast.makeText(ListParticipateActivity.this,
                            "Unable to connect to internet",
                            Toast.LENGTH_LONG).show();

                }


            }
        });


    }


}
package com.example.projetmobile;
//TODO faire un ajoute dans la liste de participation pour pouboir voir dans la prend liste ou alors modifie
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

public class AccountListingActivity extends AppCompatActivity {
    //les nom des clé dit etre les meme que ce qui sont dans la table
    private static final String KEY_SUCCESS = "success";
    private static final String KEY_DATA = "data";
    private static final String KEY_ACCOUNT_ID = "aid";
    private static final String KEY_TITLE = "title";
    private static final String KEY_PERSON_ID = "uid";
    private static final String BASE_URL = "https://pw.lacl.fr/~u21505006/ProjetAndroid/";
    private ArrayList<HashMap<String, String>> accountList;
    private ListView accountListView;
    private ProgressDialog pDialog;
    private String personId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_listing);
        accountListView = findViewById(R.id.accountList);
        personId = getIntent().getStringExtra(KEY_PERSON_ID);
        new AllSelectAccountAsynTask().execute();
    }

    /**
     * Selection tous les nom de compte existe dans la base de donnée est affiche
     */
    private class AllSelectAccountAsynTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AccountListingActivity.this);
            pDialog.setMessage("Loading account. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpJsonParser httpJsonParser = new HttpJsonParser();
            Map<String, String> httpParams = new HashMap<>();
            httpParams.put(KEY_PERSON_ID,personId);
            JSONObject jsonObject = httpJsonParser.makeHttpRequest(
                    BASE_URL + "select_person_all_account.php", "GET",httpParams);
            if (jsonObject == null) {
                System.out.println("JSON NULL");
            }
            try {
                int success = jsonObject.getInt(KEY_SUCCESS);
                JSONArray person;
                if (success == 1) {
                    accountList = new ArrayList<>();
                    person= jsonObject.getJSONArray(KEY_DATA);
                    for (int i = 0; i < person.length(); i++) {
                        JSONObject accounts  = person.getJSONObject(i);
                        String accountId = accounts.getString(KEY_ACCOUNT_ID);
                        String title= accounts.getString(KEY_TITLE );
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(KEY_ACCOUNT_ID, accountId);
                        map.put(KEY_TITLE,title);
                        accountList.add(map);
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
                    populateAccountList();
                }
            });
        }

        private void populateAccountList() {
            ListAdapter adapter = new SimpleAdapter(
                    AccountListingActivity.this, accountList, R.layout.list_item, new String[]{
                    KEY_ACCOUNT_ID, KEY_TITLE}, new int[]{R.id.textView, R.id.textView1});
            accountListView.setAdapter(adapter);

            accountListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())) {
                        String accountId = ((TextView) view.findViewById(R.id.textView)).getText().toString();
                        Intent intent = new Intent(getApplicationContext(), ListParticipateActivity.class);
                        intent.putExtra(KEY_ACCOUNT_ID, accountId);
                        intent.putExtra(KEY_PERSON_ID,personId);
                        startActivityForResult(intent, 10);


                    } else {
                        Toast.makeText(AccountListingActivity.this, "Unable to connect to internet", Toast.LENGTH_LONG).show();

                    }

                }
            });
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(resultCode==20){
            Intent refresh = new Intent(this, AccountListingActivity.class);
            startActivity(refresh);
            this.finish();
        }
    }
}

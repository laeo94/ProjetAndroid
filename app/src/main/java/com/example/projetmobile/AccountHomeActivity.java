package com.example.projetmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//TODO le nom de cette classe
public class AccountHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_home);
        Button viewAllBtn = (Button) findViewById(R.id.viewAllBtn);
        Button addNewBtn = (Button) findViewById(R.id.addNewBtn);
        viewAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkStatus.isNetworkAvailable(getApplicationContext())){
                    Intent intent = new Intent(getApplicationContext(),AccountListingActivity.class);
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
                }else{
                    Toast.makeText(AccountHomeActivity.this,"Unable to connect to internet",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

package com.example.kryptex;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText input ;
    TextView ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences c_a = getSharedPreferences("address",MODE_PRIVATE);
        String s = c_a.getString("current_address","Your wallet adress");
        ad = (TextView) findViewById(R.id.textView);
        String f = "YOUR ADRESS IS:\n" + s;
        ad.setText(f);
    }

    public void Saveadress(View view){
        input = (EditText) findViewById(R.id.walletaddress);
        ad = (TextView) findViewById(R.id.textView);
        String input_address = input.getText().toString();
        SharedPreferences address = getSharedPreferences("address",MODE_PRIVATE);
        SharedPreferences.Editor mining_address = address.edit();
        String f = "YOUR ADRESS IS:\n" + input_address;
        mining_address.putString("current_address",input_address);
        mining_address.apply();
        Toast.makeText(this,"Your Address is saved sucessful",Toast.LENGTH_SHORT).show();
        ad.setText(f);
    }

}
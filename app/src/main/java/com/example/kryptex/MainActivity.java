package com.example.kryptex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        oldSwitch();
    }

    String on = "On",off = "Off";
    SwitchCompat not;

    public void cardClick(View view){
        startActivity(new Intent(MainActivity.this,LoginActivity.class));
    }


    public void switchHandler(View view){
        not = findViewById(R.id.switch1);
        boolean b = not.isChecked();
        SharedPreferences switchState = getSharedPreferences("switch",MODE_PRIVATE);
        SharedPreferences.Editor switchEdit = switchState.edit();
        switchEdit.putBoolean("oldstate",b);
        switchEdit.apply();
        Log.d("pranav","switchHandler ran successfully");
        if (b){
            not.setText(on);
        }
        else{
            not.setText(off);
        }
    }

    public void oldSwitch(){
        not = (SwitchCompat) findViewById(R.id.switch1);
        SharedPreferences switchState = getSharedPreferences("switch",0);
        boolean b = switchState.getBoolean("oldstate",false);
        not.setChecked(b);
        if (b){
            not.setText(on);
        }
        else{
            not.setText(off);
        }
        Log.d("pranav","oldSwitch ran successfully");
    }
}
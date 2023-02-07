package com.example.kryptex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ResourceCursorAdapter;
import android.widget.Switch;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;



import java.io.IOException;

public class MainActivity extends AppCompatActivity {
//    private TextView scrap;
    private TextView workers,hashrate,avg_hashrate,balance;
    String on = "On",off = "Off";
    Switch not;


    public class Background extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {

            try {
                Document doc = (Document) Jsoup.connect(strings[0]).get();
                Elements e = doc.getElementsByClass("font-bold text-2xl md:text-lg xl:text-2xl");
                String[] words=e.text().split("\\s");
//                notificationPusher(words[0],words[1],words[3],words[5],0);
//                scrap.setText(e.text());
                workers.setText(words[0]);
                hashrate.setText(words[1]);
                avg_hashrate.setText(words[3]);
                balance.setText(words[5]);
                return e.text();
            }
            catch (Exception e){
                return "Something went wrong";
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        workers = findViewById(R.id.workers);
        hashrate = findViewById(R.id.hashrate);
        avg_hashrate = findViewById(R.id.avg_hashrate);
        balance = findViewById(R.id.balance);

        SharedPreferences switchState = getSharedPreferences("switch",0);
        boolean b = switchState.getBoolean("oldstate",false);

        oldSwitch();

        String url1 = "https://pool.kryptex.com/en/xmr/miner/stats/";
        SharedPreferences address = getSharedPreferences("address",MODE_PRIVATE);
        String url2 = address.getString("current_address","abcd");

        Background bg = new Background();
        bg.execute(url1+url2);

        if (b){
            startService(new Intent(this, MyService.class));
        }
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        SharedPreferences switchState = getSharedPreferences("switch",0);
//        boolean b = switchState.getBoolean("oldstate",false);
//        if (b){
//            startService(new Intent(this, MyService.class));
//        }
//    }

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
        if (b){
            not.setText(on);
        }
        else{
            not.setText(off);
        }
        oldSwitch();
    }

    public void oldSwitch(){
        not =  findViewById(R.id.switch1);
        SharedPreferences switchState = getSharedPreferences("switch",0);
        boolean b = switchState.getBoolean("oldstate",false);
        not.setChecked(b);
        if (b){
            not.setText(on);
        }
        else{
            not.setText(off);
        }
    }


}
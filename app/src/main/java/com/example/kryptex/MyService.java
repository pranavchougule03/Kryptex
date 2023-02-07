package com.example.kryptex;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private static final String CHANNEL_ID = "id 1";
    private static final int NOTIFICATION_ID = 1;

    public class Background extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                Document doc = (Document) Jsoup.connect(strings[0]).get();
                String s = doc.title();
                Elements e = doc.getElementsByClass("font-bold text-2xl md:text-lg xl:text-2xl");
                String[] words=e.text().split("\\s");
                notificationPusher(words[0],words[1],words[3],words[5],0);
//                scrap.setText(e.text());
                return e.text();
            }
            catch (Exception e){
                return "Something went wrong";
            }
        }
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MyService.Background s = new MyService.Background();
        String url1 = "https://pool.kryptex.com/en/xmr/miner/stats/";
        SharedPreferences address = getSharedPreferences("address",MODE_PRIVATE);
        String url2 = address.getString("current_address","abcd");
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                       new MyService.Background().execute(url1+url2);
                                  }

                              },
                0,
                600000);
//        return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent(this, MyReceiver.class);

        sendBroadcast(broadcastIntent);
//        stoptimertask();
    }

    public void notificationPusher(String worker,String curhash,String avghash,String balance,int color){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) ResourcesCompat.getDrawable(getResources(),R.drawable.kryptex,null);
        Bitmap largeIcon = bitmapDrawable.getBitmap();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(this)
                    .setLargeIcon(largeIcon)
                    .setSmallIcon(R.drawable.kryptex)
                    .setContentTitle("Current Stats..")
                    .setContentText("WORKERS: "+worker+"\nCURRENT HASHRATE: "+curhash+"AVG. HASHRATE: "+avghash+"\nBALENCE DUE: "+balance)
                    .setColor(Color.RED)
                    .setColorized(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("WORKERS: "+worker+"\nCURRENT HASHRATE: "+curhash+"\nAVG. HASHRATE: "+avghash+"\nBALENCE DUE: "+balance))
                    .setChannelId(CHANNEL_ID)
                    .build();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"stats",NotificationManager.IMPORTANCE_HIGH));
        }


        notificationManager.notify(NOTIFICATION_ID,notification);

    }
}
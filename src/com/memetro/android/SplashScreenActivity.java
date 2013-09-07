package com.memetro.android;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

import com.memetro.android.oauth.Utils;

public class SplashScreenActivity extends Activity {

    private long splashDelay = 800; //0.8 secs
    private Utils Utils = new Utils();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent().setClass(SplashScreenActivity.this, MainActivity.class);
                if (!"".equals(Utils.getToken(getApplicationContext()))){
                    mainIntent = new Intent().setClass(SplashScreenActivity.this, DashboardActivity.class);
                }
                mainIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(mainIntent);
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, splashDelay);
    }

}

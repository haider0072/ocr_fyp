package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread splashThread = new Thread()
        {
            @Override
            public void run() {
                try
                {
                    sleep(2000);
                    SharedPref sharedPref = new SharedPref(getApplicationContext());
                    Intent main_activityIntent;
                    if(sharedPref.isUserLogin()){
                        main_activityIntent = new Intent(getApplicationContext(), result_activity.class);
                    }
                    else{
                        main_activityIntent = new Intent(getApplicationContext(), login.class);
                    }
                    startActivity(main_activityIntent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        splashThread.start();
    }
}

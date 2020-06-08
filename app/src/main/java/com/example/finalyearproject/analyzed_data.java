package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class analyzed_data extends AppCompatActivity {

    TextView sName,sDate,sPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyzed_data);
        sName = findViewById(R.id.storeName);
        sDate = findViewById(R.id.storeDate);
        sPrice = findViewById(R.id.storePrice);

        Intent intent = getIntent();
        String text = intent.getStringExtra(result_activity.EXTRA_TEXT);
        String textD = intent.getStringExtra(result_activity.EXTRA_DATE);
        sName.setText(text);
        sDate.setText(textD);
    }



}

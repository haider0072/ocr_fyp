package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class analyzed_data extends AppCompatActivity {

    TextView sName,sDate,sPrice;
    Button confirm;

    MyHelper myHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyzed_data);
        sName = findViewById(R.id.storeName);
        sDate = findViewById(R.id.storeDate);
        sPrice = findViewById(R.id.storePrice);
        confirm = findViewById(R.id.confirm);
        myHelper = new MyHelper(this);
        sqLiteDatabase = myHelper.getWritableDatabase();


        Intent intent = getIntent();
        String text = intent.getStringExtra(result_activity.EXTRA_TEXT);
        String textD = intent.getStringExtra(result_activity.EXTRA_DATE);
        String textA = intent.getStringExtra(result_activity.EXTRA_AMOUNT);

//        SimpleDateFormat secondsimpleDateFormat = new SimpleDateFormat("MM/DD/YY|DD/MM/YY|YYYY, Mon DD|DD Mon, YYYY|DD-MM-YYYY");
//        Date dateTwo = null;
//        try{
//            dateTwo = secondsimpleDateFormat.parse(textD);
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-mm-yyyy");
//            String finalSecondDate = simpleDateFormat.format(dateTwo);
//            sDate.setText(finalSecondDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        sDate.setText(textD);
        sName.setText(text);
        sPrice.setText(textA);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String storeName = sName.getText().toString();
                String dateName = sDate.getText().toString();
                String priceName = sPrice.getText().toString();
                //showMessage("Sana lobe yooooou");
                showMessage(storeName+" "+dateName+" "+priceName);
                if(myHelper.insertData(storeName,"","",dateName,priceName)){
                    showMessage("Information inserted success!");
                }
                else{
                    showMessage("Dua hate yooooou");
                }
//                String storen = myHelper.ChkExistingUser();
//                showMessage("Data from DB"+storen);
            }
        });
    }

    void showMessage(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }



}

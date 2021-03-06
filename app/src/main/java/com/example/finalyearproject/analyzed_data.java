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
import java.time.Year;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class analyzed_data extends AppCompatActivity {
    //Declarations
    TextView sName,sDate,sPrice,sDay,sMonth,sYear;
    Button confirm,cancel;
    String day,month,year;

    //sqlite declaration

    MyHelper myHelper;
    SQLiteDatabase sqLiteDatabase;

    private String number = " \\d*\\.?\\d*$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyzed_data);
        sName = findViewById(R.id.storeName);
        sDate = findViewById(R.id.storeDate);
        sPrice = findViewById(R.id.storePrice);
        sDay = findViewById(R.id.storeDay);
        sMonth = findViewById(R.id.storeMonth);
        sYear = findViewById(R.id.storeYear);
        confirm = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);

        myHelper = new MyHelper(this); //myHelper object
        sqLiteDatabase = myHelper.getWritableDatabase();  //database open kernay kai liye ab use ker sktay


        Intent intent = getIntent();
        String text = intent.getStringExtra(result_activity.EXTRA_TEXT); // yaha result activity sai extracted data utha rhay
        String textD = intent.getStringExtra(result_activity.EXTRA_DATE);
        String textA = intent.getStringExtra(result_activity.EXTRA_AMOUNT);

        // text view mai data set ker rhay
        sDate.setText(textD);
        sName.setText(text);
        sPrice.setText(textA);

        // agay database mai store kernay kai liye text view sai data utha rhay
        final String storeName = sName.getText().toString();
        final String dateName = sDate.getText().toString();
        final String priceName = sPrice.getText().toString();

        // yahan sai date format mai kernay ka kaam horeha
        Date newDate;
        String  bless = "Bless";

        // kyon keh bless ka format agay peechay tha to usko sahi kernay kai liye format change kiya takkay sab aik format mai hon
        if (bless.equals(storeName)) {

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            try {
                newDate = formatter.parse(dateName);
                String[] dateFormat = newDate.toString().split(" ");
                day = dateFormat[2];
                month = dateFormat[1];
                year = dateFormat[5];
            } catch (Exception e) {
                e.getMessage();
            }

        }
        else {
            try {
                newDate = new Date(dateName);
                String dateParts[] = newDate.toString().split(" ");
                day = dateParts[2];
                month = dateParts[1];
                year = dateParts[5];
            }catch (Exception e) {
                e.getMessage();
            }

        }

        // date ko teen part mai jo divide kiya usko string mai save kerdia agay database mai bhejnay kai liye
        final String storeDateDay = day;
        final String storeDateMonth = month;
        final String storeDateYear = year;

        // text view mai set kernay kai liye
        sDay.setText(day);
        sMonth.setText(month);
        sYear.setText(year);

//        final Matcher priceMatch = Pattern.compile(number).matcher(priceName);


        // yeh confirm kai button ka onClick Listener hai
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // error handling ka kaam bhi horeha is if condition mai//
                if (!(priceName.isEmpty()) && priceName.matches("\\d*\\.?\\d*$")
                        && storeDateDay!=null&&storeDateMonth!=null&&storeDateYear!=null && storeName!=null) {
                    myHelper.insertData(storeName,storeDateDay,storeDateMonth,storeDateYear,priceName);
                    showMessage("Information inserted success!");
                    Intent myIntent = new Intent(v.getContext(),result_activity.class);
                    startActivity(myIntent);
                    finishAffinity();
                }

                else{
                    showMessage("Information not inserted.. invalid data");
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(),result_activity.class);
                startActivityForResult(myIntent,0);
            }
        });

    }

    void showMessage(String msg)
    {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }



}

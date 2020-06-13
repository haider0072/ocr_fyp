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

    TextView sName,sDate,sPrice,sDay,sMonth,sYear;
    Button confirm;
    String day,month,year;

    MyHelper myHelper;
    SQLiteDatabase sqLiteDatabase;

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

        myHelper = new MyHelper(this);
        sqLiteDatabase = myHelper.getWritableDatabase();


        Intent intent = getIntent();
        String text = intent.getStringExtra(result_activity.EXTRA_TEXT);
        String textD = intent.getStringExtra(result_activity.EXTRA_DATE);
        String textA = intent.getStringExtra(result_activity.EXTRA_AMOUNT);
//        String textDateDay = intent.getStringExtra(result_activity.EXTRA_DAY);
//        String textDateMonth = intent.getStringExtra(result_activity.EXTRA_MONTH);
//        String textDateYear = intent.getStringExtra(result_activity.EXTRA_YEAR);

        sDate.setText(textD);
        sName.setText(text);
        sPrice.setText(textA);

        final String storeName = sName.getText().toString();
        final String dateName = sDate.getText().toString();
        final String priceName = sPrice.getText().toString();

        Date newDate;
        String  bless = "Bless";

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

        final String storeDateDay = day;
        final String storeDateMonth = month;
        final String storeDateYear = year;

        sDay.setText(day);
        sMonth.setText(month);
        sYear.setText(year);


//        sDay.setText(textDateDay);
//        sMonth.setText(textDateMonth);
//        sYear.setText(textDateYear);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(myHelper.insertData(storeName,storeDateDay,storeDateMonth,storeDateYear,priceName)){
                    showMessage("Information inserted success!" +day+" "+month+" "+year);
                }
                else{
                    showMessage("Information not inserted");
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

//                for (int i=0;i<bless.length;i++) {
//                bless[0].equals(storeName)
//                        newDate = new Date(dateName);
//                        String dateParts[] = newDate.toString().split(" ");
//                        day = dateParts[2];
//                        month = dateParts[1];
//                        year = dateParts[5];

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

}

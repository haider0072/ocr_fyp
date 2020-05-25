package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class analyzed_data extends AppCompatActivity {

    TextView myResult;
    TextView dateResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyzed_data);
        myResult = findViewById(R.id.aResult);
        dateResult = findViewById(R.id.date);

        date();
    }

    public class Regex {
        @SuppressLint("SetTextI18n")
        public void findPattern(String str, String ptrn){
            String inputCharSeq = str;
            Pattern pattern = Pattern.compile(ptrn, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(inputCharSeq);
            if(matcher.find())
            {
                dateResult.setText("Imatiaz Super Market");
            }
//                System.out.println(getOneLineSubString(str, matcher.end()+1));
        }
    }

    public void date(){
        String str = "Imtiaz\n" +
                "S, MPTON\n" +
                "haider@gmail.com\n" +
                "mtiaz\n" +
                "NY3181\n" +
                "DATE:\n" +
                "08-Jan-2022\n" +
                "CARD NUMBER: ******* ****1453\n" +
                "TIME:\n" +
                "16:23\n" +
                "SEQUENCE NUMBER: 2334\n" +
                "WITHDRAWAL FROM\n" +
                "ACCOUNT ENDING WITH: XXXXXXXXXXx6817\n" +
                "CHECKING\n" +
                "AMOUNT\n" +
                "AVAILABLE BALANCE:\n" +
                "PRESENT BALANCE:\n" +
                "$500.000\n" +
                "$329,174.91\n" +
                "$329,174.91";
//        String ptrn = "(^(([0-9])|([0-2][0-9])|([3][0-1]))\\-(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)\\-\\d{4}$)";
        String ptrn = "(Imtiaz|imtiaz|mtiaz|tiaz)";
        Regex r = new Regex();
        String arr[] = str.split("\n");
        String firstWord = arr[0];
        r.findPattern(firstWord,ptrn);
    }

}

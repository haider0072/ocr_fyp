package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class graph2 extends AppCompatActivity {
    PieChart pieChart;

    MyHelper myHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph2);

        pieChart = findViewById(R.id.pieChart);

        myHelper = new MyHelper(this);
        sqLiteDatabase = myHelper.getWritableDatabase();

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.BLACK);
        pieChart.setTransparentCircleRadius(61f);

        //db fucntion for getting the records from database
        Cursor c = myHelper.getNameBaseData();
        ArrayList<PieEntry> yvalues = new ArrayList<>();
        c.moveToFirst();

        while (!c.isAfterLast()) {

            // null could happen if we used our empty constructor

            if (c.getString(c.getColumnIndex("storeName")) != null) {
                String stname = c.getString(c.getColumnIndex("storeName"));
                String stamount = c.getString(1);
                yvalues.add(new PieEntry(Float.parseFloat(stamount),stname));
            }
            c.moveToNext();
        }
        PieDataSet dataSet =  new PieDataSet(yvalues,"Stores");

        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData data = new PieData((dataSet));
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.WHITE);

        pieChart.setData(data);
    }

}
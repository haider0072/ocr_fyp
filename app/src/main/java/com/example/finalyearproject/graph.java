package com.example.finalyearproject;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class graph extends AppCompatActivity {

    PieChart pieChart;

    MyHelper myHelper;
    SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
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
        Cursor c = myHelper.getMonthBaseData();
        //Cursor c = myHelper.getAllData();
        //chart start
        ArrayList<PieEntry> yvalues = new ArrayList<>();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            // null could happen if we used our empty constructor
            if (c.getString(c.getColumnIndex("storeDateMonth")) != null) {
//                String stname = c.getString(c.getColumnIndex("storeName"));
                String stdate = c.getString(c.getColumnIndex("storeDateMonth"));
                String stamount = c.getString(1);
                yvalues.add(new PieEntry(Float.parseFloat(stamount),stdate));
            }
            c.moveToNext();
        }
//        yvalues.add(new PieEntry(32000f,"Imtiaz Super Market"));
//        yvalues.add(new PieEntry(32000f,"Chase Super Market"));
//        yvalues.add(new PieEntry(32000f,"Bless Super Market"));
//        yvalues.add(new PieEntry(32000f,"Al jadeed Super Market"));
//        yvalues.add(new PieEntry(32000f,"Needz Super Market"));
//        yvalues.add(new PieEntry(32000f,"Naheed Super Market"));
//        yvalues.add(new PieEntry(32000f,"Gulshan Super Market"));
        //chart end
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
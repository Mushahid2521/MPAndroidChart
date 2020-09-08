package com.example.mpandroidchart;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LineChart salesChart;
    PieChart productChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createMonthlyLineChart();
        createProductChart();

    }

    private void createProductChart() {
        productChart = findViewById(R.id.pie_chart);

        float[] productSold = {300, 1000, 2500, 1200};
        String[] productNames = {"Furniture", "Cosmetic", "Food", "Ticket"};
        Integer[] productColors = {Color.DKGRAY, Color.RED, Color.GREEN, Color.BLUE};



    }

    private void createMonthlyLineChart() {
        salesChart = findViewById(R.id.chart);
        Map<Integer, Integer> sales = new HashMap<>();
        sales = getMontylySales();
        String[] XLabels = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep",
                                "Oct", "Nov", "Dec"};

        if(sales.size()==XLabels.length) {
            createSalesChart(sales, XLabels);
        }
    }

    private Map<Integer, Integer> getMontylySales() {
        Map<Integer, Integer> sales = new HashMap<>();
        // Day , Amount
        sales.put(1,10);
        sales.put(2,20);
        sales.put(3, 160);
        sales.put(4, 66);
        sales.put(5, 50);
        sales.put(6, 70);
        sales.put(7, 100);
        sales.put(8, 2000);
        sales.put(9, 1500);
        sales.put(10, 1500);
        sales.put(11, 1500);
        sales.put(12, 1500);
        return sales;
    }


    private void createSalesChart(Map<Integer, Integer> monthly_sales, String[] XLabels) {
        ArrayList<Entry> chart_entries = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();

        for (Map.Entry<Integer, Integer> sale: monthly_sales.entrySet()) {
            chart_entries.add(new Entry(sale.getValue(), sale.getKey()-1)); // Index starts from 0 and the labels are mapped to this index
        }

        // Adding the X labels as String
        for (int i=0; i<monthly_sales.size(); i++) {
            days.add(XLabels[i]);
        }

        final LineDataSet lineDataSet = new LineDataSet(chart_entries, "Line Chart");
        LineData data = new LineData(days,  lineDataSet);


        salesChart.setDescription("");

        lineDataSet.setCircleColor(R.color.colorPrimaryDark);
        lineDataSet.setCircleRadius(3.5f);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setLineWidth(2.5f);



        lineDataSet.setColor(R.color.colorPrimary);

        lineDataSet.setDrawValues(false);

        XAxis xAxis = salesChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelsToSkip(0);
        xAxis.setLabelRotationAngle(45);



        YAxis axisLeft = salesChart.getAxisLeft();
        YAxis axisRight = salesChart.getAxisRight();
        axisRight.setEnabled(false);


        axisLeft.setAxisMinValue(0f);
        axisRight.setAxisMinValue(0f);

        axisLeft.setLabelCount(6, true);



        axisLeft.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                if (value > 1000) {
                    return  new Float(value/1000).toString().replaceAll("\\.?0*$", "") +"K";
                }
                else {
                    return new Float(value).toString().replaceAll("\\.?0*$", "");
                }
            }
        });

        // Draw the gradients below line
        lineDataSet.setDrawFilled(true);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.line_chart_gradient);
        lineDataSet.setFillDrawable(drawable);

        salesChart.setDrawMarkerViews(true);
        CustomMarker customMarkerView = new CustomMarker(this, R.layout.marker_layout);
        salesChart.setMarkerView(customMarkerView);
        salesChart.setTouchEnabled(true);

        // Disable Y axis zoom
        salesChart.setScaleYEnabled(false);

        data.setValueTextSize(12f);
        salesChart.setData(data);
        salesChart.getLegend().setEnabled(false);
        salesChart.animateY(500);
        salesChart.invalidate();
    }
}
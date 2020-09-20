package com.example.mpandroidchart;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.math.MathUtils;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
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
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LineChart salesChart;
    PieChart productChart;
    BarChart ratingsChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createMonthlyLineChart();
        createProductChart();
        createRatingBarChart();

    }

    private void createProductChart() {
        productChart = findViewById(R.id.pie_chart);

        int[] orderTypeCounts = {10, 20, 30, 40};
        String[] orderCategories = {"Cancelled", "Confirmed", "Picked", "Delivered"};

        Integer[] productColors = {
                Color.parseColor("#FFF44336"), //
                Color.parseColor("#FFFFC107"), //
                Color.parseColor("#FF8BC34A"), //
                Color.parseColor("#FF4CAF60")  //
        };

        ArrayList<PieEntry> sales_entries = new ArrayList<>();
        PieDataSet pieDataSet;
        PieData pieData;


        // Handle the initial state when all the values are 0. The graph vanishes
        if(orderTypeCounts[0]+orderTypeCounts[1]+orderTypeCounts[2]+orderTypeCounts[3]==0) {
            orderTypeCounts[2] = 1;
            for (int i=0; i<orderCategories.length; i++) {
                sales_entries.add(new PieEntry(orderTypeCounts[i], orderCategories[i]));
            }

            pieDataSet = new PieDataSet(sales_entries, "");
            pieData = new PieData(pieDataSet);

            final DecimalFormat decimalFormat = new DecimalFormat("##.##");
            pieData.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return "0%";
                }
            });

        } else {

            for (int i=0; i<orderCategories.length; i++) {
                sales_entries.add(new PieEntry(orderTypeCounts[i], orderCategories[i]));
            }

            pieDataSet = new PieDataSet(sales_entries, "");
            pieData = new PieData(pieDataSet);

            final DecimalFormat decimalFormat = new DecimalFormat("##.##");
            pieData.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return decimalFormat.format(value)+"%";
                }
            });
        }


        pieDataSet.setValueTextSize(10);


        pieDataSet.setColors(Arrays.asList(productColors));
        pieDataSet.setSliceSpace(3);
        pieDataSet.setSelectionShift(5);
        // Outside values
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        Legend legend = productChart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTextSize(10);
        legend.setWordWrapEnabled(true);
        legend.setDrawInside(false);

        productChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);
        productChart.setDrawEntryLabels(false);
        productChart.setUsePercentValues(true);
        productChart.setData(pieData);
        productChart.getLegend().setEnabled(true);
        productChart.getDescription().setEnabled(false);
        productChart.setTouchEnabled(false);
        productChart.invalidate();

    }

    private void createRatingBarChart() {
        ratingsChart = findViewById(R.id.ratingBar);
        Map<Integer, Integer> ratings = new HashMap<>();
        ratings = getRatingsData();
        String[] rating_labels = {"1★", "2★", "3★", "4★", "5★"};
        createRatingsChart(ratings, rating_labels);
    }

    private Map<Integer, Integer> getRatingsData() {
        Map<Integer, Integer> sales = new HashMap<>();
        // Rating , Counts
        sales.put(0, 1000);
        sales.put(1, 2000);
        sales.put(2, 3000);
        sales.put(3, 4000);
        sales.put(4, 5000);
        return sales;
    }

    private void createMonthlyLineChart() {
        salesChart = findViewById(R.id.chart);
        Map<Integer, Integer> sales = new HashMap<>();
        // Get the sale in in array from 0 index
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
        sales.put(0,10);
        sales.put(1,20);
        sales.put(2, 160);
        sales.put(3, 66);
        sales.put(4, 50);
        sales.put(5, 70);
        sales.put(6, 100);
        sales.put(7, 2000);
        sales.put(8, 1500);
        sales.put(9, 1500);
        sales.put(10, 1500);
        sales.put(11, 1500);
        return sales;
    }


    private void createSalesChart(Map<Integer, Integer> monthly_sales, final String[] XLabels) {
        ArrayList<Entry> chart_entries = new ArrayList<>();
        ArrayList<String> days = new ArrayList<>();

        for (Map.Entry<Integer, Integer> sale: monthly_sales.entrySet()) {
            //chart_entries.add(new Entry(sale.getValue(), sale.getKey()-1)); // Index starts from 0 and the labels are mapped to this index
            chart_entries.add(new Entry(sale.getKey(), sale.getValue()));
        }

        // Adding the X labels as String
        for (int i=0; i<monthly_sales.size(); i++) {
            days.add(XLabels[i]);
        }

        final LineDataSet lineDataSet = new LineDataSet(chart_entries, "Line Chart");
        LineData data = new LineData(lineDataSet);


        salesChart.getDescription().setEnabled(false);

        lineDataSet.setCircleColor(R.color.colorPrimaryDark);
        lineDataSet.setCircleRadius(3.5f);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setLineWidth(2.5f);



        lineDataSet.setColor(R.color.colorPrimary);

        lineDataSet.setDrawValues(false);

        XAxis xAxis = salesChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(XLabels.length-1);
        xAxis.setLabelRotationAngle(45);

        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int idx = (int) value;
                return XLabels[idx];
            }
        });



        YAxis axisLeft = salesChart.getAxisLeft();
        YAxis axisRight = salesChart.getAxisRight();
        axisRight.setEnabled(false);


        axisLeft.setAxisMinValue(0f);
        axisRight.setAxisMinValue(0f);

        axisLeft.setLabelCount(6, true);



        axisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
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

    private void createRatingsChart(Map<Integer, Integer> ratingsCount, final String[] XLabels) {
        ArrayList<BarEntry> chart_entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (Map.Entry<Integer, Integer> rating: ratingsCount.entrySet()) {
            //chart_entries.add(new BarEntry(rating.getValue(), rating.getKey()-1)); //Starting from 0, else create issue
            chart_entries.add(new BarEntry(rating.getKey(), rating.getValue()));
        }


        BarDataSet barDataSet = new BarDataSet(chart_entries, "");
        barDataSet.setColors(new int[]{
                Color.parseColor("#FFF44336"), // 1
                Color.parseColor("#FFFF9800"), // 2
                Color.parseColor("#FFFFC107"), // 3
                Color.parseColor("#FF8BC34A"), //4
                Color.parseColor("#FF4CAF50") // 5
        });




        BarData data = new BarData(barDataSet);

        ratingsChart.getDescription().setEnabled(false);

        ratingsChart.setDrawValueAboveBar(true);

        XAxis xAxis = ratingsChart.getXAxis();
        xAxis.setLabelCount(5);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int idx = (int) value;
                return XLabels[idx];
            }
        });


        YAxis axisLeft = ratingsChart.getAxisLeft();
        YAxis axisRight = ratingsChart.getAxisRight();
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        axisRight.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);


        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        axisLeft.setDrawGridLines(false);
        axisLeft.setDrawLabels(false);
        axisLeft.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawAxisLine(false);
        axisRight.setDrawLabels(false);

        ratingsChart.setScaleEnabled(false);
        ratingsChart.setData(data);
        ratingsChart.getLegend().setEnabled(false);
        ratingsChart.animateY(500);
        ratingsChart.invalidate();
    }
}
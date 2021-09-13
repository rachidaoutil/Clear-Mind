package com.stageapp.riskmanage.Adapters;

import static android.content.Context.USAGE_STATS_SERVICE;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.models.App;
import com.stageapp.riskmanage.models.Categorie;
import com.stageapp.riskmanage.models.DayStat;
import com.stageapp.riskmanage.models.DayUsageStats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.paperdb.Paper;


public class AppsTrendViewPagerAdapter extends PagerAdapter {

    private Context ctx;
    LayoutInflater mLayoutInflater;
    private String[] Week = new String[] {
            "Sun", "Mon", "Tue", "Wed", "Thu", "Fr", "Sat"};
    private String[] Colors = new String[] {
        "#219ebc", "#023047", "#ffb703", "#fb8500", "#eae4e9", "#fff1e6", "#fad2e1", "#e2ece9"};

    BarChart barChart;
    ArrayList<String> dates;
    Random random;
    ArrayList<BarEntry> barEntries;

//    ListView listView;
    RecyclerView listView;
    TextView avrg_text;
    TextView min_text;
    TextView max_text;
    TextView total_text;

    LinearLayoutManager mLayoutManager;

    boolean WeekChart;
    long Avrg;
    long Count;
    long Max;
    long Min;
    long total;
    private CombinedChart chart;
    private int count = 23;
    protected final String[] months = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };
    protected final String[] hours = new String[] {
            "00h", "1h", "2h", "3h", "4h", "5h", "6h", "7h", "8h", "9h", "10h", "11h", "12h", "13h", "14h", "15h", "16h", "17h", "18h", "19h", "20h", "21h", "22h", "23h"
    };




    public AppsTrendViewPagerAdapter(Context ctx, Boolean weekChart) {
        this.ctx = ctx;
        this.WeekChart = weekChart;
        mLayoutInflater = LayoutInflater.from(ctx);

    }




    @Override
    public Object instantiateItem(View collection, int position) {

        View v = mLayoutInflater.inflate( R.layout.app_stats_trend, (ViewGroup) collection, false);

        chart = v.findViewById(R.id.chart1);
        chart.getDescription().setEnabled(false);
        chart.setBackgroundColor(R.drawable.my_gradient_drawable3);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);



        chart.animateX(2500);
        //mChart.invalidate();



        // draw bars behind lines
        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.BUBBLE, CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE, CombinedChart.DrawOrder.SCATTER
        });

        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setTextColor(Color.WHITE);

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(true);
        rightAxis.setTextColor(Color.WHITE);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setEnabled(false);

        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY); // return the hour in 24 hrs format (ranging from 0-23)
        int currentDay = rightNow.get(Calendar.DAY_OF_WEEK); // return the hour in 24 hrs format (ranging from 0-23)


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);

        Log.e("Day",currentDay+"");

        CombinedData data = new CombinedData();
        DayUsageStats stats = Paper.book().read("WeekUsageStats",new DayUsageStats("0",0,0));
        List<Categorie> myCategorie = new ArrayList<>();
        LimitLine ll2 = new LimitLine(5, "Avrg");

        if (WeekChart){
            rightAxis.setAxisMinimum(0); // this replaces setStartAtZero(true)
            rightAxis.setAxisMaximum(12);

            xAxis.setLabelCount(7,true);
            xAxis.setAxisMinimum(0);
            xAxis.setAxisMaximum(6);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    int day = (int) Math.abs(value - currentDay % Week.length);
                    Log.e("Days",day+"");
                    return Week[(int) value];
                }
            });

            ll2.setTextColor(Color.WHITE);
            ll2.setLineWidth(4f);
            ll2.setLineColor(Color.WHITE);
            ll2.enableDashedLine(10f, 12f, 0f);
            ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            ll2.setTextSize(10f);
            rightAxis.addLimitLine(ll2);

           myCategorie = Paper.book().read("CategoriesDay",new ArrayList<>());

            data.setData(generateBarWeekData(myCategorie));
        }
        else {
            rightAxis.setAxisMinimum(0); // this replaces setStartAtZero(true)
            rightAxis.setAxisMaximum(60);

            xAxis.setLabelCount(5,true);
            xAxis.setAxisMinimum(0);
            xAxis.setAxisMaximum(24);
            xAxis.setValueFormatter(new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return hours[(int) Math.abs(currentHour-value % hours.length)];
                }
            });
            myCategorie = Paper.book().read("CategoriesWeek",new ArrayList<>());
            data.setData(generateBarData(myCategorie));
        }




        chart.setData(data);
        chart.invalidate();


        ((ViewPager)collection).addView(v);
        return v;
    }

    private LineData generateLineData(List<DayStat> dayStats) {

        Collections.sort(dayStats, Comparator.comparingLong(DayStat::getPointTime));

        LineData d = new LineData();
        ArrayList<Entry> entries = new ArrayList<>();

        for (int index = 0; index < count; index++)
            entries.add(new Entry(index + 1, dayStats.get(index).getPointData()));

        LineDataSet set = new LineDataSet(entries, "Usage Time");
        set.setColor(Color.WHITE);
        set.setLineWidth(2.5f);
        set.setCircleColor(Color.rgb(255, 86, 104));
        set.setCircleRadius(5f);
        set.setFillColor(Color.WHITE);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setDrawValues(false);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData(List<Categorie> categorieList) {

        ArrayList<BarEntry> entries2 = new ArrayList<>();
        String[] titles = new  String[categorieList.size()];
        int[] colors = new  int[categorieList.size()];


        for (int index = 0; index < count; index++) {

            // stacked
//            entries2.add(new BarEntry(0, new float[]{getRandom(13, 12), getRandom(13, 12)}));
           // stacked
            float[] vals = new  float[categorieList.size()];

            for (int i = 0; i < categorieList.size();i++) {
                vals[i] = ((float) getRandom(59, 0));
                titles[i] = categorieList.get(i).getName();
                colors[i] =  Color.parseColor(Colors[categorieList.get(i).getId()]);

            }
            entries2.add(new BarEntry(index+1,vals));
        }

        BarDataSet set2 = new BarDataSet(entries2, "");

        set2.setStackLabels(titles);
        set2.setColors(colors);

        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setDrawValues(false);


//        BarDataSet set3 = new BarDataSet(entries2, "");
//        set3.setStackLabels(new String[]{"Stack 2", "Stack 3"});
//        set3.setColors(Color.rgb(67, 16, 255), Color.rgb(23, 197, 255));
////        set2.setValueTextColor(Color.rgb(61, 165, 255));
//        set3.setValueTextSize(10f);
//        set3.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set3.setDrawValues(false);


        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData( set2);
//        d.setBarWidth(barWidth);

        // make this BarData object grouped
//        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;
    }
    private BarData generateBarWeekData(List<Categorie> categorieList) {

        ArrayList<BarEntry> entries2 = new ArrayList<>();
        String[] titles = new  String[categorieList.size()];
        int[] colors = new  int[categorieList.size()];


        for (int index = -1; index < 8; index++) {

            // stacked
//            entries2.add(new BarEntry(0, new float[]{getRandom(13, 12), getRandom(13, 12)}));
           // stacked
            float[] vals = new  float[categorieList.size()];

            for (int i = 0; i < categorieList.size();i++) {
                vals[i] = ((float) getRandom(12, 0));
                titles[i] = categorieList.get(i).getName();
                colors[i] =  Color.parseColor(Colors[categorieList.get(i).getId()]);

            }
            entries2.add(new BarEntry(index+1,vals));
        }

        BarDataSet set2 = new BarDataSet(entries2, "");

        set2.setStackLabels(titles);
        set2.setColors(colors);

        set2.setAxisDependency(YAxis.AxisDependency.LEFT);
        set2.setDrawValues(false);


//        BarDataSet set3 = new BarDataSet(entries2, "");
//        set3.setStackLabels(new String[]{"Stack 2", "Stack 3"});
//        set3.setColors(Color.rgb(67, 16, 255), Color.rgb(23, 197, 255));
////        set2.setValueTextColor(Color.rgb(61, 165, 255));
//        set3.setValueTextSize(10f);
//        set3.setAxisDependency(YAxis.AxisDependency.LEFT);
//        set3.setDrawValues(false);


        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData( set2);
//        d.setBarWidth(barWidth);

        // make this BarData object grouped
//        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;
    }


    protected float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }

    @Override
    public void destroyItem(View collection, int position, Object view) {
        ((ViewPager) collection).removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public void startUpdate(View arg0) {
    }

    @Override
    public void finishUpdate(View arg0) {
    }




}


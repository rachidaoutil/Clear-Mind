package com.stageapp.riskmanage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Orientation;
import com.anychart.enums.ScaleStackMode;
import com.anychart.scales.Linear;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.stageapp.riskmanage.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class CallTrendViewPagerAdapter extends PagerAdapter {

    private Context ctx;
    LayoutInflater mLayoutInflater;

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

    long Avrg;
    long Count;
    long Max;
    long Min;
    long total;

    AnyChartView anyChartView;




    public CallTrendViewPagerAdapter(Context ctx) {
        this.ctx = ctx;
        mLayoutInflater = LayoutInflater.from(ctx);

    }




    @Override
    public Object instantiateItem(View collection, int position) {

        View v = mLayoutInflater.inflate( R.layout.call_stats_trend, (ViewGroup) collection, false);
        TextView tv = (TextView) v.findViewById(R.id.title);

         listView = v.findViewById(R.id.apps_list);
         avrg_text = (TextView) v.findViewById(R.id.avrg_data);

         total_text = (TextView) v.findViewById(R.id.tot_data);



//         this.total_text.setText(getDurationBreakdown(total,false));
//         this.avrg_text.setText(getDurationBreakdown(total/Count,false));









//        TextView view = new TextView(ctx)
//        tv.setText(data.get(position));
        ((ViewPager)collection).addView(v);
        return v;
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


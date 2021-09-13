package com.stageapp.riskmanage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.stageapp.riskmanage.R;

import java.util.ArrayList;
import java.util.Random;

public class PagerAdapter extends androidx.viewpager.widget.PagerAdapter {

    private Context ctx;
    LayoutInflater mLayoutInflater;

     ArrayList<String> dates;
    Random random;

    ListView listView;
    TextView avrg_text;
    TextView min_text;
    TextView max_text;
    TextView total_text;

    long Avrg;
    long Count;
    long Max;
    long Min;
    long total;




    public PagerAdapter(Context ctx) {
        this.ctx = ctx;
        mLayoutInflater = LayoutInflater.from(ctx);

    }


    @Override
    public Object instantiateItem(View collection, int position) {

        View v = mLayoutInflater.inflate( R.layout.list_view, (ViewGroup) collection, false);

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


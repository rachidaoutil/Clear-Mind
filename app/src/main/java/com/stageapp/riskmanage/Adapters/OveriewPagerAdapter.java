package com.stageapp.riskmanage.Adapters;

import static com.stageapp.riskmanage.Utils.TimeUtiles.getDurationBreakdown;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.Utils.LoadAppsData;
import com.stageapp.riskmanage.models.App;
import com.stageapp.riskmanage.models.Categorie;
import com.stageapp.riskmanage.models.DayUsageStats;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import io.paperdb.Paper;


public class OveriewPagerAdapter extends PagerAdapter {

    private Context ctx;
    LayoutInflater mLayoutInflater;

    RecyclerView listView;


    LinearLayoutManager mLayoutManager;

    View mView;
    TextView totalTxt;
    TextView dateTxt;

    ViewPager viewPager2;

    DayUsageStats stats;







    public OveriewPagerAdapter(Context ctx) {
        this.ctx = ctx;
        mLayoutInflater = LayoutInflater.from(ctx);

    }




    @Override
    public Object instantiateItem(View collection, int position) {

        List<App> appList = new ArrayList<>();
        ArrayList<Categorie> myCategorie = new ArrayList<>();



        int resId = 0;
        switch (position) {
            case 0:
                Log.e("view","view"+position);
//                resId =  R.layout.overview_card_today;
//                listView.swapAdapter(new NotificationRecylcleAdapter(this.ctx),true );

                this.mView = mLayoutInflater.inflate( R.layout.overview_card_today, (ViewGroup) collection, false);
                this.totalTxt = mView.findViewById(R.id.total_data);
                this.dateTxt = mView.findViewById(R.id.date_txt);
                this.listView = mView.findViewById(R.id.catg_list);
                this.viewPager2 = mView.findViewById(R.id.viewPager2);
                this.viewPager2.setAdapter(new AppsTrendViewPagerAdapter(ctx.getApplicationContext(),false));


                this.mLayoutManager = new LinearLayoutManager(this.ctx);
                this.listView.setLayoutManager(this.mLayoutManager);

                this.stats = Paper.book().read("DayUsageStats",new DayUsageStats("0",0,0));
                myCategorie = Paper.book().read("CategoriesDay",new ArrayList<>());

                totalTxt.setText(getDurationBreakdown(stats.duration));
                dateTxt.setText((stats.date));//

                RecylcleTodayAdapter adapter = new RecylcleTodayAdapter(this.ctx);
                adapter.addItems(myCategorie);
                Log.e("Apps","Size: "+myCategorie.size());
                // attach the adapter to a ListView
                this.listView.setAdapter(adapter);




                //             t
                // his.listView = mView.findViewById(R.id.catg_list);
//                listView.swapAdapter(new RecylcleTodayAdapter(this.ctx),true );



                break;
            case 1:
                Log.e("view","view"+position);
//                resId =  R.layout.overview_card_week;
//                listView.swapAdapter(new NotificationRecylcleAdapter(this.ctx),true );

                this.mView = mLayoutInflater.inflate( R.layout.overview_card_week, (ViewGroup) collection, false);
                this.totalTxt = mView.findViewById(R.id.tot_data);
                this.dateTxt = mView.findViewById(R.id.avrg_data);
                this.listView = mView.findViewById(R.id.catg_list);
                this.viewPager2 = mView.findViewById(R.id.viewPager2);

                this.mLayoutManager = new LinearLayoutManager(this.ctx);
                this.listView.setLayoutManager(this.mLayoutManager);


                this.viewPager2.setAdapter(new AppsTrendViewPagerAdapter(ctx.getApplicationContext(),true));


                this.stats = Paper.book().read("WeekUsageStats",new DayUsageStats("0",0,0));
                totalTxt.setText(getDurationBreakdown(stats.duration));
                dateTxt.setText(getDurationBreakdown(stats.avreg));//

                myCategorie = Paper.book().read("CategoriesWeek",new ArrayList<>());

                RecylcleWeekAdapter weekAdapter = new RecylcleWeekAdapter(this.ctx);
                weekAdapter.addItems(myCategorie);
                Log.e("Apps","Size: "+myCategorie.size());
                // attach the adapter to a ListView
                this.listView.setAdapter(weekAdapter);


                break;
            case 2:


                break;

        }


        // Group the usageStats by application and sort them by total time in foreground




        ((ViewPager)collection).addView(mView);
        return mView;
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
        return 2;
    }

    @Override
    public void startUpdate(View arg0) {
    }

    @Override
    public void finishUpdate(View arg0) {
    }





}


package com.stageapp.riskmanage.Adapters;

import static android.content.Context.USAGE_STATS_SERVICE;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;
import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.Utils.LoadAppsData;
import com.stageapp.riskmanage.models.App;
import com.stageapp.riskmanage.models.DayUsageStats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.paperdb.Paper;


public class AppsUsageViewPagerAdapter extends PagerAdapter {

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




    public AppsUsageViewPagerAdapter(Context ctx) {
        this.ctx = ctx;
        mLayoutInflater = LayoutInflater.from(ctx);

    }




    @Override
    public Object instantiateItem(View collection, int position) {

        View v = mLayoutInflater.inflate( R.layout.app_stats, (ViewGroup) collection, false);
        TextView tv = (TextView) v.findViewById(R.id.title);

         listView = v.findViewById(R.id.apps_list);
         avrg_text = (TextView) v.findViewById(R.id.avrg_data);
         total_text = (TextView) v.findViewById(R.id.tot_data);

        this.mLayoutManager = new LinearLayoutManager(this.ctx);
        this.listView.setHasFixedSize(false);
        this.listView.setLayoutManager(this.mLayoutManager);
        new LoadAppsData(this.ctx).loadStatistics(false);

        DayUsageStats stats = Paper.book().read("DayUsageStats",new DayUsageStats("0",0,0));
        total_text.setText(getDurationBreakdown(stats.duration));
        avrg_text.setText(getDurationBreakdown(stats.avreg));

        ArrayList<App> appList = Paper.book().read("Apps", new ArrayList<>());


//        AppsAdapter adapter = new AppsAdapter(this.ctx, appsList);
        RecylcleAppsAdapter adapter = new RecylcleAppsAdapter(this.ctx);
        adapter.addItems(appList);
        Log.e("Apps","Size: "+appList.size());
        // attach the adapter to a ListView
        listView.setAdapter(adapter);








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


    public ArrayList<String> getList(Calendar startDate, Calendar endDate){
        ArrayList<String> list = new ArrayList<String>();
        while(startDate.compareTo(endDate)<=0){
            list.add(getDate(startDate));
            startDate.add(Calendar.DAY_OF_MONTH,1);
        }
        return list;
    }

    public String getDate(Calendar cld){
        String curDate = cld.get(Calendar.YEAR) + "/" + (cld.get(Calendar.MONTH) + 1) + "/"
                +cld.get(Calendar.DAY_OF_MONTH);
        try{
            Date date = new SimpleDateFormat("yyyy/MM/dd").parse(curDate);
            curDate =  new SimpleDateFormat("yyy/MM/dd").format(date);
        }catch(ParseException e){
            e.printStackTrace();
        }
        return curDate;
    }


    /**
     * load the usage stats for last 24h
     */
    public void loadStatistics() {
        UsageStatsManager usm = (UsageStatsManager) this.ctx.getSystemService(USAGE_STATS_SERVICE);
        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  System.currentTimeMillis() - 1000*3600*24,  System.currentTimeMillis());
        appList = appList.stream().filter(app -> app.getTotalTimeInForeground() > 0 && !app.getPackageName().contains("com.google.android") && !app.getPackageName().contains("com.android")).collect(Collectors.toList());

        // Group the usageStats by application and sort them by total time in foreground
        if (appList.size() > 0) {
            Map<String, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getPackageName(), usageStats);
            }
            showAppsUsage(mySortedMap);
        }
    }

    /**
     * check if the application info is still existing in the device / otherwise it's not possible to show app detail
     * @return true if application info is available
     */
    private boolean isAppInfoAvailable(UsageStats usageStats) {
        try {
            this.ctx.getApplicationContext().getPackageManager().getApplicationInfo(usageStats.getPackageName(), 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /**
     * helper method to get string in format hh:mm:ss from miliseconds
     *
     * @param millis (application time in foreground)
     * @return string in format hh:mm:ss from miliseconds
     */
    private String getDurationBreakdown(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }




        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        return (hours + " h " +  minutes + " m " + seconds + " s");
    }



    public void showAppsUsage(Map<String, UsageStats> mySortedMap) {
        //public void showAppsUsage(List<UsageStats> usageStatsList) {
        ArrayList<App> appsList = new ArrayList<>();
        List<UsageStats> usageStatsList = new ArrayList<>(mySortedMap.values());

        // sort the applications by time spent in foreground
        Collections.sort(usageStatsList, (z1, z2) -> Long.compare(z1.getTotalTimeInForeground(), z2.getTotalTimeInForeground()));

        // get total time of apps usage to calculate the usagePercentage for each app
        long totalTime = usageStatsList.stream().map(UsageStats::getTotalTimeInForeground).mapToLong(Long::longValue).sum();

        String categoryTitle = "";
        //fill the appsList
        for (UsageStats usageStats : usageStatsList) {
            try {
                String packageName = usageStats.getPackageName();
                Drawable icon = this.ctx.getDrawable(R.drawable.person3);
                String[] packageNames = packageName.split("\\.");
                String appName = packageNames[packageNames.length-1].trim();
                PackageManager pm = this.ctx.getPackageManager();
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int appCategory = applicationInfo.category;
                    categoryTitle = (String) ApplicationInfo.getCategoryTitle(this.ctx, appCategory);
                    // ...
                }
                Log.e("Apps",packageName+": "+categoryTitle);


                if(isAppInfoAvailable(usageStats)){
                    ApplicationInfo ai = this.ctx.getApplicationContext().getPackageManager().getApplicationInfo(packageName, 0);
                    icon = this.ctx.getApplicationContext().getPackageManager().getApplicationIcon(ai);
                    appName = this.ctx.getApplicationContext().getPackageManager().getApplicationLabel(ai).toString();
                }

                long usageDuration = (usageStats.getTotalTimeInForeground());
                int usagePercentage = (int) (usageStats.getTotalTimeInForeground() * 100 / totalTime);

                App usageStatDTO = new App(icon, appName, usagePercentage, usageDuration);
                appsList.add(usageStatDTO);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }


        // reverse the list to get most usage first
        Collections.reverse(appsList);
        // build the adapter
        this.mLayoutManager = new LinearLayoutManager(this.ctx);
        this.listView.setHasFixedSize(false);
        this.listView.setLayoutManager(this.mLayoutManager);

        ArrayList<App> appList = Paper.book().read("Apps", new ArrayList<>());


//        AppsAdapter adapter = new AppsAdapter(this.ctx, appsList);
        RecylcleAppsAdapter adapter = new RecylcleAppsAdapter(this.ctx);
        adapter.addItems(appList);
        // attach the adapter to a ListView
        listView.setAdapter(adapter);

//        showHideItemsWhenShowApps();
    }




}


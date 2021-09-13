package com.stageapp.riskmanage.Utils;

import static android.content.Context.USAGE_STATS_SERVICE;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.models.App;
import com.stageapp.riskmanage.models.Categorie;
import com.stageapp.riskmanage.models.DayUsageStats;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.paperdb.Paper;

public class LoadAppsData {

    Context context;
    public static enum Period {
            DAY,
            WEEK
    }

    public LoadAppsData(Context context) {
        this.context = context;
    }

    /**
     * load the usage stats for last 24h
     */
    public void loadStatistics(Boolean weekStats) {
        int Interval = UsageStatsManager.INTERVAL_DAILY;;
        long startTime = System.currentTimeMillis() - 1000*3600*24;

        if (weekStats) {
            Interval = UsageStatsManager.INTERVAL_WEEKLY;
            startTime = System.currentTimeMillis() - 1000*3600*24*7;
        }

        UsageStatsManager usm = (UsageStatsManager) this.context.getSystemService(USAGE_STATS_SERVICE);
        List<UsageStats> appList = usm.queryUsageStats(Interval,  startTime,  System.currentTimeMillis());
        appList = appList.stream().filter(app -> app.getTotalTimeInForeground() > 0 ).collect(Collectors.toList());

        // Group the usageStats by application and sort them by total time in foreground
        if (appList.size() > 0) {
            Map<String, UsageStats> mySortedMap = new TreeMap<>();
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getPackageName(), usageStats);
            }
            showAppsUsage(mySortedMap,weekStats);
        }
    }

    /**
     * check if the application info is still existing in the device / otherwise it's not possible to show app detail
     * @return true if application info is available
     */
    private boolean isAppInfoAvailable(UsageStats usageStats) {
        try {
            this.context.getApplicationContext().getPackageManager().getApplicationInfo(usageStats.getPackageName(), 0);
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



    public void showAppsUsage(Map<String, UsageStats> mySortedMap,Boolean weekStats) {
        //public void showAppsUsage(List<UsageStats> usageStatsList) {
        ArrayList<App> appsList = new ArrayList<>();
        ArrayList<Categorie> CategorieList = new ArrayList<>();
        List<UsageStats> usageStatsList = new ArrayList<>(mySortedMap.values());


        // sort the applications by time spent in foreground
        Collections.sort(usageStatsList, Comparator.comparingLong(UsageStats::getTotalTimeInForeground));

        // get total time of apps usage to calculate the usagePercentage for each app
        long totalTime = usageStatsList.stream().map(UsageStats::getTotalTimeInForeground).mapToLong(Long::longValue).sum();

        String categoryTitle = "";
        int categoryID = 0;
        //fill the appsList
        for (UsageStats usageStats : usageStatsList) {
            try {
                String packageName = usageStats.getPackageName();
                Drawable icon = this.context.getDrawable(R.drawable.person3);
                String[] packageNames = packageName.split("\\.");
                String appName = packageNames[packageNames.length-1].trim();
                PackageManager pm = this.context.getPackageManager();
                ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);

                if(isAppInfoAvailable(usageStats)){

                    ApplicationInfo ai = this.context.getApplicationContext().getPackageManager().getApplicationInfo(packageName, 0);
                    icon = this.context.getApplicationContext().getPackageManager().getApplicationIcon(ai);
                    appName = this.context.getApplicationContext().getPackageManager().getApplicationLabel(ai).toString();

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        categoryID = applicationInfo.category;
                        categoryTitle = (String) ApplicationInfo.getCategoryTitle(this.context, categoryID);
                        if (categoryTitle == null) {
                            categoryTitle = "UNKNOWN";
                        }
                        // ...
//                        this.categorie = new Categorie(appCategory,categoryTitle,new ArrayList<>(),"",0);
//                        switch (appCategory){
//
//                            case ApplicationInfo.CATEGORY_GAME:
//                                this.categorie
//                                break;
//                            case ApplicationInfo.CATEGORY_AUDIO:
//
//                                break;
//                            case ApplicationInfo.CATEGORY_VIDEO:
//
//                                break;
//
//                            case ApplicationInfo.CATEGORY_IMAGE:
//
//                                break;
//
//                            case ApplicationInfo.CATEGORY_SOCIAL:
//
//                                break;
//
//                            case ApplicationInfo.CATEGORY_NEWS:
//
//                                break;
//
//                            case ApplicationInfo.CATEGORY_MAPS:
//
//                                break;
//
//                            case ApplicationInfo.CATEGORY_PRODUCTIVITY:
//
//                                break;
//
//                            case ApplicationInfo.CATEGORY_UNDEFINED:
//
//                                break;
//
//                        }
                    }
                    Log.e("Apps",packageName+": "+categoryTitle);


                }

                long usageDuration = (usageStats.getTotalTimeInForeground());
                int usagePercentage = (int) (usageStats.getTotalTimeInForeground() * 100 / totalTime);

                App usageStatDTO = new App(icon,packageName, appName,categoryID,categoryTitle, usagePercentage, usageDuration);
                appsList.add(usageStatDTO);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        // reverse the list to get most usage first
        Collections.reverse(appsList);
        Paper.book().write("Apps", appsList);
        DateFormat df = new SimpleDateFormat(" MMM d, ''yy");
        String date = df.format(Calendar.getInstance().getTime());

        Log.e("Date",date);

        if (weekStats){
            Paper.book().write("WeekUsageStats",new DayUsageStats(date,(totalTime),totalTime/7));

            return;
        }
        Paper.book().write("DayUsageStats",new DayUsageStats(date,totalTime,totalTime/appsList.size()));


    }


}

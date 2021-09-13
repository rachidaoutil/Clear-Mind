package com.stageapp.riskmanage.Adapters;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.CallLog;
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
import com.stageapp.riskmanage.models.Call;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class CallsUsageViewPagerAdapter extends PagerAdapter {

    private List<String> data;
    private Context ctx;
    LayoutInflater mLayoutInflater;

    BarChart barChart;
    ArrayList<String> dates;
    Random random;
    ArrayList<BarEntry> barEntries;

    RecyclerView listView;
    TextView avrg_text;
    TextView min_text;
    TextView max_text;
    TextView total_text;

    long Avrg;
    long Count;
    long Max;
    long Min;
    long total;




    public CallsUsageViewPagerAdapter(Context ctx) {
        this.ctx = ctx;
        mLayoutInflater = LayoutInflater.from(ctx);

    }

    @Override
    public int getCount() {
        return 1;
    }


    @Override
    public Object instantiateItem(View collection, int position) {

        View v = mLayoutInflater.inflate( R.layout.call_stats, (ViewGroup) collection, false);

        listView = v.findViewById(R.id.calls_list);




        buttonCallLog();




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
    public void startUpdate(View arg0) {
    }

    @Override
    public void finishUpdate(View arg0) {
    }



    /**
     * helper method to get string in format hh:mm:ss from miliseconds
     *
     * @param millis (application time in foreground)
     * @return string in format hh:mm:ss from miliseconds
     */
    private String getDurationBreakdown(long millis,boolean CalcAppsStats) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        if(CalcAppsStats){
//            if(millis > this.Max){
//                this.Max = millis;
//            }
//
//            if(millis < this.Min){
//                this.Max = millis;
//            }
            this.total += millis;
            this.Count += 1;
        }


        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        return (hours + " h " +  minutes + " m " + seconds + " s");
    }





    public void buttonCallLog(){
//        textView.setText("Call Logging Started ... ");
        String stringOutput = "";
        ArrayList<Call> callsList = new ArrayList<>();

        ContentResolver cr = this.ctx.getContentResolver();

        Uri uriCallLogs = Uri.parse("content://call_log/calls");
//        Cursor cursorCallLogs = this.ctx.getContentResolver().query(uriCallLogs, null,null,null);
        Cursor cursorCallLogs = cr.query(CallLog.Calls.CONTENT_URI, null, null, null, android.provider.CallLog.Calls.DATE + " DESC");

        cursorCallLogs.moveToFirst();
        do {
            String stringNumber = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.NUMBER));
            String stringName = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.CACHED_NAME));
            long Duration = cursorCallLogs.getLong(cursorCallLogs.getColumnIndex(CallLog.Calls.DURATION));
            String stringType = cursorCallLogs.getString(cursorCallLogs.getColumnIndex(CallLog.Calls.TYPE));
            Drawable icon = this.ctx.getDrawable(R.drawable.no_image);

            stringOutput = stringOutput + "Number: " + stringNumber
                    + "\nName: " + stringName
                    + "\nDuration: " + Duration
                    + "\n Type: " + stringType
                    + "\n\n";

            Call mcall = new Call(icon,stringName,stringNumber,10,1000*(Duration));
            Log.e("Call",stringOutput);
            callsList.add(mcall);
        }while (cursorCallLogs.moveToNext());


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this.ctx);
        this.listView.setHasFixedSize(false);
        this.listView.setLayoutManager(mLayoutManager);
        RecylcleCallsAdapter adapter = new RecylcleCallsAdapter(this.ctx);
        adapter.addItems(callsList);
        Log.e("Calls","Size: "+callsList.size());
        // attach the adapter to a ListView
        listView.setAdapter(adapter);






    }
}





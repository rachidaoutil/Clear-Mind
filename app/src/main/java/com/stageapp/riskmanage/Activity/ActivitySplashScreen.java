package com.stageapp.riskmanage.Activity;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.Services.usageService;
import com.stageapp.riskmanage.Utils.LoadAppsData;
import com.stageapp.riskmanage.Utils.LoadCategoriesData;
import com.stageapp.riskmanage.models.App;
import com.stageapp.riskmanage.models.Categorie;
import com.stageapp.riskmanage.models.DayStat;
import com.stageapp.riskmanage.models.DayUsageStats;
import com.stageapp.riskmanage.models.Profile;
import com.stageapp.riskmanage.models.mNotification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import io.paperdb.Paper;

public class ActivitySplashScreen extends AppCompatActivity {

    ImageView backbtn;
    List<App> Apps;

    public ProgressBar progressBar;
    public Button button;
    public boolean r = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.button = (Button) findViewById(R.id.button);
        button.setVisibility(8);

        List<DayStat> dayStats = new ArrayList<>();

        for (int i = 1; i <= 24 ; i++) {

            try {

                dayStats.add(new DayStat(System.currentTimeMillis()-60*60*1000*i, (long) getRandom(59,0)));

            }catch (Exception e){
                Log.e("DayStats",""+e.getMessage());

            }
        }

        Paper.book().write("DayStats",dayStats);

        new LoadCategoriesData(this).loadStatistics(false);
        new LoadCategoriesData(this).loadStatistics(true);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Profile profile = Paper.book().read("User",new Profile("","","","",""));
                if (!profile.getUsername().isEmpty()){
                    ActivitySplashScreen.this.startActivity(new Intent(ActivitySplashScreen.this,MainActivity.class));
                    return;
                }
                ActivitySplashScreen.this.startActivity(new Intent(ActivitySplashScreen.this,UsernameAcitvity.class));


            }
        }, (long) 2600);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    protected float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }

}

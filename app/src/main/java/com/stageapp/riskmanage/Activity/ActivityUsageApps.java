package com.stageapp.riskmanage.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.stageapp.riskmanage.Adapters.AppsTrendViewPagerAdapter;
import com.stageapp.riskmanage.Adapters.AppsUsageViewPagerAdapter;
import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.Utils.NavigationItemSelectedListener;

public class ActivityUsageApps extends AppCompatActivity {

    RelativeLayout card;
    TextView titletv, desctv;


    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    ListView cardView;
    ViewPager viewPager;
    RecyclerView recyclerView;
    LinearLayoutManager mLayoutManager;
    LinearLayout appsBtn;
    ViewPager trend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_apps);

        card = findViewById(R.id.card);
        titletv = findViewById(R.id.titletv);
        desctv = findViewById(R.id.desctv);
//        cardView = findViewById(R.id.cardview);
        viewPager = findViewById(R.id.viewPager);
        trend = findViewById(R.id.viewPager2);
//        recyclerView = findViewById(R.id.recyclerview);

        /*---------------------Hooks------------------------*/

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(new NavigationItemSelectedListener(this,drawerLayout));
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        viewPager.setAdapter(new AppsUsageViewPagerAdapter(this));



        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }



}

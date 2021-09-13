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
import com.stageapp.riskmanage.Adapters.CallTrendViewPagerAdapter;
import com.stageapp.riskmanage.Adapters.CallsUsageViewPagerAdapter;
import com.stageapp.riskmanage.R;

public class ActivityUsageCalls extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

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
        setContentView(R.layout.activity_stats_calls);

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

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        viewPager.setAdapter(new CallsUsageViewPagerAdapter(this));
        trend.setAdapter(new CallTrendViewPagerAdapter(this));






//
//        card.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,MoreInfo.class);
//                Pair[] pairs = new Pair[3];
//                pairs[0] = new Pair<View, String>(card, "card");
//                pairs[1] = new Pair<View, String>(titletv, "title");
//                pairs[2] = new Pair<View, String>(desctv, "desc");
//
//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
//
//                startActivity(intent, options.toBundle());
//            }
//        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_profile:
                Log.e("click","Profile clickeed");
                Intent intent = new Intent(ActivityUsageCalls.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_login: menu.findItem(R.id.nav_logout).setVisible(true);
                menu.findItem(R.id.nav_profile).setVisible(true);
                menu.findItem(R.id.nav_login).setVisible(false);
                break;
            case R.id.nav_logout: menu.findItem(R.id.nav_logout).setVisible(false);
                menu.findItem(R.id.nav_profile).setVisible(false);
                menu.findItem(R.id.nav_login).setVisible(true);
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START); return true;
    }

}

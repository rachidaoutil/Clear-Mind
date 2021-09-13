package com.stageapp.riskmanage.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.stageapp.riskmanage.Adapters.MainViewPagerAdapter;
import com.stageapp.riskmanage.Adapters.OveriewPagerAdapter;
import com.stageapp.riskmanage.R;

public class ActivityOverview extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RelativeLayout card;
    TextView titletv, desctv,nfTxt,recTxt;
    ImageView nfImg,recImg;



    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    ViewPager viewPager;
    ViewPager viewPager2;

    LinearLayout todayBtn;
    LinearLayout weekBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        weekBtn = findViewById(R.id.week_btn);
        todayBtn = findViewById(R.id.today_btn);
        viewPager = findViewById(R.id.today_week_pager);

        nfTxt = findViewById(R.id.nf_text);
        nfImg = findViewById(R.id.nf_img);
        recTxt = findViewById(R.id.rec_text);
        recImg = findViewById(R.id.rec_img);

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

        viewPager.setAdapter(new OveriewPagerAdapter(this));

        todayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("btn","ntif btn clicked");
                ActivityOverview.this.viewPager.setCurrentItem(0);
            }
        });
        weekBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("btn","ntif btn recmndBtn");

                ActivityOverview.this.viewPager.setCurrentItem(1);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ActivityOverview.this.ChangeTap(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_profile:
                Log.e("click","Profile clickeed");
                Intent intent = new Intent(ActivityOverview.this, ProfileActivity.class);
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

    private void ChangeTap(int p){
       hideShowIcon(nfImg,true);
       hideShowIcon(recImg,true);
       changeUnchangeText(nfTxt,true);
       changeUnchangeText(recTxt,true);
        if (p == 0) {
            hideShowIcon(nfImg,false);
            changeUnchangeText(nfTxt,false);
        }else {
            hideShowIcon(recImg,false);
            changeUnchangeText(recTxt,false);
        }
    }

    private void hideShowIcon(ImageView img,boolean undo){
        if(!undo){
            img.setVisibility(View.VISIBLE);
        }else {
            img.setVisibility(View.INVISIBLE);
        }
    }


    private void changeUnchangeText(TextView txt, Boolean undo){
        if (undo) {
            txt.setTypeface(txt.getTypeface(),Typeface.NORMAL);
            return;
        }
        txt.setTypeface(txt.getTypeface(),Typeface.BOLD);
    }

}

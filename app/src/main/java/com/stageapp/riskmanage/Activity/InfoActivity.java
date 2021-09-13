package com.stageapp.riskmanage.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.Utils.NavigationItemSelectedListener;

public class InfoActivity extends AppCompatActivity {

    ImageView backbtn;
    TextView title;
    TextView text;
    String mTitle;
    String mText;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        this.mTitle = getIntent().getStringExtra("title");
        this.mText = getIntent().getStringExtra("text");

        this.title = findViewById(R.id.title);
        this.text = findViewById(R.id.text);

        this.title.setText(mTitle);
        this.text.setText(mText);


        /*---------------------Hooks------------------------*/

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(new NavigationItemSelectedListener(this,this.drawerLayout));
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();




        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
}

package com.stageapp.riskmanage.Activity;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.google.android.material.navigation.NavigationView;
import com.stageapp.riskmanage.Adapters.MainViewPagerAdapter;
import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.Services.usageService;
import com.stageapp.riskmanage.Utils.NavigationItemSelectedListener;
import com.stageapp.riskmanage.models.Profile;
import com.stageapp.riskmanage.models.Recommandation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_PERMISSION_READ_CALL_LOG = 0;
    private final int REQUEST_PERMISSION_PACKAGE_USAGE_STATS =1;


    RelativeLayout card;
    TextView titletv, desctv,nfTxt,recTxt,usernameTxt;
    ImageView nfImg,recImg;



    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;
    ViewPager viewPager;

    RelativeLayout appsBtn;
    RelativeLayout overviewBtn;
    RelativeLayout callsBtn;
    LinearLayout ntifBtn;
    LinearLayout recmndBtn;
    RelativeLayout settingsBtn;


    private String getJsonFromAssets() {
        String jsonString;
        try {
            InputStream is = getResources().openRawResource(R.raw.data);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

    private void loadData(){
        try {
            JSONObject jSONObject = new JSONObject(getJsonFromAssets());
            JSONArray jsonArray = new JSONArray(jSONObject.getString("data"));
            List<Recommandation> recommandations = new ArrayList<>();
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject mjSONObject = jsonArray.getJSONObject(i);
                    recommandations.add(new Recommandation(mjSONObject.getString("title"),mjSONObject.getString("description"),mjSONObject.getString("date"),true));
                }
                Paper.book().write("Recommandations",recommandations);

            }

        }catch (Exception e){
            Log.e("JSON","smth wnet wrong while prasinf your json"+e.getMessage());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        card = findViewById(R.id.card);
        titletv = findViewById(R.id.titletv);
        usernameTxt = findViewById(R.id.username);
        desctv = findViewById(R.id.desctv);
        appsBtn = findViewById(R.id.apps_btn);
        callsBtn = findViewById(R.id.calls_btn);
        ntifBtn = findViewById(R.id.notif_btn);
        recmndBtn = findViewById(R.id.recmnd_btn);
        settingsBtn = findViewById(R.id.settings_btn);
        overviewBtn = findViewById(R.id.overview_btn);

        viewPager = findViewById(R.id.viewPager);

        nfTxt = findViewById(R.id.nf_text);
        nfImg = findViewById(R.id.nf_img);
        recTxt = findViewById(R.id.rec_text);
        recImg = findViewById(R.id.rec_img);

        Profile profile = Paper.book().read("User",new Profile("","","name","",""));
        this.usernameTxt.setText(profile.getUsername()+"!");
        loadData();

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

        viewPager.setAdapter(new MainViewPagerAdapter(this));

        appsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getGrantStatus(android.Manifest.permission.PACKAGE_USAGE_STATS)){
                    startActivity(new Intent(MainActivity.this, ActivityUsageApps.class));
                    return;
                }
                Intent intent = new Intent(MainActivity.this,GrandPermissionActivity.class);
                intent.putExtra("permission",android.Manifest.permission.PACKAGE_USAGE_STATS);
                startActivity(intent);

            }
        });
        callsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getGrantStatus(android.Manifest.permission.READ_CALL_LOG)){
                    startActivity(new Intent(MainActivity.this, ActivityUsageCalls.class));
                    return;
                }
                showExplanation("permission","Grand this", android.Manifest.permission.READ_CALL_LOG,REQUEST_PERMISSION_READ_CALL_LOG);

            }
        });
        ntifBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("btn","ntif btn clicked");
                MainActivity.this.viewPager.setCurrentItem(1);
            }
        });
        recmndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("btn","ntif btn recmndBtn");

                MainActivity.this.viewPager.setCurrentItem(0);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("btn","btn Settings");

                startActivity(new Intent(MainActivity.this, ActivitySettings.class));
            }
        });

        overviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("btn","btn Settings");

                startActivity(new Intent(MainActivity.this, ActivityOverview.class));
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MainActivity.this.ChangeTap(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }


    private boolean getGrantStatus(String permission) {

        switch (permission){
            case android.Manifest.permission.PACKAGE_USAGE_STATS:
                AppOpsManager appOps = (AppOpsManager) getApplicationContext()
                        .getSystemService(Context.APP_OPS_SERVICE);

                int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS,
                        android.os.Process.myUid(), getApplicationContext().getPackageName());

                if (mode == AppOpsManager.MODE_DEFAULT) {
                    return (getApplicationContext().checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
                } else {
                    return (mode == MODE_ALLOWED);
                }
            case android.Manifest.permission.READ_CALL_LOG:
                return (getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED);
        }
        return false;

    }




    private void ChangeTap(int p){
       hideShowIcon(recImg,true);
       hideShowIcon(nfImg,true);
       changeUnchangeText(recTxt,true);
       changeUnchangeText(nfTxt,true);
        if (p == 1) {
            hideShowIcon(recImg,false);
            changeUnchangeText(recTxt,false);
        }else {
            hideShowIcon(nfImg,false);
            changeUnchangeText(nfTxt,false);
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

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_READ_CALL_LOG:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(MainActivity.this,  ActivityUsageCalls.class));

                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_PERMISSION_PACKAGE_USAGE_STATS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(MainActivity.this, ActivityUsageApps.class));

                    Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }




}

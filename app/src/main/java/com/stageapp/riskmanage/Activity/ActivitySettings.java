package com.stageapp.riskmanage.Activity;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.Services.usageService;
import com.stageapp.riskmanage.Utils.NavigationItemSelectedListener;
import com.xw.repo.BubbleSeekBar;

public class ActivitySettings extends AppCompatActivity {


    private final int REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 0;
    private final int REQUEST_PERMISSION_READ_PHONE_STATE = 1;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    Menu menu;

    SwitchCompat InCarBtn,BattreyBtn,SignalBtn,recommendedSettingBtn,getRecommendationsBtn,sreentimealertBtn,screentimereportsBtn;
    BubbleSeekBar usageTime,callLenght,freq;

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        this.settings = getSharedPreferences("Prefs", 0);
        this.editor   = settings.edit();
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

        /*---------------------Set Buttons------------------------*/
        InCarBtn = findViewById(R.id.incar_btn);
        BattreyBtn = findViewById(R.id.battrey_btn);
        SignalBtn = findViewById(R.id.signal_btn);

        screentimereportsBtn = findViewById(R.id.screentimereports_btn);
        sreentimealertBtn = findViewById(R.id.sreentimealert_btn);


        recommendedSettingBtn = findViewById(R.id.recommended_settings_btn);
        getRecommendationsBtn = findViewById(R.id.getrcmnd_btn);
        SignalBtn = findViewById(R.id.signal_btn);

        usageTime = findViewById(R.id.usage_time_seekbar);
        callLenght = findViewById(R.id.call_lenght_seekbar);
        freq = findViewById(R.id.notif_freq_seekbar);

        sreentimealertBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (getGrantStatus(android.Manifest.permission.PACKAGE_USAGE_STATS)){
                    if (isChecked) {
                        if(!usageService.isDestroyed)
                            startService(new Intent(ActivitySettings.this, usageService.class));
                    }else {
                            editor.putBoolean("sreentimeAlert",false);
                            editor.commit();
                            stopService(new Intent(ActivitySettings.this, usageService.class));

                    }
                    return;
                }
                Intent intent = new Intent(ActivitySettings.this,GrandPermissionActivity.class);
                intent.putExtra("permission",android.Manifest.permission.PACKAGE_USAGE_STATS);
                startActivity(intent);
            }
        });

        recommendedSettingBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setRecommendedSetting();
                }
            }
        });




        usageTime.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                Log.e("usageTime","Real Time Progress "+progress);


            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                Log.e("usageTime","Final Progress "+progress);


            }
        });
        callLenght.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                Log.e("callLenght","Real Time Progress "+progress);


            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                Log.e("callLenght","Final Progress "+progress);

            }
        });
        freq.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                Log.e("freq","Real Time Progress "+progress);


            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                Log.e("freq","Final Progress "+progress);


            }
        });

        freq.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (ActivitySettings.this.recommendedSettingBtn.isChecked()) {
                    ActivitySettings.this.recommendedSettingBtn.setChecked(false);
                }
                return false;
            }
        });
        callLenght.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (ActivitySettings.this.recommendedSettingBtn.isChecked()) {
                    ActivitySettings.this.recommendedSettingBtn.setChecked(false);
                }
                return false;
            }
        });
        usageTime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (ActivitySettings.this.recommendedSettingBtn.isChecked()) {
                    ActivitySettings.this.recommendedSettingBtn.setChecked(false);
                }
                return false;
            }
        });

        InCarBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    CheckPermission(ActivitySettings.this.SignalBtn);
                }

            }
        });
        BattreyBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    CheckPermission(ActivitySettings.this.SignalBtn);
                }
            }
        });
        SignalBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        CheckPermission(ActivitySettings.this.SignalBtn);
                    }
            }
        });

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);




    }

    @Override
    protected void onStart() {
        super.onStart();

        this.sreentimealertBtn.setChecked(this.settings.getBoolean("sreentimeAlert",false));


        this.usageTime.setProgress(this.settings.getInt("usageTime",0));
        this.callLenght.setProgress(this.settings.getInt("callLenght",0));
        this.freq.setProgress(this.settings.getInt("freq",0));

        this.InCarBtn.setChecked(this.settings.getBoolean("inCar",false));
        this.BattreyBtn.setChecked(this.settings.getBoolean("battreyisLow",false));
        this.SignalBtn.setChecked(this.settings.getBoolean("signalisLow",false));
        this.recommendedSettingBtn.setChecked(this.settings.getBoolean("recommendedSetting",false));
        this.getRecommendationsBtn.setChecked(this.settings.getBoolean("getRecommendations",false));

//        Toast.makeText(getApplicationContext(), "onStart called "+this.usageTime.getProgress(), Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "onStop called "+this.settings.getInt("usageTime",0), Toast.LENGTH_LONG).show();



    }

    @Override
    protected void onStop() {
        super.onStop();

        //Set params
        updateNotificationsSettings(0,SignalBtn.isChecked());
        updateNotificationsSettings(1,BattreyBtn.isChecked());
        updateNotificationsSettings(2,InCarBtn.isChecked());
        updateNotificationsSettings(3,recommendedSettingBtn.isChecked());
        updateNotificationsSettings(4,getRecommendationsBtn.isChecked());

        //Limits
        updateLimitsSettings(0,usageTime.getProgress()*60 * 60 * 1000 );
        updateLimitsSettings(1,callLenght.getProgress());
        updateLimitsSettings(2,freq.getProgress());

//        Toast.makeText(getApplicationContext(), "onStop called "+this.settings.getInt("usageTime",0), Toast.LENGTH_LONG).show();


    }

    private void updateNotificationsSettings(int level, boolean arg){

        switch (level){
            case 0: //Signal Notifications
                editor.putBoolean("signalisLow", arg);
                break;

            case 1: //battrey Notifications
                editor.putBoolean("battreyisLow", arg);
                break;

            case 2: //InCar Notifications
                editor.putBoolean("inCar", arg);
                break;
            case 3: //InCar Notifications
                editor.putBoolean("recommendedSetting", arg);
                break;
            case 4: //InCar Notifications
                editor.putBoolean("getRecommendations", arg);
                break;

        }
        editor.commit();

    }
    private void updateLimitsSettings(int level,int arg){

        switch (level){
            case 0: //Usage Time limit
                editor.putInt("usageTime", arg);
                break;

            case 1: //Call long limit
                editor.putInt("callLenght", arg);
                break;

            case 2: //Frequencey of Notifications
                editor.putInt("freq", arg);
                break;


        }
        editor.commit();

    }

    private void setRecommendedSetting(){
        this.usageTime.setProgress(5);
        this.callLenght.setProgress(20);
        this.freq.setProgress(5);
    }

    private void CheckPermission(SwitchCompat btn){
        if (!getGrantStatus(Manifest.permission.READ_PHONE_STATE)){
            showExplanation("READ PHONE STATE)","Needed Permission!",Manifest.permission.READ_PHONE_STATE,REQUEST_PERMISSION_READ_PHONE_STATE);
            btn.setChecked(false);
        }
        if (!getGrantStatus(Manifest.permission.ACCESS_FINE_LOCATION)){
            showExplanation("ACCESS FINE LOCATION","Needed Permission!",Manifest.permission.ACCESS_FINE_LOCATION,REQUEST_PERMISSION_ACCESS_FINE_LOCATION);
            btn.setChecked(false);
        }
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
            case Manifest.permission.ACCESS_FINE_LOCATION:
                return (getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
            case Manifest.permission.READ_PHONE_STATE:
                return (getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED);
        }
        return false;

    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_ACCESS_FINE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(ActivitySettings.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivitySettings.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_PERMISSION_READ_PHONE_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(ActivitySettings.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ActivitySettings.this, "Permission  Denied!", Toast.LENGTH_SHORT).show();
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

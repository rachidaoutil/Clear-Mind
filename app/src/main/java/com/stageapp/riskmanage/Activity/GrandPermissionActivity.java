package com.stageapp.riskmanage.Activity;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.stageapp.riskmanage.R;


public class GrandPermissionActivity extends AppCompatActivity {


    Button enableBtn, showBtn;
    TextView permissionDescriptionTv, usageTv;
    ListView appsList;
    String permission;


    // each time the application gets in foreground -> getGrantStatus and render the corresponding buttons
    @Override
    protected void onStart() {
        super.onStart();
        if (getGrantStatus(this.permission)) {
            showHideWithPermission();
            showBtn.setOnClickListener(view -> startActivity(new Intent(GrandPermissionActivity.this.getApplicationContext(), ActivityUsageApps.class)));
        } else {
            showHideNoPermission();
            enableBtn.setOnClickListener(view -> startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        this.permission = getIntent().getStringExtra("permission");
        enableBtn = findViewById(R.id.enable_btn);
        showBtn =  findViewById(R.id.show_btn);
        permissionDescriptionTv =findViewById(R.id.permission_description_tv);
        usageTv =  findViewById(R.id.usage_tv);
        appsList =  findViewById(R.id.apps_list);

    }

    /**
     * helper method used to show/hide items in the view when  PACKAGE_USAGE_STATS permission is not allowed
     */
    public void showHideNoPermission() {
        enableBtn.setVisibility(View.VISIBLE);
        permissionDescriptionTv.setVisibility(View.VISIBLE);
        showBtn.setVisibility(View.GONE);
        usageTv.setVisibility(View.GONE);
        appsList.setVisibility(View.GONE);

    }

    /**
     * helper method used to show/hide items in the view when  PACKAGE_USAGE_STATS permission allowed
     */
    public void showHideWithPermission() {
        enableBtn.setVisibility(View.GONE);
        permissionDescriptionTv.setVisibility(View.GONE);
        showBtn.setVisibility(View.VISIBLE);
        usageTv.setVisibility(View.GONE);
        appsList.setVisibility(View.GONE);
    }

    /**
     * check if PACKAGE_USAGE_STATS permission is aloowed for this application
     * @return true if permission granted
     */
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
            case Manifest.permission.READ_CALL_LOG:
                return (getApplicationContext().checkCallingOrSelfPermission(Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED);
        }
        return false;

    }



}

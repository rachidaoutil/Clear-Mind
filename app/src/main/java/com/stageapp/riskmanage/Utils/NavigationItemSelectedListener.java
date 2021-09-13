package com.stageapp.riskmanage.Utils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.stageapp.riskmanage.Activity.MainActivity;
import com.stageapp.riskmanage.Activity.ProfileActivity;
import com.stageapp.riskmanage.R;

public class NavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener{
    Context context;
    Menu menu;
    DrawerLayout drawerLayout;
    public NavigationItemSelectedListener(Context context, DrawerLayout drawerLayout) {
        this.context = context;
        this.drawerLayout = drawerLayout;

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_profile:
                Log.e("click","Profile clickeed");
                Intent intent = new Intent(this.context, ProfileActivity.class);
                this.context.startActivity(intent);
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
                Toast.makeText(this.context, "Share", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START); return true;
    }
}

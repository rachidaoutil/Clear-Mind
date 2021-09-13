package com.stageapp.riskmanage.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.models.mNotification;
import com.stageapp.riskmanage.models.Recommandation;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;


public class MainViewPagerAdapter extends PagerAdapter {

    private Context ctx;
    LayoutInflater mLayoutInflater;

    RecyclerView listView;


    LinearLayoutManager mLayoutManager;

    long Avrg;
    long Count;
    long Max;
    long Min;
    long total;




    public MainViewPagerAdapter(Context ctx) {
        this.ctx = ctx;
        mLayoutInflater = LayoutInflater.from(ctx);

    }




    @Override
    public Object instantiateItem(View collection, int position) {

        View v = mLayoutInflater.inflate( R.layout.mian_tabs_rec, (ViewGroup) collection, false);

        listView = v.findViewById(R.id.apps_list);
//        this.mLayoutManager = new LinearLayoutManager(this.ctx);
        this.mLayoutManager = new LinearLayoutManager(this.ctx);
        this.mLayoutManager.setReverseLayout(false);
        this.mLayoutManager.setStackFromEnd(true);
        this.listView.setHasFixedSize(false);
        this.listView.setLayoutManager(this.mLayoutManager);

        int resId = 0;
        switch (position) {
            case 0:
                Log.e("view","view"+position);
                ArrayList<Recommandation> recmndList = Paper.book().read("Recommandations", new ArrayList<>());

                RecommandationRecylcleAdapter rAdapter = new RecommandationRecylcleAdapter(this.ctx);
                rAdapter.addItems(recmndList);
                // attach the adapter to a ListView
                listView.swapAdapter(rAdapter,true );

                break;
            case 1:

                Log.e("view","view "+position);
                List<mNotification> notifList = new ArrayList<>();

                try {
                    notifList = Paper.book().read("Notifications", new ArrayList<>());

                }catch (Exception e){
                    Log.e("view","view " +e.getMessage());

                }

                NotificationRecylcleAdapter nAdapter = new NotificationRecylcleAdapter(this.ctx);
                nAdapter.addItems(notifList);
                // attach the adapter to a ListView
                listView.swapAdapter(nAdapter,true );

                break;


        }



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
    public int getCount() {
        return 2;
    }

    @Override
    public void startUpdate(View arg0) {
    }

    @Override
    public void finishUpdate(View arg0) {
    }





}


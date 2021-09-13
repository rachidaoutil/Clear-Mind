package com.stageapp.riskmanage.Adapters;


import static com.stageapp.riskmanage.Utils.TimeUtiles.getDurationBreakdown;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.models.App;

import java.util.ArrayList;


public class RecylcleAppsAdapter extends RecyclerView.Adapter<RecylcleAppsAdapter.ViewHolder> {
    Context context;
    /* access modifiers changed from: private */
    private ArrayList<App> mDataset;



    public RecylcleAppsAdapter(Context context) {
        this.context = context;
        this.mDataset = new ArrayList();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View convertView;
        TextView app_name_tv;
        TextView usage_duration_tv;
        TextView usage_perc_tv;
        ImageView icon_img;
        ProgressBar progressBar;




        public ViewHolder(View view) {
            super(view);
            this.convertView = view;

            this.app_name_tv = convertView.findViewById(R.id.app_name_tv);
            this.usage_duration_tv =  convertView.findViewById(R.id.usage_duration_tv);
            this.usage_perc_tv = convertView.findViewById(R.id.usage_perc_tv);
            this.icon_img =  convertView.findViewById(R.id.icon_img);
            this.progressBar = convertView.findViewById(R.id.progressBar);


        }
    }


    public void addItems(ArrayList<App> list) {
        this.mDataset.addAll(list);
        notifyItemRangeInserted(this.mDataset.size(), list.size());
    }
//
    public void reData(ArrayList<App> list) {
        this.mDataset = list;
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_app, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder,final int i) {

        viewHolder.app_name_tv.setText(this.mDataset.get(i).appName);
        viewHolder.usage_duration_tv.setText(getDurationBreakdown(this.mDataset.get(i).usageDuration));
        viewHolder.usage_perc_tv.setText(this.mDataset.get(i).usagePercentage + "%");
        if (this.mDataset.get(i).appIcon != null) {
           viewHolder.icon_img.setImageDrawable( getIcon(this.mDataset.get(i).pkg));
        }
        viewHolder.progressBar.setProgress(this.mDataset.get(i).usagePercentage);

//        viewHolder.Appname.setText(this.mDataset.get(i).getTitle());
//        viewHolder.Pack.setText(this.mDataset.get(i).getPack());
//        if (this.mDataset.get(i).getIcon() != null) {
//            viewHolder.Icon.setImageDrawable(RecylcleAdapter.this.context.getResources().getDrawable(R.drawable.logo));
//        }
//        viewHolder.cv.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent intent = new Intent(RecylcleAdapter.this.context, MoreInfo.class);
//                intent.putExtra("num",i);
//                RecylcleAdapter.this.context.startActivity(intent);
//
//            }
//        });

    }

    public int getItemCount() {
        return this.mDataset.size();
    }

    private Drawable getIcon(String pkg){
        Drawable icon = null;
        try {
            ApplicationInfo ai = this.context.getApplicationContext().getPackageManager().getApplicationInfo(pkg, 0);
            icon = this.context.getApplicationContext().getPackageManager().getApplicationIcon(ai);

        }catch (Exception e){
            Log.e("Icon",e.getMessage()+"");
        }
        return icon;

    }



}



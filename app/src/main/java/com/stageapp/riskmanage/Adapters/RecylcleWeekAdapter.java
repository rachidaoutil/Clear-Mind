package com.stageapp.riskmanage.Adapters;


import static com.stageapp.riskmanage.Utils.TimeUtiles.getDurationBreakdown;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.models.Categorie;

import java.util.ArrayList;


public class RecylcleWeekAdapter extends RecyclerView.Adapter<RecylcleWeekAdapter.ViewHolder> {
    Context context;
    /* access modifiers changed from: private */
    private ArrayList<Categorie> mDataset;



    public RecylcleWeekAdapter(Context context) {
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


    public void addItems(ArrayList<Categorie> list) {
        this.mDataset.addAll(list);
        notifyItemRangeInserted(this.mDataset.size(), list.size());
    }
//
    public void reData(ArrayList<Categorie> list) {
        this.mDataset = list;
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_app, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder,final int i) {

        viewHolder.app_name_tv.setText(this.mDataset.get(i).name);
        viewHolder.usage_duration_tv.setText(getDurationBreakdown(this.mDataset.get(i).timeSpentDay));
        viewHolder.usage_perc_tv.setText(this.mDataset.get(i).percnt + "%");
//        viewHolder.icon_img.setImageDrawable(this.mDataset.get(i).appIcon);
        viewHolder.progressBar.setProgress(this.mDataset.get(i).percnt);

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





}



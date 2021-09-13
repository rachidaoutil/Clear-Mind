package com.stageapp.riskmanage.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.models.mNotification;

import java.util.ArrayList;
import java.util.List;


public class NotificationRecylcleAdapter extends RecyclerView.Adapter<NotificationRecylcleAdapter.ViewHolder> {
    Context context;
    /* access modifiers changed from: private */
    private List<mNotification> mDataset;




    public static class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView title;
        TextView descrp;
//        TextView type;
        TextView date;
        TextView priorty;




        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.title = (TextView) view.findViewById(R.id.title);
            this.descrp = (TextView) view.findViewById(R.id.descrp);
//            this.type = (TextView) view.findViewById(R.id.type);
            this.date = (TextView) view.findViewById(R.id.date);
            this.priorty = (TextView) view.findViewById(R.id.priorty);
        }
    }

    public NotificationRecylcleAdapter(Context context) {
        this.context = context;
        this.mDataset = new ArrayList();
    }

    public void addItems(List<mNotification> list) {
        this.mDataset.addAll(list);
        notifyItemRangeInserted(this.mDataset.size(), list.size());
    }
//
    public void reData(List<mNotification> list) {
        this.mDataset = list;
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item_view, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder,final int i) {

        viewHolder.title.setText(this.mDataset.get(i).getnTitle());
        viewHolder.descrp.setText(this.mDataset.get(i).getnDescrp());
        viewHolder.date.setText(this.mDataset.get(i).getnDate());
//        viewHolder.type.setText(this.mDataset.get(i).getnType());
        viewHolder.priorty.setText(this.mDataset.get(i).getnPriorty());
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
        return mDataset.size();
    }




}



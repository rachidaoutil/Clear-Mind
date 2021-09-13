package com.stageapp.riskmanage.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stageapp.riskmanage.Activity.InfoActivity;
import com.stageapp.riskmanage.R;
import com.stageapp.riskmanage.models.Recommandation;

import java.util.ArrayList;
import java.util.List;


public class RecommandationRecylcleAdapter extends RecyclerView.Adapter<RecommandationRecylcleAdapter.ViewHolder> {
    Context context;
    /* access modifiers changed from: private */
    private List<Recommandation> mDataset;




    public static class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView title;
        TextView date;
//        TextView saved;
        RelativeLayout card;




        public ViewHolder(View view) {
            super(view);
            this.mView = view;
            this.card = view.findViewById(R.id.card);
            this.title = (TextView) view.findViewById(R.id.rtitle);
            this.date = (TextView) view.findViewById(R.id.rdate);
//            this.saved = (TextView) view.findViewById(R.id.rsaved);
        }
    }

    public RecommandationRecylcleAdapter(Context context) {
        this.context = context;
        this.mDataset = new ArrayList();
    }

    public void addItems(List<Recommandation> list) {
        this.mDataset.addAll(list);
        notifyItemRangeInserted(this.mDataset.size(), list.size());
    }
    //
    public void reData(List<Recommandation> list) {
        this.mDataset = list;
        notifyDataSetChanged();
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recomnd_item_view, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder,final int i) {

        viewHolder.title.setText(this.mDataset.get(i).getrTitle());
        viewHolder.date.setText(this.mDataset.get(i).getDate());
        if (this.mDataset.get(i).isrSave()) {
//            viewHolder.saved.setText("Saved");
        }

        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                RecommandationRecylcleAdapter activity = RecommandationRecylcleAdapter.this;
                Intent intent = new Intent(RecommandationRecylcleAdapter.this.context, InfoActivity.class);
                intent.putExtra("title",activity.mDataset.get(i).getrTitle());
                intent.putExtra("text",activity.mDataset.get(i).getrBref());
                activity.context.startActivity(intent);

            }
        });
    }

    public int getItemCount() {
        return mDataset.size();
    }




}



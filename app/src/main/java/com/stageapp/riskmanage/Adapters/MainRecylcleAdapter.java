package com.stageapp.riskmanage.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.stageapp.riskmanage.R;


public class MainRecylcleAdapter extends RecyclerView.Adapter<MainRecylcleAdapter.ViewHolder> {
    Context context;
    /* access modifiers changed from: private */
//    private List<Result> mDataset;



    public static class ViewHolder extends RecyclerView.ViewHolder {




        public ViewHolder(View view) {
            super(view);
//            this.viewi = view;
//            this.cv = (CardView) view.findViewById(R.id.itemCard);
//            this.Appname = (TextView) view.findViewById(R.id.Applabel);
//            this.Icon = (ImageView) view.findViewById(R.id.Icon);
//            this.nativeBannerAdContainer = (RelativeLayout) view.findViewById(R.id.native_banner_ad_container);
//            this.ll_adsnative = (LinearLayout) view.findViewById(R.id.ll_native_ads);
        }
    }

    public MainRecylcleAdapter(Context context) {
        this.context = context;
//        this.mDataset = new ArrayList();
    }

//    public void addItems(List<Result> list) {
//        this.mDataset.addAll(list);
//        notifyItemRangeInserted(this.mDataset.size(), list.size());
//    }
//
//    public void reData(List<Result> list) {
//        this.mDataset = list;
//        notifyDataSetChanged();
//    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item_view, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder,final int i) {

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
        return 10;
    }




}



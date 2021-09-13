package com.stageapp.riskmanage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.stageapp.riskmanage.R;

public class ProfileAdapter extends BaseAdapter {
    LayoutInflater inflter;
    Context context;
    public ProfileAdapter(Context context) {
        this.context = context;
        inflter = (LayoutInflater.from(context));

    }


    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        // Check if an existing view is being reused, otherwise inflate the view

        convertView = inflter.inflate(R.layout.notification_item_view, null);


        // Lookup view for data population


        // Populate the data into the template view using the data object


        // Return the completed view to render on screen
        return convertView;    }
}

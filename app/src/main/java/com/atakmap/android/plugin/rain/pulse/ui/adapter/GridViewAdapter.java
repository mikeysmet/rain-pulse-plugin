package com.atakmap.android.plugin.rain.pulse.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {
    private final Context mContext;
    private final int[] gridBoxes;
    private final GridView gridView;

    public GridViewAdapter(Context context, int[] gridBoxes, GridView gridView) {
        this.mContext = context;
        this.gridBoxes = gridBoxes;
        this.gridView = gridView;
    }

    public int getCount() {
        return this.gridBoxes.length;
    }

    public long getItemId(int position) {
        return 0L;
    }

    public Object getItem(int position) {
        return null;
    }

    @SuppressLint("ResourceType")
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView dummyTextView = new TextView(parent.getContext());
        dummyTextView.setGravity(4);
        dummyTextView.setTextColor(Color.parseColor("#ffffff"));
//        try {
//            dummyTextView.setBackgroundColor(Color.TRANSPARENT);
//            dummyTextView.setText(""+position);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        dummyTextView.setMinHeight(this.gridView.getHeight() / 6);
        return dummyTextView;
    }
}
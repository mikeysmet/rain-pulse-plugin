package com.atakmap.android.plugin.rain.pulse.cot;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.atakmap.android.maps.PointMapItem;

public class PulseCotDetailView extends LinearLayout {

    private static final String TAG = "PulseCotDetailView";
    private View _view;
    private Context _pluginContext;

    public PulseCotDetailView(Context pluginContext) {
        super(pluginContext);
    }


    public void setup(Context pluginContext){
        _pluginContext = pluginContext;
//        _view = LayoutInflater.from(pluginContext).inflate(R.layout.pulse_cot_details, this);

    }

    public void setItem(PointMapItem pointMapItem) {
        //parse out the heart rate and attributes and update UI
    }


}

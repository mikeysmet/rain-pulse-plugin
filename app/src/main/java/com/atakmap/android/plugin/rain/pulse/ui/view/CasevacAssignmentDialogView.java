package com.atakmap.android.plugin.rain.pulse.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.atakmap.android.plugin.rain.pulse.R;


public class CasevacAssignmentDialogView extends LinearLayout {
    private Context _pluginContext;
    private View _view;


    public CasevacAssignmentDialogView(Context context) {
        super(context);
    }

    public CasevacAssignmentDialogView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CasevacAssignmentDialogView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setup(Context pluginContext) {
        _pluginContext = pluginContext;
        _view = LayoutInflater.from(pluginContext).inflate(R.layout.layout_casevac_assignment, this);
    }

}

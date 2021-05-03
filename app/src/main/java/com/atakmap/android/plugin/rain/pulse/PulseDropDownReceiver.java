
package com.atakmap.android.plugin.rain.pulse;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.atak.plugins.impl.PluginLayoutInflater;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.dropdown.DropDown.OnStateListener;
import com.atakmap.android.dropdown.DropDownReceiver;

import com.atakmap.android.plugin.rain.pulse.model.TeamManager;
import com.atakmap.android.plugin.rain.pulse.prefs.PulsePreferenceFragment;
import com.atakmap.android.plugin.rain.pulse.ui.frag.PulseDropDownFragment;
import com.atakmap.android.plugin.rain.pulse.ui.frag.PulseSelfFragment;
import com.atakmap.android.plugin.rain.pulse.ui.frag.PulseToolbarFragment;
import com.atakmap.coremap.log.Log;

public class PulseDropDownReceiver extends DropDownReceiver implements
        OnStateListener {

    public static final String TAG = PulseDropDownReceiver.class
            .getSimpleName();

    public static final String SHOW_PLUGIN = "com.atakmap.android.plugin.rain.pulse.SHOW_PLUGIN";
    private MapView _mapView;
    private Context _pluginContext;
    private LayoutInflater _inflater;
    private PulseDropDownFragment _parentFragment;

    /**************************** CONSTRUCTOR *****************************/

    public PulseDropDownReceiver(final MapView mapView,
                                 final Context context, TeamManager teamManager) {
        super(mapView);
        _pluginContext = context;
        _inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        _mapView = mapView;
        _pluginContext = context;
        _parentFragment = new PulseDropDownFragment(_mapView, _pluginContext, teamManager);
        setAssociationKey(PulsePreferenceFragment.KEY);

    }

    /**************************** PUBLIC METHODS *****************************/

    public void disposeImpl() {
    }

    /**************************** INHERITED METHODS *****************************/

    @Override
    public void onReceive(Context context, Intent intent) {

        final String action = intent.getAction();
        if (action == null)
            return;


        if (action.equals(SHOW_PLUGIN)) {

            Log.d(TAG, "showing plugin drop down");
            showDropDown(_parentFragment, HALF_WIDTH, FULL_HEIGHT, FULL_WIDTH,
                    HALF_HEIGHT);
        }
    }

    @Override
    public void onDropDownSelectionRemoved() {
    }

    @Override
    public void onDropDownVisible(boolean v) {
    }

    @Override
    public void onDropDownSizeChanged(double width, double height) {
    }

    @Override
    public void onDropDownClose() {
    }

    public PulseToolbarFragment getToolbar(){
        //easy way to register status interface inside the toolbar.
        return _parentFragment.getToolbar();
    }

    public PulseSelfFragment getSelfFragment(){
        //easy way to register status interface inside the toolbar.
        return _parentFragment.getSelfFragment();
    }



}

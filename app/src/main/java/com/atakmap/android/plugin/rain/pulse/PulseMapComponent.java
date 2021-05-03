
package com.atakmap.android.plugin.rain.pulse;

import android.content.Context;
import android.content.Intent;
import com.atakmap.android.ipc.AtakBroadcast.DocumentedIntentFilter;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.dropdown.DropDownMapComponent;

import com.atakmap.android.medline.MedLineView;
import com.atakmap.android.plugin.rain.pulse.casevac.PulseCasevacProcessor;
import com.atakmap.android.plugin.rain.pulse.cot.PulseCotDetailManager;
import com.atakmap.android.plugin.rain.pulse.model.TeamManager;
import com.atakmap.android.plugin.rain.pulse.prefs.PulsePreferenceFragment;
import com.atakmap.android.plugin.rain.pulse.service.PulseServiceManager;
import com.atakmap.android.plugin.rain.pulse.track.PulseBloodhoundManager;
import com.atakmap.android.plugin.rain.pulse.track.sim.PulseHelivacSimulator;
import com.atakmap.android.plugin.rain.pulse.util.RunnableManager;
import com.atakmap.app.preferences.ToolsPreferenceFragment;
import com.atakmap.coremap.log.Log;

import static com.atakmap.android.maps.MapView._mapView;

public class PulseMapComponent extends DropDownMapComponent {

    private static final String TAG = "PulseMapComponent";

    private Context _pluginContext;

    private PulseDropDownReceiver _dropDownReceiver;
    private TeamManager _teamManager;
    private PulseServiceManager _pulseManager;

    private PulseCotDetailManager _detailManager;
    private PulseCasevacProcessor _casevacProcessor;

    public void onCreate(final Context context, Intent intent,
            final MapView view) {

        context.setTheme(R.style.ATAKPluginTheme);
        super.onCreate(context, intent, view);
        new RunnableManager(view);
        _pluginContext = context;

        _pulseManager = new PulseServiceManager(context, view);
        _detailManager = new PulseCotDetailManager(context, view);

        _teamManager = new TeamManager(_pluginContext, view);
        _dropDownReceiver = new PulseDropDownReceiver( view, context, _teamManager);

        Log.d(TAG, "registering the plugin filter");
        DocumentedIntentFilter ddFilter = new DocumentedIntentFilter();
        ddFilter.addAction(PulseDropDownReceiver.SHOW_PLUGIN);
        registerDropDownReceiver(_dropDownReceiver, ddFilter);

        _pulseManager.addBluetoothStatusListener(_dropDownReceiver.getToolbar());
        _pulseManager.addOnHeartRateUpdatedListener(_dropDownReceiver.getToolbar());
        _pulseManager.addOnHeartRateUpdatedListener(_detailManager);
        _detailManager.addSelfUpdateListener(_dropDownReceiver.getSelfFragment());

        _detailManager.addUpdateListener(_teamManager);

        android.util.Log.d(TAG, "DETAIL_MANAGER: " + _detailManager.toString());

        // for custom preferences
        ToolsPreferenceFragment
                .register(
                        new ToolsPreferenceFragment.ToolPreference(
                                "Pulse Preferences",
                                "Pulse user settings ",
                                PulsePreferenceFragment.KEY,
                                context.getResources().getDrawable(
                                        R.drawable.pulse_red, null),
                                new PulsePreferenceFragment(context)));


        _casevacProcessor = new PulseCasevacProcessor(_pluginContext, _mapView);
        MedLineView.getInstance(_mapView).addExternalMedevacProcessor(_pluginContext.getDrawable(R.drawable.pulse_red), "Pulse", _casevacProcessor);

        new PulseHelivacSimulator(_pluginContext, _mapView);
        new PulseBloodhoundManager(_pluginContext, _mapView);
    }

    @Override
    protected void onDestroyImpl(Context context, MapView view) {
        super.onDestroyImpl(context, view);
        _pulseManager.onDestroy();
        _detailManager.onDestroy();
        _casevacProcessor.onDestroy();
        MedLineView.getInstance(_mapView).removeExternalMedevacProcessor(_casevacProcessor);
        PulseHelivacSimulator.getInstance().onDestroy();
    }

}

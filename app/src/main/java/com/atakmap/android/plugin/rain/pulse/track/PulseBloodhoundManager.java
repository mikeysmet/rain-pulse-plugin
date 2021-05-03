package com.atakmap.android.plugin.rain.pulse.track;

import android.content.Context;
import android.util.Log;

import com.atakmap.android.bloodhound.link.BloodHoundLinkManager;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.maps.PointMapItem;
import com.atakmap.android.plugin.rain.pulse.util.RunnableManager;
import com.atakmap.android.toolbars.RangeAndBearingMapComponent;
import com.atakmap.android.toolbars.RangeAndBearingMapItem;

public class PulseBloodhoundManager {
    private static final String TAG = "PulseBloodhoundManager";

    /**
     * Used to monitor incoming heli track (uid)
     *  UID is provided by the Pulse WILCO response (for now it is hardcoded)
     *  When UID track is updated, we will try to start a bloodhound (Range and Bearing)
     *      from the heli to the casevac location.
     * */

    private static PulseBloodhoundManager _instance;
    private String _medivacUid = "";
    private PointMapItem _casevacItem;
    PointMapItem aircraft;
    public static PulseBloodhoundManager getInstance(){return _instance;}

    private final Context _pluginContext;
    private MapView _mapView;


    public PulseBloodhoundManager(Context pluginContext, MapView mapView){
        _pluginContext = pluginContext;
        _mapView = mapView;
        _instance = this;
    }


    public void onDestroy() {
    }

    public void start(String inBoundUid, String casevacUid) {

        RunnableManager.getInstance().post(new Runnable() {
            @Override
            public void run() {

                try {
                    _medivacUid = inBoundUid;
                    _casevacItem = (PointMapItem) _mapView.getRootGroup().deepFindItem("uid", casevacUid);
                    do {
                        aircraft = (PointMapItem) _mapView.getRootGroup().deepFindUID(inBoundUid);
                    }while (aircraft == null);
                    Log.d(TAG, "run_cas_sim: " + _casevacItem + " -- " + aircraft);
                    RangeAndBearingMapItem rab = RangeAndBearingMapItem.createOrUpdateRABLine("pulse", aircraft, _casevacItem);
                    RangeAndBearingMapComponent.getGroup().addItem(rab);
                    rab.persist(_mapView.getMapEventDispatcher(), null,
                            this.getClass());
                    BloodHoundLinkManager.getInstance().addLink(rab);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }
}

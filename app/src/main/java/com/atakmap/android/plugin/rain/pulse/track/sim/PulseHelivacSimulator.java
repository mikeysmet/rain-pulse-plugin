package com.atakmap.android.plugin.rain.pulse.track.sim;

import android.content.Context;
import android.os.Handler;

import com.atakmap.android.cot.CotMapComponent;
import com.atakmap.android.maps.MapView;
import com.atakmap.coremap.cot.event.CotDetail;
import com.atakmap.coremap.cot.event.CotEvent;
import com.atakmap.coremap.cot.event.CotPoint;
import com.atakmap.coremap.maps.coords.GeoCalculations;
import com.atakmap.coremap.maps.coords.GeoPoint;
import com.atakmap.coremap.maps.time.CoordinatedTime;

public class PulseHelivacSimulator {

    /**
     * Used to simulate a Friendly MEDIVAC aircraft going from home base to the location
     * of the CASEVAC -  will start bloodhound and thread the track
     * */

    public static final String SIM_UID_DEFAULT = "pulse-atak-casevac-inbound-heli";

    private static PulseHelivacSimulator _instance;
    private CotEvent _heliEvent;
    private Handler _handler;
    private int _updateCount;
    private int _updateStopCount;
    private float _updateDistanceM;
    private float _updatedHeadingDegTrue;

    public static PulseHelivacSimulator getInstance(){return _instance;}

    private final Context _pluginContext;
    private final MapView _mapView;

    public PulseHelivacSimulator(Context pluginContext, MapView mapView){
        _pluginContext = pluginContext;
        _mapView = mapView;
        _instance = this;
    }

    public void onDestroy() {
        if(_handler == null)return;
        _handler.removeCallbacksAndMessages(null);
    }

    public void start(CotEvent requestEvent, String eta, String callsign) {
        GeoPoint destination = requestEvent.getGeoPoint();
        int travelTimeSeconds = 4500;//TODO - find a better way to parse that eta time.
        int distanceMeters = 100000;
        float headingDegTrue = 45.0f;
        GeoPoint fakeStartPoint = GeoCalculations.pointAtDistance(destination, headingDegTrue,distanceMeters );

        float inboundHeadingDegTrue = headingDegTrue+180;
        float speedMps = distanceMeters/travelTimeSeconds;

        _heliEvent =  createHeliEvent(SIM_UID_DEFAULT, callsign,inboundHeadingDegTrue, speedMps );
        _heliEvent.setPoint(new CotPoint(fakeStartPoint));

        startFlying(fakeStartPoint, destination, travelTimeSeconds,inboundHeadingDegTrue, speedMps);
    }

    public static CotEvent createHeliEvent(String uid, String callsign, float headingDegTrue, float speedMps){
        CotEvent event = new CotEvent();
        event.setType("a-f-A-M-H-H");
        event.setUID(uid);
        event.setHow(CotEvent.HOW_MACHINE_GENERATED);

        CoordinatedTime time = new CoordinatedTime();
        event.setTime(time);
        event.setStart(time);
        event.setStale(time.addHours(1));
        CotDetail detail = new CotDetail("detail");

        CotDetail contact = new CotDetail("contact");
        contact.setAttribute("callsign", callsign);
        detail.addChild(contact);

        CotDetail track = new CotDetail("track");
        track.setAttribute("course", String.valueOf(headingDegTrue));
        track.setAttribute("speed", String.valueOf(speedMps));
        detail.addChild(track);

        event.setDetail(detail);
        return event;
    }

    private void startFlying(GeoPoint fakeStartPoint, GeoPoint destination, int travelTimeSeconds, float headingDegTrue, float speedMps) {
        //update once per second for travelTimeSeconds
        _updateStopCount = travelTimeSeconds;
        _updateDistanceM = speedMps;
        _updatedHeadingDegTrue = headingDegTrue;

        _handler = new Handler();
        final int delay = 1000; // 1000 milliseconds == 1 second
        _updateCount = 0;

        _handler.postDelayed(new Runnable() {
            public void run() {
                if(_updateStopCount == _updateCount)return;
                updatePosition();
                sendEvent();
                _updateCount++;
                _handler.postDelayed(this, delay);
            }
        }, delay);

    }

    private void updatePosition() {
        GeoPoint start = _heliEvent.getGeoPoint();
        GeoPoint newPoint = GeoCalculations.pointAtDistance(start, _updatedHeadingDegTrue, _updateDistanceM);
        updateEvent(newPoint);
    }

    private void sendEvent() {
        CotMapComponent.getExternalDispatcher()
                .dispatchToBroadcast(_heliEvent);
    }

    private void updateEvent(GeoPoint newLocation)
    {
        _heliEvent.setPoint(new CotPoint(newLocation));
        CoordinatedTime time = new CoordinatedTime();
        _heliEvent.setTime(time);
        _heliEvent.setStart(time);
        _heliEvent.setStale(time.addHours(1));
    }

}

package com.atakmap.android.plugin.rain.pulse.cot;

import android.util.Log;

import com.atakmap.android.cot.detail.CotDetailHandler;
import com.atakmap.android.maps.MapItem;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.maps.Marker;
import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;
import com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs;
import com.atakmap.comms.CommsMapComponent;
import com.atakmap.coremap.cot.event.CotDetail;
import com.atakmap.coremap.cot.event.CotEvent;
import com.atakmap.coremap.maps.coords.GeoPoint;
import com.atakmap.coremap.maps.time.CoordinatedTime;

public class PulseDetailHandler extends CotDetailHandler {

    private static final String TAG = "PulseCotDetailHandler";

    private static final String COT_TYPE_CASEVAC = "b-r-f-h-c";


    private final PulseCotDetailManager _parent;
    private final MapView _mapView;
    //for now - lets store the patient ID here
    private String _patientTeamId;

    public PulseDetailHandler(PulseCotDetailManager parent, MapView mapView) {
        super(PulseCotDetail.DETAIL_PULSE);
        _parent = parent;
        _mapView = mapView;
    }

    @Override
    public CommsMapComponent.ImportResult toItemMetadata(MapItem mapItem, CotEvent cotEvent, CotDetail cotDetail) {
        String name = cotDetail.getElementName();
        if (name.equals(PulseCotDetail.DETAIL_PULSE)) {
            mapItem.setMetaParcelable(PulseCotDetail.DETAIL_PULSE, cotDetail);
            Log.d(TAG, "found_pulse_alert: " + cotEvent.getUID() + " Type: " + cotEvent.getType() + "\n" + cotDetail);

            //if the CASEVAC Event contains a pulse detail, that
            // is the new patient ID.
            if (cotEvent.getType().equals(COT_TYPE_CASEVAC)) {
                updatePatientId(cotDetail);
            } else {
                Log.d("PARENT_INFO", cotDetail.toString());
                updateParent(mapItem, cotDetail);
            }
            return CommsMapComponent.ImportResult.SUCCESS;
        }

        return CommsMapComponent.ImportResult.IGNORE;
    }

    private void updatePatientId(CotDetail cotDetail) {
        TeamMemberInputs update = PulseCotDetail.toTeamMember(cotDetail);
        Log.d("updatePatientId", update.getTmCallsign() + " | " + update.tmCasualty);
        PulsePrefs.setPatientId(update.getTmCombatID());
        _patientTeamId = update.getTmCombatID();

        update.setTmCasualty(update.tmCasualty);

        _parent.handleTeamUpdate(update);
    }


    private void updateParent(MapItem mapItem, CotDetail cotDetail) {
        //create team member struct and pass to TeamRelay
        Log.d("UPDATE_PARENT", cotDetail.toString());
        if (!(mapItem instanceof Marker)) return;
        Marker marker = (Marker) mapItem;
        GeoPoint location = marker.getPoint();
        String uid = marker.getUID();
        String callsign = marker.getTitle();
        String type = marker.getType();
        int color = marker.getColor();
        TeamMemberInputs update = PulseCotDetail.toTeamMember(cotDetail);
        update.setTmLon(location.getLongitude());
        update.setTmLat(location.getLatitude());

        update.setLastReportTime(new CoordinatedTime().getMilliseconds());

        _parent.handleTeamUpdate(update);
    }

    @Override
    public boolean toCotDetail(MapItem mapItem, CotEvent cotEvent, CotDetail cotDetail) {
        return false;
    }

    public void setCurrentPatientId(String patientId) {
        _patientTeamId = patientId;
    }
}

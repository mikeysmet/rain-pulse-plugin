package com.atakmap.android.plugin.rain.pulse.cot;

import com.atakmap.android.cot.detail.CotDetailHandler;
import com.atakmap.android.maps.MapItem;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugin.rain.pulse.cot.event.PulseCasevacEvent;
import com.atakmap.comms.CommsMapComponent;
import com.atakmap.coremap.cot.event.CotDetail;
import com.atakmap.coremap.cot.event.CotEvent;

public class PulseCasevacDetailHandler  extends CotDetailHandler {
    private static final String TAG = "PulseCasevacDetailHandler";

    private MapView _mapView;


    public PulseCasevacDetailHandler(MapView mapView){
        super(PulseCasevacEvent.COT_DETAIL_PULSE_CASEVAC);
        _mapView = mapView;
    }


    @Override
    public CommsMapComponent.ImportResult toItemMetadata(MapItem mapItem, CotEvent cotEvent, CotDetail cotDetail) {
        String name = cotDetail.getElementName();
        if(!name.equals(PulseCasevacEvent.COT_DETAIL_PULSE_CASEVAC))return CommsMapComponent.ImportResult.IGNORE;
        //handle casevac update message
        PulseCasevacRequestManager.getInstance().update(cotEvent);
        return CommsMapComponent.ImportResult.SUCCESS;
    }

    @Override
    public boolean toCotDetail(MapItem mapItem, CotEvent cotEvent, CotDetail cotDetail) {
        return false;
    }
}

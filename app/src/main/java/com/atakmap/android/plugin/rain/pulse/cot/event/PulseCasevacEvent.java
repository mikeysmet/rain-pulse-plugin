package com.atakmap.android.plugin.rain.pulse.cot.event;

import android.util.Log;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugin.rain.pulse.model.CasevacDetailsInputs;
import com.atakmap.coremap.cot.event.CotDetail;
import com.atakmap.coremap.cot.event.CotEvent;
import com.atakmap.coremap.maps.time.CoordinatedTime;

import java.util.UUID;

import static com.atakmap.android.plugin.rain.pulse.cot.PulseCotDetail.TAG;

@SuppressWarnings("UnnecessaryLocalVariable")
public class PulseCasevacEvent {

    /**
     * Tasking types and extensions are based on content from
     * CoT-Request-V2.pdf
     *  CoT’s Generalized Request/Response/Status Coordination (MITRE)
     * */

    public static final String TYPE_REQUEST = "t-a-r-c";
    public static final String TYPE_CANTCO = "y-c-f";//reply, complete, failure
    public static final String TYPE_ACK = "y-a-r";//reply, ack, received
    public static final String TYPE_WILCO = "y-a-w";//reply, ack, wilco
    public static final String TYPE_EXECUTING = "y-s-e";//reply, status, executing started
    //use treatment type when updating treatment timeline/status
    public static final String TYPE_TREATMENT = "y-s-e-u";//reply, status, executing, update

    public static final String COT_DETAIL_PULSE_CASEVAC = "__pulse_casevac";

    private static final String COT_DETAIL_PULSE_CASEVAC_REQUEST = "request";
    private static final String COT_DETAIL_ATTR_PULSE_SENDER_UID = "sender_uid";
    private static final String COT_DETAIL_ATTR_PULSE_CASEVAC_EVENT_UID = "casevac_uid";
    private static final String COT_DETAIL_PULSE_CASEVAC_EVENT_CALLSIGN = "casevac_callsign";

    public static final String COT_DETAIL_PULSE_CASEVAC_RESPONSE = "casevac_response";

    private static CotEvent _coreCasevacEvent;

    public PulseCasevacEvent(CotEvent medevacEvent) {
        //start event.
        _coreCasevacEvent = medevacEvent;
    }

    public CotEvent toRequestEvent(){
        //create the
        CotEvent pulseEvent = getNewBaseEvent();
        pulseEvent.setPoint(_coreCasevacEvent.getCotPoint());
        pulseEvent.setType(TYPE_REQUEST);

        CotDetail pulseDetail = new CotDetail(COT_DETAIL_PULSE_CASEVAC);
        pulseDetail.addChild(getRequestDetail());
        //what else do we need to see here?
        pulseEvent.getDetail().addChild(pulseDetail);

        return pulseEvent;
    }

    public static CotEvent getRequestEvent(CotEvent medevacEvent){
        CotEvent pulseEvent = getNewBaseEvent();
        pulseEvent.setPoint(medevacEvent.getCotPoint());
        pulseEvent.setType(TYPE_REQUEST);

        CotDetail pulseDetail = new CotDetail(COT_DETAIL_PULSE_CASEVAC_REQUEST);
        pulseDetail.addChild(getRequestDetail());
        //what else do we need to see here?
        pulseEvent.getDetail().addChild(pulseDetail);

        return pulseEvent;
    }

    public static CotEvent getWilcoEvent(CotEvent requestEvent, CotDetail pulseRequestDetail)
    {
        CotEvent wilcoEvent = getBaseEvent(requestEvent, TYPE_WILCO);
        return wilcoEvent;
    }

    public static CotEvent getCantcoEvent(CotEvent requestEvent, CotDetail pulseRequestDetail)
    {
        CotEvent cantcoEvent = getBaseEvent(requestEvent, TYPE_CANTCO);
        cantcoEvent.setType(TYPE_CANTCO);
        return cantcoEvent;
    }

    public static CotEvent getAckEvent(CotEvent requestEvent, CotDetail pulseRequestDetail)
    {
        CotEvent ackEvent = getBaseEvent(requestEvent, TYPE_ACK);
        ackEvent.setType(TYPE_ACK);
        return ackEvent;
    }

    public static CotEvent getExecutionEvent(CotEvent requestEvent, CasevacDetailsInputs casevacDetailsInputs)
    {
        CotEvent executionEvent = getBaseEvent(requestEvent, TYPE_EXECUTING);
        CotDetail pulseDetail = executionEvent.findDetail(COT_DETAIL_PULSE_CASEVAC);
        if(pulseDetail == null)
        {
            Log.d(TAG, "We're having some issues here");
            return null;
        }
        if(_coreCasevacEvent != null){
            String casevacUid = _coreCasevacEvent.getUID();
            pulseDetail.setAttribute(COT_DETAIL_ATTR_PULSE_CASEVAC_EVENT_UID, casevacUid);
        }
        pulseDetail.addChild(casevacDetailsInputs.toCotDetail());
        //what else do we need to see here?
        executionEvent.getDetail().addChild(pulseDetail);
        return executionEvent;
    }

    private static CotEvent getNewBaseEvent() {
        CotEvent event = new CotEvent();
        event.setUID(UUID.randomUUID().toString());
        event.setHow("m-g");
        event.setStart(new CoordinatedTime());
        event.setTime(new CoordinatedTime());
        event.setStale(new CoordinatedTime().addHours(1));//TODO - how long do we stale?
        event.setDetail(new CotDetail());
        CotDetail pulseDetail = new CotDetail(COT_DETAIL_PULSE_CASEVAC);
        //pulseDetail.addChild(getCasevacUid());
        pulseDetail.addChild(getRequestDetail());
        event.getDetail().addChild(pulseDetail);

        return event;
    }

    private static CotEvent getBaseEvent(CotEvent requestEvent, String newType)
    {
        CotEvent event = getNewBaseEvent();
        event.setUID(requestEvent.getUID());
        event.setPoint(requestEvent.getCotPoint());
        event.setType(newType);
        return event;
    }

    private static CotDetail getRequestDetail() {
        //Can we get by with just having the sender UID, or do we need an IP and port?
        //<request notify=“adocs.aoc.af.mil:18000:tcp” />
        CotDetail requestDetail = new CotDetail(COT_DETAIL_PULSE_CASEVAC_REQUEST);
        requestDetail.setAttribute(COT_DETAIL_ATTR_PULSE_SENDER_UID, MapView.getDeviceUid());
        if(_coreCasevacEvent != null){
            String casevacUid = _coreCasevacEvent.getUID();
            requestDetail.setAttribute(COT_DETAIL_ATTR_PULSE_CASEVAC_EVENT_UID, casevacUid);
        }
        return requestDetail;
    }

    public static String getContactUid(CotDetail pulseDetail){
        //attempt to get the UID, so we can send the response back to the sender.
        if(pulseDetail == null)return null;
        CotDetail pulseRequestDetail = pulseDetail.getFirstChildByName(0, COT_DETAIL_PULSE_CASEVAC_REQUEST);
        if(pulseRequestDetail == null)return null;
        String uid = pulseRequestDetail.getAttribute(COT_DETAIL_ATTR_PULSE_SENDER_UID);
        return uid;
    }

    public static String getCasevacUid(CotDetail pulseDetail){
        //attempt to get the UID, so we can open marker details of the casevac
        if(pulseDetail == null)return null;
        CotDetail pulseRequestDetail = pulseDetail.getFirstChildByName(0, COT_DETAIL_PULSE_CASEVAC_REQUEST);
        if(pulseRequestDetail == null)return null;
        String uid = pulseRequestDetail.getAttribute(COT_DETAIL_ATTR_PULSE_CASEVAC_EVENT_UID);
        return uid;
    }

}

package com.atakmap.android.plugin.rain.pulse.casevac;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.atakmap.android.contact.Contact;
import com.atakmap.android.contact.ContactPresenceDropdown;
import com.atakmap.android.contact.Contacts;
import com.atakmap.android.cot.CotMapComponent;
import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.medline.MedLineView;
import com.atakmap.android.plugin.rain.pulse.cot.PulseCotDetail;
import com.atakmap.android.plugin.rain.pulse.cot.event.PulseCasevacEvent;
import com.atakmap.android.plugin.rain.pulse.model.CasevacDetailsInputs;
import com.atakmap.android.plugin.rain.pulse.model.PulseMistReport;
import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;
import com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs;
import com.atakmap.android.plugin.rain.pulse.track.PulseBloodhoundManager;
import com.atakmap.android.plugin.rain.pulse.track.sim.PulseHelivacSimulator;
import com.atakmap.android.plugin.rain.pulse.ui.dialog.CasevacConfirmationDialog;
import com.atakmap.android.plugin.rain.pulse.util.RunnableManager;
import com.atakmap.comms.CommsMapComponent;
import com.atakmap.comms.CotServiceRemote;
import com.atakmap.coremap.cot.event.CotDetail;
import com.atakmap.coremap.cot.event.CotEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PulseCasevacProcessor implements MedLineView.ExternalMedevacProcessor {
    /**
     *  Add button to the CASEVAC SEND
     *      1. Add Pulse MIST report to the CoT Event
     *      2. Save Patient ID, which will populate patient monitor.
     *      3. Show Contacts List
     *      4. Send CASEVAC REQUEST.
     *
     */


    private static final String TAG = "PulseCasevacProcessor";

    private static final String EXTRA_COT_EVENT = "com.atakmap.contact.CotEvent";
    private static final String EXTRA_COT_EVENT_MULTIPLE = "com.atakmap.contact.MultipleCotEvents";

    private static final String ACTION_SHOW_DETAILS = "com.atakmap.android.cotdetails.COTINFO";
    private static final String EXTRA_UID = "targetUID";

    private final Context _pluginContext;
    private final MapView _mapView;
    private static CasevacUpdateInterface casevacUpdateInterfaceListener;

    private static PulseCasevacProcessor _instance;
    private TeamMemberInputs _patient;
    private CasevacDetailsInputs _casevacDetailsInputs;
    private PulseMistReport _mistReport;
    private CotEvent _casevacRequestEvent;
    private boolean _request = false;
    private List<CasevacUpdateInterface> _listeners = new ArrayList<>();
    private CasevacDetailsInputs _lastCasevacStatus;
    private CotEvent _originalCasevacEvent;

    public static PulseCasevacProcessor getInstance(){return _instance;}

    public PulseCasevacProcessor(Context pluginContext, MapView mapView){
        _pluginContext = pluginContext;
        _mapView = mapView;
        _instance = this;
        CommsMapComponent.getInstance().addOnCotEventListener(_cotEventListener);
    }

    public void onDestroy(){
        CommsMapComponent.getInstance().removeOnCotEventListener(_cotEventListener);
    }

    public void onCasevacWilco() {
        //save boolean var here for casevac
        PulsePrefs.setIsCasevacInProgress(true);
    }

    @Override
    public boolean processMedevac(CotEvent cotEvent) {
        Log.d(TAG, "Processing Medevac Request");

        _originalCasevacEvent = cotEvent;

        RunnableManager.getInstance().toast("Processing Pulse CASEVAC");
        //first send out the entire casevac via broadcast to send out to the team.
        PulsePrefs.setPatientId(_patient.getTmCombatID());

        CotDetail pulseDetail = PulseCotDetail.toDetail(_patient);
        cotEvent.getDetail().addChild(pulseDetail);
        //b-r-f-h-c
        CotMapComponent.getExternalDispatcher().dispatchToBroadcast(cotEvent);

        sendCotEvents(cotEvent);
        return true;
    }

    private void sendCotEvents(CotEvent medevacEvent){
        PulseCasevacEvent casevacEvent = new PulseCasevacEvent(medevacEvent);
        //save the request to handle future wilco/cantco
        _casevacRequestEvent = casevacEvent.toRequestEvent();

        Intent contactList = new Intent();
        contactList.setAction(ContactPresenceDropdown.SEND_LIST);
        //contactList.putExtra("targetUID", medevacEvent.getUID());
        //add CoT Event parcelable
        //contactList.putExtra(EXTRA_COT_EVENT, medevacEvent);
        List<CotEvent> eventList = new ArrayList<>();
        if(_mistReport != null){
            _mistReport.addMistToEvent(medevacEvent);
        }
        eventList.add(medevacEvent);
        eventList.add(_casevacRequestEvent);
        contactList.putExtra(EXTRA_COT_EVENT_MULTIPLE, (Serializable) eventList);
        AtakBroadcast.getInstance().sendBroadcast(contactList);
    }

    public void setCurrentCasevacData(TeamMemberInputs patient, PulseMistReport mistReport) {
        //set patient data, so we can add this information when publishing the casevac.
        _patient = patient;
        _mistReport = mistReport;
    }

    public void setCurrentCasevacAssignmentData(CasevacDetailsInputs casevacAssignmentData){
        _casevacDetailsInputs = casevacAssignmentData;
        Log.d(TAG, "setCurrentCasevacAssignmentData: " + _casevacDetailsInputs);
    }

    private final CotServiceRemote.CotEventListener _cotEventListener = (cotEvent, bundle) -> {
        Log.d(TAG, "received message of type " + cotEvent.getType());
        CotDetail pulseRequestDetail = cotEvent.getDetail().getFirstChildByName(0, PulseCasevacEvent.COT_DETAIL_PULSE_CASEVAC);
        if(pulseRequestDetail == null)return;
        Log.d(TAG, "FOUND OUR EVENT!!!!!!");
        //TODO - show wilco canto dialog.
        processIncomingPulseEvent(cotEvent, pulseRequestDetail);
    };

    private void processIncomingPulseEvent(CotEvent cotEvent, CotDetail pulseRequestDetail) {

        Log.d(TAG, "processIncomingPulseEvent: - - ->" + cotEvent + "\n " + pulseRequestDetail);
        String type = cotEvent.getType();

        if(type.equals(PulseCasevacEvent.TYPE_REQUEST)){
            //first send ack, then show dialog
            //send ack
            sendAck(cotEvent, pulseRequestDetail);
            //show wilco cantco
            showRequestDialog(cotEvent, pulseRequestDetail);
        }
        else if(type.equals(PulseCasevacEvent.TYPE_ACK)){
            //update UI to acknowledge the request has been received.
            processAcknowledgement(cotEvent, pulseRequestDetail);

        }
        else if(type.equals(PulseCasevacEvent.TYPE_CANTCO)){
            //update UI to
            processCantco(cotEvent);
        }
        else if(type.equals(PulseCasevacEvent.TYPE_WILCO)){
            //WILCO - await an update with
            processWilco(cotEvent, pulseRequestDetail);
        }
        else if(type.equals(PulseCasevacEvent.TYPE_EXECUTING)) {
            processExecutionUpdate(cotEvent, pulseRequestDetail);
        }
    }

    private void processExecutionUpdate(CotEvent cotEvent, CotDetail pulseRequestDetail) {
        //update patient monitor
        CasevacDetailsInputs status = CasevacDetailsInputs.parsePulseDetail(pulseRequestDetail);
        if(status == null){
            Log.d(TAG, "execution status is null");
        }
        _lastCasevacStatus = status;
        for (CasevacUpdateInterface listener:_listeners) {
            listener.onCasevacUpdated(status.getStatus(), status.getEta(), status.getCasevacCallsign());
        }

        PulseBloodhoundManager.getInstance().start(status.getInBoundUid(), _originalCasevacEvent.getUID());
    }


    private void sendAck(CotEvent cotEvent, CotDetail pulseRequestDetail) {
        CotEvent ackEvent = PulseCasevacEvent.getAckEvent(cotEvent, pulseRequestDetail);
        sendEvent(ackEvent, pulseRequestDetail);
    }

    private void sendEvent(CotEvent pulseEvent, CotDetail pulseRequestDetail) {
        //send all external events here
        String destinationUid = PulseCasevacEvent.getContactUid(pulseRequestDetail);

        if(destinationUid != null){
            Log.d(TAG, "destination UID is valid, finding contact");
            Contact contact = Contacts.getInstance().getContactByUuid(destinationUid);
            if(contact != null){
                Log.d(TAG, "contact is valid, sending direct");
                CotMapComponent.getExternalDispatcher().dispatchToContact(pulseEvent, contact);
                return;
            }
            Log.d(TAG, "contact is not valid");
        }else{
            Log.d(TAG, "destination UID is NOT valid");
        }
        Log.d(TAG, "sending broadcast and hoping.");
        //broadcast I guess and hope for the best
        CotMapComponent.getExternalDispatcher().dispatchToBroadcast(pulseEvent);
    }

    private void processCantco(CotEvent cotEvent) {
        Log.d(TAG, cotEvent.getUID() + " was canceled");
        RunnableManager.getInstance().toast("PULSE CASEVAC CANTCO'd");
        //TODO - what happens now that we have a denied request?
    }

    private void processAcknowledgement(CotEvent cotEvent, CotDetail pulseRequestDetail) {
        //update UI
        Log.d(TAG, "CASEVAC REQUEST RECEIVED!!!!");
        RunnableManager.getInstance().toast("CASEVAC REQUEST RECEIVED!!");

        //TODO - how do we handle the Acknowledgement?


    }

    private void processWilco(CotEvent cotEvent, CotDetail pulseRequestDetail) {
        Log.d(TAG, "WILCO RECEIVED!!!!");
        RunnableManager.getInstance().toast("WILCO RECEIVED!!");
        //TODO - what happens now that we have an accepted request? Show casevac toolbar
        onCasevacWilco();
        Log.d(TAG, "processWilco: " +  "\n " + pulseRequestDetail);
        //Show the casevac tool bar for incoming updates
    }

    private void showRequestDialog(CotEvent cotEvent, CotDetail pulseRequestDetail) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                openCasevacDetails(pulseRequestDetail);
                CasevacConfirmationDialog dialog = new CasevacConfirmationDialog(_mapView, _pluginContext, cotEvent, pulseRequestDetail);
                dialog.show();
            }
        });
    }

    private void openCasevacDetails(CotDetail pulseRequestDetail) {
        String casevacUid = PulseCasevacEvent.getCasevacUid(pulseRequestDetail);
        if(casevacUid == null)
        {
            Log.d(TAG, "why is casevac UID null");
            return;
        }

        Intent showDetailsEvent = new Intent(ACTION_SHOW_DETAILS);
        showDetailsEvent.putExtra(EXTRA_UID, casevacUid);
        AtakBroadcast.getInstance().sendBroadcast(showDetailsEvent);
    }

    public void sendResponse(CotEvent cotEvent, CotDetail pulseRequestDetail, boolean accepted) {
        CotEvent responseEvent;
        Log.d(TAG, "Sending CASEVAC Response");
        if(accepted){
            Log.d(TAG, "Sending WILCO Response");
            responseEvent = PulseCasevacEvent.getWilcoEvent(cotEvent, pulseRequestDetail);
        }else{
            Log.d(TAG, "Sending CANTCO Response");
            responseEvent = PulseCasevacEvent.getCantcoEvent(cotEvent, pulseRequestDetail);
        }
        sendEvent(responseEvent, pulseRequestDetail);
    }

    public void sendUpdate(CotEvent requestEvent,  CotDetail pulseRequestDetail, CasevacDetailsInputs casevacDetailsInputs) {
        CotEvent updateEvent = PulseCasevacEvent.getExecutionEvent(requestEvent, casevacDetailsInputs);
        sendEvent(updateEvent, pulseRequestDetail);

        PulseHelivacSimulator.getInstance().start(requestEvent, "", casevacDetailsInputs.getCasevacCallsign());
    }

    public void addOnCasevacUpdateListener(CasevacUpdateInterface updateInterface) {
        _listeners.add(updateInterface);

        if(_lastCasevacStatus == null)return;
        updateInterface.onCasevacUpdated(_lastCasevacStatus.getStatus(),
                _lastCasevacStatus.getEta(), _lastCasevacStatus.getCasevacCallsign());

    }

    public void removeOnCasevacUpdateListener(CasevacUpdateInterface updateInterface) {
        _listeners.remove(updateInterface);
    }
}

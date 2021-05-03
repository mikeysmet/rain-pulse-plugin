package com.atakmap.android.plugin.rain.pulse.cot;

import android.util.Log;

import com.atakmap.android.plugin.rain.pulse.util.RunnableManager;
import com.atakmap.coremap.cot.event.CotEvent;

public class PulseCasevacRequestManager {
    //filter for all casevac tasking messages.

    private static final String TAG = "PulseCasevacManager";

    private static PulseCasevacRequestManager _instance;



    public PulseCasevacRequestManager(){
        _instance = this;
    }

    public static PulseCasevacRequestManager getInstance() {
        if(_instance == null){
            _instance = new PulseCasevacRequestManager();
        }
        return _instance;
    }

    public void update(CotEvent cotEvent) {
        Log.d(TAG, "CASEVAC Message Received");
        Log.d(TAG, " message type: " + cotEvent.getType());
        //process incoming tasking update.
        RunnableManager.getInstance().toast("CASEVAC message Received!!!");

    }
}

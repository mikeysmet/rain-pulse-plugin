package com.atakmap.android.plugin.rain.pulse.ui.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugin.rain.pulse.casevac.PulseCasevacProcessor;
import com.atakmap.coremap.cot.event.CotDetail;
import com.atakmap.coremap.cot.event.CotEvent;

public class CasevacConfirmationDialog implements Dialog.OnClickListener {
    /**
     * Display WILCO and CANTCO
     *
     */

    private AlertDialog.Builder _alertDialogBuilder;
    private AlertDialog _alertDialog;
    private Context _dialogContext;
    private MapView _mapView;
    private final Context _pluginContext;
    private CasevacAssignmentDialog _view;
    private CotEvent _cotEvent;
    private CotDetail _pulseRequestDetail;

    public CasevacConfirmationDialog(MapView mapView, Context pluginContext, CotEvent cotEvent, CotDetail pulseRequestDetail) {
        _mapView = mapView;
        _dialogContext = mapView.getContext();
        _pluginContext = pluginContext;
        _cotEvent = cotEvent;
        _pulseRequestDetail = pulseRequestDetail;
        _alertDialogBuilder = new AlertDialog.Builder(mapView.getContext());
        _alertDialogBuilder.setTitle("Pulse Casevac Request");
        _alertDialogBuilder.setMessage("Do you accept this CASEVAC: ");
        _alertDialogBuilder.setPositiveButton("WILCO", this);
        _alertDialogBuilder.setNegativeButton("CANTCO", this);
        _alertDialogBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                sendCantco();
            }
        });
    }

    public void show(){
        //first show casevac
        _mapView.post(new Runnable() {
            @Override
            public void run() {
                _alertDialog = _alertDialogBuilder.create();
                _alertDialog.show();
            }
        });
    }
    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_NEUTRAL:
                return;
            case AlertDialog.BUTTON_POSITIVE:
                handleWilco();
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                sendCantco();
                break;
        }
    }

    private void handleWilco() {
        PulseCasevacProcessor.getInstance().sendResponse(_cotEvent, _pulseRequestDetail,true );
        _view = new CasevacAssignmentDialog(_pluginContext, _mapView, _cotEvent, _pulseRequestDetail);
        _view.show();
    }

    private void sendCantco(){
        PulseCasevacProcessor.getInstance().sendResponse(_cotEvent, _pulseRequestDetail,false );
    }
}

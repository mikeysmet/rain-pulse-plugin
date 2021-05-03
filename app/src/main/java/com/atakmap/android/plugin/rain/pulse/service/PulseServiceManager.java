package com.atakmap.android.plugin.rain.pulse.service;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugin.rain.pulse.util.PulseIntentUtils;
import com.atakmap.android.pulse.plugin.IGarminDataListener;
import com.atakmap.android.pulse.plugin.IGarminPairingInterface;
import com.atakmap.android.pulse.plugin.IGarminService;
import com.atakmap.android.pulse.plugin.IGarminStatusListener;

import java.util.concurrent.ConcurrentLinkedQueue;

public class PulseServiceManager {
    /*
     * handles the service connection and appropriate listeners
     * */
    private static final String TAG = "PulseServiceManager";

    private final Context _pluginContext;
    private final Context _serviceContext;
    private final MapView _mapView;

    private IGarminService _garminInterface;

    private boolean _garminBound;

    private android.app.AlertDialog _permissionsDialog;
    private boolean _ignorePermissionsRequest;

    private ConcurrentLinkedQueue<BluetoothStatusInterface> _statusListeners = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<HeartRateUpdateInterface> _heartRateListeners = new ConcurrentLinkedQueue<>();


    public PulseServiceManager(Context pluginContext, MapView mapView){
        _pluginContext = pluginContext;
        _mapView = mapView;
        _serviceContext = mapView.getContext();
        startService();
    }

    private void startService() {
        try {

            Intent serviceIntent = new Intent( "com.atakmap.android.pulse.GarminService");
            serviceIntent.setPackage("com.atakmap.android.pulse");
            _serviceContext.bindService(serviceIntent, _serviceConnection, Context.BIND_AUTO_CREATE);

        } catch (Exception e) {
            Log.d(TAG, "service not working");
            e.printStackTrace();
        }
    }

    public void onDestroy(){
        try {
            _garminInterface.unregisterDataInterface(_dataListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            _garminInterface.unregisterStatusInterface(_statusListener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        _serviceContext.unbindService(_serviceConnection);
    }

    private ServiceConnection _serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(_serviceContext, "Garmin Service Started!!", Toast.LENGTH_SHORT).show();

            _garminInterface = IGarminService.Stub.asInterface(service);

            try {
                _garminInterface.registerDataInterface(_dataListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                _garminInterface.registerStatusInterface(_statusListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            try {
                _garminInterface.registerPairingInterface(_pairingInterface);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            _garminBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected:" + name);
            _garminBound = false;
        }
    };

    private IGarminPairingInterface _pairingInterface = new IGarminPairingInterface.Stub() {
        @Override
        public void onPairingRequested() throws RemoteException {
            showPairingDialog();
        }
    };

    private void showPairingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(_mapView.getContext());
        builder.setTitle("Enter Passkey");

        final EditText input = new EditText(_mapView.getContext());

        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String text = input.getText().toString();
            int passkey = Integer.parseInt(text);
            try {
                _garminInterface.enterPairingCredentials(passkey);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }


    private IGarminDataListener _dataListener = new IGarminDataListener.Stub() {
        @Override
        public void onDataReceived(int heartRate, int heartRateVariability, int spo2, int respiration, int bodyBattery, int stress, long readingTimeStamp) throws RemoteException {
            for (HeartRateUpdateInterface heartRateInterface:_heartRateListeners ) {
                heartRateInterface.onDataReceived(heartRate, heartRateVariability, spo2, respiration, bodyBattery, stress, readingTimeStamp);
                Log.d(TAG, "data_received_HR:" + heartRate + " " +  heartRateVariability + " Spo2:" + spo2 + " resp:" + respiration + " bodyBatt:" + bodyBattery + " stress:" + stress +" @ "+ readingTimeStamp );
            }
        }
    };

    private IGarminStatusListener _statusListener = new IGarminStatusListener.Stub() {
        @Override
        public void onStatusUpdated(String state, String message) throws RemoteException {

            for (BluetoothStatusInterface statusInterface:_statusListeners ) {
                statusInterface.onLinkStatusUpdated(state, message);
                Log.d(TAG, "onStatusUpdated: " + state +" --- " + message);
            }

            if(state.equals("ERROR") && message.equals(PulseIntentUtils.INTENT_ACTION_BT_PERMISSIONS)){
                if(_ignorePermissionsRequest)return;
                if(_permissionsDialog != null && _permissionsDialog.isShowing())return;

            }
        }
    };

}

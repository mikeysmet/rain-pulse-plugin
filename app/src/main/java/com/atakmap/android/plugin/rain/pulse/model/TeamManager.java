package com.atakmap.android.plugin.rain.pulse.model;

import android.content.Context;
import android.util.Log;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugin.rain.pulse.cot.PulseNetworkTeamUpdateInterface;
import com.atakmap.android.pulse.plugin.IGarminDataListener;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TeamManager implements PulseNetworkTeamUpdateInterface {
    /**
     * Handle all incoming GPS Packets and correlate with the DB and user selections.
     */
    private static final String TAG = "PulseTeamManager";

    private final Context _pluginContext;
    private final Context _activityContext;
    private final MapView _mapView;


    private ConcurrentHashMap<Integer, TeamMemberInputs> _activeTeamMemberMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, TeamMemberInputs> _patientMap = new ConcurrentHashMap<>();

    private ConcurrentLinkedQueue<IGarminDataListener> _heartRateListeners = new ConcurrentLinkedQueue<>();
    private ConcurrentLinkedQueue<TeamMemberUpdateInterface> _trackListeners = new ConcurrentLinkedQueue<>();
    private TeamMemberInputs _lastPatientUpdate;

    public TeamManager(Context context, MapView mapView) {
        _pluginContext = context;
        _mapView = mapView;
        _activityContext = mapView.getContext();
    }

    public ConcurrentHashMap<Integer, TeamMemberInputs> getMap() {
        return _activeTeamMemberMap;
    }

    public ConcurrentHashMap<Integer, TeamMemberInputs> getPatientMap() {
        return _patientMap;
    }

    public void addHeartRateListener(IGarminDataListener trackInterface) {
        _heartRateListeners.add(trackInterface);
    }

    public void addTrackListener(TeamMemberUpdateInterface trackInterface) {
        _trackListeners.add(trackInterface);

        if(_lastPatientUpdate == null)return;
        trackInterface.onPatientUpdated(_lastPatientUpdate);
    }

    public void removeTrackListener(TeamMemberUpdateInterface trackInterface) {
        _trackListeners.remove(trackInterface);
    }

    @Override
    public void onTeamUpdateReceived(TeamMemberInputs update) {

        if(update.isTmCasualty()){
            onPatientUpdateReceived(update);
        }
        else if(_activeTeamMemberMap.containsKey(update.combatID)){
            _activeTeamMemberMap.put(update.combatID, update);
            onItemUpdated(update);
        }
        else{
            _activeTeamMemberMap.put(update.combatID, update);
            onItemAdded(update);
        }
    }

    @Override
    public void onPatientUpdateReceived(TeamMemberInputs update) {
        Log.d(TAG, "PATIENT Updating");
        _lastPatientUpdate = update;
        _patientMap.put(update.combatID, update);
        for (TeamMemberUpdateInterface listener : _trackListeners) {
            listener.onTeamMemberUpdated(update);
            listener.onPatientUpdated(update);
        }
    }


    private void onItemAdded(TeamMemberInputs teamMemberData) {
        for (TeamMemberUpdateInterface listener : _trackListeners) {
            listener.onTeamMemberAdded(teamMemberData);
        }
    }

    private void onItemUpdated(TeamMemberInputs teamMemberData) {
        for (TeamMemberUpdateInterface listener : _trackListeners) {
            listener.onTeamMemberUpdated(teamMemberData);
        }
    }

    public TeamMemberInputs getPatient() {
        return _lastPatientUpdate;
    }
}

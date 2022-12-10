package com.atakmap.android.plugin.rain.pulse.cot;

import android.util.Log;

import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;
import com.atakmap.android.plugin.rain.pulse.service.PulseSelfUpdateInterface;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class PulseCotRelay {
    private static final String TAG = "PulseCotRelay";
    /**
     * Used to keep track of all the TeamMember updates out of the CoT thread.
     * */

    private final ConcurrentLinkedQueue<PulseNetworkTeamUpdateInterface> _teamListener = new ConcurrentLinkedQueue<>();
    private final ConcurrentLinkedQueue<PulseSelfUpdateInterface> _selfListeners = new ConcurrentLinkedQueue<>();

    private ConcurrentLinkedQueue<TeamMemberInputs> _updateQueue;
    private boolean _isRunning;
    private PulseNetworkUpdateRunnable _updateRunnable;
    private Future<?> _updateTask;

    public void start() {
        _isRunning = true;
        _updateQueue = new ConcurrentLinkedQueue<>();
        _updateRunnable = new PulseNetworkUpdateRunnable();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        _updateTask = executorService.submit(_updateRunnable);
    }

    public void stop(){
        _updateTask.cancel(true);
        _isRunning = false;
    }

    public void addListener(PulseNetworkTeamUpdateInterface listener){
        _teamListener.add(listener);
    }

    public void removeListener(PulseNetworkTeamUpdateInterface listener){
        _teamListener.remove(listener);
    }

    public void update(TeamMemberInputs update) {
        _updateQueue.add(update);
    }

    public void updateUser(TeamMemberInputs currentUser) {
        for (PulseSelfUpdateInterface listener: _selfListeners) {
            listener.onCurrentUserUpdated(currentUser);
            Log.d(TAG, "updateUser_relay: " + currentUser.isTmCasualty());
        }
    }

    public void addSelfUpdateListener(PulseSelfUpdateInterface updateInterface) {
        _selfListeners.add(updateInterface);
    }

    private class PulseNetworkUpdateRunnable implements Runnable {

        @Override
        public void run() {
            while(_isRunning){
                if(_updateQueue.size() > 0){
                    //updateDataListeners(_payloadQueue.poll());
                    TeamMemberInputs update = _updateQueue.poll();
                    updateTeamListeners(update);
                    //Log.d(TAG, "STATUS queue size is " + _bluetoothStatusQueue.size());
                }
                else{
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    private void updateTeamListeners(TeamMemberInputs update) {
        for (PulseNetworkTeamUpdateInterface listener: _teamListener) {
            listener.onTeamUpdateReceived(update);
            Log.d(TAG, "updateListeners_COT_RELAY: " + update.getTmCallsign());
        }
    }


}

package com.atakmap.android.plugin.rain.pulse.cot;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;

import com.atakmap.android.cot.CotMapComponent;
import com.atakmap.android.cot.detail.CotDetailManager;
import com.atakmap.android.cotdetails.CoTInfoMapComponent;
import com.atakmap.android.cotdetails.ExtendedInfoView;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.maps.PointMapItem;
import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;
import com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs;
import com.atakmap.android.plugin.rain.pulse.service.HeartRateUpdateInterface;
import com.atakmap.android.plugin.rain.pulse.service.PulseSelfUpdateInterface;
import com.atakmap.coremap.cot.event.CotDetail;

public class PulseCotDetailManager implements SharedPreferences.OnSharedPreferenceChangeListener, HeartRateUpdateInterface {
    private static final String TAG = "PulseCotDetailManager";

    private Context _pluginContext;
    private MapView _mapView;

    private PulseDetailHandler _pulseHandler;
    private PulseCasevacDetailHandler _casevacHandler;

    private ExtendedInfoView _detailExtension;

    private PulseCotRelay _relay;
    private TeamMemberInputs _currentUser;
    private CotDetail _userDetail;

    public PulseCotDetailManager(Context pluginContext, MapView mapView) {
        _pluginContext = pluginContext;
        _mapView = mapView;

        _relay = new PulseCotRelay();
        _relay.start();

        _pulseHandler = new PulseDetailHandler(this, mapView);
        //load patient ID pref, in the event of a crash/restart
        _pulseHandler.setCurrentPatientId(PulsePrefs.getPatientId());
        CotDetailManager.getInstance().registerHandler(_pulseHandler);

        _casevacHandler = new PulseCasevacDetailHandler(mapView);
        CotDetailManager.getInstance().registerHandler(_casevacHandler);

        registerDetailsView();

        PulsePrefs.getPrefs().registerOnSharedPreferenceChangeListener(this);
        _currentUser = PulsePrefs.getUserInfo();
        _userDetail = PulseCotDetail.toDetail(_currentUser);
    }

    public void onDestroy() {
        CotDetailManager.getInstance().unregisterHandler(_pulseHandler);
        CotDetailManager.getInstance().unregisterHandler(_casevacHandler);
        _relay.stop();
        if (_detailExtension == null) return;
        _detailExtension.removeAllViews();
        CoTInfoMapComponent.getInstance().unregister(_detailExtension);
        _detailExtension = null;

        PulsePrefs.getPrefs().unregisterOnSharedPreferenceChangeListener(this);
    }

    private void registerDetailsView() {
        final PulseCotDetailView detailView = new PulseCotDetailView(_pluginContext);
        detailView.setup(_pluginContext);
        _detailExtension = new ExtendedInfoView(_pluginContext) {
            @Override
            public void setMarker(PointMapItem pointMapItem) {
                if (!pointMapItem.hasMetaValue(PulseCotDetail.DETAIL_PULSE)) {
                    this.setVisibility(View.GONE);
                    return;
                }
                detailView.setItem(pointMapItem);
                this.setVisibility(View.VISIBLE);
            }
        };

        _detailExtension.addView(detailView);
        CoTInfoMapComponent.getInstance().register(_detailExtension);

        _currentUser = PulsePrefs.getUserInfo();
        updateUserDetail();
    }

    public void handleTeamUpdate(TeamMemberInputs update) {
        if (_relay == null) return;

        update.setTmCasualty(update.getTmCombatID().equals(PulsePrefs.getPatientId()));
        _relay.update(update);
        Log.d(TAG, "handleTeamUpdate: " + update.getTmCallsign() + " | "+ update.tmCasualty);
    }

    public void addUpdateListener(PulseNetworkTeamUpdateInterface listener) {
        _relay.addListener(listener);
    }

    public void removeUpdateListener(PulseNetworkTeamUpdateInterface listener) {
        _relay.removeListener(listener);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (!PulsePrefs.isPulsePreference(s)) return;
        TeamMemberInputs updated = PulsePrefs.getUserInfo();
        _currentUser.update(updated);
        updateUserDetail();
        _pulseHandler.setCurrentPatientId(PulsePrefs.getPatientId());
    }

    /**
     * Allows for a Self SA (PPLI) message to contain additional dynamically registered information.
     * This detail can be modified by the client without having to add it again to the self marker.
     */
    //public void addAdditionalDetail(final String detailName, final CotDetail detail)
    //CotMapComponent.addAdditionalDetail
    private void updateUserDetail() {
        if (_userDetail == null || _currentUser == null) return;
        CotMapComponent.getInstance().addAdditionalDetail(_userDetail.getElementName(), _userDetail);

    }

    @Override
    public void onDataReceived(int heartRate, int heartRateVariability, int spo2, int respiration, int bodyBattery, int stress, long timeStamp) {
        if (heartRate != -1) {
            _currentUser.setTmHeartRate(String.valueOf(heartRate));
        }
        if (heartRateVariability != -1 && heartRateVariability != -2) {
            _currentUser.setTmHeartRateVar(String.valueOf(heartRate));
        }
        if (spo2 != -1) {
            _currentUser.setTmSp02(String.valueOf(spo2));
        }
        if (respiration != -1 && respiration != -2) {
            _currentUser.setTmRespiration(String.valueOf(respiration));
        }
        if (bodyBattery != -1 && bodyBattery != -2) {
            _currentUser.setTmBodyBattery(String.valueOf(bodyBattery));
        }
        if (stress != -1 && stress != -2) {
            _currentUser.setTmStress(String.valueOf(stress));
        }
        PulseCotDetail.updateDetail(_userDetail, _currentUser);
        updateUserDetail();
        _currentUser.setLastReportTime(timeStamp);
        _relay.updateUser(_currentUser);
    }

    public void addSelfUpdateListener(PulseSelfUpdateInterface updateInterface) {
        _relay.addSelfUpdateListener(updateInterface);
        updateInterface.onCurrentUserUpdated(_currentUser);
    }

    public void addMonitorUpdateListener(PulseNetworkTeamUpdateInterface updateInterface) {
        _relay.addListener(updateInterface);
        updateInterface.onPatientUpdateReceived(_currentUser);
    }


}

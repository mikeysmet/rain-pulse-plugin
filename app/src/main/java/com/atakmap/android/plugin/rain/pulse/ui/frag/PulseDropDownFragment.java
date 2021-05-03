package com.atakmap.android.plugin.rain.pulse.ui.frag;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugin.rain.pulse.R;
import com.atakmap.android.plugin.rain.pulse.model.TeamManager;
import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;
import com.atakmap.android.plugin.rain.pulse.ui.dialog.TraumaDialog;

@SuppressLint("ValidFragment")

public class PulseDropDownFragment extends Fragment implements PulseFragmentInterface {

    private static final String TAG = "PulseSDropDownFragment";

    private MapView _mapView;
    private Context _pluginContext;

    private final static int REQUEST_ENABLE_BT = 1;

    private View _root;
    private FrameLayout _fragmentContainer;
    private FrameLayout _toolbarContainer;
    private TeamMemberInputs _teamMemberInputs;
    private TeamManager _teamManager;
    //fragments
    private PulseToolbarFragment _toolbar;
    private PulseHomeFragment _homeFragment;
    private PulseSelfFragment _selfFragment;
    private Fragment _currentFragment;

    @SuppressLint("ValidFragment")
    public PulseDropDownFragment(MapView mapView, Context pluginContext, TeamManager teamManager) {
        _mapView = mapView;
        _pluginContext = pluginContext;
        _teamManager = teamManager;
        _toolbar = new PulseToolbarFragment(mapView, pluginContext, this);//_trackManager
        _homeFragment = new PulseHomeFragment(_mapView, pluginContext, this, teamManager);//track manager to be added
        _selfFragment = new PulseSelfFragment(_mapView, pluginContext, this, teamManager);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater pluginInflater = LayoutInflater.from(_pluginContext);
        _root = pluginInflater.inflate(R.layout.pulse_frag_dropdown, container, false);
        return _root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();
        _fragmentContainer = _root.findViewById(R.id.frame_pulse_dropdown);
        showHomeFragment();
        showSelfFragment();
    }

    private void setupToolbar() {
        _toolbarContainer = _root.findViewById(R.id.frame_pulse_toolbar);
        getChildFragmentManager().beginTransaction()
                .replace(_toolbarContainer.getId(), _toolbar).commit();
    }

    public boolean handleBackButton() {
        if (_currentFragment == null) {
        }
        return false;
    }

    public PulseToolbarFragment getToolbar() {
        return _toolbar;
    }

    public PulseSelfFragment getSelfFragment() {
        return _selfFragment;
    }

    public PulseHomeFragment getHomeFragment() {
        return _homeFragment;
    }

    @Override
    public void showTraumaDialog(TeamMemberInputs teamMemberInputs) {
        TraumaDialog traumaDialog = new TraumaDialog(_pluginContext, _mapView, teamMemberInputs);
        Log.d(TAG, "showTraumaDialog: " + "\n" + teamMemberInputs.isTmCasualty()
        +  "\n" + teamMemberInputs.getTmCallsign());
        traumaDialog.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if bluetooth is enabled
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                onStart();
            } else {
                Log.e("BLUETOOTH", "Bluetooth has not been enabled. Application cannot proceed.");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void showFragment(Fragment fragment) {
        try {
            getChildFragmentManager().beginTransaction()
                    .replace(_fragmentContainer.getId(), fragment).commit();
            _currentFragment = fragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showHomeFragment() {
        if (_homeFragment == null) {
            _homeFragment = new PulseHomeFragment(_mapView, _pluginContext, this, _teamManager);
        }
        showFragment(_homeFragment);
    }

    @Override
    public void showSelfFragment() {
        if (_selfFragment == null) {
            _selfFragment = new PulseSelfFragment(_mapView, _pluginContext, this, _teamManager);
        }
        showFragment(_selfFragment);
    }



}

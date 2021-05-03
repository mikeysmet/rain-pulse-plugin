package com.atakmap.android.plugin.rain.pulse.ui.frag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugin.rain.pulse.R;
import com.atakmap.android.plugin.rain.pulse.model.TeamManager;
import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;
import com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs;
import com.atakmap.android.plugin.rain.pulse.service.PulseSelfUpdateInterface;
import com.atakmap.android.plugin.rain.pulse.ui.adapter.TeamRecyclerAdapter;
import com.atakmap.android.plugin.rain.pulse.util.RunnableManager;


import static com.atakmap.android.maps.MapView.getMapView;

@SuppressLint("ValidFragment")
public class PulseSelfFragment extends Fragment implements PulseSelfUpdateInterface {

    private static final String TAG = "PulseDropDownFragment";
    private PulseFragmentInterface _parent;

    TeamMemberInputs _selfInfo;

    MapView _mapView;
    private Context _pluginContext;
    TeamManager _trackManager;

    private View _root;
    private RecyclerView _selfRecycler;
    private TeamRecyclerAdapter recyclerAdapter;

    @SuppressLint("ValidFragment")
    public PulseSelfFragment(MapView mapView, Context pluginContext, PulseFragmentInterface parent, TeamManager trackManager) {
        _mapView = mapView;
        _pluginContext = pluginContext;
        _parent = parent;
        _trackManager = trackManager;
        _selfInfo = PulsePrefs.getUserInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater pluginInflater = LayoutInflater.from(_pluginContext);
        _root = pluginInflater.inflate(R.layout.layout_self_container, container, false);
        return _root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _selfRecycler = _root.findViewById(R.id.recycler_self_view);
        _selfRecycler.setHasFixedSize(true);
        _selfRecycler.setLayoutManager(new LinearLayoutManager(_pluginContext, LinearLayoutManager.VERTICAL, false));
        ((SimpleItemAnimator) _selfRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
        recyclerAdapter = new TeamRecyclerAdapter(_pluginContext, _mapView, _parent);
        _selfRecycler.setAdapter(recyclerAdapter);

        recyclerAdapter.insert(_selfInfo);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onCurrentUserUpdated(TeamMemberInputs user) {
        _selfInfo = user;
        _selfInfo.setTmLat(getMapView().getSelfMarker().getPoint().getLatitude());
        _selfInfo.setTmLon( getMapView().getSelfMarker().getPoint().getLongitude());
        Log.d(TAG, "onCurrentUserUpdated: " + user.getTmHeartRate());
        if (recyclerAdapter == null) return;
        RunnableManager.getInstance().post(new Runnable() {
            @Override
            public void run() {
                recyclerAdapter.update(user);
            }
        });
    }
}
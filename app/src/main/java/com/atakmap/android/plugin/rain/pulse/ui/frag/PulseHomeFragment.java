package com.atakmap.android.plugin.rain.pulse.ui.frag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugin.rain.pulse.R;
import com.atakmap.android.plugin.rain.pulse.model.TeamManager;
import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;
import com.atakmap.android.plugin.rain.pulse.model.TeamMemberUpdateInterface;
import com.atakmap.android.plugin.rain.pulse.ui.adapter.TeamRecyclerAdapter;
import com.atakmap.android.plugin.rain.pulse.util.RunnableManager;

public class PulseHomeFragment extends Fragment implements TeamMemberUpdateInterface {

    private final MapView _mapView;
    private final Context _pluginContext;
    private final TeamManager _teamManager;
    private String searchResult = "";
    private View _root;
    private PulseFragmentInterface _parent;

    private RecyclerView _trackListRecycler;
    private TeamRecyclerAdapter _trackAdapter;


    @SuppressLint("ValidFragment")
    public PulseHomeFragment(MapView mapView, Context pluginContext, PulseFragmentInterface parent, TeamManager teamManager) {
        _mapView = mapView;
        _parent = parent;
        _pluginContext = pluginContext;
        _teamManager = teamManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater pluginInflater = LayoutInflater.from(_pluginContext);
        _root = pluginInflater.inflate(R.layout.pulse_frag_home, container, false);

        return _root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            _trackListRecycler = _root.findViewById(R.id.recycle_track_list);
            _trackListRecycler.setHasFixedSize(true);
            _trackListRecycler.setLayoutManager(new LinearLayoutManager(_pluginContext, LinearLayoutManager.VERTICAL, false));
            ((SimpleItemAnimator) _trackListRecycler.getItemAnimator()).setSupportsChangeAnimations(false);

            _trackAdapter = new TeamRecyclerAdapter(_pluginContext, _mapView, _parent);
            _trackAdapter.setup(_teamManager.getMap());
            _trackListRecycler.setAdapter(_trackAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            _teamManager.addTrackListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            _teamManager.removeTrackListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTeamMemberUpdated(TeamMemberInputs teamMemberInputs) {
        RunnableManager.getInstance().post(() -> {
            _trackAdapter.update(teamMemberInputs);
            Log.i("TAG_COUNT", ": " + _trackAdapter.getItemCount() + " --- " + searchResult);
        });
    }

    @Override
    public void onTeamMemberAdded(final TeamMemberInputs inputs) {
        RunnableManager.getInstance().post(new Runnable() {
            @Override
            public void run() {
                _trackAdapter.insert(inputs);
                Log.i("TAG_COUNT", ": " + _trackAdapter.getItemCount() +"  -- "+ inputs.tmCasualty);
                _trackAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onPatientUpdated(TeamMemberInputs patient) {
        //BROCK - does HomeFragment care about patient?  or is that the Toolbar's job?


    }


}
package com.atakmap.android.plugin.rain.pulse.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugin.rain.pulse.R;
import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;
import com.atakmap.android.plugin.rain.pulse.ui.frag.PulseFragmentInterface;
import com.atakmap.coremap.maps.coords.GeoPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class TeamRecyclerAdapter extends RecyclerView.Adapter<TeamRecyclerAdapter.ActiveTeamViewHolder> {

    private static final String TAG = "TrackRecyclerAdapter";

    private final Context _pluginContext;
    private MapView _mapView;
    public List<String> expandedIdList;
    public ConcurrentHashMap<Integer, TeamMemberInputs> _map;
    public PulseFragmentInterface _parent;
    TeamMemberInputs teamMemberData;
    public int count = 0;

    public TeamRecyclerAdapter(Context pluginContext, MapView mapView, PulseFragmentInterface parent) {
        this._map = new ConcurrentHashMap<>();
        this._pluginContext = pluginContext;
        this._parent = parent;
        _mapView = mapView;
        expandedIdList = new ArrayList<>();
        Log.d(TAG, "TRACK_RECYCLER: ");
    }

    @NonNull
    @Override
    public ActiveTeamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(_pluginContext);
        View view = inflater.inflate(R.layout.item_main_data, parent, false);

        return new ActiveTeamViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull  ActiveTeamViewHolder holder, final int position) {
        teamMemberData = new ArrayList<>(_map.values()).get(position);
        holder.activeUUID.setText(teamMemberData.getTmCombatID());
        if (!_mapView.getDeviceCallsign().equals(teamMemberData.getTmCallsign())) {
            Log.d(TAG, "CALL_SIGN_CHECK: " + _mapView.getDeviceCallsign());
            holder.cardTextID.setText(teamMemberData.getTmCallsign());
            holder.callsign.setVisibility(View.INVISIBLE);
        } else {
            holder.callsign.setText(teamMemberData.getTmCallsign());
        }
        holder.assetType.setText(teamMemberData.getTmAssetType());
        holder._ivWatchType.setImageResource(R.drawable.garmin_fenix);

        if (teamMemberData.getTmHeartRate() == null) {
            holder._activeWatchIndicator.setImageResource(R.drawable.light_red);
            holder.heartRate.setText("---");
            holder.textViewSelfConnectionStatus.setText("Disconnected");
        } else {
            holder._activeWatchIndicator.setImageResource(R.drawable.light_green);
            holder.textViewSelfConnectionStatus.setText("Connected");
            holder.heartRate.setText(String.valueOf(teamMemberData.getTmHeartRate()));
        }
        try {
            if (teamMemberData.getTmSp02() == null || teamMemberData.getTmSp02().equals("-1") || teamMemberData.getTmSp02().equals("-2")) {
                holder.pulseOx.setText("---");
            } else {
                holder.pulseOx.setText(String.valueOf(teamMemberData.getTmSp02()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (teamMemberData.getTmRespiration() == null || teamMemberData.getTmRespiration().equals("-1") || teamMemberData.getTmRespiration().equals("-2")) {
            holder.respiration.setText("---");
        } else {
            holder.respiration.setText(String.valueOf(teamMemberData.getTmRespiration()));
        }
        if (teamMemberData.getTmBodyBattery() == null || teamMemberData.getTmBodyBattery().equals("-1") || teamMemberData.getTmBodyBattery().equals("-2")) {
            holder.bodyBattery.setText("---");
        } else {
            holder.bodyBattery.setText(String.valueOf(teamMemberData.getTmBodyBattery()));
        }
        if (teamMemberData.getTmStress() == null || teamMemberData.getTmStress().equals("-1") || teamMemberData.getTmStress().equals("-2")) {
            holder.stress.setText("---");
        } else {
            holder.stress.setText(String.valueOf(teamMemberData.getTmStress()));
        }

        holder.lastReport.setText(teamMemberData.getTmLastReport());
        holder.location.setText(teamMemberData.getTmLocation());
        holder.exertionLevel.setText(teamMemberData.getExertionPercentageDisplay());

        holder.allergy.setText(teamMemberData.getTmAllergies());
        holder.bloodType.setText(teamMemberData.getTmBloodType());
        if (teamMemberData.isTmAuthorizeFDP()) {
            holder.fdpAuth.setText("Authorized");
        } else {
            holder.fdpAuth.setText("Not Authorized");
        }
        try {

            if (teamMemberData.isExpandView()) {
                holder.ivMoreExpand.setVisibility(View.GONE);
                holder.ivMoreCollapse.setVisibility(View.VISIBLE);
                holder.linearLayoutExtras.setVisibility(View.VISIBLE);
            } else {
                holder.ivMoreExpand.setVisibility(View.VISIBLE);
                holder.ivMoreCollapse.setVisibility(View.GONE);
                holder.linearLayoutExtras.setVisibility(View.GONE);
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
    }


    // Insert a new item to the RecyclerView on a predefined position
    public void insert(TeamMemberInputs data) {
        int tagID = data.combatID;
        if (!_map.containsKey(tagID)) {
            _map.put(tagID, data);
            notifyItemInserted(_map.size());
        } else {
            int position = new ArrayList<Integer>(_map.keySet()).indexOf(tagID);
            _map.put(tagID, data);
            notifyItemChanged(position);
        }
    }

    public void update(TeamMemberInputs data) {
        int tagID = data.combatID;
        if (!_map.containsKey(tagID)) {
            _map.put(tagID, data);

            notifyItemInserted(_map.size());
        } else {
            TeamMemberInputs current = _map.get(tagID);
            data.setExpandView(current.isExpandView());
            _map.put(tagID, data);
            Log.d(TAG, "updating (from update) tag " + tagID);
            int position = new ArrayList<Integer>(_map.keySet()).indexOf(tagID);
            notifyItemChanged(position);
        }
    }

    @Override
    public int getItemCount() {
        return _map.size();
    }

    public void setup(ConcurrentHashMap<Integer, TeamMemberInputs> map) {
        _map.clear();

        for (ConcurrentHashMap.Entry<Integer, TeamMemberInputs> entry : map.entrySet()) {
            _map.put(entry.getKey(), entry.getValue());
            Log.d(TAG, "inserting (from insert) tag " + entry.getKey());
            notifyItemInserted(_map.size());
        }
    }

    public void showCasevac(int teamMemberID) {
        if (_map.contains(teamMemberID)) {
            int tagID = Integer.parseInt(String.valueOf(_map));
            teamMemberData = _map.get(tagID);
        }
        //set casevac params here for the team member. if multiple callsigns are input into casevac manifest, we need to attach team
        //members to the new map. this map will be sent to the patient recycler adapter
        Log.d(TAG, "showCasevac: " + teamMemberData.getTmCombatID() + "\n" + teamMemberData.getTmCallsign() );
        _parent.showTraumaDialog(teamMemberData);
    }

    public void removeItem(int position) {
        _map.remove(position);
        notifyItemRemoved(position);
    }


    public class ActiveTeamViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, View.OnLongClickListener {

        public LinearLayout linearLayoutExtras;
        public LinearLayout linearLayoutTraumaTreatments;

        public TextView activeUUID;
        public TextView cardTextID;
        public TextView callsign;
        public TextView assetType;
        public TextView respiration;
        public TextView bodyBattery;
        public TextView stress;

        public ImageView _ivWatchType;
        public ImageView _activeWatchIndicator;
        public ImageView ivMoreExpand;
        public ImageView ivMoreCollapse;
        public ImageView ivMarkerVisibilityShow;
        public ImageView ivMarkerVisibilityHide;

        public TextView lastReport;
        public TextView textViewSelfConnectionStatus;
        public TextView heartRate;
        public TextView pulseOx;
        public TextView exertionLevel;
        public TextView location;
        public ImageView ivTrackHistoryShow;
        public ImageView ivTrackHistoryHide;
        public ImageView _ivCasevac;

        public TextView bloodType;
        public TextView allergy;
        public TextView fdpAuth;

        public ActiveTeamViewHolder(View itemView) {
            super(itemView);

            ivMoreExpand = itemView.findViewById(R.id.iv_active_extras_show);
            ivMoreCollapse = itemView.findViewById(R.id.iv_active_extras_hide);

            activeUUID = itemView.findViewById(R.id.tv_item_self_combat_id);
            cardTextID = itemView.findViewById(R.id.tv_card_id);
            _ivWatchType = itemView.findViewById(R.id.iv_self_asset_type);

            callsign = itemView.findViewById(R.id.tv_item_self_callsign);
            assetType = itemView.findViewById(R.id.tv_active_self_asset_type);
            heartRate = itemView.findViewById(R.id.tv_item_self_heart_rate);
            pulseOx = itemView.findViewById(R.id.tv_pulse_ox);
            respiration = itemView.findViewById(R.id.tv_brpm);
            bodyBattery = itemView.findViewById(R.id.tv_body_batt);

            stress = itemView.findViewById(R.id.tv_stress);
            lastReport = itemView.findViewById(R.id.tv_last_report);
            exertionLevel = itemView.findViewById(R.id.tv_item_self_exert);
            _ivCasevac = itemView.findViewById(R.id.iv_tm_casevac);
            _activeWatchIndicator = itemView.findViewById(R.id.iv_watch_active_lt);
            textViewSelfConnectionStatus = itemView.findViewById(R.id.tv_item_self_connected);
            location = itemView.findViewById(R.id.tv_item_self_location);
            linearLayoutExtras = itemView.findViewById(R.id.ll_data_card);
            linearLayoutTraumaTreatments = itemView.findViewById(R.id.ll_treatments_administered);

            ivMoreExpand.setOnClickListener(this);
            ivMoreCollapse.setOnClickListener(this);
            //TODO format R.id's for a consistent naming convention
            bloodType = itemView.findViewById(R.id.tv_self_blood_type);
            allergy = itemView.findViewById(R.id.tv_allergies);
            fdpAuth = itemView.findViewById(R.id.cb_fdp_data);

            itemView.setOnLongClickListener(this);
            _ivWatchType.setOnClickListener(this);
            linearLayoutTraumaTreatments.setOnClickListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (buttonView.isPressed()) {
                TeamMemberInputs teamMemberData = new ArrayList<>(_map.values()).get(getAdapterPosition());
                teamMemberData.setBroadcasting(isChecked);
                if(teamMemberData.isTmCasualty()){
                    linearLayoutTraumaTreatments.setVisibility(View.VISIBLE);
                }else {
                    linearLayoutTraumaTreatments.setVisibility(View.GONE);

                }
            }
        }

        // onClick Listener for view
        @Override
        public void onClick(View v) {
            final TeamMemberInputs teamMemberData = new ArrayList<>(_map.values()).get(getAdapterPosition());
            if (v.isPressed()) {
                if (v == ivMoreExpand) {
                    teamMemberData.setExpandView(true);
                    ivMoreExpand.setVisibility(View.GONE);
                    ivMoreCollapse.setVisibility(View.VISIBLE);
                    linearLayoutExtras.setVisibility(View.VISIBLE);
                }
                if (v == ivMoreCollapse) {
                    teamMemberData.setExpandView(false);
                    ivMoreExpand.setVisibility(View.VISIBLE);
                    ivMoreCollapse.setVisibility(View.GONE);
                    linearLayoutExtras.setVisibility(View.GONE);

                }
                if (v == ivMarkerVisibilityHide) {
                    ivMarkerVisibilityHide.setVisibility(View.GONE);
                    ivMarkerVisibilityShow.setVisibility(View.VISIBLE);
                    teamMemberData.setVisible(true);
                }
                if (v == ivTrackHistoryShow) {
                    Intent historyIntent = new Intent("com.atakmap.android.track.TRACK_USERLIST");
                    AtakBroadcast.getInstance().sendBroadcast(historyIntent);
                    ivTrackHistoryShow.setVisibility(View.GONE);
                    ivTrackHistoryHide.setVisibility(View.VISIBLE);
                }
                if (v == ivTrackHistoryHide) {
                    ivTrackHistoryShow.setVisibility(View.VISIBLE);
                    ivTrackHistoryHide.setVisibility(View.GONE);

                }
                if (v == linearLayoutTraumaTreatments){
                    //not doing treatments at this time
                    //TODO -  we need a better plan for treatments
                    //_parent.showPatientTreatmentsDialog(teamMemberData.getTmCombatID());

                }
                if (v == _ivCasevac) {
                    showCasevac(teamMemberData.combatID);
                }

                if (v == _ivWatchType) {
                    new Thread(() -> {
                        MapView.getMapView().getMapController().zoomTo(.0005d, false);
                        MapView.getMapView().getMapController().panTo(
                                new GeoPoint(teamMemberData.getTmLat(), teamMemberData.getTmLon()),
                                false);
                        Log.d(TAG, "onClick_team: " + teamMemberData.getTmCallsign() + " " + teamMemberData.getTmLat() + " " + teamMemberData.getTmLon());
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ignored) {
                        }
                    }).start();
                }
            }


        }

        @Override
        public boolean onLongClick(View v) {

            _ivWatchType.setVisibility(View.GONE);
            try {
                _ivCasevac.setVisibility(View.VISIBLE);
                _ivCasevac.setOnClickListener(this);

            } catch (Exception e) {
                e.printStackTrace();
            }
            count++;
            if (count > 1) {
                _ivWatchType.setVisibility(View.VISIBLE);
                _ivCasevac.setVisibility(View.GONE);
                Toast.makeText(MapView.getMapView().getContext(), "Cancel Casevac", Toast.LENGTH_SHORT).show();
                count = 0;
            }else {
                Toast.makeText(MapView.getMapView().getContext(), "Initiate Casevac", Toast.LENGTH_SHORT).show();

            }

            return true;
        }
    }
}

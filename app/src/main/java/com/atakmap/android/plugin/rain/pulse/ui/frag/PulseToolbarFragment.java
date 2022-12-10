package com.atakmap.android.plugin.rain.pulse.ui.frag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugin.rain.pulse.R;
import com.atakmap.android.plugin.rain.pulse.casevac.CasevacUpdateInterface;
import com.atakmap.android.plugin.rain.pulse.casevac.PulseCasevacProcessor;
import com.atakmap.android.plugin.rain.pulse.prefs.PulsePrefs;
import com.atakmap.android.plugin.rain.pulse.service.BluetoothStatusInterface;
import com.atakmap.android.plugin.rain.pulse.service.HeartRateUpdateInterface;
import com.atakmap.android.plugin.rain.pulse.util.PulseIntentUtils;
import com.atakmap.android.plugin.rain.pulse.util.RunnableManager;


import static com.atakmap.android.maps.MapView.getMapView;

public class PulseToolbarFragment extends Fragment implements BluetoothStatusInterface, HeartRateUpdateInterface, CasevacUpdateInterface {
    public static final String TAG = "PulseToolbar";

    private MapView _mapView;
    private Context _pluginContext;
    private PulseFragmentInterface _parent;
    private CasevacUpdateInterface casevacUpdateInterface;

    private View _root;
    private String _state = "";
    private String _message = "";
    public boolean tagsHidden = false;
    public boolean userAdded = false;
    public boolean monitorVisible = false;
    public boolean casevacInProgress = false;

    int _heartRate;
    int _heartRateVariability;
    int _spo2;
    int _respiration;
    int _bodyBattery;
    int _stress;
    long _timestamp;

    private LinearLayout _batteryIndicatorLayout;

    private ImageView _connectionStateImage;
    private TextView _connectionStateText;
    private TextView textViewHeartRate;

    private TextView _batteryIndicatorTextView;
    private ImageView _ivShowSelf;
    private ImageView _ivHideSelf;

    private ImageView _addUserImageView;
    private ImageView _optionsImageView;
    private LinearLayout linearLayoutToolbarHeartRate;
    private LinearLayout linearLayoutPatientMonitor;
    private FrameLayout frameLayoutCasevacToolbar;

    private TextView _casevacStatusText, _casevacEtaText, _casevacCallsignText;

    public PulseToolbarFragment(MapView mapView, Context pluginContext, PulseFragmentInterface parent) {
        _mapView = mapView;
        _pluginContext = pluginContext;
        _parent = parent;
    }

    public void setCasevacUpdateInterface(CasevacUpdateInterface listener) {
        casevacUpdateInterface = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater pluginInflater = LayoutInflater.from(_pluginContext);
        _root = pluginInflater.inflate(R.layout.pulse_frag_toolbar, container, false);
        return _root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        _connectionStateText = _root.findViewById(R.id.tv_toolbar_conn_status);

        _addUserImageView = _root.findViewById(R.id.iv_add_user);
        //_addUserImageView.setOnClickListener(v -> _parent.showAddUserDialog());

        if (!userAdded) {
            _addUserImageView.setVisibility(View.GONE);

        } else {
            _addUserImageView.setVisibility(View.VISIBLE);
        }
        _connectionStateImage = _root.findViewById(R.id.iv_pulse_toolbar_connection_state);
        _optionsImageView = _root.findViewById(R.id.iv_toolbar_options);
        textViewHeartRate = _root.findViewById(R.id.tv_item_self_heart_rate);
        _ivShowSelf = _root.findViewById(R.id.iv_show_self);
        _ivHideSelf = _root.findViewById(R.id.iv_hide_self);
        linearLayoutToolbarHeartRate = _root.findViewById(R.id.toolbar_heart_rate);
        frameLayoutCasevacToolbar = _root.findViewById(R.id.fl_casevac_toolbar);

        _casevacStatusText = frameLayoutCasevacToolbar.findViewById(R.id.tv_casevac_status);
        _casevacCallsignText = frameLayoutCasevacToolbar.findViewById(R.id.tv_casevac_call_sign);
        _casevacEtaText = frameLayoutCasevacToolbar.findViewById(R.id.tv_casevac_eta);

        setupOptionsMenu(_optionsImageView);
        updateToolbarState();
        hideSelfFragment();
        showSelfFragment();
        /*SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getMapView().getContext());
        casevacInProgress = sharedPref.getBoolean("casevac_in_progress", false);*/
        casevacInProgress = PulsePrefs.isCasevacInProgress();
        Log.d(TAG, "onViewCreated: " + casevacInProgress);

        if (casevacInProgress) {
            frameLayoutCasevacToolbar.setVisibility(View.VISIBLE);
        } else {
            frameLayoutCasevacToolbar.setVisibility(View.GONE);
        }
    }

    private void showSelfFragment() {
        _ivShowSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _ivHideSelf.setVisibility(View.VISIBLE);
                _ivShowSelf.setVisibility(View.GONE);
                linearLayoutToolbarHeartRate.setVisibility(View.INVISIBLE);
                _parent.showSelfFragment();
            }
        });
    }

    private void showPatientMonitorFragment() {
        _parent.showPatientMonitorFragment();

    }

//    private void showPatientQueueFragment() {
//        _parent.showPatientQueueFragment();
//    }

    private void hideSelfFragment() {
        _ivHideSelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _ivHideSelf.setVisibility(View.GONE);
                _ivShowSelf.setVisibility(View.VISIBLE);
                linearLayoutToolbarHeartRate.setVisibility(View.VISIBLE);
                _parent.showHomeFragment();
            }
        });
    }

    private void hidePatientMonitorFragment() {
        _parent.showHomeFragment();
    }

    public void onCasevacComplete() {
        casevacInProgress = false;
        //save boolean var here for casevac
        PulsePrefs.setIsCasevacInProgress(false);
        RunnableManager.getInstance().toast("SAVED CASEVAC Variable");
        frameLayoutCasevacToolbar.setVisibility(View.GONE);
    }

    private void setupOptionsMenu(ImageView _optionsImageView) {
        _optionsImageView.setOnClickListener(v -> {
            //Creating the instance of PopupMenu
            Context wrapper = new ContextThemeWrapper(_pluginContext, R.style.popupMenuStyle);
            PopupMenu popup = new PopupMenu(wrapper, _optionsImageView);

            showPopup(popup);
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    String clickedItem = item.getTitle().toString().trim();
                    Log.d(TAG, "onMenuItemClick: " + item);
                    switch (clickedItem) {
                        //TODO - move these options to an enum or small struct.
                        case "Settings":
                            PulseIntentUtils.openSettings();
                            break;

                        case "Search":
                            Toast.makeText(_pluginContext, "Not Yet Implemented", Toast.LENGTH_SHORT).show();
                            break;

                        case "Delete Data":
                            //_parent.showConfirmationDialog();
                            Toast.makeText(_pluginContext, "Not Yet Implemented", Toast.LENGTH_SHORT).show();
                            break;

                        case "Patient Monitor":
//                          showPatientQueueFragment();
                            showPatientMonitorFragment();
                            monitorVisible = true;
                            break;

                        case "Close Monitor":
                            hidePatientMonitorFragment();
                            _parent.showHomeFragment();
                            _parent.showSelfFragment();
                            monitorVisible = false;
                            break;

                        case "Casevac Complete":
                            onCasevacComplete();
                            _parent.showHomeFragment();
                            _parent.showSelfFragment();
                            break;
                        case "Reset Treatments":
                            Toast.makeText(_pluginContext, "Not Yet Implemented", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return true;
                }
            });

            popup.show(); //showing popup menu
        });
    }

    @Override
    public void onLinkStatusUpdated(String state, String message) {
        if (_state.equals(state) && _message.equals(message)) return;
        _state = state;
        _message = message;
        _mapView.post(new Runnable() {
            @Override
            public void run() {
                updateToolbarState();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void updateToolbarState() {
        try {
            Log.d(TAG, "updateToolbarState: " + _state + " | " + _message);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (_connectionStateImage == null || _connectionStateText == null) return;

//        _batteryIndicatorLayout.setVisibility(View.GONE);

        switch (_state) {

            case "OFF":
                _connectionStateImage.setImageResource(R.drawable.light_red);
                break;
            case "DISCONNECTED":
                _connectionStateImage.setImageResource(R.drawable.light_red);
                _connectionStateImage.setMaxHeight(16);
                _connectionStateImage.setMaxWidth(16);
                break;
            case "SCANNING":
                _connectionStateText.setText(_state);
                _connectionStateImage.setImageResource(R.drawable.light_grey);

            case "GATT_INIT":

                _connectionStateImage.setImageResource(R.drawable.light_grey);

            case "CONNECTED":
            case "GT_CONNECTED":
            case "OPERATIONAL":
                _connectionStateImage.setImageResource(R.drawable.light_green);
                break;
//                _batteryIndicatorLayout.setVisibility(View.VISIBLE);
        }
        if (_state.equals("ERROR")) {
            _connectionStateText.setText("SCANNING");

        } else {
            _connectionStateText.setText(_state);

        }
    }

    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void showPopup(PopupMenu popupMenu) {

        popupMenu.getMenu().add(1, 1, 1, menuIconWithText(_pluginContext.getResources().getDrawable(R.drawable.ic_settings_24dp), _pluginContext.getResources().getString(R.string.settings)));
//        popupMenu.getMenu().add(2, 2, 2, menuIconWithText(_pluginContext.getResources().getDrawable(R.drawable.ic_search), _pluginContext.getResources().getString(R.string.search)));

        if (!monitorVisible) {
            popupMenu.getMenu().add(2, 2, 2, menuIconWithText(_pluginContext.getResources().getDrawable(R.drawable.ic_patient_monitor), _pluginContext.getResources().getString(R.string.patient_monitor)));

        } else {
            popupMenu.getMenu().add(2, 2, 2, menuIconWithText(_pluginContext.getResources().getDrawable(R.drawable.image_clear), _pluginContext.getResources().getString(R.string.close_monitor)));

        }
        popupMenu.getMenu().add(3, 3, 3, menuIconWithText(_pluginContext.getResources().getDrawable(R.drawable.ic_delete_foreground), _pluginContext.getResources().getString(R.string.delete_self)));
        if (casevacInProgress) {
            popupMenu.getMenu().add(4, 4, 4, menuIconWithText(_pluginContext.getResources().getDrawable(R.drawable.add_circle), _pluginContext.getResources().getString(R.string.casevac_complete)));
        } else {
            popupMenu.getMenu().removeItem(4);
        }
        popupMenu.getMenu().add(5, 5, 5, menuIconWithText(_pluginContext.getResources().getDrawable(R.drawable.image_clear), _pluginContext.getResources().getString(R.string.reset_treatments)));

        popupMenu.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        PulseCasevacProcessor.getInstance().addOnCasevacUpdateListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        PulseCasevacProcessor.getInstance().removeOnCasevacUpdateListener(this);
    }

    public void onDataReceived(int heartRate, int heartRateVariability, int spo2, int respiration, int bodyBattery, int stress, long timeStamp) {
        Log.d(TAG, "FRAGMENT_UPDATE: " + timeStamp + " SPO2_rx: " + spo2);
        _timestamp = timeStamp;
        _heartRate = heartRate;
        _heartRateVariability = heartRateVariability;
        _spo2 = spo2;
        _respiration = respiration;
        _bodyBattery = bodyBattery;
        _stress = stress;

        _mapView.post(new Runnable() {
            @Override
            public void run() {
                updateHeartRate();
            }
        });
    }

    public void updateHeartRate() {
        Log.d(TAG, "heartRateStatus: " + _heartRate + "\n" + _heartRateVariability + "\n" + _spo2 + "\n" + _respiration + "\n" + _bodyBattery);
        try {
            if (_heartRate != -1) {
                textViewHeartRate.setText(String.valueOf(_heartRate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCasevacUpdated(String status, String eta, String callsign) {

        Handler mainHandler = new Handler(getMapView().getContext().getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                _casevacEtaText.setText(eta);
                _casevacStatusText.setText(status);
                _casevacCallsignText.setText(callsign);
                frameLayoutCasevacToolbar.refreshDrawableState();
            }
        };
        mainHandler.post(myRunnable);
    }
}

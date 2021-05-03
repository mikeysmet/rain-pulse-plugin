package com.atakmap.android.plugin.rain.pulse.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.atakmap.android.gui.PluginSpinner;
import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugin.rain.pulse.R;
import com.atakmap.android.plugin.rain.pulse.casevac.PulseCasevacProcessor;
import com.atakmap.android.plugin.rain.pulse.model.CasevacDetailsInputs;
import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;
import com.atakmap.android.plugin.rain.pulse.ui.view.CasevacAssignmentDialogView;
import com.atakmap.android.plugin.rain.pulse.util.RunnableManager;
import com.atakmap.coremap.cot.event.CotDetail;
import com.atakmap.coremap.cot.event.CotEvent;

public class CasevacAssignmentDialog implements DialogInterface.OnClickListener {

    private final LayoutInflater _pluginInflater;
    private final Context _dialogContext;

    AlertDialog _dialog;

    private CasevacAssignmentDialogView _view;
    public static final String SHOW_CASEVAC_ASSIGNMENT_VIEW = "com.atakmap.android.pulsetool.SHOW_CASEVAC_ASSIGNMENT";
    private final Context _pluginContext;
    TeamMemberInputs teamMemberCausality;
    PluginSpinner spinnerPlatformType, spinnerETAHour, spinnerETAMin;
    String callsignAssigned, platformTypeSelected, etaHourSelected, etaMinSelected;
    CasevacDetailsInputs casevacDetailsInputs;
    RadioGroup radioGroupLevelStatus;
    RadioButton radioButtonLevelOne, radioButtonLevelTwo, radioButtonLevelThree, radioButtonEnroute;
    EditText editTextCallsignAssignment, editTextCasevacRemarks;
    String levelStatus = "";

    private CotEvent _cotEvent;
    private CotDetail _pulseRequestDetail;

    public CasevacAssignmentDialog(Context pluginContext, MapView mapView, CotEvent cotEvent, CotDetail pulseRequestDetail) {
        _pluginInflater = LayoutInflater.from(pluginContext);
        _pluginContext = pluginContext;
        _dialogContext = mapView.getContext();
        _cotEvent = cotEvent;
        _pulseRequestDetail = pulseRequestDetail;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(_dialogContext);

        _view = new CasevacAssignmentDialogView(_pluginContext);
        _view.setup(_pluginContext);
        builder.setPositiveButton("Confirm", this);
        builder.setNegativeButton("Cancel", this);
        builder.setTitle("Enter Casevac Details");
        builder.setView(_view);
        _dialog = builder.show();

        findViews();
        getRadioGroupStatus();
        getCasevacPlatformChangeListener();
        getCasevacETAHr();
        getCasevacETAMin();
        casevacDetailsInputs = new CasevacDetailsInputs();
    }

    public void findViews() {

        editTextCallsignAssignment = _view.findViewById(R.id.ed_casevac_assignment_cs);
        editTextCasevacRemarks = _view.findViewById(R.id.et_casevac_remarks);
        spinnerPlatformType = _view.findViewById(R.id.spinner_casevac_asset);
        spinnerETAHour = _view.findViewById(R.id.spinner_eta_hr);
        spinnerETAMin = _view.findViewById(R.id.spinner_eta_min);

        radioGroupLevelStatus = _view.findViewById(R.id.radio_level_status);
        radioButtonLevelOne = _view.findViewById(R.id.radio_lvl1);
        radioButtonLevelTwo = _view.findViewById(R.id.radio_lvl2);
        radioButtonLevelThree = _view.findViewById(R.id.radio_lvl3);
        radioButtonEnroute = _view.findViewById(R.id.radio_enroute);

    }

    public void getCasevacPlatformChangeListener() {

//        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> analgesicAdapterOne = ArrayAdapter.createFromResource(_view.getContext(),
                R.array.casevac_platform_array, R.layout.spinner_item);
        // Apply the adapter to the spinner
        spinnerPlatformType.setAdapter(analgesicAdapterOne);
        spinnerPlatformType.getSelectedItem();

        spinnerPlatformType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                platformTypeSelected = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getCasevacETAHr() {

//        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> analgesicAdapterOne = ArrayAdapter.createFromResource(_view.getContext(),
                R.array.casevac_eta_hr, R.layout.spinner_item);
        // Apply the adapter to the spinner
        spinnerETAHour.setAdapter(analgesicAdapterOne);
        spinnerETAHour.getSelectedItem();

        spinnerETAHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etaHourSelected = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getCasevacETAMin() {

//        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> analgesicAdapterOne = ArrayAdapter.createFromResource(_view.getContext(),
                R.array.casevac_eta_min, R.layout.spinner_item);
        // Apply the adapter to the spinner
        spinnerETAMin.setAdapter(analgesicAdapterOne);
        spinnerETAMin.getSelectedItem();

        spinnerETAMin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                etaMinSelected = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_NEUTRAL:
                return;
            case AlertDialog.BUTTON_POSITIVE:
                setCasevacDetails();
                PulseCasevacProcessor.getInstance().sendUpdate(_cotEvent,  _pulseRequestDetail, casevacDetailsInputs);
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                break;
        }
    }

    private void setCasevacDetails() {

        callsignAssigned = String.valueOf(editTextCallsignAssignment.getText());
        casevacDetailsInputs.setStatus(casevacDetailsInputs.getStatus());
        casevacDetailsInputs.setCasevacCallsign(callsignAssigned);
        casevacDetailsInputs.setCasevacPlatform(platformTypeSelected);
        casevacDetailsInputs.setEta(etaHourSelected + ":" + etaMinSelected);
        casevacDetailsInputs.setRemarks(String.valueOf(editTextCasevacRemarks.getText()));

        Log.d("TAG", "setCasevacDetails: " + casevacDetailsInputs.getStatus() + " " + casevacDetailsInputs.getCasevacCallsign() + " " + casevacDetailsInputs.getCasevacPlatform() + " " + casevacDetailsInputs.getEta());
    }

    private void getRadioGroupStatus() {
        radioGroupLevelStatus.setOnCheckedChangeListener((radioGroup, checkedID) -> {

            Log.d("TAG", "onCheckedChanged: " + checkedID);
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.radio_lvl1:
                    levelStatus = " Level 1";
                    break;

                case R.id.radio_lvl2:
                    levelStatus = " Level 2";

                    break;

                case R.id.radio_lvl3:
                    levelStatus = " Level 3";

                    break;

                case R.id.radio_enroute:
                    levelStatus = " Enroute";

                    break;

                default:
                    break;

            }
            casevacDetailsInputs.setStatus(levelStatus);
        });
    }

    private void showToast(final String message) {
        //cant toast on the service thread!
        RunnableManager.getInstance().post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(_pluginContext, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}

package com.atakmap.android.plugin.rain.pulse.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.Toast;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugin.rain.pulse.R;
import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;
import com.atakmap.android.plugin.rain.pulse.ui.view.TraumaDialogView;
import com.atakmap.android.plugin.rain.pulse.util.RunnableManager;

public class TraumaDialog implements DialogInterface.OnClickListener {

    private final LayoutInflater _pluginInflater;
    private final Context _dialogContext;
    private Button buttonReset;

    AlertDialog _dialog;

    private TraumaDialogView _view;
    public static final String SHOW_TRAUMA_VIEW = "com.atakmap.android.pulsetool.SHOW_TRAUMA_VIEW";
    private final Context _pluginContext;
    TeamMemberInputs teamMemberCasualty;

    public TraumaDialog(Context pluginContext, MapView mapView, TeamMemberInputs teamMemberInputs) {
        _pluginInflater = LayoutInflater.from(pluginContext);
        _pluginContext = pluginContext;
        _dialogContext = mapView.getContext();
        teamMemberCasualty = teamMemberInputs;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(_dialogContext);

        _view = new TraumaDialogView(_pluginContext);
        _view.setup(_pluginContext, teamMemberCasualty);
        builder.setPositiveButton("Confirm", this);
        builder.setNegativeButton("Cancel", this);
        builder.setTitle("Enter Trauma");
        builder.setView(_view);
        _view.setupCombatId(teamMemberCasualty);
        _dialog = builder.show();
        _view.restoreDefaultSelectionsOnClick();
        buttonReset = _view.findViewById(R.id.button_reset_prefs);


    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_NEUTRAL:
                return;
            case AlertDialog.BUTTON_POSITIVE:
                _view.makeCasevacRequestOnClick(teamMemberCasualty);
//                buttonReset.performClick();
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                break;
        }
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

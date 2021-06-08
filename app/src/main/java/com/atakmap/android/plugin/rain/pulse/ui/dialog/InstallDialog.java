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
import com.atakmap.android.plugin.rain.pulse.ui.view.InstallDialogView;
import com.atakmap.android.plugin.rain.pulse.ui.view.TraumaDialogView;
import com.atakmap.android.plugin.rain.pulse.util.RunnableManager;

public class InstallDialog implements DialogInterface.OnClickListener {

    private final LayoutInflater _pluginInflater;
    private final Context _dialogContext;
    private Button buttonReset;

    AlertDialog _dialog;

    private InstallDialogView _view;
    public static final String SHOW_INSTALL_VIEW = "com.atakmap.android.pulsetool.SHOW_INSTALL_VIEW";
    private final Context _pluginContext;

    public InstallDialog(Context pluginContext, MapView mapView) {
        _pluginInflater = LayoutInflater.from(pluginContext);
        _pluginContext = pluginContext;
        _dialogContext = mapView.getContext();
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(_dialogContext);

        _view = new InstallDialogView(_pluginContext);
        _view.setup(_pluginContext);
        builder.setPositiveButton("Ok", this);
        builder.setNegativeButton("Cancel", this);
        builder.setTitle("Please Install Pulse App");
        builder.setView(_view);
        _dialog = builder.show();


    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_NEUTRAL:
                return;
            case AlertDialog.BUTTON_POSITIVE:
//                Send user to the play store or another file to install
//                _view.installPulseOnClick(teamMemberCasualty);

//                buttonReset.performClick();
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                break;
        }
    }
}

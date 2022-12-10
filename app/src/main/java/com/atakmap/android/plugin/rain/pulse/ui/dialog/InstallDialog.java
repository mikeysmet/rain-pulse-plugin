package com.atakmap.android.plugin.rain.pulse.ui.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugin.rain.pulse.R;
import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;
import com.atakmap.android.plugin.rain.pulse.ui.view.InstallDialogView;
import com.atakmap.android.plugin.rain.pulse.ui.view.TraumaDialogView;
import com.atakmap.android.plugin.rain.pulse.util.RunnableManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.Context.MODE_ENABLE_WRITE_AHEAD_LOGGING;
import static android.content.Context.MODE_WORLD_READABLE;
import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.getIntentOld;

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
        builder.setTitle("Install Pulse App");
        builder.setView(_view);
        _dialog = builder.show();


    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case AlertDialog.BUTTON_NEUTRAL:
                return;
            case AlertDialog.BUTTON_POSITIVE:


                break;
            case AlertDialog.BUTTON_NEGATIVE:
                break;
        }
    }
}

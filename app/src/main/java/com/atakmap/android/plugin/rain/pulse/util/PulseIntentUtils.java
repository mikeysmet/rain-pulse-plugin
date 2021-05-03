package com.atakmap.android.plugin.rain.pulse.util;

import android.content.Intent;

import com.atakmap.android.ipc.AtakBroadcast;
import com.atakmap.android.plugin.rain.pulse.prefs.PulsePreferenceFragment;

public class PulseIntentUtils {

    public static final String INTENT_ACTION_BT_PERMISSIONS = "PULSE_BT_PERMISSIONS";

    private static final String INTENT_ACTION_ADVANCED_SETTINGS = "com.atakmap.app.ADVANCED_SETTINGS";

    public static void openSettings() {
        Intent intent = new Intent(INTENT_ACTION_ADVANCED_SETTINGS);
        intent.putExtra("prefkey", PulsePreferenceFragment.KEY);
        AtakBroadcast.getInstance().sendBroadcast(intent);
    }
}

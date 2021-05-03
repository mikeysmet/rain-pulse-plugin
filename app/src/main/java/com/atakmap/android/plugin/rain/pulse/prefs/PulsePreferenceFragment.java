package com.atakmap.android.plugin.rain.pulse.prefs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.util.Log;

import com.atakmap.android.gui.PanEditTextPreference;
import com.atakmap.android.gui.PanListPreference;
import com.atakmap.android.plugin.rain.pulse.R;
import com.atakmap.android.preference.PluginPreferenceFragment;

import java.util.ArrayList;
import java.util.List;

public class PulsePreferenceFragment extends PluginPreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static Context staticPluginContext;
    public static final String TAG = "PulsePreferenceFragment";

    public static final String KEY = "PulsePreferenceFragment";


    /**
     * Only will be called after this has been instantiated with the 1-arg constructor.
     * Fragments must has a zero arg constructor.
     */
    public PulsePreferenceFragment() {
        super(staticPluginContext, R.xml.preferences);
    }

    @SuppressLint("ValidFragment")
    public PulsePreferenceFragment(final Context pluginContext) {
        super(pluginContext, R.xml.preferences);
        staticPluginContext = pluginContext;

        PulsePrefs.getPrefs().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSummaryValues();
    }

    private void loadSummaryValues() {
        try {
            for (Preference preference : getPreferences(null)) {
                if (!PulsePrefs.shouldSetSummary(preference.getKey())) continue;
                updateSummary(preference);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateSummary(Preference preference) {
        if (preference instanceof PanEditTextPreference) {
            preference.setSummary(((PanEditTextPreference) preference).getText());
        }
        if (preference instanceof PanListPreference) {
            preference.setSummary(((PanListPreference) preference).getEntry());
        }
    }

    public List<Preference> getPreferences(PreferenceGroup group) {
        if (group == null)
            group = getPreferenceScreen();
        List<Preference> ret = new ArrayList<Preference>();
        for (int i = 0; i < group.getPreferenceCount(); i++) {
            Preference pref = group.getPreference(i);
            if (pref instanceof PreferenceGroup)
                ret.addAll(getPreferences((PreferenceGroup) pref));
            else
                ret.add(pref);
        }
        return ret;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d(TAG, "onSharedPreferenceChanged " + s);
        if (!PulsePrefs.shouldSetSummary(s)) return;
        loadSummaryValues();
    }
}

package com.atakmap.android.plugin.rain.pulse.prefs;//package com.atakmap.android.pulse.prefs;
//
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager;
//
//import com.atakmap.android.maps.MapView;
//import com.atakmap.android.pulse.model.TeamMemberInputs;
//import com.atakmap.android.pulse.model.TreatmentInputs;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//public class PulseTreatmentPrefs {
//
//    private static final String KEY_CheckBoxes = "pulse_treatment_pref";
//    private static final String KEY_TYPE = "pulse_treatment_pref";
//    private static final String KEY_AGE = "pulse_treatment_pref";
//    private static final String KEY_GENDER = "pulse_treatment_pref";
//    private static final String KEY_BLOOD = "pulse_treatment_pref";
//    private static final String KEY_ALLERGY = "pulse_treatment_pref";
//    private static final String KEY_FDP = "pulse_treatment_pref";
//    private static final String KEY_REMARK = "pulse_treatment_pref";
//
//
//    private static final List<String> _pulseKeys = new ArrayList<>(Arrays.asList(
//            KEY_ID, KEY_TYPE, KEY_AGE, KEY_GENDER,KEY_BLOOD, KEY_ALLERGY,KEY_FDP,KEY_REMARK   ));
//
//    private static final List<String> _summaryDisplay = new ArrayList<>(Arrays.asList(
//            KEY_ID, KEY_TYPE, KEY_AGE, KEY_BLOOD, KEY_ALLERGY  ));
//
//    public static boolean shouldSetSummary(String key) {
//        return _summaryDisplay.contains(key);
//    }
//
//    public static boolean isPulsePreference(String key){
//        return _pulseKeys.contains(key);
//    }
//
//    private static SharedPreferences _prefs;
//
//    public static SharedPreferences getPrefs() {
//        if (_prefs == null) {
//            try {
//                _prefs = PreferenceManager.getDefaultSharedPreferences(
//                        MapView.getMapView().getContext());
//            } catch (Exception e) {
//                _prefs = null;
//            }
//        }
//        return _prefs;
//    }
//
//    public static TreatmentInputs getTreatmentInfo() {
//        SharedPreferences prefs = getPrefs();
//        TeamMemberInputs inputs = new TeamMemberInputs();
//
//        String combatId = prefs.getString(KEY_ID, "1");
//        inputs.setTmCombatID(combatId);
//
//        String callsign = MapView.getMapView().getDeviceCallsign();
//        inputs.setTmCallsign(callsign);
//
//        String type = prefs.getString(KEY_TYPE, "");
//        inputs.setTmAssetType(type);
//
//        String age = prefs.getString(KEY_AGE, "18");
//        inputs.setAge(age);
//
//        String bloodType = prefs.getString(KEY_BLOOD, "");
//        inputs.setTmbloodType(bloodType);
//
//        String allergy = prefs.getString(KEY_ALLERGY, "");
//        inputs.setTmAllergies(allergy);
//
//        String remarks = prefs.getString(KEY_REMARK, "");
//        inputs.setRemarks(remarks);
//
//        boolean fdpAuth = prefs.getBoolean(KEY_FDP, false);
//        inputs.setTmAuthorizeFDP(fdpAuth);
//
//        return inputs;
//    }
//}

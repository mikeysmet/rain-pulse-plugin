package com.atakmap.android.plugin.rain.pulse.prefs;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.atakmap.android.maps.MapView;
import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;
import com.atakmap.coremap.maps.time.CoordinatedTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PulsePrefs {

    private static final String KEY_ID = "pulse_pref_combat_id";
    private static final String KEY_TYPE = "pulse_pref_type";
    private static final String KEY_AGE = "pulse_pref_age";
    private static final String KEY_GENDER = "pulse_pref_gender";
    private static final String KEY_BLOOD = "pulse_pref_blood";
    private static final String KEY_ALLERGY = "pulse_pref_allergy";
    private static final String KEY_FDP = "pulse_pref_fdp_enabled";
    private static final String KEY_REMARK = "pulse_pref_remark";

    private static final String KEY_PATIENT_ID = "pulse_pref_patient_id";
    private static final String KEY_PATIENT_STALE = "pulse_pref_patient_stale";
    //This user (when enabled) is broadcasting as the patient
    private static final String KEY_PATIENT_ENABLED = "pulse_pref_patient_enabled";

    private static final String KEY_CASEVAC_IN_PROGRESS = "pulse_casevac_in_progress";

    //Treatment Prefs
    public static final String KEY_CASEVAC_TREATMENTS_CHECKBOXES = "treatments_checkboxes_";
    public static final String KEY_CASEVAC_TQ_CHECKED = "tq_checkboxes_";
    public static final String KEY_CASEVAC_TQ_TIME = "tq_checkboxes_time_";

    public static final String KEY_CASEVAC_BODY_PART = "body_part_text";
    public static final String KEY_CASEVAC_BODY_PART_VIS = "body_part_text_vis";
    public static final String KEY_CASEVAC_BODY_PART_LOC_X = "body_part_text_x";
    public static final String KEY_CASEVAC_BODY_PART_LOC_Y = "body_part_text_y";

    public static final String KEY_CASEVAC_INJURY_IV_DONUT_POS = "injury_iv_donut_position_vis";
    public static final String KEY_CASEVAC_INJURY_IV_DONUT_X = "injury_iv_donut_x";
    public static final String KEY_CASEVAC_INJURY_IV_DONUT_Y = "injury_iv_donut_y";

    public static final String KEY_CASEVAC_IV_CONFIRM_CHECK_MARK_VIS = "confirmation_check_vis";
    public static final String KEY_CASEVAC_CONFIRMATION_IV_DONUT_X = "confirmation_iv_donut_x";
    public static final String KEY_CASEVAC_CONFIRMATION_IV_DONUT_Y = "confirmation_iv_donut_y";

    public static final String KEY_CASEVAC_FLUID_LIST = "treatment_table_fluid_list";
    public static final String KEY_CASEVAC_BLOOD_PROD_LIST = "treatment_table_blood_product_list";
    public static final String KEY_CASEVAC_ANALGESIC_LIST = "treatment_table_analgesic_list";
    public static final String KEY_CASEVAC_ANTIBIOTIC_LIST = "treatment_table_antibiotic_list";
    public static final String KEY_CASEVAC_TCCC_VIS = "tccc_visibilty";

    private static final List<String> _pulseKeys = new ArrayList<>(Arrays.asList(
            KEY_ID, KEY_TYPE, KEY_AGE, KEY_GENDER, KEY_BLOOD, KEY_ALLERGY, KEY_FDP, KEY_REMARK, KEY_PATIENT_ID));

    private static final List<String> _summaryDisplay = new ArrayList<>(Arrays.asList(
            KEY_ID, KEY_TYPE, KEY_AGE, KEY_BLOOD, KEY_ALLERGY, KEY_PATIENT_ID));

    private static final List<String> _treatmentSummary = new ArrayList<>(Arrays.asList(
            KEY_ID,
            KEY_CASEVAC_TREATMENTS_CHECKBOXES,
            KEY_CASEVAC_TQ_CHECKED,
            KEY_CASEVAC_TQ_TIME,
            KEY_CASEVAC_BODY_PART,
            KEY_CASEVAC_FLUID_LIST,
            KEY_CASEVAC_BLOOD_PROD_LIST,
            KEY_CASEVAC_ANALGESIC_LIST,
            KEY_CASEVAC_ANTIBIOTIC_LIST
    ));

    public static boolean shouldSetSummary(String key) {
        return _summaryDisplay.contains(key);
    }

    public static List<String> getTreatmentSummary(List<String> _treatmentSummaryList) {

        return _treatmentSummaryList;
    }

    public static boolean isPulsePreference(String key) {
        return _pulseKeys.contains(key);
    }

    private static SharedPreferences _prefs;

    public static SharedPreferences getPrefs() {
        if (_prefs == null) {
            try {
                _prefs = PreferenceManager.getDefaultSharedPreferences(
                        MapView.getMapView().getContext());
            } catch (Exception e) {
                _prefs = null;
            }
        }
        return _prefs;
    }

    public static TeamMemberInputs getUserInfo() {
        SharedPreferences prefs = getPrefs();
        TeamMemberInputs inputs = new TeamMemberInputs();

        String combatId = prefs.getString(KEY_ID, "1");
        inputs.setTmCombatID(combatId);

        String callsign = MapView.getMapView().getDeviceCallsign();
        inputs.setTmCallsign(callsign);

        String type = prefs.getString(KEY_TYPE, "");
        inputs.setTmAssetType(type);

        String age = prefs.getString(KEY_AGE, "18");
        inputs.setAge(age);

        String bloodType = prefs.getString(KEY_BLOOD, "");
        inputs.setTmBloodType(bloodType);

        String allergy = prefs.getString(KEY_ALLERGY, "");
        inputs.setTmAllergies(allergy);

        String remarks = prefs.getString(KEY_REMARK, "");
        inputs.setRemarks(remarks);

        boolean fdpAuth = prefs.getBoolean(KEY_FDP, false);
        inputs.setTmAuthorizeFDP(fdpAuth);

        return inputs;
    }

    public static void setPatientId(String tmCombatID) {
        SharedPreferences prefs = getPrefs();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_PATIENT_ID, tmCombatID);
        //keep alive for 6 hours.
        CoordinatedTime time = new CoordinatedTime();
        time.addHours(6);
        editor.putString(KEY_PATIENT_STALE, time.toString());
        editor.commit();
    }

    public static String getPatientId() {
        SharedPreferences prefs = getPrefs();
        return prefs.getString(KEY_PATIENT_ID, "-1");
    }

    public static boolean isCasevacInProgress() {
        SharedPreferences prefs = getPrefs();
        return prefs.getBoolean(KEY_CASEVAC_IN_PROGRESS, false);
    }

    public static void setIsCasevacInProgress(boolean inProgress) {
        SharedPreferences prefs = getPrefs();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(KEY_CASEVAC_IN_PROGRESS, inProgress);
        editor.apply();
    }

}

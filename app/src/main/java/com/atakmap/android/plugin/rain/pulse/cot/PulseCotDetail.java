package com.atakmap.android.plugin.rain.pulse.cot;

import android.util.Log;

import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;
import com.atakmap.coremap.cot.event.CotDetail;

public class PulseCotDetail {

    public static final String TAG = "PulseCotDetail";
    /**
     * PulseCotDetail:
     * Handle all Model <==> CoT XML conversion here.
     */
    public static final String DETAIL_PULSE = "__pulse";

    public static final String ATTR_ID = "combatId";
    public static final String ATTR_CALLSIGN = "callsign";
    public static final String ATTR_AGE = "age";
    public static final String ATTR_TYPE = "type";
    public static final String ATTR_BLOOD = "bloodType";
    public static final String ATTR_ALLERGY = "allergy";
    public static final String ATTR_REMARK = "remark";
    public static final String ATTR_FDP = "fdp_auth";
    public static final String ATTR_HEART_RATE_VARIABLE = "heartRateVariable";
    public static final String ATTR_SP02 = "spo2";
    public static final String ATTR_RESPIRATION = "respiration";
    public static final String ATTR_BODY_BATTERY = "body_battery";
    public static final String ATTR_STRESS = "stress";

    public static final String ATTR_HEART_RATE = "heartRate";

    public int heartRate = -1;
    public int heartRateVar = -1;
    public int spo2 = -1;
    public int respiration = -1;
    public int bodyBattery = -1;
    public int stressLevel = -1;


    public PulseCotDetail(CotDetail cotDetail) {
        if (!cotDetail.getElementName().equals(DETAIL_PULSE)) return;

        String heartRateStr = cotDetail.getAttribute(ATTR_HEART_RATE);
        if (heartRateStr == null || heartRateStr.isEmpty()) return;
        try {
            this.heartRate = Integer.parseInt(heartRateStr);
        } catch (NumberFormatException e) {
            Log.d(TAG, "bad heart rate");
        }
        String heartRateSVart = cotDetail.getAttribute(ATTR_HEART_RATE_VARIABLE);
        if (heartRateSVart == null || heartRateSVart.isEmpty()) return;
        try {
            this.heartRateVar = Integer.parseInt(heartRateSVart);
        } catch (NumberFormatException e) {
            Log.d(TAG, "bad heart var");
        }
        String sp02PercentString = cotDetail.getAttribute(ATTR_SP02);
        if (sp02PercentString == null || sp02PercentString.isEmpty()) return;
        try {
            this.spo2 = Integer.parseInt(sp02PercentString);
        } catch (NumberFormatException e) {
            Log.d(TAG, "bad sp02");
        }
        String respirationString = cotDetail.getAttribute(ATTR_RESPIRATION);
        if (respirationString == null || respirationString.isEmpty()) return;
        try {
            this.respiration = Integer.parseInt(respirationString);
        } catch (NumberFormatException e) {
            Log.d(TAG, "bad respiration");
        }
        String bodyBatteryString = cotDetail.getAttribute(ATTR_BODY_BATTERY);
        if (bodyBatteryString == null || bodyBatteryString.isEmpty()) return;
        try {
            this.bodyBattery = Integer.parseInt(bodyBatteryString);
        } catch (NumberFormatException e) {
            Log.d(TAG, "bad bodyBattery");
        }
        String stressString = cotDetail.getAttribute(ATTR_STRESS);
        if (stressString == null || stressString.isEmpty()) return;
        try {
            this.stressLevel = Integer.parseInt(stressString);
        } catch (NumberFormatException e) {
            Log.d(TAG, "bad stress");
        }

    }

    public static CotDetail toDetail(TeamMemberInputs user) {
        CotDetail pulseDetail = new CotDetail(DETAIL_PULSE);
        pulseDetail.setAttribute(ATTR_ID, user.getTmCombatID());
        pulseDetail.setAttribute(ATTR_CALLSIGN, user.getTmCallsign());
        pulseDetail.setAttribute(ATTR_AGE, user.getAge());
        pulseDetail.setAttribute(ATTR_TYPE, user.getTmAssetType());
        pulseDetail.setAttribute(ATTR_ALLERGY, user.getTmAllergies());
        pulseDetail.setAttribute(ATTR_BLOOD, user.getTmBloodType());
        pulseDetail.setAttribute(ATTR_FDP, user.isTmAuthorizeFDP() ? "true" : "false");
        pulseDetail.setAttribute(ATTR_SP02, user.getTmSp02());
        pulseDetail.setAttribute(ATTR_RESPIRATION, user.getTmRespiration());
        pulseDetail.setAttribute(ATTR_BODY_BATTERY, user.getTmBodyBattery());
        pulseDetail.setAttribute(ATTR_STRESS, user.getTmStress());
        pulseDetail.setAttribute(ATTR_HEART_RATE, user.getTmHeartRate());
        pulseDetail.setAttribute(ATTR_HEART_RATE_VARIABLE, user.getTmHeartRateVar());
        pulseDetail.setAttribute(ATTR_REMARK, user.getRemarks());

        return pulseDetail;
    }

    public static void updateDetail(CotDetail userDetail, TeamMemberInputs currentUser) {
        userDetail.setAttribute(ATTR_HEART_RATE, currentUser.getTmHeartRate());
        userDetail.setAttribute(ATTR_HEART_RATE_VARIABLE, currentUser.getTmHeartRateVar());
        userDetail.setAttribute(ATTR_SP02, currentUser.getTmSp02());
        userDetail.setAttribute(ATTR_RESPIRATION, currentUser.getTmRespiration());
        userDetail.setAttribute(ATTR_BODY_BATTERY, currentUser.getTmBodyBattery());
        userDetail.setAttribute(ATTR_STRESS, currentUser.getTmStress());

    }


    public static TeamMemberInputs toTeamMember(CotDetail cotDetail) {
        TeamMemberInputs update = new TeamMemberInputs();

        update.setTmCombatID(getAttribute(ATTR_ID, cotDetail));
        update.setTmCallsign(getAttribute(ATTR_CALLSIGN, cotDetail));
        update.setAge(getAttribute(ATTR_AGE, cotDetail));
        update.setTmAssetType(getAttribute(ATTR_TYPE, cotDetail));
        update.setRemarks(getAttribute(ATTR_REMARK, cotDetail));
        update.setTmAllergies(getAttribute(ATTR_ALLERGY, cotDetail));
        update.setTmBloodType(getAttribute(ATTR_BLOOD, cotDetail));
        update.setTmAuthorizeFDP(getBooleanAttribute(ATTR_FDP, cotDetail));
        //heartRate last so we can attempt to get a valid exertion calculation
        //TODO - add exertion to the CoT so we dont have to calculate it
        update.setTmHeartRateVar(getAttribute(ATTR_HEART_RATE_VARIABLE, cotDetail));
        update.setTmSp02(getAttribute(ATTR_SP02, cotDetail));
        update.setTmRespiration(getAttribute(ATTR_RESPIRATION, cotDetail));
        update.setTmBodyBattery(getAttribute(ATTR_BODY_BATTERY, cotDetail));
        update.setTmStress(getAttribute(ATTR_STRESS, cotDetail));
        update.setTmHeartRate(getAttribute(ATTR_HEART_RATE, cotDetail));

        return update;
    }

    public static String getAttribute(String key, CotDetail cotDetail) {
        String value = cotDetail.getAttribute(key);
        if (value == null) {
            value = "n/a";
        }
        return value;
    }

    private static boolean getBooleanAttribute(String key, CotDetail cotDetail) {
        String value = cotDetail.getAttribute(key);
        if (value == null) {
            return false;
        }
        return value.equals("true");
    }
}

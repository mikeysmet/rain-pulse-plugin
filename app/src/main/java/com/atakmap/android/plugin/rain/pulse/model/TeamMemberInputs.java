package com.atakmap.android.plugin.rain.pulse.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class TeamMemberInputs implements Parcelable {

    public int combatID;

    private long dbID;
    String tmCombatID;
    String tmCallsign;
    String tmHeartRate;
    String tmHeartRateVar;
    String tmSp02;
    String tmRespiration;
    String tmBodyBattery;
    String tmStress;
    String tmLocation;
    String tmLastReport;
    String age;
    String tmTourniquetTimeStamp;
    String tmBloodType;
    String tmAssetType;
    String tmAllergies;
    double tmDistance;
    double tmLat;
    double tmLon;
    int tmDirection;
    int tmBattery;

    boolean tmTourniquet;
    boolean tmAuthorizeFDP;

    public boolean tmCasualty;
    private boolean expandView;
    private boolean isActive;
    private boolean isBroadcasting;
    private boolean isVisible;
    private String remarks;

    private float _exertionPercentage;


    public static final Creator<TeamMemberInputs> CREATOR = new Creator<TeamMemberInputs>() {
        public TeamMemberInputs createFromParcel(Parcel in) {
            return new TeamMemberInputs(in);
        }

        public TeamMemberInputs[] newArray(int size) {
            return new TeamMemberInputs[size];
        }
    };

    public TeamMemberInputs(long dbID, String tagID, boolean isActive) {
        this.tmCombatID = tagID;
        combatID = Integer.parseInt(tagID);
        this.isActive = isActive;
        this.dbID = dbID;
        this.tmCallsign = "unknownCS";
        tmLocation = "0,0";
        tmAssetType = "N/A";
    }

    public TeamMemberInputs(String tmCombatID, String tmCallsign, String tmHeartRate, String tmHeartRateVar, String tmSp02, String tmRespiration, String tmBodyBattery, String tmStress, String tmLocation, String tmLastReport, String tmTourniquetTimeStamp,
                            String tmBloodType, String tmAssetType, String tmAllergies, double tmDistance, int tmDirection, int tmBattery, boolean tmTourniquet,
                            String age, boolean tmAuthorizeFDP, boolean tmCasualty, boolean expandView, boolean isActive, boolean isBroadcasting, boolean isVisible, String remarks) {
        this.tmCombatID = tmCombatID;
        combatID = Integer.parseInt(tmCombatID);

        this.tmCallsign = tmCallsign;
        this.tmHeartRate = tmHeartRate;
        this.tmHeartRateVar = tmHeartRateVar;
        this.tmSp02 = tmSp02;
        this.tmRespiration = tmRespiration;
        this.tmBodyBattery = tmBodyBattery;
        this.tmStress = tmStress;
        this.tmLocation = tmLocation;
        this.tmLastReport = tmLastReport;
        this.tmTourniquetTimeStamp = tmTourniquetTimeStamp;
        this.tmBloodType = tmBloodType;
        this.tmAssetType = tmAssetType;
        this.tmAllergies = tmAllergies;
        this.tmDistance = tmDistance;
        this.tmDirection = tmDirection;
        this.tmBattery = tmBattery;
        this.tmTourniquet = tmTourniquet;
        this.age = age;
        this.tmAuthorizeFDP = tmAuthorizeFDP;
        this.tmCasualty = tmCasualty;
        this.expandView = expandView;
        this.isActive = isActive;
        this.isBroadcasting = isBroadcasting;
        this.isVisible = isVisible;
        this.remarks = remarks;
    }

    public TeamMemberInputs(String tmCombatID, String tmCallsign, String tmHeartRate, String tmHeartRateVar, String tmSp02, String tmRespiration, String tmBodyBattery,
                            String tmStress, String tmLocation, String tmLastReport, String tmTourniquetTimeStamp, String tmBloodType,
                            String tmAssetType, String tmAllergies, double tmDistance, boolean tmTourniquet, String age,
                            boolean tmAuthorizeFDP, boolean tmCasualty, int tmDirection, int tmBattery) {

        this.tmCombatID = tmCombatID;
        combatID = Integer.valueOf(tmCombatID);

        this.tmCallsign = tmCallsign;
        this.tmHeartRate = tmHeartRate;
        this.tmHeartRateVar = tmHeartRateVar;
        this.tmSp02 = tmSp02;
        this.tmRespiration = tmRespiration;
        this.tmBodyBattery = tmBodyBattery;
        this.tmStress = tmStress;
        this.tmLocation = tmLocation;
        this.tmLastReport = tmLastReport;
        this.tmTourniquetTimeStamp = tmTourniquetTimeStamp;
        this.tmBloodType = tmBloodType;
        this.tmAssetType = tmAssetType;
        this.tmAllergies = tmAllergies;
        this.tmDistance = tmDistance;
        this.tmTourniquet = tmTourniquet;
        this.age = age;
        this.tmAuthorizeFDP = tmAuthorizeFDP;
        this.tmCasualty = tmCasualty;
        this.tmDirection = tmDirection;
        this.tmBattery = tmBattery;
    }

    public TeamMemberInputs(String tmCombatID, String tmCallsign, String tmHeartRate, String tmSp02,
                            String tmRespiration, String tmBodyBattery, String tmStress, String tmBloodType,
                            String tmAssetType, String age) {
        this.tmCombatID = tmCombatID;
        combatID = Integer.parseInt(tmCombatID);

        this.tmCallsign = tmCallsign;
        this.tmHeartRate = tmHeartRate;
        this.tmSp02 = tmSp02;
        this.tmRespiration = tmRespiration;
        this.tmBodyBattery = tmBodyBattery;
        this.tmStress = tmStress;
        this.tmBloodType = tmBloodType;
        this.tmAssetType = tmAssetType;
        this.age = age;

    }

    public TeamMemberInputs() {
    }

    public long getDbID() {
        return dbID;
    }

    public void setDbID(long dbID) {
        this.dbID = dbID;
    }

    public String getTmCombatID() {
        return this.tmCombatID;
    }

    public void setTmCombatID(String tmCombatID) {
        this.tmCombatID = tmCombatID;
        this.combatID = Integer.valueOf(tmCombatID);
    }

    public String getTmCallsign() {
        return this.tmCallsign;
    }

    public void setTmCallsign(String tmCallsign) {
        this.tmCallsign = tmCallsign;
    }

    public String getTmHeartRate() {
        return this.tmHeartRate;
    }

    public void setTmHeartRate(String tmHeartRate) {
        this.tmHeartRate = tmHeartRate;
        determineExertion();
    }

    public String getTmHeartRateVar() {
        return tmHeartRateVar;
    }

    public void setTmHeartRateVar(String tmHeartRateVar) {
        this.tmHeartRateVar = tmHeartRateVar;
    }

    private void determineExertion() {
        int age = Integer.parseInt(getAge());
        int maxHeartRate = 220 - age;
        int restingHeartRate = 75;
        double avgHRR = maxHeartRate - restingHeartRate;
        double tgtHeartRateLow = avgHRR * .7 + restingHeartRate;
        double tgtHeartRateHigh = avgHRR * .85 + restingHeartRate;
        int heartRate = Integer.parseInt(getTmHeartRate());

        float percentage = ((float) heartRate / maxHeartRate) * 100;

        //String intPercent = String.valueOf((int)percentage);
        // Log.d(TAG, "runExertionCalculation: " + intPercent);
        _exertionPercentage = percentage;
    }

    public float getExertionPercentage() {
        return _exertionPercentage;
    }

    public String getTmSp02() {
        return this.tmSp02;
    }

    public void setTmSp02(String tmSp02) {
        this.tmSp02 = tmSp02;
    }


    public String getTmRespiration() {
        return tmRespiration;
    }

    public void setTmRespiration(String tmRespiration) {
        this.tmRespiration = tmRespiration;
    }

    public String getTmBodyBattery() {
        return tmBodyBattery;
    }

    public void setTmBodyBattery(String tmBodyBattery) {
        this.tmBodyBattery = tmBodyBattery;
    }

    public String getTmStress() {
        return tmStress;
    }

    public void setTmStress(String tmStress) {
        this.tmStress = tmStress;
    }

    public String getTmLocation() {
        return this.tmLocation;
    }

    public void setTmLocation(String tmLocation) {
        this.tmLocation = tmLocation;
    }

    public String getTmLastReport() {
        return this.tmLastReport;
    }

    public void setTmLastReport(String tmLastReport) {
        this.tmLastReport = tmLastReport;
    }

    public String getTmTourniquetTimeStamp() {
        return this.tmTourniquetTimeStamp;
    }

    public void setTmTourniquetTimeStamp(String tmTourniquetTimeStamp) {
        this.tmTourniquetTimeStamp = tmTourniquetTimeStamp;
    }

    public String getTmBloodType() {
        return this.tmBloodType;
    }

    public void setTmBloodType(String tmBloodType) {
        this.tmBloodType = tmBloodType;
    }

    public String getTmAssetType() {
        return this.tmAssetType;
    }

    public void setTmAssetType(String tmAssetType) {
        this.tmAssetType = tmAssetType;
    }

    public String getTmAllergies() {
        return tmAllergies;
    }

    public void setTmAllergies(String tmAllergies) {
        this.tmAllergies = tmAllergies;
    }

    public double getTmDistance() {
        return this.tmDistance;
    }

    public void setTmDistance(double tmDistance) {
        this.tmDistance = tmDistance;
    }

    public double getTmLat() {
        return tmLat;
    }

    public void setTmLat(double tmLat) {
        this.tmLat = tmLat;
    }

    public double getTmLon() {
        return tmLon;
    }

    public void setTmLon(double tmLon) {
        this.tmLon = tmLon;
    }

    public boolean isTmTourniquet() {
        return this.tmTourniquet;
    }

    public void setTmTourniquet(boolean tmTourniquet) {
        this.tmTourniquet = tmTourniquet;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public boolean isTmAuthorizeFDP() {
        return this.tmAuthorizeFDP;
    }

    public void setTmAuthorizeFDP(boolean tmAuthorizeFDP) {
        this.tmAuthorizeFDP = tmAuthorizeFDP;
    }

    public boolean isTmCasualty() {
        return tmCasualty;
    }

    public void setTmCasualty(boolean tmCasualty) {
        this.tmCasualty = tmCasualty;
    }

    public int getTmDirection() {
        return this.tmDirection;
    }

    public void setTmDirection(int tmDirection) {
        this.tmDirection = tmDirection;
    }

    public int getTmBattery() {
        return this.tmBattery;
    }

    public void setTmBattery(int tmBattery) {
        this.tmBattery = tmBattery;
    }

    public boolean isExpandView() {
        return expandView;
    }

    public void setExpandView(boolean expandView) {
        this.expandView = expandView;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isBroadcasting() {
        return isBroadcasting;
    }

    public void setBroadcasting(boolean broadcasting) {
        isBroadcasting = broadcasting;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }


    protected TeamMemberInputs(Parcel in) {
        this.dbID = in.readLong();
        this.tmCombatID = in.readString();
        this.tmCallsign = in.readString();
        this.tmHeartRate = in.readString();
        this.tmHeartRateVar = in.readString();
        this.tmSp02 = in.readString();
        this.tmRespiration = in.readString();
        this.tmBodyBattery = in.readString();
        this.tmStress = in.readString();
        this.tmLocation = in.readString();
        this.tmLastReport = in.readString();
        this.tmTourniquetTimeStamp = in.readString();
        this.tmBloodType = in.readString();
        this.tmAssetType = in.readString();
        this.tmAllergies = in.readString();
        this.tmDistance = in.readDouble();
        this.tmLat = in.readDouble();
        this.tmLon = in.readDouble();
        this.tmTourniquet = in.readByte() != 0;
        this.age = in.readString();
        this.tmAuthorizeFDP = in.readByte() != 0;
        this.tmCasualty = in.readByte() != 0;
        this.tmDirection = in.readInt();
        this.tmBattery = in.readInt();
        this.expandView = in.readByte() != 0;
        this.isActive = in.readByte() != 0;
        this.isBroadcasting = in.readByte() != 0;
        this.isVisible = in.readByte() != 0;
        this.remarks = in.readString();


    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.dbID);
        dest.writeString(this.tmCombatID);
        dest.writeString(this.tmCallsign);
        dest.writeString(this.tmHeartRate);
        dest.writeString(this.tmHeartRateVar);
        dest.writeString(this.tmSp02);
        dest.writeString(this.tmRespiration);
        dest.writeString(this.tmBodyBattery);
        dest.writeString(this.tmStress);
        dest.writeString(this.tmLocation);
        dest.writeString(this.tmLastReport);
        dest.writeString(this.tmTourniquetTimeStamp);
        dest.writeString(this.tmBloodType);
        dest.writeString(this.tmAssetType);
        dest.writeString(this.tmAllergies);
        dest.writeDouble(this.tmDistance);
        dest.writeDouble(this.tmLat);
        dest.writeDouble(this.tmLon);
        dest.writeByte((byte) (this.tmTourniquet ? 1 : 0));
        dest.writeString(this.age);
        dest.writeByte((byte) (this.tmAuthorizeFDP ? 1 : 0));
        dest.writeByte((byte) (this.tmCasualty ? 1 : 0));
        dest.writeInt(this.tmDirection);
        dest.writeInt(this.tmBattery);
        dest.writeByte((byte) (this.expandView ? 1 : 0));
        dest.writeByte((byte) (this.isActive ? 1 : 0));
        dest.writeByte((byte) (this.isBroadcasting ? 1 : 0));
        dest.writeByte((byte) (this.isVisible ? 1 : 0));
        dest.writeString(this.remarks);
    }

    public void update(TeamMemberInputs updated) {
        //when user settings change
        this.tmCombatID = updated.tmCombatID;
        this.tmCallsign = updated.tmCallsign;
        this.age = updated.age;
        this.tmBloodType = updated.tmBloodType;
        this.tmAssetType = updated.tmAssetType;
        this.tmAllergies = updated.tmAllergies;
        this.tmAuthorizeFDP = updated.tmAuthorizeFDP;
        this.tmCasualty = updated.tmCasualty;
        this.remarks = updated.remarks;
    }

    public void setLastReportTime(long timeStamp) {
        SimpleDateFormat updateTime = new SimpleDateFormat("HH:mm:ss", Locale.US);
        String updateTimeStamp = updateTime.format(timeStamp);
        setTmLastReport(updateTimeStamp);
    }

    public String getExertionPercentageDisplay() {
        if (_exertionPercentage == 0) {
            return "n/a";
        } else {
            return String.format("%.0f", _exertionPercentage);
        }
    }

}

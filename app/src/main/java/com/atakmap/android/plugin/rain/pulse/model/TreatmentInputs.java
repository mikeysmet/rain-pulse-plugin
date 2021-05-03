package com.atakmap.android.plugin.rain.pulse.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class TreatmentInputs implements Parcelable {

    int combatID;
    List<String> mechOfInjury;
    List<String> tqTreatmentsList;
    List<String> tqTimeStampList;
    List<String> treatmentsC;
    List<String> treatmentsA;
    List<String> treatmentsB;
    List<String> treatmentsOther;
    List<String> injuryLocation;

    List<String> fluidTreatment;
    List<String> bloodTreatment;
    List<String> andalgesicTreatment;
    List<String> antibioticTreatment;

    String leftArmTimeStamp;
    String rightArmTimeStamp;
    String leftLegTimeStamp;
    String rightLegTimeStamp;
    String directPressureTimeStamp;
    String elevTimeStamp;

    public TreatmentInputs() {
    }

    public TreatmentInputs(int combatID, List<String> mechOfInjury, List<String> tqTreatmentsList, List<String> tqTimeStampList, List<String> treatmentsC,
                           List<String> treatmentsA, List<String> treatmentsB, List<String> treatmentsOther, List<String> injuryLocation,
                           List<String> fluidTreatment, List<String> bloodTreatment, List<String> andalgesicTreatment,
                           List<String> antibioticTreatment, String leftArmTimeStamp, String rightArmTimeStamp,
                           String leftLegTimeStamp, String rightLegTimeStamp, String directPressureTimeStamp, String elevTimeStamp) {

        this.combatID = combatID;
        this.mechOfInjury = mechOfInjury;
        this.tqTreatmentsList = tqTreatmentsList;
        this.tqTimeStampList = tqTimeStampList;
        this.treatmentsC = treatmentsC;
        this.treatmentsA = treatmentsA;
        this.treatmentsB = treatmentsB;
        this.treatmentsOther = treatmentsOther;
        this.injuryLocation = injuryLocation;
        this.fluidTreatment = fluidTreatment;
        this.bloodTreatment = bloodTreatment;
        this.andalgesicTreatment = andalgesicTreatment;
        this.antibioticTreatment = antibioticTreatment;
        this.leftArmTimeStamp = leftArmTimeStamp;
        this.rightArmTimeStamp = rightArmTimeStamp;
        this.leftLegTimeStamp = leftLegTimeStamp;
        this.rightLegTimeStamp = rightLegTimeStamp;
        this.directPressureTimeStamp = directPressureTimeStamp;
        this.elevTimeStamp = elevTimeStamp;
    }

    public int getCombatID() {
        return combatID;
    }

    public void setCombatID(int combatID) {
        this.combatID = combatID;
    }

    public List<String> getMechOfInjury() {
        return mechOfInjury;
    }

    public void setMechOfInjury(List<String> mechOfInjury) {
        this.mechOfInjury = mechOfInjury;
    }

    public List<String> getTqTreatmentsList() {
        return tqTreatmentsList;
    }

    public void setTqTreatmentsList(List<String> tqTreatmentsList) {
        this.tqTreatmentsList = tqTreatmentsList;
    }

    public List<String> getTqTimeStampList() {
        return tqTimeStampList;
    }

    public void setTqTimeStampList(List<String> tqTimeStampList) {
        this.tqTimeStampList = tqTimeStampList;
    }

    public List<String> getTreatmentsC() {
        return treatmentsC;
    }

    public void setTreatmentsC(List<String> treatmentsC) {
        this.treatmentsC = treatmentsC;
    }

    public List<String> getTreatmentsA() {
        return treatmentsA;
    }

    public void setTreatmentsA(List<String> treatmentsA) {
        this.treatmentsA = treatmentsA;
    }

    public List<String> getTreatmentsB() {
        return treatmentsB;
    }

    public void setTreatmentsB(List<String> treatmentsB) {
        this.treatmentsB = treatmentsB;
    }

    public List<String> getTreatmentsOther() {
        return treatmentsOther;
    }

    public void setTreatmentsOther(List<String> treatmentsOther) {
        this.treatmentsOther = treatmentsOther;
    }

    public List<String> getInjuryLocation() {
        return injuryLocation;
    }

    public void setInjuryLocation(List<String> injuryLocation) {
        this.injuryLocation = injuryLocation;
    }

    public List<String> getFluidTreatment() {
        return fluidTreatment;
    }

    public void setFluidTreatment(List<String> fluidTreatment) {
        this.fluidTreatment = fluidTreatment;
    }

    public List<String> getBloodTreatment() {
        return bloodTreatment;
    }

    public void setBloodTreatment(List<String> bloodTreatment) {
        this.bloodTreatment = bloodTreatment;
    }

    public List<String> getAnalgesicTreatment() {
        return andalgesicTreatment;
    }

    public void setAnalgesicTreatment(List<String> andalgesicTreatment) {
        this.andalgesicTreatment = andalgesicTreatment;
    }

    public List<String> getAntibioticTreatment() {
        return antibioticTreatment;
    }

    public void setAntibioticTreatment(List<String> antibioticTreatment) {
        this.antibioticTreatment = antibioticTreatment;
    }

    public String getLeftArmTimeStamp() {
        return leftArmTimeStamp;
    }

    public void setLeftArmTimeStamp(String leftArmTimeStamp) {
        this.leftArmTimeStamp = leftArmTimeStamp;
    }

    public String getRightArmTimeStamp() {
        return rightArmTimeStamp;
    }

    public void setRightArmTimeStamp(String rightArmTimeStamp) {
        this.rightArmTimeStamp = rightArmTimeStamp;
    }

    public String getLeftLegTimeStamp() {
        return leftLegTimeStamp;
    }

    public void setLeftLegTimeStamp(String leftLegTimeStamp) {
        this.leftLegTimeStamp = leftLegTimeStamp;
    }

    public String getRightLegTimeStamp() {
        return rightLegTimeStamp;
    }

    public void setRightLegTimeStamp(String rightLegTimeStamp) {
        this.rightLegTimeStamp = rightLegTimeStamp;
    }

    public String getDirectPressureTimeStamp() {
        return directPressureTimeStamp;
    }

    public void setDirectPressureTimeStamp(String directPressureTimeStamp) {
        this.directPressureTimeStamp = directPressureTimeStamp;
    }

    public String getElevTimeStamp() {
        return elevTimeStamp;
    }

    public void setElevTimeStamp(String elevTimeStamp) {
        this.elevTimeStamp = elevTimeStamp;
    }

    protected TreatmentInputs(Parcel in) {

        combatID = in.readInt();

        if (in.readByte() == 0x01) {
            mechOfInjury = new ArrayList<String>();
            in.readList(mechOfInjury, String.class.getClassLoader());
        } else {
            mechOfInjury = null;
        }
        if (in.readByte() == 0x01) {
            tqTreatmentsList = new ArrayList<String>();
            in.readList(tqTreatmentsList, Integer.class.getClassLoader());
        } else {
            tqTreatmentsList = null;
        }
        if (in.readByte() == 0x01) {
            tqTimeStampList = new ArrayList<String>();
            in.readList(tqTimeStampList, String.class.getClassLoader());
        } else {
            tqTimeStampList = null;
        }
        if (in.readByte() == 0x01) {
            treatmentsC = new ArrayList<String>();
            in.readList(treatmentsC, String.class.getClassLoader());
        } else {
            treatmentsC = null;
        }
        if (in.readByte() == 0x01) {
            treatmentsA = new ArrayList<String>();
            in.readList(treatmentsA, String.class.getClassLoader());
        } else {
            treatmentsA = null;
        }
        if (in.readByte() == 0x01) {
            treatmentsB = new ArrayList<String>();
            in.readList(treatmentsB, String.class.getClassLoader());
        } else {
            treatmentsB = null;
        }
        if (in.readByte() == 0x01) {
            treatmentsOther = new ArrayList<String>();
            in.readList(treatmentsOther, String.class.getClassLoader());
        } else {
            treatmentsOther = null;
        }
        if (in.readByte() == 0x01) {
            injuryLocation = new ArrayList<String>();
            in.readList(injuryLocation, String.class.getClassLoader());
        } else {
            injuryLocation = null;
        }
        if (in.readByte() == 0x01) {
            fluidTreatment = new ArrayList<String>();
            in.readList(fluidTreatment, String.class.getClassLoader());
        } else {
            fluidTreatment = null;
        }
        if (in.readByte() == 0x01) {
            bloodTreatment = new ArrayList<String>();
            in.readList(bloodTreatment, String.class.getClassLoader());
        } else {
            bloodTreatment = null;
        }
        if (in.readByte() == 0x01) {
            andalgesicTreatment = new ArrayList<String>();
            in.readList(andalgesicTreatment, String.class.getClassLoader());
        } else {
            andalgesicTreatment = null;
        }
        if (in.readByte() == 0x01) {
            antibioticTreatment = new ArrayList<String>();
            in.readList(antibioticTreatment, String.class.getClassLoader());
        } else {
            antibioticTreatment = null;
        }
        leftArmTimeStamp = in.readString();
        rightArmTimeStamp = in.readString();
        leftLegTimeStamp = in.readString();
        rightLegTimeStamp = in.readString();
        directPressureTimeStamp = in.readString();
        elevTimeStamp = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(combatID);
        if (mechOfInjury == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mechOfInjury);
        }
        if (tqTreatmentsList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tqTreatmentsList);
        }
        if (tqTimeStampList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tqTimeStampList);
        }
        if (treatmentsC == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(treatmentsC);
        }
        if (treatmentsA == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(treatmentsA);
        }
        if (treatmentsB == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(treatmentsB);
        }
        if (treatmentsOther == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(treatmentsOther);
        }
        if (injuryLocation == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(injuryLocation);
        }
        if (fluidTreatment == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(fluidTreatment);
        }
        if (bloodTreatment == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(bloodTreatment);
        }
        if (andalgesicTreatment == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(andalgesicTreatment);
        }
        if (antibioticTreatment == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(antibioticTreatment);
        }
        dest.writeString(leftArmTimeStamp);
        dest.writeString(rightArmTimeStamp);
        dest.writeString(leftLegTimeStamp);
        dest.writeString(rightLegTimeStamp);
        dest.writeString(directPressureTimeStamp);
        dest.writeString(elevTimeStamp);
    }

    @SuppressWarnings("unused")
    public static final Creator<TreatmentInputs> CREATOR = new Creator<TreatmentInputs>() {
        @Override
        public TreatmentInputs createFromParcel(Parcel in) {
            return new TreatmentInputs(in);
        }

        @Override
        public TreatmentInputs[] newArray(int size) {
            return new TreatmentInputs[size];
        }
    };
}
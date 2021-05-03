package com.atakmap.android.plugin.rain.pulse.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TreatmentsTimelineInputs implements Parcelable {


    String treatmentTimeStamp;
    String treatmentAttribute;

    public TreatmentsTimelineInputs() {
    }

    public TreatmentsTimelineInputs(String treatmentTimeStamp, String treatmentAttribute) {
        this.treatmentTimeStamp = treatmentTimeStamp;
        this.treatmentAttribute = treatmentAttribute;
    }

    public String getTreatmentTimeStamp() {
        return treatmentTimeStamp;
    }

    public void setTreatmentTimeStamp(String treatmentTimeStamp) {
        this.treatmentTimeStamp = treatmentTimeStamp;
    }

    public String getTreatmentAttribute() {
        return treatmentAttribute;
    }

    public void setTreatmentAttribute(String treatmentAttribute) {
        this.treatmentAttribute = treatmentAttribute;
    }

    protected TreatmentsTimelineInputs(Parcel in) {
        treatmentTimeStamp = in.readString();
        treatmentAttribute = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(treatmentTimeStamp);
        dest.writeString(treatmentAttribute);
    }

    @SuppressWarnings("unused")
    public static final Creator<TreatmentsTimelineInputs> CREATOR = new Creator<TreatmentsTimelineInputs>() {
        @Override
        public TreatmentsTimelineInputs createFromParcel(Parcel in) {
            return new TreatmentsTimelineInputs(in);
        }

        @Override
        public TreatmentsTimelineInputs[] newArray(int size) {
            return new TreatmentsTimelineInputs[size];
        }
    };
}

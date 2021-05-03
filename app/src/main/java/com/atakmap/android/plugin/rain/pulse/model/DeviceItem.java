package com.atakmap.android.plugin.rain.pulse.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DeviceItem implements Parcelable {


    String Name;
    String DeviceMacAddress;


    public DeviceItem() {
    }

    public DeviceItem(String name, String deviceMacAddress) {

        Name = name;
        DeviceMacAddress = deviceMacAddress;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDeviceMacAddress() {
        return DeviceMacAddress;
    }

    public void setDeviceMacAddress(String deviceMacAddress) {
        DeviceMacAddress = deviceMacAddress;
    }

    protected DeviceItem(Parcel in) {
        Name = in.readString();
        DeviceMacAddress = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Name);
        dest.writeString(DeviceMacAddress);
    }

    @SuppressWarnings("unused")
    public static final Creator<DeviceItem> CREATOR = new Creator<DeviceItem>() {
        @Override
        public DeviceItem createFromParcel(Parcel in) {
            return new DeviceItem(in);
        }

        @Override
        public DeviceItem[] newArray(int size) {
            return new DeviceItem[size];
        }
    };
}
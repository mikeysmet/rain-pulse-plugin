package com.atakmap.android.plugin.rain.pulse.service;

public interface HeartRateUpdateInterface {
    void onDataReceived(int heartRate,int heartRateVariability, int spo2, int respiration, int bodyBattery, int stress, long readingTimeStamp);
}

// IGarminDataListener.aidl
package com.atakmap.android.pulse.plugin;



interface IGarminDataListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
      void onDataReceived(in int heartRate, in int heartRateVariability,  in int spo2, in int respiration, in int bodyBattery, in int stress, in long readingTimeStamp );

}

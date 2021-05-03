// IGarminStatusListener.aidl
package com.atakmap.android.pulse.plugin;

// Declare any non-default types here with import statements

interface IGarminStatusListener {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onStatusUpdated(String state, String message);
}

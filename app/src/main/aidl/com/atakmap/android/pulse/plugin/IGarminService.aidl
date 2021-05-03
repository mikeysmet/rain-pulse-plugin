// IGarminService.aidl
package com.atakmap.android.pulse.plugin;

// Declare any non-default types here with import statements
import com.atakmap.android.pulse.plugin.IGarminDataListener;
import com.atakmap.android.pulse.plugin.IGarminStatusListener;
import com.atakmap.android.pulse.plugin.IGarminPairingInterface;

interface IGarminService {
     void unregisterDataInterface(IGarminDataListener listener);
     void registerDataInterface(IGarminDataListener listener);

     void unregisterStatusInterface(IGarminStatusListener listener);
     void registerStatusInterface(IGarminStatusListener listener);

     void unregisterPairingInterface(IGarminPairingInterface listener);
     void registerPairingInterface(IGarminPairingInterface listener);

     void enterPairingCredentials(in int pin);
}

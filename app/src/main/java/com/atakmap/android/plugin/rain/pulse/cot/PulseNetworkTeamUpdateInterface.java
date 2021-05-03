package com.atakmap.android.plugin.rain.pulse.cot;


import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;

public interface PulseNetworkTeamUpdateInterface {
    void onTeamUpdateReceived(TeamMemberInputs update);
    void onPatientUpdateReceived(TeamMemberInputs update);
}

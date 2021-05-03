package com.atakmap.android.plugin.rain.pulse.model;


public interface TeamMemberUpdateInterface {
    void onTeamMemberUpdated(TeamMemberInputs teamMemberInputs);
    void onTeamMemberAdded(TeamMemberInputs teamMemberInputs);
    void onPatientUpdated(TeamMemberInputs patient);
}

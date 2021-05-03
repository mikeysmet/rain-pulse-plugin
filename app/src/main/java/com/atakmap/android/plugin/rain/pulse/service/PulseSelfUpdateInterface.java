package com.atakmap.android.plugin.rain.pulse.service;


import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;

public interface PulseSelfUpdateInterface {
    void onCurrentUserUpdated(TeamMemberInputs user);
}

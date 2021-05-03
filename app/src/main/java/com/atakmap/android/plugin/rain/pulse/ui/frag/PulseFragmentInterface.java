package com.atakmap.android.plugin.rain.pulse.ui.frag;

import androidx.fragment.app.Fragment;

import com.atakmap.android.plugin.rain.pulse.model.TeamMemberInputs;

public interface PulseFragmentInterface {
    void showSelfFragment();
    void showHomeFragment();
    void showFragment(Fragment fragment);
    void showTraumaDialog(TeamMemberInputs teamMemberInputs);
}

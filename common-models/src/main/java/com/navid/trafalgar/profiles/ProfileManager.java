package com.navid.trafalgar.profiles;

import java.io.File;
import java.util.Collection;

public interface ProfileManager {

    Collection<ProfileStatus> listProfiles();

    void setActiveProfile(String email);

    File getHome();

    String getSessionId();

    ProfileStatus createProfile(String email);

    boolean isOnline();
}

package com.navid.trafalgar.profiles;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author casa
 */
public interface ProfileManager {

    Collection<ProfileStatus> listProfiles();

    void setActiveProfile(String email);

    File getHome();

    String getSessionId();

    ProfileStatus createProfile(String email);

    public boolean isOnline();
}

package com.navid.trafalgar.profiles;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

public interface ProfileManager {

    Collection<ProfileStatus> listProfiles();

    void setActiveProfile(String email);

    File getHome();

    String getSessionId();

    ProfileStatus createProfile(String email);

    Properties getProperties();

    void updateProperties(Map<String,String> p);

    boolean isOnline();
}

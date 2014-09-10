package com.navid.trafalgar.mod.counterclock.profile;

import com.google.common.base.Optional;

/**
 *
 * @author casa
 */
public class ProfileStatus {
     
    private Boolean verified;
    
    private Boolean online;
    
    private Optional<String> username;
    
    private String email;

    /**
     * @return the verified
     */
    public Boolean getVerified() {
        return verified;
    }

    /**
     * @param verified the verified to set
     */
    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    /**
     * @return the username
     */
    public Optional<String> getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(Optional<String> username) {
        this.username = username;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * @return the online
     */
    public Boolean getOnline() {
        return online;
    }

    /**
     * @param online the online to set
     */
    public void setOnline(Boolean online) {
        this.online = online;
    }
    
    
    
    @Override
    public String toString() {
        return getVerified()? (getUsername().get() + " (verified)") : (getEmail() + " (unverified)");
    }

    
    
    
}
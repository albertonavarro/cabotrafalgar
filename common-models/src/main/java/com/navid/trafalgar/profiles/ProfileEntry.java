package com.navid.trafalgar.profiles;

/**
 *
 * @author casa
 */
public class ProfileEntry {
    
    private String name;
    
    private String folderHome;
    
    private String token;
    
    private String email;
    
    public ProfileEntry() {
        
    }
    
    public ProfileEntry(String email, String folderName, String token) {
        this.email = email;
        this.folderHome = folderName;
        this.token = token;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the folderHome
     */
    public String getFolderHome() {
        return folderHome;
    }

    /**
     * @param folderHome the folderHome to set
     */
    public void setFolderHome(String folderHome) {
        this.folderHome = folderHome;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
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
    
    
    
}

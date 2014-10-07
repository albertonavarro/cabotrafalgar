package com.navid.trafalgar.mod.counterclock.profile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.lazylogin.client.user.v0.CreateTokenRequest;
import com.lazylogin.client.user.v0.CreateTokenResponse;
import com.lazylogin.client.user.v0.LoginWithTokenRequest;
import com.lazylogin.client.user.v0.LoginWithTokenResponse;
import com.lazylogin.client.user.v0.Token;
import com.lazylogin.client.user.v0.UserCommands;
import com.navid.lazylogin.context.RequestContextContainer;
import com.navid.trafalgar.modapi.GenericModRegisterer;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;

/**
 *
 * @author casa
 */
public class FileProfileManager implements ProfileManager {

    private ProfileInternal activeProfile;

    private Map<String, ProfileInternal> profiles = new HashMap<String, ProfileInternal>();

    @Resource(name = "mod.counterclock.clientUser")
    private UserCommands userCommandsClient;

    @Resource(name = "mod.counterclock.requestContextContainer")
    private RequestContextContainer container;
    
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    
    File trafalgarFolder;

    @Override
    public Collection<ProfileStatus> listProfiles() {
        return Collections2.transform(profiles.values(), INTERNAL_TO_STATUS);
    }

    @Override
    public void setActiveProfile(String email) {
        activeProfile = profiles.get(email);
    }

    @Override
    public File getHome() {
        return new File(activeProfile.getFolderHome());
    }

    @Override
    public String getSessionId() {
        if(activeProfile.getSessionId() != null){
            return activeProfile.getSessionId();
        } else if (activeProfile.getToken() != null) {
            LoginWithTokenResponse response = 
                    userCommandsClient.loginWithToken(new LoginWithTokenRequest(){{setToken(new Token(){{setToken(activeProfile.getToken());}});}});
            activeProfile.setSessionId(response.getResponse().getSessionid());
            return activeProfile.getSessionId();
        } else return null;
        
    }

    @Override
    public ProfileStatus createProfile(String email) {
        ProfileInternal selected;

        if (profiles.containsKey(email)) {
            selected = profiles.get(email);
        } else {
            Pair.TokenSession tokenSession = createToken(email);
            selected = new ProfileInternal();
            selected.setEmail(email);
            selected.setSessionId(tokenSession.session());
            selected.setToken(tokenSession.token());
            File newFolder = new File(trafalgarFolder, email);
            if(!newFolder.exists()){
                newFolder.mkdir();
            }
            selected.setFolderHome(newFolder.getAbsolutePath());
            profiles.put(email, selected);
        }

        saveFile();
        
        return INTERNAL_TO_STATUS.apply(selected);
    }
    

    
    private Pair.TokenSession createToken(String email) {
        CreateTokenRequest ctr = new CreateTokenRequest();
        ctr.setEmail(email);
        CreateTokenResponse response = userCommandsClient.createToken(ctr);
        return new AutoValue_Pair_TokenSession(response.getToken().getToken(), response.getSessionid().getSessionid());
    }
    
    
    private void loadFromFile(){     
        FileContent fileContent = null;
        String userHome = System.getProperty("user.home");
        File userHomeFile = new File(userHome);
        trafalgarFolder = new File(userHomeFile, ".cabotrafalgar");
        if (!trafalgarFolder.exists()) {
            trafalgarFolder.mkdir();
        }
        
        try {
            fileContent = mapper.readValue(new File(trafalgarFolder, "profileconfig.yml"), FileContent.class);
        } catch (IOException ex) {
            Logger.getLogger(GenericModRegisterer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(fileContent != null){
            for (ProfileEntry entry : fileContent.profiles) {
                profiles.put(entry.getEmail(), FILE_TO_INTERNAL.apply(entry) );
            }
        } else {
            profiles.put("DEFAULT", new ProfileInternal(){{
                setEmail("DEFAULT");
            }});
        }
    }
    
    private void saveFile() {
        try {
            FileContent fileContent = new FileContent();
            fileContent.profiles = Collections2.transform(profiles.values(), INTERNAL_TO_FILE);
            mapper.writeValue(new File("/home/casa/.cabotrafalgar/profileconfig.yml"), fileContent);
        } catch (IOException ex) {
            Logger.getLogger(FileProfileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String createSessionId(String token) {
        LoginWithTokenResponse response = userCommandsClient.loginWithToken(new LoginWithTokenRequest(){{setToken(new Token(){{setToken(token);}});}});
        return response.getResponse().getSessionid();
    }

    /**
     * @param userCommandsClient the userCommandsClient to set
     */
    public void setUserCommandsClient(UserCommands userCommandsClient) {
        this.userCommandsClient = userCommandsClient;
    }

    /**
     * @param container the container to set
     */
    public void setContainer(RequestContextContainer container) {
        this.container = container;
    }

    @Override
    public boolean isOnline() {
        return this.activeProfile.isOnline();
    }

    public static class FileContent {
        public Collection<ProfileEntry> profiles;
    }
    
    private final Function<ProfileInternal, ProfileStatus> INTERNAL_TO_STATUS
            = new Function<ProfileInternal, ProfileStatus>() {

                @Override
                public ProfileStatus apply(final ProfileInternal f) {
                    return new ProfileStatus() {
                        {
                            setEmail(f.getEmail());
                            setVerified(false);
                            setUsername(Optional.<String>absent());
                        }
                    };

                }
            };
    
     private final Function<ProfileInternal, ProfileEntry> INTERNAL_TO_FILE
            = new Function<ProfileInternal, ProfileEntry>() {

                @Override
                public ProfileEntry apply(final ProfileInternal f) {
                    return new ProfileEntry() {
                        {
                            setEmail(f.getEmail());
                            setFolderHome(f.getFolderHome());
                            setToken(f.getToken());
                        }
                    };

                }
            };
     
     private final Function<ProfileEntry, ProfileInternal> FILE_TO_INTERNAL
            = new Function<ProfileEntry, ProfileInternal>() {

                @Override
                public ProfileInternal apply(final ProfileEntry f) {
                    return new ProfileInternal() {
                        {
                            setEmail(f.getEmail());
                            setFolderHome(f.getFolderHome());
                            setToken(f.getToken());
                        }
                    };

                }
            };

    
}

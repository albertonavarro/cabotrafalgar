package com.navid.trafalgar.profiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Collections2;
import com.lazylogin.client.user.v0.CreateTokenRequest;
import com.lazylogin.client.user.v0.CreateTokenResponse;
import com.lazylogin.client.user.v0.GetInfoRequest;
import com.lazylogin.client.user.v0.GetInfoResponse;
import com.lazylogin.client.user.v0.LoginWithTokenRequest;
import com.lazylogin.client.user.v0.LoginWithTokenResponse;
import com.lazylogin.client.user.v0.Token;
import com.lazylogin.client.user.v0.UserCommands;
import com.navid.lazylogin.context.RequestContextContainer;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author casa
 */
public class FileProfileManager implements ProfileManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileProfileManager.class);

    private ProfileInternal activeProfile;

    private Map<String, ProfileInternal> profiles = new HashMap<String, ProfileInternal>();

    @Resource(name = "mod.counterclock.clientUser")
    private UserCommands userCommandsClient;

    @Resource(name = "mod.counterclock.requestContextContainer")
    private RequestContextContainer container;

    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

    File trafalgarFolder;

    File configFile;

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
        File file = new File(activeProfile.getFolderHome());
        if (!file.exists()) {
            file.mkdir();
        }

        return file;
    }

    @Override
    public String getSessionId() {
        return getSessionId(activeProfile);
    }

    private String getSessionId(ProfileInternal profile) {
        if (profile.getSessionId() != null) {
            return profile.getSessionId();
        } else if (profile.getToken() != null) {
            LoginWithTokenResponse response
                    = userCommandsClient.loginWithToken(
                            new LoginWithTokenRequest().withToken(
                                    new Token().withToken(profile.getToken())));
            profile.setSessionId(response.getResponse().getSessionid());
            return profile.getSessionId();
        } else {
            return null;
        }

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
            if (!newFolder.exists()) {
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

    private void initConfigFile() {

        String userHome = System.getProperty("user.home");
        File userHomeFile = new File(userHome);
        trafalgarFolder = new File(userHomeFile, ".cabotrafalgar");
        if (!trafalgarFolder.exists()) {
            trafalgarFolder.mkdir();
        }

        configFile = new File(trafalgarFolder, "profileconfig.yml");
    }

    private void loadFromFile() {
        FileContent fileContent = null;

        initConfigFile();

        if (configFile.exists()) {
            try {
                fileContent = mapper.readValue(new File(trafalgarFolder, "profileconfig.yml"), FileContent.class);
            } catch (IOException ex) {
                LOGGER.error("Error loading profileconfig.yml", ex);
            }
        }

        if (fileContent != null) {
            for (ProfileEntry entry : fileContent.profiles) {
                profiles.put(entry.getEmail(), FILE_TO_INTERNAL.apply(entry));
            }
        } else {
            profiles.put("DEFAULT", new ProfileInternal() {
                {
                    setEmail("DEFAULT");
                    File newFolder = new File(trafalgarFolder, getEmail());
                    if (!newFolder.exists()) {
                        newFolder.mkdir();
                    }
                    setFolderHome(newFolder.getAbsolutePath());
                }
            });
            saveFile();
        }

        setActiveProfile("DEFAULT");
    }

    private void saveFile() {
        try {
            FileContent fileContent = new FileContent();
            fileContent.profiles = Collections2.transform(profiles.values(), INTERNAL_TO_FILE);
            mapper.writeValue(configFile, fileContent);
        } catch (IOException ex) {
            LOGGER.error("Error saving {}", configFile, ex);
        }
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
                    return new ProfileStatus(f.getEmail(), f.getName() != null, Optional.fromNullable(f.getName()));
                }
            };

    private final Function<ProfileInternal, ProfileEntry> INTERNAL_TO_FILE
            = new Function<ProfileInternal, ProfileEntry>() {

                @Override
                public ProfileEntry apply(final ProfileInternal f) {
                    return new ProfileEntry(f.getEmail(), f.getFolderHome(), f.getToken());
                }
            };

    private final Function<ProfileEntry, ProfileInternal> FILE_TO_INTERNAL
            = new Function<ProfileEntry, ProfileInternal>() {

                @Override
                public ProfileInternal apply(final ProfileEntry f) {

                    ProfileInternal result = new ProfileInternal(f.getEmail(), f.getFolderHome(), f.getToken());

                    try {
                        String sessionId = getSessionId(result);
                        GetInfoResponse response = userCommandsClient.getInfo(new GetInfoRequest().withSessionid(sessionId));
                        result.setName(response.getName());
                    } catch (Exception e) {
                        LOGGER.error("Error loading info for {}", f.getEmail(), e);
                    }

                    return result;
                }
            };

}
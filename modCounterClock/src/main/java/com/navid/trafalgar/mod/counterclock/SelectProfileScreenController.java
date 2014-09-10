
package com.navid.trafalgar.mod.counterclock;

import static com.google.common.collect.Lists.newArrayList;
import com.navid.trafalgar.mod.counterclock.profile.ProfileManager;
import com.navid.trafalgar.mod.counterclock.profile.ProfileStatus;
import com.navid.trafalgar.model.AShipModel;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.screenflow.ScreenFlowManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author casa
 */
public class SelectProfileScreenController implements ScreenController {

    /**
     * From bind
     */
    private Nifty nifty;
    /**
     * From bind
     */
    private Screen screen;
    /**
     * Internal usage
     */
    private ProfileStatus selectedItem;
    /**
     * Singleton
     */
    @Autowired
    private GameConfiguration gameConfiguration;
    /**
     * Singleton
     */
    @Autowired
    private ProfileManager profileManager;
    /**
     * 
     */
    @Autowired
    private ScreenFlowManager screenFlowManager;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    /**
     * @param gameConfiguration the gameConfiguration to set
     */
    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    

    /**
     * @param screenFlowManager the screenFlowManager to set
     */
    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }

    

    @Override
    public void onStartScreen() {
        gameConfiguration.getPreGameModel().removeFromModel(AShipModel.class);
        
        fillListWithShips();
    }

    @Override
    public void onEndScreen() {
        emptyList();
    }

    private void fillListWithShips() {
        ListBox shipList = screen.findNiftyControl("profileList", ListBox.class);

        shipList.clear();
        shipList.addAllItems(newArrayList(profileManager.listProfiles()));
        
        selectedItem =  shipList.getItems().isEmpty()? null : (ProfileStatus) shipList.getItems().get(0);
    }

    private void emptyList() {
        ListBox shipList = screen.findNiftyControl("profileList", ListBox.class);

        shipList.clear();
    }

    /**
     * Subscribing to changes in the file list
     *
     * @param id
     * @param event
     */
    @NiftyEventSubscriber(id = "shipList")
    public void onShipChanged(final String id, final ListBoxSelectionChangedEvent<ProfileStatus> event) {
        selectedItem = event.getSelection().get(0);
    }

    public void goTo(String nextScreen) {
        profileManager.setActiveProfile(selectedItem.getEmail());

        nifty.gotoScreen(nextScreen);
    }

    public void next() {
        screenFlowManager.changeNextScreen();
        goTo("redirector");
    }

    public void back() {
        screenFlowManager.changePreviousScreen();
        nifty.gotoScreen("redirector");
    }
    
    public void add() {
        TextField newProfile = screen.findNiftyControl("newprofile", TextField.class);
        profileManager.createProfile(newProfile.getRealText());
        fillListWithShips();
    }

    /**
     * @param profileManager the profileManager to set
     */
    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }
    
}
  
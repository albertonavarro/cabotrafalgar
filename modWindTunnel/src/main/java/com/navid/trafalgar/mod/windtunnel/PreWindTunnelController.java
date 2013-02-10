package com.navid.trafalgar.mod.windtunnel;

import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import com.navid.trafalgar.model.GameConfiguration;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author alberto
 */
public class PreWindTunnelController implements ScreenController {

    private Nifty nifty;
    private Screen screen;
    private WindTunnelMain game;
    private final Application app;
    private final AppSettings settings;

    public PreWindTunnelController(Application app, AppSettings settings , WindTunnelMain windMain) {
        this.app = app;
        this.settings = settings;
        game = windMain;
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    
    
    private static class ListItem {
        
        private String name;
        
        private String picture;

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
         * @return the picture
         */
        public String getPicture() {
            return picture;
        }

        /**
         * @param picture the picture to set
         */
        public void setPicture(String picture) {
            this.picture = picture;
        }
        
        @Override
        public String toString(){
            return name;
        }
        
    }

    public void onStartScreen() {
        
        fillListWithShips();
        
    }

    public void onEndScreen() {
        
        emptyList();
        
    }
    
    private void fillListWithShips(){
        ListBox shipList = screen.findNiftyControl("shipList", ListBox.class);
        
        ListItem item1 = new ListItem();
        item1.setName("ModelZero");
        item1.setPicture("Effects/Explosion/flame.png");
        
        shipList.addItem(item1);
        
        ListItem item2 = new ListItem();
        item2.setName("ModelOne");
        item2.setPicture("Interfaces/Images/logo.gif");
        
        shipList.addItem(item2);
        
        selectedItem = item1;
    }
    
    private void emptyList() {
        ListBox shipList = screen.findNiftyControl("shipList", ListBox.class);
        
        shipList.clear();
    }
    
    @NiftyEventSubscriber(id = "shipList")
    public void onShipSelectionChanged(final String id, final ListBoxSelectionChangedEvent<ListItem> event) {
        selectedItem = event.getSelection().get(0);
    }
    
    ListItem selectedItem;

    public void goTo(String nextScreen) {
        
        GameConfiguration gc = new GameConfiguration();
        gc.setMap("Games/WindTunnel/WindTunnel.json");
        gc.setShowGhost(true);
        gc.setShipName(selectedItem.getName());
        game.setGameConfiguration(gc);
       
        nifty.gotoScreen(nextScreen);  // switch to another screen
    }
}

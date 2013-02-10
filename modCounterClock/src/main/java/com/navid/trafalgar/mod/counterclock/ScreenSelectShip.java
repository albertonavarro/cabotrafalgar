package com.navid.trafalgar.mod.counterclock;

import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.model.GameConfiguration;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class ScreenSelectShip implements ScreenController {

    /**
     * From bind
     */
    private Nifty nifty;
    
    /**
     * From bind
     */
    private Screen screen;
    
    /**
     * Singleton
     */
    @Autowired
    private GameConfiguration gameConfiguration;
    
    /**
     * Singleton
     */
    @Autowired
    private Builder2 builder;
    
    private ScreenSelectShip.ListItem selectedItem;

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
     * @param builder the builder to set
     */
    public void setBuilder(Builder2 builder) {
        this.builder = builder;
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
        public String toString() {
            return name;
        }
    }

    public void onStartScreen() {
        fillListWithShips();
    }

    public void onEndScreen() {
        emptyList();
    }

    private void fillListWithShips() {
        ListBox shipList = screen.findNiftyControl("shipList", ListBox.class);

        Collection<BuilderInterface> builders = builder.getBuilder(Builder2.Category.ship);

        for (BuilderInterface currentBuilder : builders) {
            com.navid.trafalgar.mod.counterclock.ScreenSelectShip.ListItem item1 = new com.navid.trafalgar.mod.counterclock.ScreenSelectShip.ListItem();
            item1.setName(currentBuilder.getType());
            shipList.addItem(item1);
        }
        
        selectedItem = (ListItem) shipList.getItems().get(0);
    }

    private void emptyList() {
        ListBox shipList = screen.findNiftyControl("shipList", ListBox.class);

        shipList.clear();
    }

    @NiftyEventSubscriber(id = "shipList")
    public void onShipChanged(final String id, final ListBoxSelectionChangedEvent<com.navid.trafalgar.mod.counterclock.ScreenSelectShip.ListItem> event) {
        selectedItem = event.getSelection().get(0);
    }

    public void goTo(String nextScreen) {
        gameConfiguration.setShipName(selectedItem.getName());

        nifty.gotoScreen(nextScreen); 
    }
}

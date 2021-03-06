package com.navid.trafalgar.mod.common;

import com.navid.trafalgar.model.*;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

public class SelectShipScreenController extends GameMenuController {

    /**
     * Internal usage
     */
    private SelectShipScreenController.ListItem selectedItem;
    /**
     * Singleton
     */
    @Autowired
    private GameConfiguration gameConfiguration;
    /**
     * Singleton
     */
    @Autowired
    private ModelBuilder builder;

    /**
     * @param gameConfiguration the gameConfiguration to set
     */
    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    /**
     * @param builder the builder to set
     */
    public void setBuilder(ModelBuilder builder) {
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

    @Override
    public void doOnStartScreen() {
        gameConfiguration.reset();

        fillListWithShips();
    }

    @Override
    public void doOnEndScreen() {
        gameConfiguration.setShipName(selectedItem.getName());

        emptyList();
    }

    private void fillListWithShips() {
        ListBox shipList = screen.findNiftyControl("shipList", ListBox.class);

        Collection<BuilderInterface> builders = builder.getBuilder(ModelBuilder.Category.ship);
        List<ListItem> listItems = newArrayList();

        for (BuilderInterface currentBuilder : builders) {
            ListItem item1 = new ListItem();
            item1.setName(currentBuilder.getType());
            listItems.add(item1);
        }

        Collections.sort(listItems, new Comparator<ListItem>() {
            @Override
            public int compare(ListItem o1, ListItem o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        shipList.addAllItems(listItems);

        selectedItem = (ListItem) shipList.getItems().get(0);
    }

    private void emptyList() {
        ListBox shipList = screen.findNiftyControl("shipList", ListBox.class);

        shipList.clear();
    }

    /**
     * Subscribing to changes in the file list
     *
     * @param id
     * @param event
     */
    @NiftyEventSubscriber(id = "shipList")
    public void onShipChanged(final String id, final ListBoxSelectionChangedEvent<ListItem> event) {
        if(!event.getSelection().isEmpty()){
            selectedItem = event.getSelection().get(0);
        } else {
            selectedItem = null;
        }
    }


}

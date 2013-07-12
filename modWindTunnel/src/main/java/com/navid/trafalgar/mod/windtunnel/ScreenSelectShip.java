package com.navid.trafalgar.mod.windtunnel;

import com.google.common.collect.HashMultimap;
import com.jme3.input.KeyInput;
import com.navid.trafalgar.definition2.Entry;
import com.navid.trafalgar.input.Command;
import com.navid.trafalgar.input.CommandGenerator;
import com.navid.trafalgar.input.CommandStateListener;
import com.navid.trafalgar.input.GeneratorBuilder;
import com.navid.trafalgar.input.KeyboardCommandStateListener;
import com.navid.trafalgar.model.AShipModel;
import com.navid.trafalgar.model.AShipModelTwo;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    
    @Autowired
    private GeneratorBuilder generatorBuilder;
    private ScreenSelectShip.ListItem selectedItem;

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
     * @param builder the builder to set
     */
    public void setBuilder(Builder2 builder) {
        this.builder = builder;
    }

    /**
     * @param generatorBuilder the generatorBuilder to set
     */
    public void setGeneratorBuilder(GeneratorBuilder generatorBuilder) {
        this.generatorBuilder = generatorBuilder;
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
    public void onStartScreen() {
        fillListWithShips();
    }

    @Override
    public void onEndScreen() {
        emptyList();
    }

    private void fillListWithShips() {
        ListBox shipList = screen.findNiftyControl("shipList", ListBox.class);

        Collection<BuilderInterface> builders = builder.getBuilder(Builder2.Category.ship);

        for (BuilderInterface currentBuilder : builders) {
            ListItem item1 = new ListItem();
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
    public void onShipChanged(final String id, final ListBoxSelectionChangedEvent<ListItem> event) {
        selectedItem = event.getSelection().get(0);
    }

    public void goTo(String nextScreen) {
        gameConfiguration.setShipName(selectedItem.getName());
        
        Collection c = builder.build(new Entry(){{
            setType(selectedItem.getName());
            setName("player1");
            setValues(new HashMap<String, String>());
        }});
        
        AShipModelTwo s = (AShipModelTwo) c.iterator().next(); 
        
        Set<Command> commands = s.getCommands();
        
        HashMultimap<Command, CommandGenerator> gens = generatorBuilder.getGeneratorsFor(commands);
        
        Set<CommandStateListener> commandListeners = new HashSet<CommandStateListener>();
        for(Map.Entry<Command, CommandGenerator> currentEntry : gens.entries()){
            CommandStateListener commandStateListener = currentEntry.getValue().generateCommandStateListener(currentEntry.getKey());
            commandListeners.add(commandStateListener);
            
        }
        
        gameConfiguration.getPreGameModel().addToModel(Collections.singleton(s));
        
        gameConfiguration.getPreGameModel().addToModel(commandListeners);
        
        int key0 = KeyInput.KEY_A;
        List<KeyboardCommandStateListener> keyboards = gameConfiguration.getPreGameModel().getByType(KeyboardCommandStateListener.class);
        for(KeyboardCommandStateListener currentkey : keyboards){
            currentkey.setKeycode(key0++);
        }
        
        nifty.gotoScreen(nextScreen);
    }
    
    
}

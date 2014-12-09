/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.mod.common;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jme3.input.KeyInput;
import com.navid.trafalgar.input.GeneratorBuilder;
import com.navid.trafalgar.input.KeyboardCommandStateListener;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.profiles.ProfileManager;
import com.navid.trafalgar.screenflow.ScreenFlowManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bushe.swing.event.EventTopicSubscriber;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class SelectKeyboardControlsScreenController implements ScreenController {

    @Autowired
    private ScreenFlowManager screenFlowManager;

    @Autowired
    private ProfileManager profileManager;

    private static final Map<Integer, String> reverseMap = new HashMap<Integer, String>();

    static {
        reverseMap.put(KeyInput.KEY_A, "A");
        reverseMap.put(KeyInput.KEY_S, "S");
        reverseMap.put(KeyInput.KEY_D, "D");
        reverseMap.put(KeyInput.KEY_F, "F");
    }
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
     *
     */
    @Autowired
    private GeneratorBuilder generatorBuilder;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {

        File keyboardHistory = new File(profileManager.getHome(), "keyboardHistory.properties");
        final Properties properties = new Properties();

        if (keyboardHistory.exists()) {
            try {
                properties.load(new FileReader(keyboardHistory));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SelectKeyboardControlsScreenController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SelectKeyboardControlsScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        EventTopicSubscriber<ListBoxSelectionChangedEvent> eventHandler;

        final Map<String, KeyboardCommandStateListener> keyListeners
                = Maps.uniqueIndex(
                        gameConfiguration.getPreGameModel().getByType(KeyboardCommandStateListener.class),
                        new Function<KeyboardCommandStateListener, String>() {
                            @Override
                            public String apply(KeyboardCommandStateListener input) {
                                return input.toString();
                            }
                        });

        eventHandler = new EventTopicSubscriber<ListBoxSelectionChangedEvent>() {
            @Override
            public void onEvent(String string, ListBoxSelectionChangedEvent t) {
                keyListeners.get(string).setKeycode(((ListItem) t.getSelection().get(0)).getValue());
            }
        };

        for (final Entry<String, KeyboardCommandStateListener> currentListener : keyListeners.entrySet()) {
            ListBox listBoxController = screen.findNiftyControl(currentListener.getKey(), ListBox.class);

            List<ListItem> itemList = generateKeys();
            listBoxController.addAllItems(itemList);

            if (properties.containsKey(currentListener.getKey())) {
                int index = Iterables.indexOf(itemList, new Predicate<ListItem>() {

                    @Override
                    public boolean apply(ListItem t) {
                        return t.value == Integer.parseInt((String) properties.get(currentListener.getKey()));
                    }
                });

                listBoxController.selectItemByIndex(index);
                currentListener.getValue().setKeycode(((ListItem) listBoxController.getSelection().get(0)).getValue());

            } else {
                listBoxController.selectItemByIndex(0);
                currentListener.getValue().setKeycode(((ListItem) listBoxController.getSelection().get(0)).getValue());
            }

            nifty.subscribe(screen, currentListener.getKey(), ListBoxSelectionChangedEvent.class, eventHandler);
        }

    }

    private List<ListItem> generateKeys() {
        List<ListItem> listResult = Lists.newArrayList();
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_A), KeyInput.KEY_A));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_S), KeyInput.KEY_S));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_D), KeyInput.KEY_D));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_F), KeyInput.KEY_F));

        return listResult;
    }

    @Override
    public void onEndScreen() {

        try {
            File keyboardHistory = new File(profileManager.getHome(), "keyboardHistory.properties");

            Properties properties = new Properties();

            List<KeyboardCommandStateListener> listeners = gameConfiguration.getPreGameModel().getByType(KeyboardCommandStateListener.class);

            for (KeyboardCommandStateListener listener : listeners) {
                properties.put(listener.toString(), Integer.toString(listener.getKeycode()));
            }

            properties.store(new FileWriter(keyboardHistory), "User commands");
        } catch (IOException ex) {
            Logger.getLogger(SelectKeyboardControlsScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void goTo(String nextScreen) {
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

    /**
     * @param screenFlowManager the screenFlowManager to set
     */
    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }

    /**
     * @param gameConfiguration the gameConfiguration to set
     */
    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    /**
     * @param generatorBuilder the generatorBuilder to set
     */
    public void setGeneratorBuilder(GeneratorBuilder generatorBuilder) {
        this.generatorBuilder = generatorBuilder;
    }

    private void setDefaults() {

    }

    /**
     * @param profileManager the profileManager to set
     */
    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    private static class ListItem {

        private String keyName;
        private int value;

        public ListItem(String keyName, int value) {
            this.keyName = keyName;
            this.value = value;
        }

        @Override
        public String toString() {
            return keyName;
        }

        /**
         * @return the keyName
         */
        public String getKeyName() {
            return keyName;
        }

        /**
         * @param keyName the keyName to set
         */
        public void setKeyName(String keyName) {
            this.keyName = keyName;
        }

        /**
         * @return the value
         */
        public int getValue() {
            return value;
        }

        /**
         * @param value the value to set
         */
        public void setValue(int value) {
            this.value = value;
        }
    }

}

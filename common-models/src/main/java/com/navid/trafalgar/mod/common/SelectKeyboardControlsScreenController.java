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
        reverseMap.put(KeyInput.KEY_0, "0");
        reverseMap.put(KeyInput.KEY_1, "1");
        reverseMap.put(KeyInput.KEY_2, "2");
        reverseMap.put(KeyInput.KEY_3, "3");
        reverseMap.put(KeyInput.KEY_4, "4");
        reverseMap.put(KeyInput.KEY_5, "5");
        reverseMap.put(KeyInput.KEY_6, "6");
        reverseMap.put(KeyInput.KEY_7, "7");
        reverseMap.put(KeyInput.KEY_8, "8");
        reverseMap.put(KeyInput.KEY_9, "9");
        reverseMap.put(KeyInput.KEY_A, "A");
        reverseMap.put(KeyInput.KEY_B, "B");
        reverseMap.put(KeyInput.KEY_C, "C");
        reverseMap.put(KeyInput.KEY_D, "D");
        reverseMap.put(KeyInput.KEY_E, "E");
        reverseMap.put(KeyInput.KEY_F, "F");
        reverseMap.put(KeyInput.KEY_G, "G");
        reverseMap.put(KeyInput.KEY_H, "H");
        reverseMap.put(KeyInput.KEY_I, "I");
        reverseMap.put(KeyInput.KEY_J, "J");
        reverseMap.put(KeyInput.KEY_K, "K");
        reverseMap.put(KeyInput.KEY_L, "L");
        reverseMap.put(KeyInput.KEY_M, "M");
        reverseMap.put(KeyInput.KEY_N, "N");
        reverseMap.put(KeyInput.KEY_O, "O");
        reverseMap.put(KeyInput.KEY_P, "P");
        reverseMap.put(KeyInput.KEY_Q, "Q");
        reverseMap.put(KeyInput.KEY_R, "R");
        reverseMap.put(KeyInput.KEY_S, "S");
        reverseMap.put(KeyInput.KEY_T, "T");
        reverseMap.put(KeyInput.KEY_U, "U");
        reverseMap.put(KeyInput.KEY_V, "V");
        reverseMap.put(KeyInput.KEY_W, "W");
        reverseMap.put(KeyInput.KEY_X, "X");
        reverseMap.put(KeyInput.KEY_Y, "Y");
        reverseMap.put(KeyInput.KEY_Z, "Z");
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
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_0), KeyInput.KEY_0));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_1), KeyInput.KEY_1));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_2), KeyInput.KEY_2));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_3), KeyInput.KEY_3));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_4), KeyInput.KEY_4));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_5), KeyInput.KEY_5));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_6), KeyInput.KEY_6));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_7), KeyInput.KEY_7));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_8), KeyInput.KEY_8));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_9), KeyInput.KEY_9));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_A), KeyInput.KEY_A));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_B), KeyInput.KEY_B));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_C), KeyInput.KEY_C));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_D), KeyInput.KEY_D));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_E), KeyInput.KEY_E));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_F), KeyInput.KEY_F));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_G), KeyInput.KEY_G));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_H), KeyInput.KEY_H));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_I), KeyInput.KEY_I));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_J), KeyInput.KEY_J));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_K), KeyInput.KEY_K));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_L), KeyInput.KEY_L));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_M), KeyInput.KEY_M));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_N), KeyInput.KEY_N));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_O), KeyInput.KEY_O));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_P), KeyInput.KEY_P));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_Q), KeyInput.KEY_Q));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_R), KeyInput.KEY_R));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_S), KeyInput.KEY_S));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_T), KeyInput.KEY_T));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_U), KeyInput.KEY_U));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_V), KeyInput.KEY_V));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_W), KeyInput.KEY_W));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_X), KeyInput.KEY_X));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_Y), KeyInput.KEY_Y));
        listResult.add(new ListItem(reverseMap.get(KeyInput.KEY_Z), KeyInput.KEY_Z));
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

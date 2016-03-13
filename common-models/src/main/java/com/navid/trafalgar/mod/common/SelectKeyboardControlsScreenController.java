package com.navid.trafalgar.mod.common;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jme3.input.KeyInput;
import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.trafalgar.input.GeneratorBuilder;
import com.navid.trafalgar.input.KeyboardCommandStateListener;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.profiles.ProfileManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import org.bushe.swing.event.EventTopicSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public final class SelectKeyboardControlsScreenController implements ScreenController {

    private static final Logger LOG
            = LoggerFactory.getLogger(SelectKeyboardControlsScreenController.class);

    @Autowired
    private ScreenFlowManager screenFlowManager;

    @Autowired
    private ProfileManager profileManager;

    private static final Map<Integer, String> REVERSE_MAP = new HashMap<Integer, String>();

    static {
        REVERSE_MAP.put(KeyInput.KEY_0, "0");
        //REVERSE_MAP.put(KeyInput.KEY_1, "1");
        //REVERSE_MAP.put(KeyInput.KEY_2, "2");
        //REVERSE_MAP.put(KeyInput.KEY_3, "3");
        REVERSE_MAP.put(KeyInput.KEY_4, "4");
        REVERSE_MAP.put(KeyInput.KEY_5, "5");
        REVERSE_MAP.put(KeyInput.KEY_6, "6");
        REVERSE_MAP.put(KeyInput.KEY_7, "7");
        REVERSE_MAP.put(KeyInput.KEY_8, "8");
        REVERSE_MAP.put(KeyInput.KEY_9, "9");
        //REVERSE_MAP.put(KeyInput.KEY_A, "A");
        REVERSE_MAP.put(KeyInput.KEY_B, "B");
        REVERSE_MAP.put(KeyInput.KEY_C, "C");
        //REVERSE_MAP.put(KeyInput.KEY_D, "D");
        REVERSE_MAP.put(KeyInput.KEY_E, "E");
        REVERSE_MAP.put(KeyInput.KEY_F, "F");
        REVERSE_MAP.put(KeyInput.KEY_G, "G");
        REVERSE_MAP.put(KeyInput.KEY_H, "H");
        REVERSE_MAP.put(KeyInput.KEY_I, "I");
        REVERSE_MAP.put(KeyInput.KEY_J, "J");
        REVERSE_MAP.put(KeyInput.KEY_K, "K");
        REVERSE_MAP.put(KeyInput.KEY_L, "L");
        REVERSE_MAP.put(KeyInput.KEY_M, "M");
        REVERSE_MAP.put(KeyInput.KEY_N, "N");
        REVERSE_MAP.put(KeyInput.KEY_O, "O");
        REVERSE_MAP.put(KeyInput.KEY_P, "P");
        REVERSE_MAP.put(KeyInput.KEY_Q, "Q");
        REVERSE_MAP.put(KeyInput.KEY_R, "R");
        //REVERSE_MAP.put(KeyInput.KEY_S, "S");
        REVERSE_MAP.put(KeyInput.KEY_T, "T");
        REVERSE_MAP.put(KeyInput.KEY_U, "U");
        REVERSE_MAP.put(KeyInput.KEY_V, "V");
        //REVERSE_MAP.put(KeyInput.KEY_W, "W");
        REVERSE_MAP.put(KeyInput.KEY_X, "X");
        REVERSE_MAP.put(KeyInput.KEY_Y, "Y");
        REVERSE_MAP.put(KeyInput.KEY_Z, "Z");
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
        final Properties userProperties = profileManager.getProperties();

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

        if(keyListeners.isEmpty()){
            screenFlowManager.setNextScreenHint(ScreenFlowManager.SKIP);
            nifty.gotoScreen("redirector");
        }

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

            if (userProperties.containsKey(currentListener.getKey())) {
                int index = Iterables.indexOf(itemList, new Predicate<ListItem>() {

                    @Override
                    public boolean apply(ListItem t) {
                        return t.value == Integer.parseInt((String) userProperties.get(currentListener.getKey()));
                    }
                });

                //possible if there's an invalid key from history file
                if (index == -1) {
                    index = 0;
                }

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
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_0), KeyInput.KEY_0));
        //listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_1), KeyInput.KEY_1));
        //listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_2), KeyInput.KEY_2));
        //listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_3), KeyInput.KEY_3));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_4), KeyInput.KEY_4));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_5), KeyInput.KEY_5));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_6), KeyInput.KEY_6));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_7), KeyInput.KEY_7));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_8), KeyInput.KEY_8));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_9), KeyInput.KEY_9));
        //listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_A), KeyInput.KEY_A));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_B), KeyInput.KEY_B));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_C), KeyInput.KEY_C));
        //listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_D), KeyInput.KEY_D));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_E), KeyInput.KEY_E));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_F), KeyInput.KEY_F));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_G), KeyInput.KEY_G));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_H), KeyInput.KEY_H));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_I), KeyInput.KEY_I));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_J), KeyInput.KEY_J));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_K), KeyInput.KEY_K));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_L), KeyInput.KEY_L));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_M), KeyInput.KEY_M));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_N), KeyInput.KEY_N));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_O), KeyInput.KEY_O));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_P), KeyInput.KEY_P));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_Q), KeyInput.KEY_Q));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_R), KeyInput.KEY_R));
        //listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_S), KeyInput.KEY_S));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_T), KeyInput.KEY_T));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_U), KeyInput.KEY_U));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_V), KeyInput.KEY_V));
        //listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_W), KeyInput.KEY_W));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_X), KeyInput.KEY_X));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_Y), KeyInput.KEY_Y));
        listResult.add(new ListItem(REVERSE_MAP.get(KeyInput.KEY_Z), KeyInput.KEY_Z));
        return listResult;
    }

    @Override
    public void onEndScreen() {
        Map<String, String> userProperties = new HashMap<String, String>();

        for (KeyboardCommandStateListener listener : gameConfiguration.getPreGameModel().getByType(KeyboardCommandStateListener.class)) {
            userProperties.put(listener.toString(), Integer.toString(listener.getKeycode()));
        }

        profileManager.updateProperties(userProperties);
    }

    public void goTo(String nextScreen) {
        nifty.gotoScreen(nextScreen);
    }

    public void next() {
        if (validateKeys()) {
            screenFlowManager.setNextScreenHint(ScreenFlowManager.NEXT);
            goTo("redirector");
        } else {
            showMenu();
        }
    }

    public void showMenu() {
        Label label = screen.findNiftyControl("RepeatError", Label.class);
        label.setText("You cannot use the same key for more than one command");
    }

    private boolean validateKeys() {
        List<KeyboardCommandStateListener> commands
                = gameConfiguration.getPreGameModel().getByType(KeyboardCommandStateListener.class);
        Set<Integer> commandsByKey = new HashSet<Integer>();
        for (KeyboardCommandStateListener command : commands) {
            if (!commandsByKey.add(command.getKeycode())) {
                return false;
            }
        }
        return true;
    }

    public void back() {
        screenFlowManager.setNextScreenHint(ScreenFlowManager.PREV);
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

        public String getKeyName() {
            return keyName;
        }

        public void setKeyName(String keyName) {
            this.keyName = keyName;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return keyName;
        }
    }

}

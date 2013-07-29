/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.mod.windtunnel;

import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.navid.trafalgar.input.GeneratorBuilder;
import com.navid.trafalgar.input.KeyboardCommandStateListener;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.screenflow.ScreenFlowManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.Collection;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class SelectKeyboardControlsScreenController implements ScreenController {
    
    @Autowired
    private ScreenFlowManager screenFlowManager;

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

        Collection<KeyboardCommandStateListener> keyListeners = gameConfiguration.getPreGameModel().getByType(KeyboardCommandStateListener.class);
        
        for(KeyboardCommandStateListener currentListener : keyListeners){
            ListBox listBoxController = screen.findNiftyControl(currentListener.toString(), ListBox.class);
            listBoxController.addItem(new ListItem("A", KeyInput.KEY_A));
            listBoxController.addItem(new ListItem("S", KeyInput.KEY_S));
            listBoxController.addItem(new ListItem("D", KeyInput.KEY_D));
            listBoxController.addItem(new ListItem("F", KeyInput.KEY_F));
        }
    }

    @Override
    public void onEndScreen() {

    }
    
    public void goTo(String nextScreen) {
        
        
        nifty.gotoScreen(nextScreen);
    }
    
    public void next(){
        screenFlowManager.changeNextScreen();
        goTo("redirector");
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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.mod.windtunnel;

import com.google.common.collect.HashMultimap;
import com.navid.trafalgar.input.Command;
import com.navid.trafalgar.input.CommandGenerator;
import com.navid.trafalgar.input.CommandStateListener;
import com.navid.trafalgar.input.GeneratorBuilder;
import com.navid.trafalgar.model.AShipModelTwo;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.screenflow.ScreenFlowManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.RadioButtonGroup;
import de.lessvoid.nifty.controls.radiobutton.RadioButtonControl;
import de.lessvoid.nifty.controls.radiobutton.RadioButtonGroupControl;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class SelectControlsScreenController implements ScreenController {

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
    
    private HashMultimap<Command, CommandGenerator> generatedCommands;
    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        AShipModelTwo ship = gameConfiguration.getPreGameModel().getSingleByType(AShipModelTwo.class);

        Set<Command> commands = ship.getCommands();

        generatedCommands = generatorBuilder.getGeneratorsFor(commands);
    }

    @Override
    public void onEndScreen() {

    }
    
    public void previous(){
        
    }
    
    public void goTo(String nextScreen) {
        Set<CommandStateListener> commandListeners = new HashSet<CommandStateListener>();
        
        for(Command currentCommand : generatedCommands.keySet()){
            for(CommandGenerator currentGenerator : generatedCommands.get(currentCommand)){
                RadioButtonControl radioControl = screen.findControl(currentGenerator.toString(), RadioButtonControl.class);
                if (radioControl.isActivated()){
                    CommandStateListener commandStateListener = currentGenerator.generateCommandStateListener(currentCommand);
                    commandListeners.add(commandStateListener);
                }
            }
        }
        
        gameConfiguration.getPreGameModel().addToModel(commandListeners);
        
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
    
    
}

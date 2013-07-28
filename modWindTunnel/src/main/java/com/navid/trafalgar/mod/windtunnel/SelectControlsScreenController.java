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
import de.lessvoid.nifty.controls.RadioButtonStateChangedEvent;
import de.lessvoid.nifty.controls.radiobutton.RadioButtonControl;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bushe.swing.event.EventTopicSubscriber;
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
    private EventTopicSubscriber<RadioButtonStateChangedEvent> eventHandler2;
    private Map<String, String> generated = new HashMap<String, String>();

    @Override
    public void onStartScreen() {

        eventHandler2 = new EventTopicSubscriber<RadioButtonStateChangedEvent>() {
            @Override
            public void onEvent(String string, RadioButtonStateChangedEvent t) {
                if (t.isSelected()) {
                    generated.put(t.getRadioButton().getGroup().getId(), string);
                }
            }
        };

        AShipModelTwo ship = gameConfiguration.getPreGameModel().getSingleByType(AShipModelTwo.class);
        Set<Command> commands = ship.getCommands();
        HashMultimap<Command, CommandGenerator> gens = generatorBuilder.getGeneratorsFor(commands);

        for (final Command currentCommand : gens.keySet()) {
            for (final CommandGenerator currentGenerator : gens.get(currentCommand)) {
                nifty.subscribe(screen, currentGenerator.toString(), RadioButtonStateChangedEvent.class, eventHandler2);
                
                RadioButtonControl radioControl = screen.findControl(currentGenerator.toString(), RadioButtonControl.class);
                if (radioControl.isActivated()) {
                    generated.put(currentCommand.toString(), currentGenerator.toString());
                }
            }
        }
    }

    @Override
    public void onEndScreen() {
    }

    public void previous() {
    }

    public void goTo(String nextScreen) {
        Set<CommandStateListener> commandListeners = new HashSet<CommandStateListener>();
**
        gameConfiguration.getPreGameModel().addToModel(commandListeners);

        nifty.gotoScreen(nextScreen);
    }

    public void next() {
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

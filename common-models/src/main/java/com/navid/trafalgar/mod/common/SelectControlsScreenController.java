package com.navid.trafalgar.mod.common;

import com.google.common.base.Function;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.trafalgar.input.*;
import com.navid.trafalgar.model.AShipModel;
import com.navid.trafalgar.model.AShipModelInteractive;
import com.navid.trafalgar.model.GameConfiguration;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.RadioButtonStateChangedEvent;
import de.lessvoid.nifty.controls.radiobutton.RadioButtonControl;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

import java.util.*;

import org.bushe.swing.event.EventTopicSubscriber;
import org.springframework.beans.factory.annotation.Autowired;

import static com.google.common.collect.Lists.newArrayList;

public final class SelectControlsScreenController extends GameMenuController {

    /**
     * Singleton
     */
    @Autowired
    private GameConfiguration gameConfiguration;
    /**
     * Singleton
     */
    @Autowired
    private GeneratorBuilder generatorBuilder;
    /**
     * Singleton
     */
    @Autowired
    private CommandBuilder commandBuilder;

    
    private EventTopicSubscriber<RadioButtonStateChangedEvent> eventHandler;
    private final Map<String, String> generated = new HashMap<String, String>();
    private Map<String, Command> commandsMap;
    private Map<String, CommandGenerator> generatorMap;

    @Override
    public void doOnStartScreen() {
        generated.clear();

        gameConfiguration.getPreGameModel().removeFromModel(CommandStateListener.class);
        eventHandler = new EventTopicSubscriber<RadioButtonStateChangedEvent>() {
            @Override
            public void onEvent(String string, RadioButtonStateChangedEvent t) {
                if (t.isSelected()) {
                    generated.put(t.getRadioButton().getGroup().getId(), string);
                }
            }
        };


        List<AShipModelInteractive> interactives = gameConfiguration.getPreGameModel().getByType(AShipModelInteractive.class);


        Set<Command> commands = new HashSet<>();//ship.getCommands(commandBuilder);
        for (AShipModelInteractive interactive : interactives) {
            commands.addAll(interactive.getCommands(commandBuilder));
        }

        commandsMap = Maps.uniqueIndex(commands, new Function<Command, String>() {
            @Override
            public String apply(Command input) {
                return input.toString();
            }
        });

        generatorMap = generatorBuilder.getGenerators();

        Multimap<Command, CommandGenerator> gens = generatorBuilder.getGeneratorsFor(commands);

        for (final Command currentCommand : gens.keySet()) {
            for (final CommandGenerator currentGenerator : gens.get(currentCommand)) {
                nifty.subscribe(screen, currentGenerator.toString(), RadioButtonStateChangedEvent.class, eventHandler);

                RadioButtonControl radioControl = screen.findControl(currentGenerator.toString(), RadioButtonControl.class);
                if (radioControl.isActivated()) {
                    generated.put(currentCommand.toString(), currentGenerator.toString());
                }
            }
        }

    }

    @Override
    public void onEndScreen() {
        Map<Command, CommandGenerator> assignments = new HashMap<Command, CommandGenerator>();

        for (Map.Entry<String, String> entry : generated.entrySet()) {
            assignments.put(commandsMap.get(entry.getKey()), generatorMap.get(entry.getValue()));
        }

        Set<CommandStateListener> listeners = generatorBuilder.generateControllers(assignments);

        gameConfiguration.getPreGameModel().addToModel(listeners);
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

    public void setCommandBuilder(CommandBuilder commandBuilder) {
        this.commandBuilder = commandBuilder;
    }
}

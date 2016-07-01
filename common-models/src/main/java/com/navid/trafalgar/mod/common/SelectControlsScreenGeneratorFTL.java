package com.navid.trafalgar.mod.common;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.navid.nifty.flow.template.ftl.FtlTemplateGenerator;
import com.navid.trafalgar.input.Command;
import com.navid.trafalgar.input.CommandBuilder;
import com.navid.trafalgar.input.CommandGenerator;
import com.navid.trafalgar.input.GeneratorBuilder;
import com.navid.trafalgar.model.AShipModelInteractive;
import com.navid.trafalgar.model.GameConfiguration;
import de.lessvoid.nifty.Nifty;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by alberto on 9/2/15.
 */
public class SelectControlsScreenGeneratorFTL extends FtlTemplateGenerator {

    /**
     * Singleton
     */
    @Autowired
    private GameConfiguration gameConfiguration;

    @Autowired
    private GeneratorBuilder generatorBuilder;

    @Autowired
    private SelectControlsScreenController screenControlScreenController;
    /**
     * Singleton
     */
    @Autowired
    private CommandBuilder commandBuilder;


    public SelectControlsScreenGeneratorFTL(Nifty nifty) throws IOException {
        super(nifty, "/mod/common/interface_selectcontrols.xml");
    }

    @Override
    protected Map injectProperties() {
        HashMap properties = new HashMap();

        AShipModelInteractive ship = gameConfiguration.getPreGameModel().getSingleByType(AShipModelInteractive.class);
        Multimap<Command, CommandGenerator> generatorsForCommands = generatorBuilder.getGeneratorsFor(ship.getCommands(commandBuilder));

        //getting a map of maps
        Map<String, Map<String, Boolean>> mapOfMapsOfGenerators = generateMapOfMaps(generatorsForCommands);

        //sorting commands in alphabetical order of command name
        List<Command> sortedCommands = newArrayList(generatorsForCommands.keySet());
        Collections.sort(sortedCommands, new Comparator<Command>() {
            @Override
            public int compare(Command o1, Command o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        //getting all different generators
        Set<CommandGenerator> generators = new HashSet<CommandGenerator>();
        for(CommandGenerator generator : generatorsForCommands.values()) {
            generators.add(generator);
        }
        List<CommandGenerator> sortedGenerators = newArrayList(generators);
        Collections.sort(sortedGenerators, new Comparator<CommandGenerator>() {
            @Override
            public int compare(CommandGenerator one, CommandGenerator other) {
                return one.toString().compareTo(other.toString());
            }
        });

        properties.put("sortedCommands", sortedCommands);
        properties.put("sortedGenerators", sortedGenerators);
        properties.put("mapOfMapsOfGenerators", mapOfMapsOfGenerators);

        return properties;

    }

    private Map<String, Map<String, Boolean>> generateMapOfMaps(Multimap<Command, CommandGenerator> input) {
        Map<String, Map<String, Boolean>> result = new HashMap<String, Map<String, Boolean>>();

        for(Command command : input.keySet()) {
            Map<String, Boolean> mapForGenerator = new HashMap<String, Boolean>();

            for (CommandGenerator commandGenerator : input.get(command)) {
                mapForGenerator.put(commandGenerator.toString(), Boolean.TRUE);
            }

            result.put(command.toString(), mapForGenerator);
        }

        return result;
    }

    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    public void setGeneratorBuilder(GeneratorBuilder generatorBuilder) {
        this.generatorBuilder = generatorBuilder;
    }

    public void setScreenControlScreenController(SelectControlsScreenController screenControlScreenController) {
        this.screenControlScreenController = screenControlScreenController;
    }

    public void setCommandBuilder(CommandBuilder commandBuilder) {
        this.commandBuilder = commandBuilder;
    }
}

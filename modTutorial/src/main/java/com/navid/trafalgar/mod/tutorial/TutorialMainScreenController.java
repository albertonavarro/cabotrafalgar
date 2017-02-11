package com.navid.trafalgar.mod.tutorial;

import com.navid.trafalgar.input.*;
import com.navid.trafalgar.mod.common.GameMenuController;
import com.navid.trafalgar.model.AShipModelInteractive;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.model.ModelBuilder;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static com.google.common.collect.Lists.newArrayList;


public class TutorialMainScreenController extends GameMenuController {

    @Autowired
    private List<TutorialChapter> chapters;

    private ListBox listChapters;

    private Label tutorialDescription;

    private TutorialChapter selectedChapter = null;
    /**
     * Singleton
     */
    @Autowired
    private ModelBuilder modelBuilder;
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

    @Autowired
    private CommandBuilder commandBuilder;


    @Override
    public void doOnStartScreen() {
        gameConfiguration.reset();

        listChapters = screen.findNiftyControl("listChapters", ListBox.class);
        tutorialDescription = screen.findNiftyControl("chapterDescription", Label.class);

        listChapters.addAllItems(chapters);

        if (listChapters.itemCount() > 0) {
            listChapters.selectItemByIndex(0);
            setSelectedTuto((TutorialChapter) listChapters.getSelection().get(0));
        }
    }

    @NiftyEventSubscriber(id = "listChapters")
    public void onMapSelectionChanged(final String id, final ListBoxSelectionChangedEvent<TutorialChapter> event) {
        if(!event.getSelection().isEmpty()) {
            List<TutorialChapter> selection = event.getSelection();
            setSelectedTuto(selection.get(0));
        }
    }

    @Override
    public void doOnEndScreen() {
        listChapters.clear();

        HashMap<String, Object> customValues = new HashMap<String, Object>();
        customValues.put("role", "ControlProxy");

        gameConfiguration.getPreGameModel().addToModel(newArrayList(new SystemInteractions()), "system");
        gameConfiguration.getPreGameModel().addToModel(modelBuilder.getBuilder(selectedChapter.getShip()).buildControls("tutorial", customValues), "player1");
        gameConfiguration.setShipName(selectedChapter.getShip());
        gameConfiguration.setMap(selectedChapter.getMap());

        Map<Command, CommandGenerator> assignments = createAssignments(selectedChapter, gameConfiguration.getPreGameModel().getByType(AShipModelInteractive.class));

        Set<CommandStateListener> listeners = generatorBuilder.generateControllers(assignments);
        gameConfiguration.getPreGameModel().addToModel(listeners);
    }

    private Map<Command, CommandGenerator> createAssignments(TutorialChapter selectedChapter, List<AShipModelInteractive> interactives) {
        Map<Command, CommandGenerator> assignments = new HashMap<Command, CommandGenerator>();
        Set<Command> commands = new HashSet<Command>();
        for(AShipModelInteractive interactive: interactives) {
            commands.addAll(interactive.getCommands(commandBuilder));
        }

        for(final Command command : commands) {
            Map<String, CommandGenerator> generators = generatorBuilder.getGenerators();
            String generatorType = selectedChapter.getCommandAssociations().get(command.toString()).getType();
            assignments.put(command, generators.get(generatorType));
        }

        return assignments;
    }

    public void setChapters(List<TutorialChapter> chapters) {
        this.chapters = chapters;
    }

    private void setSelectedTuto(TutorialChapter tutorialChapter) {
        tutorialDescription.setText(tutorialChapter.getDescription());
        selectedChapter = tutorialChapter;
    }

    public void setModelBuilder(ModelBuilder modelBuilder) {
        this.modelBuilder = modelBuilder;
    }

    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    public void setGeneratorBuilder(GeneratorBuilder generatorBuilder) {
        this.generatorBuilder = generatorBuilder;
    }

    public void setCommandBuilder(CommandBuilder commandBuilder) {
        this.commandBuilder = commandBuilder;
    }

}

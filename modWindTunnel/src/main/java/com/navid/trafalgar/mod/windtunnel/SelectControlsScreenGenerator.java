package com.navid.trafalgar.mod.windtunnel;

import com.google.common.collect.HashMultimap;
import com.navid.trafalgar.input.Command;
import com.navid.trafalgar.input.CommandGenerator;
import com.navid.trafalgar.input.GeneratorBuilder;
import com.navid.trafalgar.model.AShipModelTwo;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.screenflow.ScreenFlowManager;
import com.navid.trafalgar.screenflow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.Screen;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class SelectControlsScreenGenerator implements ScreenGenerator {

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
    /**
     *
     */
    @Autowired
    private GeneratorBuilder generatorBuilder;
    @Autowired
    private SelectControlsScreenController screenControlScreenController;
    @Autowired
    private Nifty nifty;

    @Override
    public void buildScreen() {

        AShipModelTwo ship = gameConfiguration.getPreGameModel().getSingleByType(AShipModelTwo.class);

        Set<Command> commands = ship.getCommands();

        HashMultimap<Command, CommandGenerator> gens = generatorBuilder.getGeneratorsFor(commands);



        final PanelBuilder outerPanelBuilder = new PanelBuilder("Panel_ID") {
            {
                childLayoutVertical();
            }
        };



        for (final Command currentCommand : gens.keySet()) {

            final PanelBuilder innerPanelBuilder = new PanelBuilder("Panel_ID") {
                {
                    childLayoutHorizontal();
                }
            };
            
            outerPanelBuilder.panel(innerPanelBuilder);

            for (final CommandGenerator commandGenerator : gens.get(currentCommand)) {
                innerPanelBuilder.text(new TextBuilder("text"){{
                    text(currentCommand.toString());
                    style("nifty-label");
                    alignCenter();
                    valignCenter();
                    height("5%");
                    width("15%");
                }});
                
                innerPanelBuilder.control(new ButtonBuilder(commandGenerator.toString() + "Button", commandGenerator.toString() + "ButtonLabel") {
                    {
                        alignCenter();
                        valignCenter();
                        height("5%");
                        width("15%");
                        interactOnClick("chooseCommand(" + commandGenerator.toString() + ")");
                    }
                });
            }


        }


        Screen screen = new ScreenBuilder("selectControl") {
            {
                controller(screenControlScreenController); // Screen properties       

                // <layer>
                layer(new LayerBuilder("Layer_ID") {
                    {
                        childLayoutVertical(); // layer properties, add more...

                        // <panel>
                        panel(outerPanelBuilder);
                        // </panel>
                    }
                });
                // </layer>
            }
        }.build(nifty);
        // <screen>
    }

    /**
     * @param nifty the nifty to set
     */
    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
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

    /**
     * @param screenControlScreenController the screenControlScreenController to
     * set
     */
    public void setScreenControlScreenController(SelectControlsScreenController screenControlScreenController) {
        this.screenControlScreenController = screenControlScreenController;
    }
}

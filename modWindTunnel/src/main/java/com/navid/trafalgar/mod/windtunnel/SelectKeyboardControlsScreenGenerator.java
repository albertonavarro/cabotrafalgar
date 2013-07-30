/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.mod.windtunnel;

import com.navid.trafalgar.input.KeyboardCommandStateListener;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.screenflow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.screen.Screen;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class SelectKeyboardControlsScreenGenerator implements ScreenGenerator {

    @Autowired
    private Nifty nifty;
    @Autowired
    private SelectKeyboardControlsScreenController controller;
    /**
     * Singleton
     */
    @Autowired
    private GameConfiguration gameConfiguration;

    @Override
    public void buildScreen() {

        Collection<KeyboardCommandStateListener> keyListeners = gameConfiguration.getPreGameModel().getByType(KeyboardCommandStateListener.class);

        final PanelBuilder outerPanelBuilder = new PanelBuilder("Panel_ID") {
            {
                height("80%");
                childLayoutVertical();
            }
        };

        for (final KeyboardCommandStateListener currentCommandListener : keyListeners) {
            final PanelBuilder commandNamePanelBuilder = new PanelBuilder(currentCommandListener.toString() + "Panel") {
                {
                    childLayoutHorizontal();

                    text(new TextBuilder("text") {
                        {
                            text(currentCommandListener.toString());
                            style("nifty-label");
                            alignCenter();
                            valignCenter();
                            height("10%");
                            margin("1%");
                        }
                    });

                    control(new ListBoxBuilder(currentCommandListener.toString()) {
                        {
                            displayItems(4);
                            selectionModeSingle();
                            optionalHorizontalScrollbar();
                            optionalVerticalScrollbar();
                            alignCenter();
                            valignCenter();
                            height("10%");
                            width("10%");
                            margin("1%");
                        }
                    });



                }
            };

            outerPanelBuilder.panel(commandNamePanelBuilder);
        }

        Screen screen = new ScreenBuilder("selectKeys") {
            {
                controller(controller); // Screen properties   


                // <layer>
                layer(new LayerBuilder("Layer_ID") {
                    {
                        childLayoutVertical(); // layer properties, add more...

                        // <panel>
                        panel(outerPanelBuilder);
                        // </panel>
                        panel(new PanelBuilder("Panel_ID") {
                            {
                                height("20%");
                                childLayoutHorizontal();
                                control(new ButtonBuilder("PreviousButton", "Back") {
                                    {
                                        alignCenter();
                                        valignCenter();
                                        interactOnClick("previous()");
                                    }
                                });

                                control(new ButtonBuilder("NextButton", "Next") {
                                    {
                                        alignCenter();
                                        valignCenter();
                                        interactOnClick("next()");
                                    }
                                });
                            }
                        });

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
     * @param controller the controller to set
     */
    public void setController(SelectKeyboardControlsScreenController controller) {
        this.controller = controller;
    }

    /**
     * @param gameConfiguration the gameConfiguration to set
     */
    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    
}

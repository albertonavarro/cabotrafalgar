package com.navid.trafalgar.games;

import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.nifty.flow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.builder.TextBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.tools.Color;
import org.springframework.beans.factory.annotation.Autowired;

public final class RootScreenGenerator implements ScreenGenerator {

    @Autowired
    private Nifty nifty;
    @Autowired
    private StartScreenController startScreenController;
    @Autowired
    private ScreenFlowManager screenFlowManager;

    @Override
    public void buildScreen(String screenUniqueId, String controllerClass) {

        final PanelBuilder panelModules = new PanelBuilder("Panel_Modules") {
            {
                childLayoutVertical();
                alignCenter();
                height("80%");
            }
        };

        for (final String moduleName : screenFlowManager.getChildren()) {
            panelModules.control(new ButtonBuilder(moduleName + "ButtonLabel", moduleName) {
                {
                    alignCenter();
                    valignCenter();
                    height("11%");
                    width("20%");
                    interactOnClick("executeModule(" + moduleName + ")");
                }
            });
        }

        final PanelBuilder panelMessage = new PanelBuilder("Panel_Message") {
            {
                childLayoutVertical();
                alignCenter();
                height("10%");
                text(new TextBuilder(){{
                    text("This is an ongoing work, please send your suggestion or descriptions of malfunctioning to cabo.trafalgar.contactus@gmail.com");
                    color(new Color("#ffff00ff"));
                    style("nifty-label");
                }});
            }
        };

        final PanelBuilder panelQuit = new PanelBuilder("Panel_Quit") {
            {
                childLayoutVertical();
                height("10%");
            }
        };

        panelQuit.control(new ButtonBuilder("QuitButton", "Quit") {
            {
                alignRight();
                valignCenter();
                height("100%");
                width("20%");
                interactOnClick("quit()");
            }
        });

        new ScreenBuilder(screenUniqueId) {
            {
                controller(startScreenController);
                layer(new LayerBuilder("Layer_Main") {
                    {
                        backgroundColor(Color.BLACK);
                        childLayoutVertical();
                        panel(panelModules);
                        panel(panelMessage);
                        panel(panelQuit);
                    }
                });
            }
        }.build(nifty);

    }

    /**
     * @param nifty the nifty to set
     */
    public void setNifty(Nifty nifty) {
        this.nifty = nifty;
    }

    /**
     * @param startScreenController the startScreenController to set
     */
    public void setStartScreenController(StartScreenController startScreenController) {
        this.startScreenController = startScreenController;
    }

    /**
     * @param screenFlowManager the screenFlowManager to set
     */
    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }
}

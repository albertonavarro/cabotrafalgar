package com.navid.trafalgar.games;

import com.navid.trafalgar.screenflow.ScreenFlowManager;
import com.navid.trafalgar.screenflow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
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
    public void buildScreen() {

        final PanelBuilder panelBuilder = new PanelBuilder("Panel_ID") {
            {
                childLayoutVertical();
                alignCenter();
                height("90%");
            }
        };

        for (final String moduleName : screenFlowManager.getModuleNames()) {
            panelBuilder.control(new ButtonBuilder(moduleName + "Button", moduleName + "ButtonLabel") {
                {
                    alignCenter();
                    valignCenter();
                    height("30px");
                    width("200px");
                    interactOnClick("executeModule(" + moduleName + ")");
                }
            });
        }

        final PanelBuilder panelQuit = new PanelBuilder("Panel_Quit") {
            {
                childLayoutVertical();
                height("5%");
            }
        };

        panelQuit.control(new ButtonBuilder("QuitButton", "Quit") {
            {
                alignCenter();
                valignCenter();
                height("30px");
                width("200px");
                interactOnClick("quit()");
            }
        });

        new ScreenBuilder("start") {
            {
                controller(startScreenController);
                layer(new LayerBuilder("Panel_ID") {
                    {
                        backgroundColor(Color.randomColor());
                        childLayoutVertical();
                        panel(panelBuilder);
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

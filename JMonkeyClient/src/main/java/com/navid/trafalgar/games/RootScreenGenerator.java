/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.games;

import com.navid.trafalgar.screenflow.ScreenFlowManager;
import com.navid.trafalgar.screenflow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.Screen;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class RootScreenGenerator implements ScreenGenerator {
    
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
                childLayoutCenter(); // panel properties, add more...               
            }
        };

        for (final String moduleName : screenFlowManager.getModuleNames()) {
            panelBuilder.control(new ButtonBuilder(moduleName + "Button", moduleName +"ButtonLabel") {
                {
                    alignCenter();
                    valignCenter();
                    height("5%");
                    width("15%");
                    interactOnClick("executeModule("+ moduleName +")");
                }
            });
        }

        Screen screen = new ScreenBuilder("start") {
            {
                controller(startScreenController); // Screen properties       

                // <layer>
                layer(new LayerBuilder("Layer_ID") {
                    {
                        childLayoutVertical(); // layer properties, add more...

                        // <panel>
                        panel(panelBuilder);
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

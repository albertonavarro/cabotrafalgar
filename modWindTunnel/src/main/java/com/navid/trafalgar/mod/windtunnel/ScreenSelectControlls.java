package com.navid.trafalgar.mod.windtunnel;

import com.navid.trafalgar.input.GeneratorBuilder;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.GameConfiguration;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.LayerBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.screen.DefaultScreenController;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author alberto
 */
public class ScreenSelectControlls implements ScreenController {

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
     * Singleton
     */
    @Autowired
    private Builder2 builder;
    /**
     * 
     */
    @Autowired
    private GeneratorBuilder generatorBuilder;

    
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
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

    @Override
    public void onStartScreen() {
        // <screen>
        
        
        
    nifty.addScreen("Screen_ID", new ScreenBuilder("Hello Nifty Screen"){{
        controller(new DefaultScreenController()); // Screen properties       
 
        // <layer>
        layer(new LayerBuilder("Layer_ID") {{
            childLayoutVertical(); // layer properties, add more...
 
            // <panel>
            panel(new PanelBuilder("Panel_ID") {{
               childLayoutCenter(); // panel properties, add more...               
 
                // GUI elements
               
                control(new ButtonBuilder("Button_ID", "Hello Nifty"){{
                    alignCenter();
                    valignCenter();
                    height("5%");
                    width("15%");
                }});
 
                //.. add more GUI elements here              
 
            }});
            // </panel>
          }});
        // </layer>
      }}.build(nifty));
    // </screen>
 
    nifty.gotoScreen("Screen_ID"); // start the screen
    }

    @Override
    public void onEndScreen() {
        System.out.println("sdf");
    }
    
    public void next(){
        nifty.gotoScreen("redirector");
    }
    
    public void back(){
        nifty.gotoScreen("redirector");
    }

    public void goTo(String nextScreen) {
        nifty.gotoScreen(nextScreen);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.screenflow;

import com.navid.trafalgar.modapi.ModScreenConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author alberto
 */
public class ScreenFlowGraph {

    private List<ScreenFlowUnit> modScreenConfigurations = new ArrayList<ScreenFlowUnit>();
    private Map<String, ScreenFlowUnit> mapModScreenConfigurations = new HashMap<String, ScreenFlowUnit>();
            
    
    public void addScreen(ScreenFlowUnit currentScreenConfig) {
        modScreenConfigurations.add(currentScreenConfig);
        mapModScreenConfigurations.put(currentScreenConfig.getScreenName(), currentScreenConfig);
        
    }

    public String getStartScreenName() {
        return modScreenConfigurations.get(0).getScreenName();
    }

    public ScreenFlowUnit getScreenConfiguration(String currentScreen) {
        return mapModScreenConfigurations.get(currentScreen);
    }

    public String getNextScreenName(ScreenFlowState screenFlowState) {
        
        if("next".equals(screenFlowState.getScreenCommand())){
            if(screenFlowState.getCurrentScreen() == null){
                return modScreenConfigurations.get(0).getScreenName();
            }else{
                return modScreenConfigurations.get(modScreenConfigurations.indexOf(mapModScreenConfigurations.get(screenFlowState.getCurrentScreen())) +1).getScreenName();
            }
        } else {
            return modScreenConfigurations.get(modScreenConfigurations.indexOf(mapModScreenConfigurations.get(screenFlowState.getScreenCommand())) +1).getScreenName();

        }
    }   
    
}

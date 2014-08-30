/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.screenflow;

import com.navid.trafalgar.modapi.ModScreenConfiguration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @author alberto
 */
public class ScreenFlowManager implements ApplicationContextAware {
    
    private Map<String, ScreenFlowUnit> mapScreenDeclarations = new HashMap<String, ScreenFlowUnit>();
    
    private ScreenFlowState screenFlowState = new ScreenFlowState();
    
    @Autowired
    private Map<String, ScreenFlowGraph> screenFlowGraph = new HashMap<String, ScreenFlowGraph>();

    private ApplicationContext applicationContext;
    
    
    
    public ScreenFlowGraph addFlowGraph( String name ){
        ScreenFlowGraph result = new ScreenFlowGraph();
        screenFlowGraph.put(name, result);
        return result;
    }

    public Set<String> getModuleNames() {
        return screenFlowGraph.keySet();
    }

    public String nextScreen() {
        if(screenFlowState.getCurrentFlow() == null){
            screenFlowState.setCurrentFlow(screenFlowState.getScreenCommand());
            screenFlowState.setCurrentScreen(screenFlowGraph.get(screenFlowState.getCurrentFlow()).getStartScreenName());
        }else{
            screenFlowState.setCurrentScreen(screenFlowGraph.get(screenFlowState.getCurrentFlow()).getNextScreenName(screenFlowState));
        }
        
        ScreenFlowUnit nextScreenConfig = screenFlowGraph.get(screenFlowState.getCurrentFlow()).getScreenConfiguration(screenFlowState.getCurrentScreen());
        
        nextScreenConfig.getInterfaceConstructor().buildScreen();
        
        return screenFlowState.getCurrentScreen();
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this.applicationContext = ac;
    }

    public void changeFlow(String moduleName) {
        screenFlowState.setCurrentFlow(moduleName);
        screenFlowState.setCurrentScreen(null);
        screenFlowState.setScreenCommand(null);
    }

    public void changeNextScreen() {
        screenFlowState.setScreenCommand("next");
    }
    
    public void changePreviousScreen() {
        screenFlowState.setScreenCommand("back");
    }
    
    public void changeNextScreen(String nextScreen) {
        screenFlowState.setScreenCommand(nextScreen);
    }

    public ScreenFlowUnit getScreen(String screenName) {
        return mapScreenDeclarations.get(screenName);
    }

    public void addScreenDeclaration(ScreenFlowUnit screenFlowUnit) {
        mapScreenDeclarations.put(screenFlowUnit.getScreenName(), screenFlowUnit);
    }
    
    
}

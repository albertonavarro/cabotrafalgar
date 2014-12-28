package com.navid.trafalgar.screenflow;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;

public final class ScreenFlowManager {

    private final Map<String, ScreenFlowUnit> mapScreenDeclarations = new HashMap<String, ScreenFlowUnit>();

    private final ScreenFlowState screenFlowState = new ScreenFlowState();

    private ScreenFlowGraph root = null;

    @Autowired
    private final Map<String, ScreenFlowGraph> screenFlowGraph = new HashMap<String, ScreenFlowGraph>();

    public ScreenFlowGraph addRootFlowGraph(String name) {
        root = new ScreenFlowGraph();
        screenFlowGraph.put(name, root);
        for (ScreenFlowGraph current : screenFlowGraph.values()) {
            current.addParentFlow(root);
        }
        return root;
    }

    public ScreenFlowGraph addFlowGraph(String name) {
        ScreenFlowGraph result = new ScreenFlowGraph();
        result.addParentFlow(root);
        screenFlowGraph.put(name, result);
        return result;
    }

    public Set<String> getModuleNames() {
        return screenFlowGraph.keySet();
    }

    public String nextScreen() {
        if (screenFlowState.getCurrentFlow() == null) {
            screenFlowState.setCurrentFlow(screenFlowState.getScreenCommand());
            screenFlowState.setCurrentScreen(screenFlowGraph.get(screenFlowState.getCurrentFlow()).getStartScreenName());
        } else {
            screenFlowState.setCurrentScreen(screenFlowGraph.get(screenFlowState.getCurrentFlow()).getNextScreenName(screenFlowState));
        }

        ScreenFlowUnit nextScreenConfig
                = screenFlowGraph.get(screenFlowState.getCurrentFlow()).getScreenConfiguration(screenFlowState.getCurrentScreen());

        nextScreenConfig.getInterfaceConstructor().buildScreen();

        return screenFlowState.getCurrentScreen();
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

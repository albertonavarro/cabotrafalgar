package com.navid.trafalgar.mod.tutorial;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.trafalgar.input.GeneratorBuilder;
import com.navid.trafalgar.input.SystemInterpreter;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.statistics.AbstractStatistic;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.mod.tutorial.script.ScriptInterpreter;
import com.navid.trafalgar.mod.tutorial.statelisteners.LoadCameraStateListener;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by alberto on 16/04/16.
 */
public class NavigationScreenController implements ScreenController, BeanFactoryAware, ScriptInterpreter, SystemInterpreter {

    /*
     * Comes from bind
     */
    private Nifty nifty;
    /*
     * Comes from bind
     */
    private Screen screen;
    @Autowired
    private AppStateManager appStateManager;
    @Autowired
    private Application app;
    @Autowired
    private LoadCameraStateListener cameraManager;
    @Autowired
    private EventManager eventManager;
    @Autowired
    private ScreenFlowManager screenFlowManager;

    private TutorialMainGame game;
    private boolean showMenu;
    private boolean showControls;
    private XmlBeanFactory ctx;
    /*
     * From BeanFactoryAware
     */
    private BeanFactory beanFactory;

    @Autowired
    private GeneratorBuilder generatorBuilder;
    /**
     * Nifty GUI ScreenControl methods
     */
    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        showMenu = false;
        showMenuFunction(showMenu); //nifty keeps the status, we need to reset it.

        app.enqueue(new Callable<Void>() {

            @Override
            public Void call() {
                ctx = new XmlBeanFactory(new ClassPathResource("mod/tutorial/game-context.xml"), beanFactory);
                game = (TutorialMainGame) ctx.getBean("mod.tutorial.maingame");

                appStateManager.attach(game);

                return null;
            }
        });
    }

    @Override
    public void onEndScreen() {
        app.enqueue(new Callable<Void>() {

            @Override
            public Void call() {

                eventManager.fireEvent("UNLOAD");

                appStateManager.detach(game);

                ctx.destroySingletons();

                return null;
            }
        });
    }

    public void showMenu() {
        toggleMenu();
    }

    @Override
    public void showControls() {
        toggleShowControls();
    }

    public void clickCamera2() {
        cameraManager.setCamera2();
    }

    public void clickCamera3() {
        cameraManager.setCamera3();
    }

    public void updateShipStats() {
        ListBox listBox = screen.findNiftyControl("statsLists", ListBox.class);
        listBox.refresh();
    }

    public void fillShipStats(Collection<AbstractStatistic> stats) {
        ListBox listBox = screen.findNiftyControl("statsLists", ListBox.class);

        for (AbstractStatistic currentStat : stats) {
            listBox.addItem(currentStat);
        }
    }

    public void clearStats() {
        ListBox listBox = screen.findNiftyControl("statsLists", ListBox.class);
        listBox.clear();
    }

    public void showMenuFunction(boolean value) {
        screen.findElementByName("menuLayer").setVisible(value);
        showMenu = value;
        if (value) {
            eventManager.fireEvent("PAUSE");
        } else {
            eventManager.fireEvent("RESUME");
        }
    }

    private String prettyPrintReport(Map<String, String> commands ) {
        List<String> keys = new ArrayList<>(commands.keySet());
        Collections.sort(keys);

        StringBuilder sb = new StringBuilder();

        for (String key : keys) {
            sb.append(key + ": " + commands.get(key) + "\n");
        }

        return sb.toString();
    }

    public void showControlLayer(boolean value) {
        screen.findNiftyControl("showControlText", Label.class).setText(prettyPrintReport(generatorBuilder.generateReport()));
        screen.findElementByName("showControlLayer").setVisible(value);
        showMenu = value;
        if (value) {
            eventManager.fireEvent("PAUSE");
        } else {
            eventManager.fireEvent("RESUME");
        }
    }

    public synchronized void toggleMenu() {
        showMenu = !showMenu;
        showMenuFunction(showMenu);
    }

    public synchronized void toggleShowControls() {
        showControls = !showControls;
        showControlLayer(showControls);
    }

    public synchronized void restart() {
        showMenuFunction(false);
        eventManager.fireEvent(EventManager.FAILED);
        screenFlowManager.setNextScreenHint("tutorialSailingScreen");
        nifty.gotoScreen("redirector");
    }

    public synchronized void quit() {
        showMenuFunction(false);
        eventManager.fireEvent(EventManager.FAILED);
        screenFlowManager.setNextScreenHint("tutorialMainScreen");
        nifty.gotoScreen("redirector");
    }

    /**
     * @param cameraManager the cameraManager to set
     */
    public void setCameraManager(LoadCameraStateListener cameraManager) {
        this.cameraManager = cameraManager;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * @param app the app to set
     */
    public void setApp(Application app) {
        this.app = app;
    }

    /**
     * @param appStateManager the appStateManager to set
     */
    public void setAppStateManager(AppStateManager appStateManager) {
        this.appStateManager = appStateManager;
    }

    /**
     * @param eventManager the eventManager to set
     */
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    /**
     * @param screenFlowManager the screenFlowManager to set
     */
    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }

    @Override
    public void printMessage(String[] message) {
        screen.findElementByName("tutorialLayer").setVisible(true);
        screen.findNiftyControl("tutorialText", Label.class).setText(message[0]);
    }

    @Override
    public void cleanUpMessage() {
        screen.findElementByName("tutorialLayer").setVisible(false);
    }

    public void tutorialContinue() {
        eventManager.fireEvent("SCRIPT_STEP_ACTIONED");
    }

    @Override
    public void setStatisticsManager(StatisticsManager statisticsManager) {

    }

    public void setGeneratorBuilder(GeneratorBuilder generatorBuilder) {
        this.generatorBuilder = generatorBuilder;
    }
}

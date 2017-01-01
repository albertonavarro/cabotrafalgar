package com.navid.trafalgar.mod.tutorial;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.trafalgar.input.GeneratorBuilder;
import com.navid.trafalgar.input.SystemInterpreter;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.statistics.AbstractStatistic;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.mod.common.GamePlayController;
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
public class NavigationScreenController extends GamePlayController implements BeanFactoryAware, ScriptInterpreter {

    @Autowired
    private AppStateManager appStateManager;
    @Autowired
    private Application app;

    private TutorialMainGame game;
    private XmlBeanFactory ctx;
    /*
     * From BeanFactoryAware
     */
    private BeanFactory beanFactory;


    protected NavigationScreenController() {
        super("tutorialSailingScreen", "tutorialMainScreen");
    }


    @Override
    public void onStartScreen() {
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

    public void printMessage(String[] message) {
        screen.findElementByName("tutorialLayer").setVisible(true);
        screen.findNiftyControl("tutorialText", Label.class).setText(message[0]);
    }

    public void cleanUpMessage() {
        screen.findElementByName("tutorialLayer").setVisible(false);
    }

    public void tutorialContinue() {
        eventManager.fireEvent("SCRIPT_STEP_ACTIONED");
    }

}

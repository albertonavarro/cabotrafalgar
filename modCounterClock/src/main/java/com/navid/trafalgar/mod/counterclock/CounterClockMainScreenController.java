package com.navid.trafalgar.mod.counterclock;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.navid.trafalgar.manager.statistics.AbstractStatistic;
import com.navid.trafalgar.mod.common.GamePlayController;
import de.lessvoid.nifty.controls.ListBox;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import java.util.Collection;
import java.util.concurrent.Callable;

public final class CounterClockMainScreenController extends GamePlayController implements BeanFactoryAware {


    @Autowired
    private AppStateManager appStateManager;
    @Autowired
    private Application app;

    private CounterClockMainGame game;
    private boolean showMenu;
    private XmlBeanFactory ctx;
    /*
     * From BeanFactoryAware
     */
    private BeanFactory beanFactory;

    protected CounterClockMainScreenController() {
        super("counterclockScreen", "selectMap_counterclock");
    }

    @Override
    public void onStartScreen() {
        showMenu = false;
        showMenuFunction(showMenu); //nifty keeps the status, we need to reset it.

        app.enqueue(new Callable<Void>() {

            @Override
            public Void call() {
                ctx = new XmlBeanFactory(new ClassPathResource("mod/counterclock/game-context.xml"), beanFactory);
                game = (CounterClockMainGame) ctx.getBean("mod.counterclock.maingame");

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

    public void fillShipStats(Collection<AbstractStatistic> stats) {
        ListBox listBox = screen.findNiftyControl("statsLists", ListBox.class);

        for (AbstractStatistic currentStat : stats) {
            listBox.addItem(currentStat);
        }
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



}

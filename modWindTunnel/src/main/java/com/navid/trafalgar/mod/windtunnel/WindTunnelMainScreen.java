package com.navid.trafalgar.mod.windtunnel;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.navid.trafalgar.manager.statistics.AbstractStatistic;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.mod.common.GamePlayController;
import com.navid.trafalgar.mod.windtunnel.statelisteners.LoadCameraStateListener;
import de.lessvoid.nifty.controls.ListBox;

import java.util.Collection;
import java.util.concurrent.Callable;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public final class WindTunnelMainScreen extends GamePlayController implements BeanFactoryAware {

    @Autowired
    private AppStateManager appStateManager;
    @Autowired
    private Application app;

    private WindTunnelMainGame game;
    private XmlBeanFactory ctx;
    /*
     * From BeanFactoryAware
     */
    private BeanFactory beanFactory;

    public WindTunnelMainScreen() {
        super("windTunnelScreen", "selectShip");
    }

    @Override
    public void onStartScreen() {
        showMenuFunction(false); //nifty keeps the status, we need to reset it.

        app.enqueue(new Callable<Void>() {

            @Override
            public Void call() {
                ctx = new XmlBeanFactory(new ClassPathResource("mod/windtunnel/game-context.xml"), beanFactory);
                game = ctx.getBean("mod.windtunnel.maingame", WindTunnelMainGame.class);

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


    @Override
    public void setStatisticsManager(StatisticsManager statisticsManager) {

    }
}

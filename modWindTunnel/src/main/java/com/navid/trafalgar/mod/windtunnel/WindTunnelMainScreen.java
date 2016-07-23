package com.navid.trafalgar.mod.windtunnel;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.trafalgar.input.SystemInterpreter;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.statistics.AbstractStatistic;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import com.navid.trafalgar.mod.common.GamePlayController;
import com.navid.trafalgar.mod.windtunnel.statelisteners.LoadCameraStateListener;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.Collection;
import java.util.concurrent.Callable;
import org.springframework.beans.BeansException;
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
    @Autowired
    private LoadCameraStateListener cameraManager;

    private WindTunnelMainGame game;
    private XmlBeanFactory ctx;
    /*
     * From BeanFactoryAware
     */
    private BeanFactory beanFactory;

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


    public void clickCamera2() {
        cameraManager.setCamera2();
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


    @Override
    public void setStatisticsManager(StatisticsManager statisticsManager) {

    }
}

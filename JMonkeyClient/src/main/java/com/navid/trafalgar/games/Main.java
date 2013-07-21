package com.navid.trafalgar.games;

import com.jme3.app.Application;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.JmeSystem;
import com.navid.trafalgar.definition2.Json2AssetLoader;
import static com.navid.trafalgar.games.SpringStaticHolder.ctx;
import static com.navid.trafalgar.games.SpringStaticHolder.registerBean;
import com.navid.trafalgar.modapi.ModRegisterer;
import com.navid.trafalgar.modapi.ModScreenConfiguration;
import com.navid.trafalgar.screenflow.RedirectorScreenController;
import com.navid.trafalgar.screenflow.ScreenFlowManager;
import com.navid.trafalgar.screenflow.ScreenFlowUnit;
import com.navid.trafalgar.screenflow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.reflections.Reflections;
import org.springframework.beans.factory.BeanFactory;

public class Main extends Application {

    private static boolean record = false;

    public static void main(String[] args) {

        if (args.length == 1 && args[0].equals("record")) {
            record = true;
        }

        Main app = new Main();
        app.start();
    }

    @Override
    public void start(JmeContext.Type contextType) {

        settings = new AppSettings(true);
        if (!JmeSystem.showSettingsDialog(settings, true)) {
            return;
        }

        setSettings(settings);
        super.start(contextType);
    }

    @Override
    public void initialize() {

        super.initialize();
        super.setPauseOnLostFocus(false);
        assetManager.registerLoader(Json2AssetLoader.class, "json2");

        
        registerBean("common.assetManager", assetManager);
        registerBean("common.inputManager", inputManager);
        registerBean("common.stateManager", stateManager);
        registerBean("common.renderManager", renderManager);
        registerBean("common.appSettings", settings);
        registerBean("common.app", this);

        if (record) {
            stateManager.attach(new VideoRecorderAppState());
        }

        /**
         * Activate the Nifty-JME integration:
         */
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty nifty = niftyDisplay.getNifty();
        
        registerBean("common.nifty", nifty);
        
        guiViewPort.addProcessor(niftyDisplay);

        loadModules(nifty, nifty.getScreen("start"), settings, this);
        initScreen(nifty, ctx);
    }

    private void initScreen(Nifty nifty, final BeanFactory beanFactory) {
        
        ScreenFlowManager screenFlowManager = beanFactory.getBean(ScreenFlowManager.class);
        screenFlowManager.addFlowGraph("root").addScreen(new ScreenFlowUnit("start", beanFactory.getBean("common.RootScreenGenerator", ScreenGenerator.class), beanFactory.getBean("common.RootScreenController", ScreenController.class)));
        screenFlowManager.changeFlow("root");
        
        nifty.addScreen("redirector", new ScreenBuilder("start", beanFactory.getBean(RedirectorScreenController.class)).build(nifty));
        nifty.gotoScreen("redirector");
    }

    private void loadModules(Nifty nifty, Screen screen, AppSettings settings, Application app) {
        Reflections reflections = new Reflections("com.navid.trafalgar");

        Set<Class<? extends ModRegisterer>> result = reflections.getSubTypesOf(ModRegisterer.class);
        for (Class<? extends ModRegisterer> currentClass : result) {
            try {
                ModRegisterer currentLoader = (ModRegisterer) Class.forName(currentClass.getCanonicalName()).newInstance();
                currentLoader.generate(nifty, screen, settings, app, SpringStaticHolder.ctx);
            } catch (Exception ex) {
                Logger.getLogger(StartScreenController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void update() {
        // necessary stuff
        super.update();

        // do some animation
        float tpf = timer.getTimePerFrame();

        // update elements in all enabled states.
        stateManager.update(tpf);
        stateManager.render(renderManager);

        // render the viewports
        renderManager.render(tpf, context.isRenderable());
    }
}

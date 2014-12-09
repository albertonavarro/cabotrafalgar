package com.navid.trafalgar.games;

import com.jme3.app.Application;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.JmeSystem;
import com.navid.trafalgar.definition2.Json2AssetLoader;
import com.navid.trafalgar.modapi.ModRegisterer;
import com.navid.trafalgar.screenflow.RedirectorScreenController;
import com.navid.trafalgar.screenflow.ScreenFlowManager;
import com.navid.trafalgar.screenflow.ScreenFlowUnit;
import com.navid.trafalgar.screenflow.ScreenGenerator;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public class Main extends Application {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

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

        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        registerBean("common.nifty", nifty);

        guiViewPort.addProcessor(niftyDisplay);

        loadModules(nifty, nifty.getScreen("start"), settings, this);
        initScreen(nifty, ctx);
    }

    private void initScreen(Nifty nifty, final BeanFactory beanFactory) {

        ScreenFlowManager screenFlowManager = beanFactory.getBean(ScreenFlowManager.class);
        screenFlowManager.addRootFlowGraph("root").addScreen(new ScreenFlowUnit("start", beanFactory.getBean("common.RootScreenGenerator", ScreenGenerator.class), beanFactory.getBean("common.RootScreenController", ScreenController.class)));
        screenFlowManager.changeFlow("root");

        nifty.addScreen("redirector", new ScreenBuilder("start", beanFactory.getBean(RedirectorScreenController.class)).build(nifty));
        nifty.gotoScreen("redirector");
    }

    private void loadModules(Nifty nifty, Screen screen, AppSettings settings, Application app) {
        Reflections reflections = new Reflections("com.navid.trafalgar");

        Set<Class<? extends ModRegisterer>> result = reflections.getSubTypesOf(ModRegisterer.class);

        Collection<ModRegisterer> resultInstances = new ArrayList();

        for (Class<? extends ModRegisterer> currentClass : result) {
            try {
                ModRegisterer currentLoader = (ModRegisterer) Class.forName(currentClass.getCanonicalName()).newInstance();
                resultInstances.add(currentLoader);
            } catch (ClassNotFoundException ex) {
                LOG.error("Error loading module {}", currentClass, ex);
            } catch (InstantiationException ex) {
                LOG.error("Error loading module {}", currentClass, ex);
            } catch (IllegalAccessException ex) {
                LOG.error("Error loading module {}", currentClass, ex);
            }
        }

        for (ModRegisterer currentRegisterer : resultInstances) {
            currentRegisterer.registerSpringConfig(ctx);
        }
        for (ModRegisterer currentRegisterer : resultInstances) {
            currentRegisterer.registerInputs();
        }
        for (ModRegisterer currentRegisterer : resultInstances) {
            currentRegisterer.registerModels();
        }
        for (ModRegisterer currentRegisterer : resultInstances) {
            currentRegisterer.registerScreens(nifty);
        }
        for (ModRegisterer currentRegisterer : resultInstances) {
            currentRegisterer.registerFlow(nifty);
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

    public static XmlBeanFactory ctx = new XmlBeanFactory(new ClassPathResource("application-context.xml"));

    public static void registerSingletonBeanDefinition(String name, String className) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        beanDefinition.setBeanClassName(className);
        beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        ctx.registerBeanDefinition(name, beanDefinition);

    }

    public static void registerPrototypeBeanDefinition(String name, String className) {
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setScope(BeanDefinition.SCOPE_PROTOTYPE);
        beanDefinition.setBeanClassName(className);
        beanDefinition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        ctx.registerBeanDefinition(name, beanDefinition);
    }

    public static void registerBean(String name, Object object) {
        ctx.registerSingleton(name, object);
    }
}

package com.navid.trafalgar.games;

import com.google.common.base.Optional;
import com.jme3.app.Application;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.*;
import com.jme3.input.Input;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeContext;
import com.jme3.system.JmeSystem;
import com.navid.nifty.flow.RedirectorScreenController;
import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.nifty.flow.dto.ScreenDefinition;
import com.navid.nifty.flow.resolutors.DefaultInstanceResolutor;
import com.navid.nifty.flow.resolutors.InstanceResolutionException;
import com.navid.nifty.flow.template.ftl.StaticScreenGeneratorResolutor;
import com.navid.trafalgar.audio.MusicManager;
import com.navid.trafalgar.input.RemoteEventsManager;
import com.navid.trafalgar.maploader.v3.MapAssetLoader;
import com.navid.trafalgar.modapi.ModRegisterer;
import com.navid.trafalgar.modapi.SpringBeanResolutor;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ScreenBuilder;
import de.lessvoid.nifty.screen.Screen;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import static com.google.common.collect.Lists.newArrayList;

public final class Main extends Application {

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
    public void destroy() {
        Map<String, AutoCloseable> autocloseables = ctx.getBeansOfType(AutoCloseable.class);
        for(AutoCloseable autoCloseable : autocloseables.values()) {
            try {
                autoCloseable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    @Override
    public void start(JmeContext.Type contextType, boolean waitFor) {
        settings = new AppSettings(true);
        if (!JmeSystem.showSettingsDialog(settings, true)) {
            return;
        }

        setSettings(settings);
        super.start(contextType, waitFor);
    }

    @Override
    public void initialize() {

        super.initialize();
        super.setPauseOnLostFocus(false);
        LOG.debug("Loading MapAssetLoader");
        assetManager.registerLoader(MapAssetLoader.class, "map");

        try {
            String tempDir = File.createTempFile("something","something").getParent();
            assetManager.registerLocator(tempDir, FileLocator.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        registerBean("common.assetManager", assetManager);
        registerBean("common.inputManager", inputManager);
        registerBean("common.stateManager", stateManager);
        registerBean("common.renderManager", renderManager);
        registerBean("common.appSettings", settings);
        registerBean("common.remoteEventsManager", new RemoteEventsManager());
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
        resolutorChain.addResolutor("static", new StaticScreenGeneratorResolutor(nifty));

        nifty.loadStyleFile("nifty-default-styles.xml");
        nifty.loadControlFile("nifty-default-controls.xml");
        registerBean("common.nifty", nifty);

        guiViewPort.addProcessor(niftyDisplay);

        try {
            initScreen(nifty, ctx);
        } catch (InstanceResolutionException e) {
            e.printStackTrace();
        }
    }

    private void initScreen(Nifty nifty, final BeanFactory beanFactory) throws InstanceResolutionException {

        ScreenFlowManager screenFlowManager = beanFactory.getBean(ScreenFlowManager.class);
        screenFlowManager.addScreenDefinition(new ScreenDefinition("root", "spring:common.RootScreenController", "spring:common.RootScreenGenerator"));
        screenFlowManager.addFlowDefinition("root", Optional.<String>absent(), newArrayList("root"));

        loadModules(nifty, nifty.getScreen("start"), settings, this);

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
            } catch (Exception ex) {
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
        for (ModRegisterer currentRegisterer : resultInstances) {
            currentRegisterer.registerMusic(ctx.getBean(MusicManager.class));
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

    static XmlBeanFactory ctx = new XmlBeanFactory(new ClassPathResource("application-context.xml"));
    static DefaultInstanceResolutor resolutorChain = null;

    static {
        resolutorChain = ctx.getBean(DefaultInstanceResolutor.class);
        resolutorChain.addResolutor("spring", new SpringBeanResolutor(ctx));
    }

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

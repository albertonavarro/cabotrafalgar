/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.modapi;

import com.jme3.app.Application;
import com.jme3.system.AppSettings;
import com.navid.trafalgar.input.CommandGenerator;
import com.navid.trafalgar.input.GeneratorBuilder;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.screenflow.ScreenFlowGraph;
import com.navid.trafalgar.screenflow.ScreenFlowManager;
import com.navid.trafalgar.screenflow.ScreenFlowUnit;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.Map;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author alberto
 */
public abstract class GenericModRegisterer implements ModRegisterer {

    private void registerModels(BeanFactory beanFactory, String fileName) {
        XmlBeanFactory ctx = new XmlBeanFactory(new ClassPathResource(fileName), beanFactory);

        Map<String, BuilderInterface> builders = ctx.getBeansOfType(BuilderInterface.class);
        Builder2 builder2 = beanFactory.getBean("common.Builder", Builder2.class);

        for (BuilderInterface currentBuilder : builders.values()) {
            builder2.registerBuilder(currentBuilder);
        }
    }
    
    private void registerInputs(BeanFactory beanFactory, String fileName) {
        XmlBeanFactory ctx = new XmlBeanFactory(new ClassPathResource(fileName), beanFactory);

        Map<String, CommandGenerator> commandGenerators = ctx.getBeansOfType(CommandGenerator.class);
        GeneratorBuilder generatorBuilder = beanFactory.getBean("common.InputBuilder", GeneratorBuilder.class);

        for (CommandGenerator currentCommandGenerator : commandGenerators.values()) {
            generatorBuilder.registerBuilder(currentCommandGenerator);
        }
    }

    private XmlBeanFactory registerSpringConfig(BeanFactory beanFactory, ModConfiguration modConfiguration) {
        XmlBeanFactory ctx = new XmlBeanFactory(new ClassPathResource(modConfiguration.getModPreGameSpringConfig()), beanFactory);
        ctx.registerSingleton("mod.common.ModConfig", modConfiguration);
        return ctx;
    }
    
    private void registerScreens(Nifty nifty, BeanFactory beanFactory, ModConfiguration modConfiguration) {
        ScreenFlowManager screenFlowManager = beanFactory.getBean("common.ScreenFlowManager", ScreenFlowManager.class);
        
        for (ModScreenConfiguration currentScreenConfig : modConfiguration.getScreenDeclarations()) {
            ScreenFlowUnit screenFlowUnit = new ScreenFlowUnit(currentScreenConfig, beanFactory);
            nifty.registerScreenController(screenFlowUnit.getController());
            screenFlowManager.addScreenDeclaration(screenFlowUnit);
        }
    }

    private void registerFlow(Nifty nifty, BeanFactory beanFactory, ModConfiguration modConfiguration) {
        ScreenFlowManager screenFlowManager = beanFactory.getBean("common.ScreenFlowManager", ScreenFlowManager.class);
        ScreenFlowGraph flow = screenFlowManager.addFlowGraph(modConfiguration.getModName());
        
        for (String screenName : modConfiguration.getModuleScreenFlow()) {
            flow.addScreen(screenFlowManager.getScreen(screenName));
        }
    }

    public void generate(Nifty nifty, Screen parent, AppSettings settings, Application app, BeanFactory beanFactory, ModConfiguration modConfiguration) {

        if (modConfiguration.getModPreGameSpringConfig() != null) {
            beanFactory = registerSpringConfig(beanFactory, modConfiguration);
        }

        if (modConfiguration.getBuildersSpringConfig() != null) {
            registerModels(beanFactory, modConfiguration.getBuildersSpringConfig());
        }
        
        if (modConfiguration.getBuildersSpringConfig() != null) {
            registerInputs(beanFactory, modConfiguration.getBuildersSpringConfig());
        }
        
        if (! modConfiguration.getScreenDeclarations().isEmpty()){
            registerScreens(nifty, beanFactory, modConfiguration);
        }

        if (modConfiguration.getModName() != null) {
            registerFlow(nifty, beanFactory, modConfiguration);
        }
        
    }
}

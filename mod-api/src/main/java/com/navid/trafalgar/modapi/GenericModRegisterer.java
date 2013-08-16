/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.navid.trafalgar.modapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.navid.trafalgar.input.CommandGenerator;
import com.navid.trafalgar.input.GeneratorBuilder;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.BuilderInterface;
import com.navid.trafalgar.screenflow.ScreenFlowGraph;
import com.navid.trafalgar.screenflow.ScreenFlowManager;
import com.navid.trafalgar.screenflow.ScreenFlowUnit;
import de.lessvoid.nifty.Nifty;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 *
 * @author alberto
 */
public abstract class GenericModRegisterer implements ModRegisterer {
    
    private ModConfiguration modConfiguration;
    
    public GenericModRegisterer(InputStream configFile){
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            modConfiguration = mapper.readValue(configFile, ModConfiguration.class);
        } catch (IOException ex) {
            Logger.getLogger(GenericModRegisterer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void registerSpringConfig(BeanFactory beanFactory) {
        if (modConfiguration.getModPreGameSpringConfig() != null) {
            XmlBeanFactory ctx = new XmlBeanFactory(new ClassPathResource(modConfiguration.getModPreGameSpringConfig()), beanFactory);
            ctx.registerSingleton("mod.common.ModConfig", modConfiguration);
            modConfiguration.setBeanFactory(ctx);
        }
    }

    @Override
    public void registerModels() {
        if (modConfiguration.getBuildersSpringConfig() != null) {
            XmlBeanFactory ctx = new XmlBeanFactory(new ClassPathResource(modConfiguration.getBuildersSpringConfig()), modConfiguration.getBeanFactory());

            Map<String, BuilderInterface> builders = ctx.getBeansOfType(BuilderInterface.class);
            Builder2 builder2 = ctx.getBean("common.Builder", Builder2.class);

            for (BuilderInterface currentBuilder : builders.values()) {
                builder2.registerBuilder(currentBuilder);
            }
        }
    }

    @Override
    public void registerInputs() {
        if (modConfiguration.getBuildersSpringConfig() != null) {
            XmlBeanFactory ctx = new XmlBeanFactory(new ClassPathResource(modConfiguration.getBuildersSpringConfig()), modConfiguration.getBeanFactory());

            Map<String, CommandGenerator> commandGenerators = ctx.getBeansOfType(CommandGenerator.class);
            GeneratorBuilder generatorBuilder = ctx.getBean("common.InputBuilder", GeneratorBuilder.class);

            for (CommandGenerator currentCommandGenerator : commandGenerators.values()) {
                generatorBuilder.registerBuilder(currentCommandGenerator);
            }
        }
    }

    @Override
    public void registerScreens(Nifty nifty) {
        if (modConfiguration.getScreenDeclarations() != null) {
            BeanFactory ctx = modConfiguration.getBeanFactory();

            ScreenFlowManager screenFlowManager = ctx.getBean("common.ScreenFlowManager", ScreenFlowManager.class);

            for (ModScreenConfiguration currentScreenConfig : modConfiguration.getScreenDeclarations()) {
                ScreenFlowUnit screenFlowUnit = new ScreenFlowUnit(currentScreenConfig, ctx);
                nifty.registerScreenController(screenFlowUnit.getController());
                screenFlowManager.addScreenDeclaration(screenFlowUnit);
             }
        }
    }

    @Override
    public void registerFlow(Nifty nifty) {
        if (modConfiguration.getModuleScreenFlow() != null) {

            BeanFactory ctx = modConfiguration.getBeanFactory();

            ScreenFlowManager screenFlowManager = ctx.getBean("common.ScreenFlowManager", ScreenFlowManager.class);
            ScreenFlowGraph flow = screenFlowManager.addFlowGraph(modConfiguration.getModName());

            for (String screenName : modConfiguration.getModuleScreenFlow()) {
                flow.addScreen(screenFlowManager.getScreen(screenName));
            }
        }
    }
}

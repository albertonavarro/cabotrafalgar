package com.navid.trafalgar.modapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.nifty.flow.dto.ScreenDefinition;
import com.navid.nifty.flow.resolutors.DefaultInstanceResolutor;
import com.navid.nifty.flow.resolutors.InstanceResolutionException;
import com.navid.trafalgar.input.CommandGenerator;
import com.navid.trafalgar.input.GeneratorBuilder;
import com.navid.trafalgar.model.ModelBuilder;
import com.navid.trafalgar.model.BuilderInterface;
import de.lessvoid.nifty.Nifty;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import static com.google.common.base.Optional.of;

public abstract class GenericModRegisterer implements ModRegisterer {

    private static final Logger LOG = LoggerFactory.getLogger(GenericModRegisterer.class);

    private ModConfiguration modConfiguration;

    public GenericModRegisterer(InputStream configFile) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            modConfiguration = mapper.readValue(configFile, ModConfiguration.class);
        } catch (IOException ex) {
            LOG.error("Error reading module configuration: {}", configFile, ex);
        }
    }

    @Override
    public final void registerSpringConfig(BeanFactory beanFactory) {
        if (modConfiguration.getModPreGameSpringConfig() != null) {
            XmlBeanFactory ctx = new XmlBeanFactory(new ClassPathResource(modConfiguration.getModPreGameSpringConfig()), beanFactory);
            ctx.registerSingleton("mod.common.ModConfig", modConfiguration);
            modConfiguration.setBeanFactory(ctx);

            DefaultInstanceResolutor ir = ctx.getBean(DefaultInstanceResolutor.class);
            ir.addResolutor("spring", new SpringBeanResolutor(ctx));
        } else {
            modConfiguration.setBeanFactory(beanFactory);
        }
    }

    @Override
    public final void registerModels() {
        if (modConfiguration.getBuildersSpringConfig() != null) {
            XmlBeanFactory ctx = new XmlBeanFactory(
                    new ClassPathResource(modConfiguration.getBuildersSpringConfig()), modConfiguration.getBeanFactory());

            Map<String, BuilderInterface> builders = ctx.getBeansOfType(BuilderInterface.class);
            ModelBuilder builder2 = ctx.getBean("common.Builder", ModelBuilder.class);

            for (BuilderInterface currentBuilder : builders.values()) {
                builder2.registerBuilder(currentBuilder);
            }
        }
    }

    @Override
    public final void registerInputs() {
        if (modConfiguration.getBuildersSpringConfig() != null) {
            XmlBeanFactory ctx = new XmlBeanFactory(
                    new ClassPathResource(modConfiguration.getBuildersSpringConfig()), modConfiguration.getBeanFactory());

            Map<String, CommandGenerator> commandGenerators = ctx.getBeansOfType(CommandGenerator.class);
            if (commandGenerators.size() > 0) {
                try {
                    GeneratorBuilder generatorBuilder = ctx.getBean("common.InputBuilder", GeneratorBuilder.class);
                    for (CommandGenerator currentCommandGenerator : commandGenerators.values()) {
                        generatorBuilder.registerBuilder(currentCommandGenerator);
                    }
                } catch (Exception e) {
                    LOG.error("Error registering Inputs in module {}", modConfiguration, e);
                }

            }
        }
    }

    @Override
    public final void registerScreens(Nifty nifty) {
        if (modConfiguration.getScreenDeclarations() != null) {
            BeanFactory ctx = modConfiguration.getBeanFactory();

            ScreenFlowManager screenFlowManager = ctx.getBean("common.ScreenFlowManager", ScreenFlowManager.class);

            for (ModScreenConfiguration currentScreenConfig : modConfiguration.getScreenDeclarations()) {
                //nifty.registerScreenController();
                try {
                    screenFlowManager.addScreenDefinition(new ScreenDefinition(currentScreenConfig.getScreenName(), currentScreenConfig.getController(), currentScreenConfig.getInterfaceConstructor()));
                } catch (InstanceResolutionException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public final void registerFlow(Nifty nifty) {
        if (modConfiguration.getModuleScreenFlow() != null) {

            BeanFactory ctx = modConfiguration.getBeanFactory();

            ScreenFlowManager screenFlowManager = ctx.getBean("common.ScreenFlowManager", ScreenFlowManager.class);
            screenFlowManager.addFlowDefinition(modConfiguration.getModName(), of(modConfiguration.getModuleFlowRoot()), modConfiguration.getModuleScreenFlow());

        }
    }
}

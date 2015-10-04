package com.navid.trafalgar.modapi;

import java.util.List;
import org.springframework.beans.factory.BeanFactory;

public final class ModConfiguration {

    private String modName;

    private String modPreGameSpringConfig;

    private String modGameSpringConfig;

    private String buildersSpringConfig;

    private List<ModScreenConfiguration> screenConfigurations;

    private List<ModScreenConfiguration> screenDeclarations;

    private String moduleFlowRoot;

    private List<String> moduleScreenFlow;

    private List<ModMusicScenario> musicAmbients;

    private BeanFactory beanFactory;

    /**
     * @return the modName
     */
    public String getModName() {
        return modName;
    }

    /**
     * @param modName the modName to set
     */
    public void setModName(String modName) {
        this.modName = modName;
    }

    /**
     * @return the screenConfigurations
     */
    public List<ModScreenConfiguration> getScreenConfigurations() {
        return screenConfigurations;
    }

    /**
     * @param screenConfigurations the screenConfigurations to set
     */
    public void setScreenConfigurations(List<ModScreenConfiguration> screenConfigurations) {
        this.screenConfigurations = screenConfigurations;
    }

    /**
     * @return the modPreGameSpringConfig
     */
    public String getModPreGameSpringConfig() {
        return modPreGameSpringConfig;
    }

    /**
     * @param modPreGameSpringConfig the modPreGameSpringConfig to set
     */
    public void setModPreGameSpringConfig(String modPreGameSpringConfig) {
        this.modPreGameSpringConfig = modPreGameSpringConfig;
    }

    /**
     * @return the modGameSpringConfig
     */
    public String getModGameSpringConfig() {
        return modGameSpringConfig;
    }

    /**
     * @param modGameSpringConfig the modGameSpringConfig to set
     */
    public void setModGameSpringConfig(String modGameSpringConfig) {
        this.modGameSpringConfig = modGameSpringConfig;
    }

    /**
     * @return the buildersSpringConfig
     */
    public String getBuildersSpringConfig() {
        return buildersSpringConfig;
    }

    /**
     * @param buildersSpringConfig the buildersSpringConfig to set
     */
    public void setBuildersSpringConfig(String buildersSpringConfig) {
        this.buildersSpringConfig = buildersSpringConfig;
    }

    /**
     * @return the screenDeclarations
     */
    public List<ModScreenConfiguration> getScreenDeclarations() {
        return screenDeclarations;
    }

    /**
     * @param screenDeclarations the screenDeclarations to set
     */
    public void setScreenDeclarations(List<ModScreenConfiguration> screenDeclarations) {
        this.screenDeclarations = screenDeclarations;
    }

    /**
     * @return the moduleScreenFlow
     */
    public List<String> getModuleScreenFlow() {
        return moduleScreenFlow;
    }

    /**
     * @param moduleScreenFlow the moduleScreenFlow to set
     */
    public void setModuleScreenFlow(List<String> moduleScreenFlow) {
        this.moduleScreenFlow = moduleScreenFlow;
    }

    /**
     * @return the beanFactory
     */
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    /**
     * @param beanFactory the beanFactory to set
     */
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public String getModuleFlowRoot() {
        return moduleFlowRoot;
    }

    public void setModuleFlowRoot(String moduleFlowRoot) {
        this.moduleFlowRoot = moduleFlowRoot;
    }

    @Override
    public String toString() {
        return "Module:" + modName;
    }

    public List<ModMusicScenario> getMusicAmbients() {
        return musicAmbients;
    }

    public void setMusicAmbients(List<ModMusicScenario> musicAmbients) {
        this.musicAmbients = musicAmbients;
    }
}

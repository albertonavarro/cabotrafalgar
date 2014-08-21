package com.navid.trafalgar.mod.counterclock;

import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.persistence.CompetitorInfo;
import com.navid.trafalgar.persistence.RecordPersistenceService;
import com.navid.trafalgar.persistence.localfile.FileRecordPersistenceService;
import com.navid.trafalgar.persistence.recordserver.RecordServerPersistenceService;
import com.navid.trafalgar.screenflow.ScreenFlowManager;
import com.navid.trafalgar.util.FileUtils;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.RadioButtonStateChangedEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.xml.XmlBeanFactory;

/**
 *
 * @author anf
 */
public class ScreenSelectMap implements ScreenController, BeanFactoryAware{

    private Nifty nifty;
    private Screen screen;
    private String selectedMap;
    private Boolean showGhost = Boolean.TRUE;
    private static final Logger logger = Logger.getLogger(ScreenSelectMap.class.getName());
    
    /**
     * 
     */
    @Autowired
    private ScreenFlowManager screenFlowManager;
    
    @Autowired
    private RecordServerPersistenceService persistence;
    
    @Autowired
    private GameConfiguration gameConfiguration;
    
    private XmlBeanFactory beanFactory;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        ListBox dropDown1 = screen.findNiftyControl("dropDown1", ListBox.class);
        dropDown1.addAllItems(getMaps());
        setSelectedMap((String) dropDown1.getSelection().get(0));
    }

    @Override
    public void onEndScreen() {
        ListBox dropDown1 = screen.findNiftyControl("dropDown1", ListBox.class);
        dropDown1.clear();
    }

    public void goTo(String nextScreen) {
        gameConfiguration.setMap(selectedMap);
        gameConfiguration.setShowGhost(showGhost);
        nifty.gotoScreen(nextScreen);  // switch to another screen
    }

    @NiftyEventSubscriber(id = "dropDown1")
    public void onMapSelectionChanged(final String id, final ListBoxSelectionChangedEvent<String> event) {
        List<String> selection = event.getSelection();
        setSelectedMap(selection.get(0));
    }
    
    @NiftyEventSubscriber(id = "noghost")
    public void onNoGhost(final String id, final RadioButtonStateChangedEvent event) {
        showGhost = Boolean.FALSE;
    }

    @NiftyEventSubscriber(id = "besttime")
    public void onBestTime(final String id, final RadioButtonStateChangedEvent event) {
        showGhost = Boolean.TRUE;
    }

    private List<String> getMaps() {
        List<String> result = FileUtils.findFilesInFolder("Games/Millestone2/", false);
        return result;
    }
    
    public void next() {
        screenFlowManager.changeNextScreen();
        goTo("redirector");
    }
    
    private void setSelectedMap(String map) {

        List<CompetitorInfo> list = persistence.getTopCompetitors(4, map);

        ListBox dropDown1 = screen.findNiftyControl("listLocalTimes", ListBox.class);
        dropDown1.clear();
        for (CompetitorInfo currentTime : list) {
            dropDown1.addItem(currentTime.getTime());
        }

        selectedMap = map;

    }

    /**
     * @param gameConfiguration the gameConfiguration to set
     */
    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (XmlBeanFactory) beanFactory;
    }

    /**
     * @param persistence the persistence to set
     */
    public void setPersistence(RecordServerPersistenceService persistence) {
        this.persistence = persistence;
    }

    /**
     * @param screenFlowManager the screenFlowManager to set
     */
    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }
    
    

}

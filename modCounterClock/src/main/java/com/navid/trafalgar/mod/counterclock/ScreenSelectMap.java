package com.navid.trafalgar.mod.counterclock;

import com.navid.trafalgar.definition2.Entry;
import com.navid.trafalgar.model.Builder2;
import com.navid.trafalgar.model.CandidateRecord;
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
import static java.util.Collections.singleton;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author anf
 */
public class ScreenSelectMap implements ScreenController {

    /**
     * @param builder the builder to set
     */
    public void setBuilder(Builder2 builder) {
        this.builder = builder;
    }

    private enum ShowGhost {

        noGhost, bestLocal, bestRemote
    };

    private Nifty nifty;
    private Screen screen;
    private String selectedMap;
    private ShowGhost ghostOptions = ShowGhost.noGhost;
    private static final Logger logger = Logger.getLogger(ScreenSelectMap.class.getName());

    /**
     *
     */
    @Autowired
    private ScreenFlowManager screenFlowManager;

    @Resource
    private FileRecordPersistenceService localPersistence;

    @Resource
    private RecordServerPersistenceService remotePersistence;

    @Autowired
    private GameConfiguration gameConfiguration;

    @Autowired
    private Builder2 builder;

    @Override
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
    }

    @Override
    public void onStartScreen() {
        gameConfiguration.reset();

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

        CandidateRecord cr = null;
        if (ghostOptions == ShowGhost.bestLocal) {
            cr = localPersistence.getGhost(1, selectedMap);
        } else if (ghostOptions == ShowGhost.bestRemote) {
            cr = remotePersistence.getGhost(1, selectedMap);
        }

        if (cr != null) {
            gameConfiguration.getPreGameModel().addToModel(singleton(cr));
        }

        nifty.gotoScreen(nextScreen);  // switch to another screen
    }

    @NiftyEventSubscriber(id = "dropDown1")
    public void onMapSelectionChanged(final String id, final ListBoxSelectionChangedEvent<String> event) {
        List<String> selection = event.getSelection();
        setSelectedMap(selection.get(0));
    }

    @NiftyEventSubscriber(id = "noGhost")
    public void onNoGhost(final String id, final RadioButtonStateChangedEvent event) {
        if (event.isSelected()) {
            ghostOptions = ShowGhost.noGhost;
        }
    }

    @NiftyEventSubscriber(id = "bestLocal")
    public void onBestLocal(final String id, final RadioButtonStateChangedEvent event) {
        if (event.isSelected()) {
            ghostOptions = ShowGhost.bestLocal;
        }
    }

    @NiftyEventSubscriber(id = "bestRemote")
    public void onBestRemote(final String id, final RadioButtonStateChangedEvent event) {
        if (event.isSelected()) {
            ghostOptions = ShowGhost.bestRemote;
        }
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

        List<CompetitorInfo> listLocal = localPersistence.getTopCompetitors(4, map);
        ListBox listLocalTimes = screen.findNiftyControl("listLocalTimes", ListBox.class);
        listLocalTimes.clear();
        for (CompetitorInfo currentTime : listLocal) {
            listLocalTimes.addItem(currentTime);
        }

        List<CompetitorInfo> listRemote = remotePersistence.getTopCompetitors(4, map);
        ListBox listRemoteTimes = screen.findNiftyControl("listRemoteTimes", ListBox.class);
        listRemoteTimes.clear();
        for (CompetitorInfo currentTime : listRemote) {
            listRemoteTimes.addItem(currentTime);
        }

        selectedMap = map;
    }

    /**
     * @param gameConfiguration the gameConfiguration to set
     */
    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    /**
     * @param screenFlowManager the screenFlowManager to set
     */
    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }

    /**
     * @param localPersistence the localPersistence to set
     */
    public void setLocalPersistence(FileRecordPersistenceService localPersistence) {
        this.localPersistence = localPersistence;
    }

    /**
     * @param remotePersistence the remotePersistence to set
     */
    public void setRemotePersistence(RecordServerPersistenceService remotePersistence) {
        this.remotePersistence = remotePersistence;
    }

}

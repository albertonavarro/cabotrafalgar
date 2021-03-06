package com.navid.trafalgar.mod.counterclock;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.jme3.asset.AssetManager;
import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.trafalgar.gamemanager.GameManagerService;
import com.navid.trafalgar.lazylogin.LazyLoginService;
import com.navid.trafalgar.maploader.v3.MapDefinition;
import com.navid.trafalgar.mod.common.GameMenuController;
import com.navid.trafalgar.mod.counterclock.localfile.FileRecordPersistenceService;
import com.navid.trafalgar.mod.counterclock.statelisteners.CommonModServerControllerHelper;
import com.navid.trafalgar.model.CandidateRecord;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.model.ModelBuilder;
import com.navid.trafalgar.persistence.CompetitorInfo;
import com.navid.trafalgar.persistence.RecordServerStatusChange;
import com.navid.trafalgar.persistence.recordserver.RecordServerPersistenceService;
import com.navid.trafalgar.persistence.recordserver.RecordServerPersistenceServiceHystrixProxy;
import com.navid.trafalgar.profiles.ProfileManager;
import com.navid.trafalgar.util.FileUtils;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.ListBoxSelectionChangedEvent;
import de.lessvoid.nifty.controls.RadioButtonStateChangedEvent;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.ImageRenderer;
import de.lessvoid.nifty.render.NiftyImage;
import org.bushe.swing.event.EventService;
import org.bushe.swing.event.EventSubscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singleton;

public final class ScreenSelectMap extends GameMenuController {

    private static final int MAX_COMPETITORS = 5;

    private enum ShowGhost {
        noGhost, bestLocal, bestRemote
    };

    private ListItem selectedMap;
    private ShowGhost ghostOptions = ShowGhost.bestLocal;
    private final Map<String, NiftyImage> loadedImages = new HashMap<String, NiftyImage>();
    private static final Logger LOG = LoggerFactory.getLogger(ScreenSelectMap.class);

    @Resource
    private FileRecordPersistenceService localPersistence;

    @Resource
    private RecordServerPersistenceServiceHystrixProxy remotePersistence;

    @Autowired
    private GameConfiguration gameConfiguration;

    @Resource
    private AssetManager assetManager;

    @Autowired
    private ModelBuilder builder;

    @Autowired
    private ProfileManager profileManager;

    @Autowired
    private EventService eventService;

    @Autowired
    private LazyLoginService lazyLoginService;

    @Autowired
    private GameManagerService gameManagerService;

    private CommonModServerControllerHelper modHelper;


    @Autowired
    private RecordServerPersistenceService recordServerPersistenceService;


    private ListBox mapDropDown;
    private ListBox listLocalTimes;
    private ListBox listRemoteTimes;
    private Label mapDescription;
    private Element imageMap;

    @Override
    public void doOnStartScreen() {
        imageMap = screen.findElementByName("mapImage");
        listRemoteTimes = screen.findNiftyControl("listRemoteTimes", ListBox.class);
        listLocalTimes = screen.findNiftyControl("listLocalTimes", ListBox.class);
        mapDropDown = screen.findNiftyControl("dropDown1", ListBox.class);

        mapDescription = screen.findNiftyControl("mapDescription", Label.class);
        mapDropDown.addAllItems(getMaps());
        if (mapDropDown.itemCount() > 0) {
            mapDropDown.selectItemByIndex(0);
            setSelectedMap((ListItem) mapDropDown.getSelection().get(0));
        }
        gameConfiguration.getPreGameModel().removeFromModel(CandidateRecord.class);

        modHelper = new CommonModServerControllerHelper(eventService, recordServerPersistenceService, lazyLoginService, gameManagerService, screen);
    }

    @Override
    public void doOnEndScreen() {
        mapDropDown.clear();
        modHelper.destroy();
    }

    public void next() {
        screenFlowManager.setNextScreenHint(ScreenFlowManager.NEXT);
        goTo("redirector");
    }

    public void goTo(String nextScreen) {
        gameConfiguration.setMap(selectedMap.getName());

        CandidateRecord cr = null;
        if (ghostOptions == ShowGhost.bestLocal) {
            cr = localPersistence.getGhost(1, selectedMap.getName(), gameConfiguration.getShipName(), profileManager.getSessionId());
        } else if (ghostOptions == ShowGhost.bestRemote) {
            cr = remotePersistence.getGhost(1, selectedMap.getName(), gameConfiguration.getShipName(), profileManager.getSessionId());
        }

        if (cr != null) {
            gameConfiguration.getPreGameModel().addToModel(singleton(cr));
        }

        nifty.gotoScreen(nextScreen);  // switch to another screen
    }

    @NiftyEventSubscriber(id = "dropDown1")
    public void onMapSelectionChanged(final String id, final ListBoxSelectionChangedEvent<ListItem> event) {
        if(!event.getSelection().isEmpty()) {
            List<ListItem> selection = event.getSelection();
            setSelectedMap(selection.get(0));
        }
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

    private List<ListItem> getMaps() {
        List<String> result = FileUtils.findFilesInFolder("Maps/CounterClock/", false);
        Collections.sort(result);
        return Lists.transform(result, new Function<String, ListItem>() {
            @Override
            public ListItem apply(String f) {
                LOG.debug("Loading map {}", f);
                return new ListItem(f, f, (MapDefinition) assetManager.loadAsset(f));
            }
        });
    }

    private void setSelectedMap(ListItem map) {

        List<CompetitorInfo> listLocal
                = localPersistence.getTopCompetitors(MAX_COMPETITORS, map.getName(), gameConfiguration.getShipName(), profileManager.getSessionId());
        listLocalTimes.clear();
        for (CompetitorInfo currentTime : listLocal) {
            listLocalTimes.addItem(currentTime);
        }

        List<CompetitorInfo> listRemote
                = remotePersistence.getTopCompetitors(MAX_COMPETITORS, map.getName(), gameConfiguration.getShipName(), profileManager.getSessionId());
        listRemoteTimes.clear();
        for (CompetitorInfo currentTime : listRemote) {
            listRemoteTimes.addItem(currentTime);
        }

        if (map.getMapDefinition().getDescription() == null) {
            mapDescription.setText("Description not available.");
        } else {
            mapDescription.setText(map.getMapDefinition().getDescription());
        }

        imageMap.getRenderer(ImageRenderer.class).setImage(getImageForMap(map.getMapDefinition()));
        selectedMap = map;
    }

    NiftyImage getImageForMap(MapDefinition mapDefinition) {
        if (mapDefinition.getPicture() == null) {
            return null;
        }

        String picturePath = mapDefinition.getPicture();

        if (loadedImages.containsKey(picturePath)) {
            return loadedImages.get(picturePath);
        } else {
            NiftyImage newImage = nifty.getRenderEngine().createImage(screen, picturePath, false);
            loadedImages.put(picturePath, newImage);
            return newImage;
        }
    }

    /**
     * @param gameConfiguration the gameConfiguration to set
     */
    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
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
    public void setRemotePersistence(RecordServerPersistenceServiceHystrixProxy remotePersistence) {
        this.remotePersistence = remotePersistence;
    }

    private static class ListItem {

        private final String name;
        private final String path;
        private final MapDefinition mapDefinition;

        public ListItem(String name, String path, MapDefinition mapDefinition) {
            this.name = name;
            this.path = path;
            this.mapDefinition = mapDefinition;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @return the path
         */
        public String getPath() {
            return path;
        }

        /**
         * @return the mapDefinition
         */
        public MapDefinition getMapDefinition() {
            return mapDefinition;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public void setProfileManager(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    /**
     * @param builder the builder to set
     */
    public void setBuilder(ModelBuilder builder) {
        this.builder = builder;
    }

    /**
     * @param assetManager the assetManager to set
     */
    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public final void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setRecordServerPersistenceService(RecordServerPersistenceService recordServerPersistenceService) {
        this.recordServerPersistenceService = recordServerPersistenceService;
    }

    public LazyLoginService getLazyLoginService() {
        return lazyLoginService;
    }

    public void setLazyLoginService(LazyLoginService lazyLoginService) {
        this.lazyLoginService = lazyLoginService;
    }

    public void setGameManagerService(GameManagerService gameManagerService) {
        this.gameManagerService = gameManagerService;
    }
}

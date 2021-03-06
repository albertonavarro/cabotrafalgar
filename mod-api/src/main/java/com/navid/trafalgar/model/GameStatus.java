package com.navid.trafalgar.model;

import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.navid.trafalgar.maploader.v3.MapDefinition;
import com.navid.trafalgar.manager.statistics.FloatStatistic;
import com.navid.trafalgar.manager.statistics.StatisticsManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 *
 */
public final class GameStatus {

    private FloatStatistic time;
    //Nodes
    private final Node gameNode = new Node("Model Node");
    private final Node gameGUINode = new Node("GUI Node");
    //InitStateListener
    private ViewPort viewPort;
    private ViewPort viewPortGUI;
    //InitStateListener
    private Camera camera;
    private Camera cameraGUI;
    private MapDefinition gameDefinition;

    /**
     *
     */
    public GameStatus() {
        gameNode.setCullHint(CullHint.Never);

        gameGUINode.setQueueBucket(Bucket.Gui);
        gameGUINode.setCullHint(CullHint.Never);

    }

    /**
     *
     * @param statisticsManager
     */
    @Autowired
    public void setStatisticsManager(StatisticsManager statisticsManager) {
        time = statisticsManager.createStatistic("root", "time", 0f);
    }

    /**
     *
     * @return
     */
    public Node getGameNode() {
        return gameNode;
    }

    /**
     *
     * @return
     */
    public ViewPort getViewPort() {
        return viewPort;
    }

    /**
     *
     * @param viewPort
     */
    public void setViewPort(ViewPort viewPort) {
        this.viewPort = viewPort;
    }

    /**
     *
     * @return
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     *
     * @param camera
     */
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     *
     * @return
     */
    public Node getGameGUINode() {
        return gameGUINode;
    }

    /**
     *
     * @param cameraGUI
     */
    public void setCameraGUI(Camera cameraGUI) {
        this.cameraGUI = cameraGUI;
    }

    /**
     *
     * @param viewPortGUI
     */
    public void setViewPortGUI(ViewPort viewPortGUI) {
        this.viewPortGUI = viewPortGUI;
    }

    /**
     * @return the viewPortGUI
     */
    public ViewPort getViewPortGUI() {
        return viewPortGUI;
    }

    /**
     * @return the cameraGUI
     */
    public Camera getCameraGUI() {
        return cameraGUI;
    }

    /**
     * @return the time
     */
    public FloatStatistic getTime() {
        return time;
    }

    /**
     * @param time the time to set
     */
    public void setTime(FloatStatistic time) {
        this.time = time;
    }

    public void setGameDefinition(MapDefinition gameDefinition) {
        this.gameDefinition = gameDefinition;
    }
}

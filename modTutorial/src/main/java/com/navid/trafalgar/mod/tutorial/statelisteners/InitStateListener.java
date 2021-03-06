package com.navid.trafalgar.mod.tutorial.statelisteners;

import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.system.AppSettings;
import com.navid.trafalgar.manager.InitState;
import com.navid.trafalgar.model.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;

public final class InitStateListener implements InitState {

    @Autowired
    private RenderManager renderManager;
    @Autowired
    private GameStatus gameStatus;
    @Autowired
    private AppSettings settings;

    @Override
    public void onInit(float tpf) {

        Camera camera = new Camera(settings.getWidth(), settings.getHeight());
        camera.setFrustumPerspective(45f, (float) camera.getWidth() / camera.getHeight(), 1f, 1000f);
        camera.setFrustumFar(10000);

        ViewPort viewPort = renderManager.createMainView("Tutorial", camera);
        viewPort.setClearFlags(true, true, true);
        viewPort.setBackgroundColor(ColorRGBA.Blue);
        viewPort.attachScene(gameStatus.getGameNode());

        Camera cameraGUI = new Camera(settings.getWidth(), settings.getHeight());

        ViewPort viewPortGUI = renderManager.createPostView("TutorialGUI", cameraGUI);
        viewPortGUI.setClearFlags(false, false, false);
        viewPortGUI.attachScene(gameStatus.getGameGUINode());

        gameStatus.setCamera(camera);
        gameStatus.setViewPort(viewPort);
        gameStatus.setCameraGUI(cameraGUI);
        gameStatus.setViewPortGUI(viewPortGUI);
    }

    @Override
    public void onUnload() {

        renderManager.removeMainView("Tutorial");
        renderManager.removePostView("TutorialGUI");

        gameStatus.setCamera(null);
        gameStatus.setViewPort(null);
        gameStatus.setCameraGUI(null);
        gameStatus.setViewPortGUI(null);

        gameStatus.getGameNode().detachAllChildren();
        gameStatus.getGameGUINode().detachAllChildren();

    }

    /**
     * @param renderManager the renderManager to set
     */
    public void setRenderManager(RenderManager renderManager) {
        this.renderManager = renderManager;
    }

    /**
     * @param gameStatus the gameStatus to set
     */
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    /**
     * @param settings the settings to set
     */
    public void setSettings(AppSettings settings) {
        this.settings = settings;
    }
}

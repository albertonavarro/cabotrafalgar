package com.navid.trafalgar.mod.windtunnel.statelisteners;

import com.jme3.app.Application;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.system.AppSettings;
import com.navid.trafalgar.manager.InitState;
import com.navid.trafalgar.model.GameConfiguration;
import com.navid.trafalgar.model.GameStatus;

/**
 *
 * @author anf
 */
public class InitStateListener implements InitState {

    private RenderManager renderManager;
    private GameStatus gameStatus;
    private GameConfiguration gameConfiguration;
    private AppSettings settings;

    public InitStateListener(Application app, GameStatus gameStatus, GameConfiguration gameConfiguration) {

        this.renderManager = app.getRenderManager();
        this.settings = gameConfiguration.getAppSettings();
        this.gameStatus = gameStatus;
        this.gameConfiguration = gameConfiguration;
    }

    public void onInit(float tpf) {

        Camera camera = new Camera(settings.getWidth(), settings.getHeight());
        camera.setFrustumPerspective(45f, (float) camera.getWidth() / camera.getHeight(), 1f, 1000f);
        camera.setFrustumFar(10000);

        ViewPort viewPort = renderManager.createMainView("CounterClock", camera);
        viewPort.setClearFlags(true, true, true);
        viewPort.setBackgroundColor(ColorRGBA.Blue);
        viewPort.attachScene(gameStatus.getGameNode());

        Camera cameraGUI = new Camera(settings.getWidth(), settings.getHeight());
        
        ViewPort viewPortGUI = renderManager.createPostView("CounterClockGUI", cameraGUI);
        viewPortGUI.setClearFlags(false, false, false);
        viewPortGUI.attachScene(gameStatus.getGameGUINode());

        gameStatus.setCamera(camera);
        gameStatus.setViewPort(viewPort);
        gameStatus.setCameraGUI(cameraGUI);
        gameStatus.setViewPortGUI(viewPortGUI);
    }

    public void onUnload() {
        
        renderManager.removeMainView("CounterClock");
        renderManager.removePostView("CounterClockGUI");
        
        gameStatus.setCamera(null);
        gameStatus.setViewPort(null);
        gameStatus.setCameraGUI(null);
        gameStatus.setViewPortGUI(null);
        
        gameStatus.getGameNode().detachAllChildren();
        gameStatus.getGameGUINode().detachAllChildren();

    }
}

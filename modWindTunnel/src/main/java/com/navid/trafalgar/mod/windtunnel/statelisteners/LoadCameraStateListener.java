package com.navid.trafalgar.mod.windtunnel.statelisteners;

import com.jme3.app.Application;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.navid.trafalgar.camera.ChaseCamera;
import com.navid.trafalgar.manager.EventListener;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.model.GameStatus;
import com.navid.trafalgar.manager.LoadCamState;
import com.navid.trafalgar.manager.StartedState;
import com.navid.trafalgar.model.AShipModel;
import java.util.Collection;

/**
 *
 * @author anf
 */
public class LoadCameraStateListener implements LoadCamState, StartedState, EventListener {
    
    private static enum Cameras {NONE, FLYCAM, TARGETCAM, CHASECAM};

    private GameStatus gameStatus;
    private InputManager inputManager;
    private EventManager eventManager;
    
    private FlyByCamera flyCamControl;
    private ChaseCamera chaseCamControl;
    
    private Camera camera;
    private AShipModel player;
    private Collection<? extends Node> targets;
    private String[] mappings = {"Cam1",  "Cam2"};
    
    private Cameras selectedCam = Cameras.NONE;

    public LoadCameraStateListener(Application app, GameStatus gameStatus, EventManager eventManager) {
        this.inputManager = app.getInputManager();
        this.gameStatus = gameStatus;
        this.eventManager = eventManager;
    }
    private ActionListener actionListener = new ActionListener() {

        public void onAction(String name, boolean isPressed, float tpf) {
            if (isPressed) {
                
                if (name.equals("Cam1")) {
                    eventManager.fireEvent("DEACTIVATE_CAM");
                    eventManager.fireEvent("ACTIVATE_CAM1");
                }
                if (name.equals("Cam2")) {
                    eventManager.fireEvent("DEACTIVATE_CAM");
                    eventManager.fireEvent("ACTIVATE_CAM2");
                }
                
            }
        }
    };

    public void onLoadCam(float tpf) {
        
        this.camera = gameStatus.getCamera();
        this.player = gameStatus.getPlayerNode();
        this.targets = gameStatus.getTargetsNode();

        // Create a flying cam
        flyCamControl = new FlyByCamera(camera);
        flyCamControl.registerWithInput(inputManager);
        flyCamControl.setMoveSpeed(200);
        flyCamControl.setDragToRotate(true);


        // Enable a chasing cam
        chaseCamControl = new ChaseCamera(camera, player, inputManager);
        chaseCamControl.setSmoothMotion(true);
        chaseCamControl.setMaxDistance(100);

        inputManager.addListener(actionListener, mappings); // load my custom keybinding

        eventManager.registerListener(this, new String[]{"DEACTIVATE_CAM", "ACTIVATE_CAM1",  "ACTIVATE_CAM2"});
        eventManager.fireEvent("DEACTIVATE_CAM");
        eventManager.fireEvent("ACTIVATE_CAM2");
    }

    public void onStarted(float tpf) {
        
    }

    public void onUnload() {
        //flyCamControl.unregisterInput();
        gameStatus.getGameNode().removeControl(chaseCamControl);
        inputManager.removeListener(actionListener);
        eventManager.fireEvent("DEACTIVATE_CAM");
    }

    public void onEvent(String event) {
        if ("DEACTIVATE_CAM".equals(event)) {
            flyCamControl.setEnabled(false);
            chaseCamControl.setEnabled(false);
            flyCamControl.setDragToRotate(true);

        } else if ("ACTIVATE_CAM1".equals(event)) {
            selectedCam = Cameras.FLYCAM;
            flyCamControl.setEnabled(true);

        } else if ("ACTIVATE_CAM2".equals(event)) {
            selectedCam = Cameras.CHASECAM;
            chaseCamControl.setEnabled(true);

        }
    }
}

package com.navid.trafalgar.mod.counterclock.statelisteners;

import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.navid.trafalgar.camera.ChaseCamera;
import com.navid.trafalgar.manager.EventListener;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.LoadCamState;
import com.navid.trafalgar.manager.StartedState;
import com.navid.trafalgar.mod.counterclock.CounterClockGameModel;
import com.navid.trafalgar.mod.counterclock.camera.TargetCamera;
import com.navid.trafalgar.model.AShipModel;
import com.navid.trafalgar.model.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;

public final class LoadCameraStateListener implements LoadCamState, StartedState, EventListener {

    /**
     * @param gameModel the gameModel to set
     */
    public void setGameModel(CounterClockGameModel gameModel) {
        this.gameModel = gameModel;
    }

    /**
     * @param gameStatus the gameStatus to set
     */
    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    /**
     * @param inputManager the inputManager to set
     */
    public void setInputManager(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    /**
     * @param eventManager the eventManager to set
     */
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    private static enum Cameras {

        NONE, FLYCAM, TARGETCAM, CHASECAM
    };

    @Autowired
    private GameStatus gameStatus;
    @Autowired
    private InputManager inputManager;
    @Autowired
    private EventManager eventManager;

    private FlyByCamera flyCamControl;
    private TargetCamera targetCamControl;
    private ChaseCamera chaseCamControl;
    private Camera camera;
    private AShipModel player;
    private Collection<? extends Node> targets;
    private final String[] mappings = {"Cam1", "Cam2", "Cam3"};
    private Cameras selectedCam = Cameras.NONE;

    @Autowired
    private CounterClockGameModel gameModel;

    private final ActionListener actionListener = new ActionListener() {

        @Override
        public void onAction(String name, boolean isPressed, float tpf) {
            if (isPressed) {

                if (name.equals("Cam1")) {
                    setCamera1();
                }
                if (name.equals("Cam2")) {
                    setCamera2();
                }
                if (name.equals("Cam3")) {
                    setCamera3();
                }
            }
        }
    };

    public void setCamera1() {
        eventManager.fireEvent("DEACTIVATE_CAM");
        eventManager.fireEvent("ACTIVATE_CAM1");
    }

    public void setCamera2() {
        eventManager.fireEvent("DEACTIVATE_CAM");
        eventManager.fireEvent("ACTIVATE_CAM2");
    }

    public void setCamera3() {
        if (selectedCam == Cameras.TARGETCAM) {
            eventManager.fireEvent(EventManager.VIEW_NEXTTARGET);
        } else {
            eventManager.fireEvent("DEACTIVATE_CAM");
            eventManager.fireEvent("ACTIVATE_CAM3");
        }
    }

    @Override
    public void onLoadCam(float tpf) {

        this.camera = gameStatus.getCamera();
        this.player = (AShipModel) gameModel.getShip();
        this.targets = gameModel.getMilestones();

        // Create a flying cam
        flyCamControl = new FlyByCamera(camera);
        flyCamControl.registerWithInput(inputManager);
        flyCamControl.setMoveSpeed(200);
        flyCamControl.setDragToRotate(true);

        //Create a target cam
        targetCamControl = new TargetCamera(camera, player, new ArrayList(targets), eventManager);

        // Enable a chasing cam
        chaseCamControl = new ChaseCamera(camera, player, inputManager);
        chaseCamControl.setSmoothMotion(true);

        gameStatus.getGameNode().addControl(targetCamControl);

        inputManager.addListener(actionListener, mappings); // load my custom keybinding

        eventManager.registerListener(this,
                new String[]{"DEACTIVATE_CAM", "ACTIVATE_CAM1", "ACTIVATE_CAM2", "ACTIVATE_CAM3", "PAUSE", "RESUME"});
        eventManager.fireEvent("DEACTIVATE_CAM");
        eventManager.fireEvent("ACTIVATE_CAM3");
    }

    @Override
    public void onStarted(float tpf) {
    }

    @Override
    public void onUnload() {
        //flyCamControl.unregisterInput();

        gameStatus.getGameNode().removeControl(chaseCamControl);
        gameStatus.getGameNode().removeControl(targetCamControl);

        inputManager.removeListener(actionListener);
        eventManager.fireEvent("DEACTIVATE_CAM");
    }

    @Override
    public void onEvent(String event) {
        if ("DEACTIVATE_CAM".equals(event)) {
            flyCamControl.setEnabled(false);
            chaseCamControl.setEnabled(false);
            targetCamControl.setEnabled(false);
            flyCamControl.setDragToRotate(true);

        } else if ("ACTIVATE_CAM1".equals(event)) {
            selectedCam = Cameras.FLYCAM;
            flyCamControl.setEnabled(true);

        } else if ("ACTIVATE_CAM2".equals(event)) {
            selectedCam = Cameras.CHASECAM;
            chaseCamControl.setEnabled(true);

        } else if ("ACTIVATE_CAM3".equals(event)) {
            selectedCam = Cameras.TARGETCAM;
            targetCamControl.setEnabled(true);
        } else if ("PAUSE".equals(event)) {
            if (selectedCam == Cameras.FLYCAM) {
                flyCamControl.setEnabled(false);
            }
        } else if ("RESUME".equals(event)) {
            if (selectedCam == Cameras.FLYCAM) {
                flyCamControl.setEnabled(true);
            }
        }
    }
}

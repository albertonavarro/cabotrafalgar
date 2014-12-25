package com.navid.trafalgar.camera;

import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;

public final class ChaseCamera extends com.jme3.input.ChaseCamera {

    public ChaseCamera(Camera camera, Node target) {
        super(camera, target);
    }

    public ChaseCamera(Camera camera, Node target, InputManager inputManager) {
        super(camera, target, inputManager);
    }

    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (dragToRotate) {
            if (name.equals(ChaseCamToggleRotate) && enabled) {
                if (keyPressed) {
                    canRotate = true;
                } else {
                    canRotate = false;
                }
            }
        }
    }
}

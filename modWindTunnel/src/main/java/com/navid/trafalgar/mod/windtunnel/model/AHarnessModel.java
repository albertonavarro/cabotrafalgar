package com.navid.trafalgar.mod.windtunnel.model;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.input.controls.AnalogListener;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.navid.trafalgar.model.AShipModel;
import com.navid.trafalgar.model.Dependent;
import com.navid.trafalgar.model.GameModel;

/**
 *
 * @author alberto
 */
public abstract class AHarnessModel extends Node implements Control, Dependent {

    private Node target;

    public AHarnessModel(AssetManager assetManager) {
        super();
        initGeometry(assetManager);
    }

    public void registerInputManager(InputManager inputManager) {
        inputManager.addListener(new AnalogListener() {

            @Override
            public void onAnalog(String string, float f, float f1) {
                target.rotate(0, f, 0);
            }
        }, new String[]{"WindTunnel_RotateLeft"});

        inputManager.addListener(new AnalogListener() {

            @Override
            public void onAnalog(String string, float f, float f1) {
                target.rotate(0, -f, 0);
            }
        }, new String[]{"WindTunnel_RotateRight"});
    }

    protected abstract void initGeometry(AssetManager assetManager);

    @Override
    public final void update(float tpf) {
        target.setLocalTranslation(this.getWorldTranslation());
    }

    public void setTarget(Node target) {
        this.target = target;
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setSpatial(Spatial spatial) {
    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void resolveDependencies(GameModel gameModel) {
        this.target = (Node) gameModel.getSingleByType(AShipModel.class);
    }
}

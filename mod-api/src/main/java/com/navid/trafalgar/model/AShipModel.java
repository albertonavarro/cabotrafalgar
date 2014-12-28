package com.navid.trafalgar.model;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.navid.trafalgar.manager.EventManager;

public abstract class AShipModel extends TrafalgarNode implements Control, Dependent {

    private final String role;

    private IContext context;

    private IWind.WindGeometry windGeometry;

    public AShipModel(String role, Vector3f lookAt, AssetManager assetManager, EventManager eventManager) {
        super(lookAt, assetManager, eventManager);
        this.role = role;
    }

    public abstract void setWindNode(IWind.WindGeometry windGeometry);

    @Override
    public final Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public final void setSpatial(Spatial spatial) {
    }

    @Override
    public final void render(RenderManager rm, ViewPort vp) {
    }

    @Override
    public final void resolveDependencies(GameModel gameModel) {
        this.context = (IContext) gameModel.getSingleByType(IContext.class);
        windGeometry = this.getContext().getWind().createWindGeometryNode();
        windGeometry.setLocalTranslation(-20, 20, 0);
        this.attachChild(windGeometry);
    }

    @Override
    public void update(float tpf) {
        windGeometry.update(tpf);
    }

    public final Object getRole() {
        return role;
    }

    public abstract void setTransparent(boolean enabled);

    public abstract void updateFromRecord(StepRecord currentStepRecord);

    public abstract CandidateRecord getCandidateRecordInstance();

    public abstract StepRecord getSnapshot();

    /**
     * @return the context
     */
    public final IContext getContext() {
        return context;
    }

}

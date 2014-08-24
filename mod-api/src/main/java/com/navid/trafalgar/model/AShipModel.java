package com.navid.trafalgar.model;

import com.jme3.asset.AssetManager;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.manager.statistics.StatisticsManager;

/**
 *
 * @author alberto
 */
public abstract class AShipModel extends TrafalgarNode implements Control, Dependent {
    
    private final String role;

    protected IContext context;
    
    protected StatisticsManager statisticsManager;

    public AShipModel(String role, Vector3f lookAt, AssetManager assetManager, EventManager eventManager) {     
        super(lookAt, assetManager, eventManager);
        this.role = role;
    }

    public abstract void registerInput(InputManager inputManager);

    public abstract float getSpeed();

    public abstract void setTransparent(boolean b);




    public final boolean isEnabled() {
        return true;
    }

    public void setEnabled(boolean value) {
        //todo
    }

    public void setStatisticsManager(StatisticsManager statisticsManager) {
        this.statisticsManager = statisticsManager;

        initStatisticsManager();
    }

    protected abstract void initStatisticsManager();

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
    
    IWind.WindGeometry windGeometry;
    
    @Override
    public void resolveDependencies(GameModel gameModel) {
        this.context = (IContext) gameModel.getSingleByType(IContext.class);
        windGeometry = this.context.getWind().createWindGeometryNode();
        windGeometry.setLocalTranslation(-20, 20, 0);
        this.attachChild(windGeometry);
    }
    
    @Override
    public void update(float tpf) {
       windGeometry.update(tpf);
    }

    public Object getRole() {
        return role;
    }

}

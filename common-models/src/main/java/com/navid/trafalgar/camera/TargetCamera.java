package com.navid.trafalgar.camera;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.input.InputManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.navid.trafalgar.manager.EventListener;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.model.AMilestoneModel;
import com.navid.trafalgar.model.AShipModel;

import java.io.IOException;
import java.util.List;

import static com.navid.trafalgar.manager.EventManager.MILESTONE_REACHED;
import static com.navid.trafalgar.manager.EventManager.VIEW_NEXTTARGET;

public final class TargetCamera implements Control, EventListener {

    private Camera cam;
    private AShipModel reference;
    private List<Node> targets;
    private Node currentTarget;
    private boolean enabled;

    public TargetCamera(Camera cam, AShipModel reference, EventManager eventManager) {
        this.cam = cam;
        this.reference = reference;
        eventManager.registerListener(this, new String[]{VIEW_NEXTTARGET, MILESTONE_REACHED});
    }

    public TargetCamera(Camera cam, AShipModel reference, List<Node> target, EventManager eventManager) {
        this(cam, reference, eventManager);
        setTarget(target);
    }

    public void setTarget(List<Node> target) {
        this.targets = target;
        if (target != null && !targets.isEmpty()) {
            currentTarget = targets.get(0);
        }
    }

    @Override
    public void update(float tpf) {

        if (enabled && currentTarget != null) {
            Vector3f referencePosition = reference.getWorldTranslation();
            Vector3f difference = currentTarget.getWorldTranslation().subtract(reference.getWorldTranslation());

            Vector3f normal = difference.normalize().mult(100);
            normal.y = -25;

            cam.setLocation(referencePosition.subtract(normal));

            cam.lookAt(currentTarget.getWorldTranslation(), Vector3f.ZERO);

        }

    }

    @Override
    public void render(RenderManager rm, ViewPort vp) {
    }

    @Override
    public void setSpatial(Spatial spatial) {
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;

    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        reference.setTransparent(enabled);

    }

    public void registerWithInput(InputManager inputManager) {
    }

    @Override
    public void onEvent(String event) {
        if (targets != null && !targets.isEmpty()) {

            if (event.equals(MILESTONE_REACHED)) {
                selectNextUnreachedMilestone();
            } else if (event.equals(VIEW_NEXTTARGET)) {
                selectNextMilestone();
            }

        }
    }

    private void selectNextMilestone() {
        int currentIndex = targets.indexOf(currentTarget);
        currentTarget = targets.get((currentIndex + 1) % targets.size());
    }

    private void selectNextUnreachedMilestone() {
        for (int counter = 0; counter < targets.size(); counter++) {
            AMilestoneModel candidate = (AMilestoneModel) targets.get(counter);
            if (!candidate.getState()) {
                currentTarget = candidate;
                break;
            }
        }
    }
}

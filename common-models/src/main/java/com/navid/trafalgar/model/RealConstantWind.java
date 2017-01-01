package com.navid.trafalgar.model;

import com.jme3.asset.AssetManager;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.effect.influencers.RadialParticleInfluencer;
import com.jme3.effect.shapes.EmitterMeshFaceShape;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.scene.shape.Quad;

import static com.google.common.collect.Lists.newArrayList;

public final class RealConstantWind extends IWind implements Dependent {

    private Geometry arrowModel;
    private Vector2f vector = new Vector2f(1, 0);
    private final AssetManager assetManager;
    private Node followNode;

    public RealConstantWind(AssetManager assetManager) {
        this.assetManager = assetManager;
        //arrowModel = new ArrowModel(assetManager, new Vector3f(1, 0, 0));

        arrowModel = null;
        setWind(new Vector2f(1, 0));


    }

    public void rotateWind(float angle) {
        vector.rotateAroundOrigin(angle, false);
        arrowModel.rotate(0, -angle, 0);
    }

    @Override
    public Vector2f getWind() {
        return vector;
    }

    public void increase(float value) {
        vector.multLocal(value);
        arrowModel.scale(value);
    }

    public void setWind(Vector2f newVector) {
        Vector2f oldVector = this.vector;
        this.vector = newVector;

        //arrowModel.rotate(0, newVector.angleBetween(oldVector), 0);
        //arrowModel.scale(newVector.length() / oldVector.length());
    }

    @Override
    public void resolveDependencies(GameModel gameModel) {
        followNode = gameModel.getSingleByTypeAndName(AShipModel.class, "player1");
    }

    @Override
    public void commitDependencies() {
        Vector2f windVector = getWind();
        //arrowModel = new ArrowModel(assetManager, new Vector3f(windVector.x, 0, windVector.y));
        //arrowModel.scale(10);

        ParticleEmitter fire =
                new ParticleEmitter("Emitter", ParticleMesh.Type.Triangle, 5000);
        Material mat_red = new Material(assetManager,
                "Common/MatDefs/Misc/Particle.j3md");
        mat_red.setTexture("Texture", assetManager.loadTexture(
                "Textures/arrow.png"));
        fire.setQueueBucket(RenderQueue.Bucket.Translucent);
        fire.setMaterial(mat_red);
        fire.setEndColor(  new ColorRGBA(0f, 0f, 1f, 0.0f));   // red
        fire.setStartColor(new ColorRGBA(0f, 1f, 0f, 0.3f)); // yellow
        fire.setParticleInfluencer(new RadialParticleInfluencer());
        fire.getParticleInfluencer().setInitialVelocity(new Vector3f(0, 0, 500));
        fire.getParticleInfluencer().setVelocityVariation(0.1f);
        fire.setStartSize(0.3f);
        fire.setParticlesPerSec(1000);
        fire.setEndSize(0.0f);
        fire.setGravity(0, 0, 0);
        fire.setLowLife(1f);
        fire.setHighLife(3f);
        fire.setInWorldSpace(false);
        //fire.setFaceNormal(new Vector3f(windVector.x, 0, windVector.y));
        fire.setShape(new EmitterMeshFaceShape(newArrayList((Mesh)new Quad(150, 150))));
        fire.setFaceNormal(Vector3f.NAN);

        fire.rotate(0, (float) (Math.PI / 2), 0);
        arrowModel = fire;

        this.attachChild(arrowModel);
    }



    @Override
    public void update(float tpf) {
        Vector3f currentWind = arrowModel.getWorldRotation().getRotationColumn(2);
        Vector2f currentWind2f = new Vector2f(currentWind.x, currentWind.z);
        float angle = currentWind2f.angleBetween(getWind());

        arrowModel.setLocalTranslation(followNode.getWorldTranslation());
        arrowModel.move(-100, 0, 100);
    }


}

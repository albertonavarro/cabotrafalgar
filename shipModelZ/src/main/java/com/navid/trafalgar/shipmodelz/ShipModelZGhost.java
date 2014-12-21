package com.navid.trafalgar.shipmodelz;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.model.AShipModelGhost;
import com.navid.trafalgar.model.CandidateRecord;
import java.util.Iterator;

public class ShipModelZGhost extends AShipModelZ implements AShipModelGhost {

    private final CandidateRecord<ShipSnapshot> candidateRecord;

    private final Iterator<ShipSnapshot> iterator;
    private ShipSnapshot currentStep;

    public ShipModelZGhost(final AssetManager assetManager,
            EventManager eventManager,
            CandidateRecord<ShipSnapshot> candidateRecord) {
        super("Ghost", assetManager, eventManager);
        this.candidateRecord = candidateRecord;
        iterator = (Iterator<ShipSnapshot>) candidateRecord.getStepRecord().iterator();

        if (iterator.hasNext()) {
            currentStep = iterator.next();
        }
        
        this.setHullMaterial(new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md") {
            {
                setTexture("DiffuseMap", assetManager.loadTexture("Textures/wood.jpeg"));
            }
        });

        this.setSailMaterial(new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md") {
            {
                setTexture("DiffuseMap", assetManager.loadTexture("Textures/sail.jpg"));
            }
        });
        
        this.setTransparent(true);
    }

    float time = 0;

    @Override
    public void update(float tpf) {
        super.update(tpf);

        time += tpf;
        if (currentStep != null) {
            while (iterator.hasNext() && currentStep.getTimestamp() < time) {
                currentStep = iterator.next();
            }

            updateFromRecord(currentStep);
        }

    }
}

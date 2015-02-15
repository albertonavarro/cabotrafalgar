package com.navid.trafalgar.shipmodelleyton;

import com.jme3.asset.AssetManager;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.model.AShipModelGhost;
import com.navid.trafalgar.model.CandidateRecord;
import java.util.Iterator;

public final class ShipModelTwoGhost extends AShipModelTwo implements AShipModelGhost {

    private final CandidateRecord<ShipSnapshot> candidateRecord;

    private final Iterator<ShipSnapshot> iterator;
    private ShipSnapshot currentStep;
    private float time = 0;

    public ShipModelTwoGhost(AssetManager assetManager,
            EventManager eventManager,
            CandidateRecord<ShipSnapshot> candidateRecord) {
        super("Ghost", assetManager, eventManager);
        this.candidateRecord = candidateRecord;
        iterator = (Iterator<ShipSnapshot>) candidateRecord.getStepRecord().iterator();

        if (iterator.hasNext()) {
            currentStep = iterator.next();
        }
    }

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

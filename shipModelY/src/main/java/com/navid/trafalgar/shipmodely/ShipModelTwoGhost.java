package com.navid.trafalgar.shipmodely;

import com.jme3.asset.AssetManager;
import com.navid.trafalgar.manager.EventManager;
import com.navid.trafalgar.model.CandidateRecord;
import com.navid.trafalgar.shipmodely.ShipModelTwoPlayer.ShipSnapshot;
import java.util.Iterator;

/**
 *
 * @author casa
 */
public class ShipModelTwoGhost extends AShipModelTwo {

    private final CandidateRecord<ShipSnapshot> candidateRecord;

    private final Iterator<ShipSnapshot> iterator;
    private ShipSnapshot currentStep;

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

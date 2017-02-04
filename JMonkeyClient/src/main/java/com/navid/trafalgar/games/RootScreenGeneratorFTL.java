package com.navid.trafalgar.games;

import com.google.common.collect.Lists;
import com.navid.nifty.flow.ScreenFlowManager;
import com.navid.nifty.flow.template.ftl.FtlTemplateGenerator;
import com.navid.trafalgar.input.KeyboardCommandStateListener;
import com.navid.trafalgar.model.GameConfiguration;
import de.lessvoid.nifty.Nifty;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

public final class RootScreenGeneratorFTL extends FtlTemplateGenerator {

    /**
     * Singleton
     */
    @Autowired
    private GameConfiguration gameConfiguration;

    @Autowired
    private ScreenFlowManager screenFlowManager;

    public RootScreenGeneratorFTL(Nifty nifty) throws IOException {
        super(nifty, "/interface_root.xml");
    }

    @Override
    protected Map injectProperties() {
        List<String> unsortedChilden = newArrayList(screenFlowManager.getChildren());
        Collections.sort(unsortedChilden);

        final List<List<String>> partitionedSorted = Lists.partition(unsortedChilden, 4);

        return new HashMap() {{ put("partitionedMods", partitionedSorted);}};
    }

    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    public void setScreenFlowManager(ScreenFlowManager screenFlowManager) {
        this.screenFlowManager = screenFlowManager;
    }
}

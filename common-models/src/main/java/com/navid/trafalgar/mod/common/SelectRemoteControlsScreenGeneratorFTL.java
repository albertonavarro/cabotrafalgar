package com.navid.trafalgar.mod.common;

import com.google.common.collect.Lists;
import com.navid.nifty.flow.template.ftl.FtlTemplateGenerator;
import com.navid.trafalgar.input.KeyboardCommandStateListener;
import com.navid.trafalgar.model.GameConfiguration;
import de.lessvoid.nifty.Nifty;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

public final class SelectRemoteControlsScreenGeneratorFTL extends FtlTemplateGenerator {

    public SelectRemoteControlsScreenGeneratorFTL(Nifty nifty) throws IOException {
        super(nifty, "/mod/common/interface_remoteselector.xml");
    }

    @Override
    protected Map injectProperties() {

        return new HashMap();
    }
}

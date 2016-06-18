package com.navid.trafalgar.mod.common;

import com.google.common.collect.Lists;
import com.navid.nifty.flow.template.ftl.FtlTemplateGenerator;
import com.navid.trafalgar.input.KeyboardCommandStateListener;
import com.navid.trafalgar.input.KeyboardDigitalCommandStateListener;
import com.navid.trafalgar.model.GameConfiguration;
import de.lessvoid.nifty.Nifty;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

public final class SelectKeyboardDigitalControlsScreenGeneratorFTL extends FtlTemplateGenerator {

    /**
     * Singleton
     */
    @Autowired
    private GameConfiguration gameConfiguration;

    public SelectKeyboardDigitalControlsScreenGeneratorFTL(Nifty nifty) throws IOException {
        super(nifty, "/mod/common/interface_keyboarddigitalselector.xml");
    }

    @Override
    protected Map injectProperties() {
        Collection<KeyboardDigitalCommandStateListener> keyListeners
                = gameConfiguration.getPreGameModel().getByType(KeyboardDigitalCommandStateListener.class);

        //sorting commands in alphabetical order of command name
        List<KeyboardDigitalCommandStateListener> sortedCommands = newArrayList(keyListeners);
        Collections.sort(sortedCommands, new Comparator<KeyboardDigitalCommandStateListener>() {
            @Override
            public int compare(KeyboardDigitalCommandStateListener o1, KeyboardDigitalCommandStateListener o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        final List<List<KeyboardDigitalCommandStateListener>> partitionedSorted = Lists.partition(sortedCommands, 4);

        return new HashMap() {{ put("partitionedKeyboardCommands", partitionedSorted);}};
    }

    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }
}

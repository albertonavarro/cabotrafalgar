package com.navid.trafalgar.mod.common;

import com.google.common.collect.Lists;
import com.navid.nifty.flow.ScreenGenerator;
import com.navid.nifty.flow.template.ftl.FtlTemplateGenerator;
import com.navid.trafalgar.input.KeyboardCommandStateListener;
import com.navid.trafalgar.model.GameConfiguration;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.*;
import de.lessvoid.nifty.controls.button.builder.ButtonBuilder;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.controls.listbox.builder.ListBoxBuilder;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.tools.Color;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.*;

import static com.google.common.collect.Lists.newArrayList;

public class SelectKeyboardControlsScreenGeneratorFTL extends FtlTemplateGenerator {

    /**
     * Singleton
     */
    @Autowired
    private GameConfiguration gameConfiguration;

    public SelectKeyboardControlsScreenGeneratorFTL(Nifty nifty) throws IOException {
        super(nifty, "/mod/common/interface_keyboardselector.xml");
    }

    public SelectKeyboardControlsScreenGeneratorFTL(Nifty nifty, String file) throws IOException {
        super(nifty, file);
    }

    @Override
    protected Map injectProperties() {
        Collection<KeyboardCommandStateListener> keyListeners
                = gameConfiguration.getPreGameModel().getByType(KeyboardCommandStateListener.class);

        //sorting commands in alphabetical order of command name
        List<KeyboardCommandStateListener> sortedCommands = newArrayList(keyListeners);
        Collections.sort(sortedCommands, new Comparator<KeyboardCommandStateListener>() {
            @Override
            public int compare(KeyboardCommandStateListener o1, KeyboardCommandStateListener o2) {
                return o1.toString().compareTo(o2.toString());
            }
        });

        final List<List<KeyboardCommandStateListener>> partitionedSorted = Lists.partition(sortedCommands, 4);

        return new HashMap() {{ put("partitionedKeyboardCommands", partitionedSorted);}};
    }

    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }
}

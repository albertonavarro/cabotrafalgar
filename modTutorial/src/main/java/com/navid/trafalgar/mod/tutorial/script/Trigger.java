package com.navid.trafalgar.mod.tutorial.script;

import java.util.concurrent.Callable;

/**
 * Created by alberto on 17/04/16.
 */
public interface Trigger {

    void register(final Actionable actionable);

    void unregister();
}

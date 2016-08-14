package com.navid.trafalgar.input;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by alberto on 7/28/15.
 */
public class RemoteInputCommandGenerator implements CommandGenerator {

    @Resource
    public RemoteEventsManager remoteEventsManager;

    private Map<String, String> report = new HashMap<String, String>();

    @Override
    public Set<Class<? extends Command>> getPossibleCommands() {
        HashSet<Class<? extends Command>> result = new HashSet<>();
        result.add(Command.class);
        return result;
    }

    @Override
    public CommandStateListener generateCommandStateListener(final Command key) {
        report.put(key.toString(), "remote");
        return new RemoteInputCommandStateListener( key, remoteEventsManager);
    }

    @Override
    public Map<String, String> commandReport() {
        return report;
    }

    @Override
    public String toString() {
        return "Remote Shared Screen";
    }

    public void setRemoteEventsManager(RemoteEventsManager remoteEventsManager) {
        this.remoteEventsManager = remoteEventsManager;
    }

}

package com.navid.trafalgar.input;

import com.navid.trafalgar.manager.PrestartState;
import com.navid.trafalgar.manager.StartedState;

import javax.jms.JMSException;

/**
 * Created by alberto on 7/28/15.
 */
public class RemoteInputCommandStateListener implements CommandStateListener, PrestartState, StartedState {

    public RemoteEventsManager remoteEventsManager;

    private final Command key;

    private float value = 0;

    public RemoteInputCommandStateListener(Command key, RemoteEventsManager remoteEventsManager) {
        this.key = key;
        this.remoteEventsManager = remoteEventsManager;
    }

    @Override
    public void onUnload() {
            remoteEventsManager.unsubscribe("user", key.toString());
    }

    @Override
    public void onPrestart(float tpf) {
        try {
            remoteEventsManager.listenMessages("user", key.toString(), new RemoteEventsManager.StructureConsumer<Transport>(Transport.class){

                @Override
                protected void onMessage(Transport message) {
                    value = message.getValue();
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onStarted(float tpf) {
        key.execute(value);
    }

    public static class Transport {
        private float value;

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }
    }
}

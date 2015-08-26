package com.navid.trafalgar.input;

import com.navid.trafalgar.manager.PrestartState;
import com.navid.trafalgar.manager.StartedState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;

/**
 * Created by alberto on 7/28/15.
 */
public class RemoteInputCommandStateListener implements CommandStateListener, PrestartState, StartedState {

    private static final Logger logger = LoggerFactory.getLogger(RemoteInputCommandStateListener.class);

    public RemoteEventsManager remoteEventsManager;

    private final Command key;

    private float value = 0;
    private String userId;
    private Long gameId;

    public RemoteInputCommandStateListener(Command key, RemoteEventsManager remoteEventsManager) {
        this.key = key;
        this.remoteEventsManager = remoteEventsManager;
    }

    @Override
    public void onUnload() {
            remoteEventsManager.unsubscribe("user", key.toString().replace(' ', '_'));
    }

    @Override
    public void onPrestart(float tpf) {
        try {
            remoteEventsManager.listenMessages(gameId, userId, key.toString().replace(' ', '_'), new RemoteEventsManager.StructureConsumer<Transport>(Transport.class){

                @Override
                protected void onMessage(Transport message) {
                    logger.info("received message {} for key {}", message, key);
                    value = message.getValue();
                }
            });
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStarted(float tpf) {
        if(value != 0) {
            key.execute(value*tpf);
        }
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public static class Transport {
        private float value;

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }

        public String toString() {
            return "[value: "+value+" ]";
        }
    }

    public Command getKey() {
        return key;
    }
}

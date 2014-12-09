package com.navid.trafalgar.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 *
 */
public class StateManager implements EventListener {

    @Autowired
    private EventManager eventManager;

    /**
     *
     */
    public static enum STATES {

        /**
         *
         */
        INIT,
        /**
         *
         */
        LOADMODEL,
        /**
         *
         */
        LOADCAM,
        /**
         *
         */
        PRESTART,
        /**
         *
         */
        STARTED,
        /**
         *
         */
        ABORTED,
        /**
         *
         */
        SUCCESSFUL,
        /**
         *
         */
        FAILED,
        /**
         *
         */
        FINISHED,
        /**
         *
         */
        UNLOAD,
        /**
         *
         */
        PAUSED
    };
    private EnumMap<STATES, List<StateListener>> stateListener = new EnumMap<STATES, List<StateListener>>(STATES.class);
    private STATES currentState = STATES.INIT;

    /**
     * @return the currentState
     */
    public STATES getCurrentState() {
        return currentState;
    }

    /**
     *
     */
    public void reset() {
        stateListener.clear();
        this.setCurrentState(STATES.INIT);
        getEventManager().registerListener(this, new String[]{EventManager.ABORTED, EventManager.FAILED, EventManager.SUCCESSFUL, EventManager.PAUSED, EventManager.RESUME, EventManager.UNLOAD});
    }

    /**
     * @param currentState the currentState to set
     */
    public void setCurrentState(STATES currentState) {
        this.currentState = currentState;
    }

    /**
     *
     * @param event
     */
    public void onEvent(String event) {
        if (currentState == STATES.STARTED) {
            if (event.equals(EventManager.ABORTED)) {
                currentState = STATES.ABORTED;
            } else if (event.equals(EventManager.SUCCESSFUL)) {
                currentState = STATES.SUCCESSFUL;
            } else if (event.equals(EventManager.FAILED)) {
                currentState = STATES.FAILED;
            } else if (event.equals(EventManager.PAUSED)) {
                currentState = STATES.PAUSED;
            }
        } else if (currentState == STATES.PAUSED) {
            if (event.equals(EventManager.RESUME)) {
                currentState = STATES.STARTED;
            } else if (event.equals(EventManager.FAILED)) {
                currentState = STATES.FAILED;
            }
        }

        if (event.equals(EventManager.UNLOAD)) {
            currentState = STATES.UNLOAD;
            update(0);
        }

    }

    /**
     *
     * @param listener
     */
    public void register(StateListener listener) {
        getStateListener(STATES.UNLOAD).add(listener);

        if (listener instanceof InitState) {
            getStateListener(STATES.INIT).add(listener);
        }
        if (listener instanceof LoadModelState) {
            getStateListener(STATES.LOADMODEL).add(listener);
        }
        if (listener instanceof LoadCamState) {
            getStateListener(STATES.LOADCAM).add(listener);
        }
        if (listener instanceof PrestartState) {
            getStateListener(STATES.PRESTART).add(listener);
        }
        if (listener instanceof StartedState) {
            getStateListener(STATES.STARTED).add(listener);
        }
        if (listener instanceof AbortedState) {
            getStateListener(STATES.ABORTED).add(listener);
        }
        if (listener instanceof FailedState) {
            getStateListener(STATES.FAILED).add(listener);
        }
        if (listener instanceof SuccessfulState) {
            getStateListener(STATES.SUCCESSFUL).add(listener);
        }
        if (listener instanceof PausedState) {
            getStateListener(STATES.PAUSED).add(listener);
        }

    }

    private List<StateListener> getStateListener(STATES state) {
        List<StateListener> listeners = stateListener.get(state);
        if (listeners == null) {
            listeners = new ArrayList<StateListener>();
            stateListener.put(state, listeners);
        }
        return listeners;
    }

    /**
     *
     * @param tpf
     */
    public void update(float tpf) {

        List<StateListener> listeners = getStateListener(currentState);

        switch (currentState) {
            case INIT:
                for (StateListener currentListener : listeners) {
                    ((InitState) currentListener).onInit(tpf);
                }
                currentState = STATES.LOADMODEL;
                break;

            case LOADMODEL:
                for (StateListener currentListener : listeners) {
                    ((LoadModelState) currentListener).onLoadModel(tpf);
                }
                currentState = STATES.LOADCAM;
                break;

            case LOADCAM:
                for (StateListener currentListener : listeners) {
                    ((LoadCamState) currentListener).onLoadCam(tpf);
                }
                currentState = STATES.PRESTART;
                break;

            case PRESTART:
                for (StateListener currentListener : listeners) {
                    ((PrestartState) currentListener).onPrestart(tpf);
                }
                currentState = STATES.STARTED;
                break;

            case STARTED:
                for (StateListener currentListener : listeners) {
                    ((StartedState) currentListener).onStarted(tpf);
                }
                break;

            case ABORTED:
                for (StateListener currentListener : listeners) {
                    ((AbortedState) currentListener).onAborted(tpf);
                }
                currentState = STATES.FINISHED;
                break;

            case SUCCESSFUL:
                for (StateListener currentListener : listeners) {
                    ((SuccessfulState) currentListener).onSuccess(tpf);
                }
                currentState = STATES.FINISHED;
                break;

            case PAUSED:
                for (StateListener currentListener : listeners) {
                    ((PausedState) currentListener).onPaused(tpf);
                }
                break;
            case FAILED:
                for (StateListener currentListener : listeners) {
                    ((FailedState) currentListener).onFailed(tpf);
                }
                currentState = STATES.FINISHED;
                break;

            case FINISHED:
                for (StateListener currentListener : listeners) {
                    //currentListener.onFinished(tpf);
                }
                break;

            case UNLOAD:
                Collections.reverse(listeners);
                for (StateListener currentListener : listeners) {
                    currentListener.onUnload();
                }
                stateListener.clear();
                break;

            default:
                throw new IllegalStateException(currentState.toString());

        }
    }

    /**
     * @return the eventManager
     */
    public EventManager getEventManager() {
        return eventManager;
    }

    /**
     * @param eventManager the eventManager to set
     */
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }
}

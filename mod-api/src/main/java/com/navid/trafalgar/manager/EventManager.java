package com.navid.trafalgar.manager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 */
public class EventManager {

    /**
     *
     */
    public static final String SUCCESSFUL = "SUCCESSFUL";
    /**
     *
     */
    public static final String ABORTED = "ABORTED";
    /**
     *
     */
    public static final String FAILED = "FAILED";
    /**
     *
     */
    public static final String PAUSED = "PAUSE";
    /**
     *
     */
    public static final String RESUME = "RESUME";
    /**
     *
     */
    public static final String VIEW_NEXTTARGET = "VIEW_NEXTTARGET";
    /**
     *
     */
    public static final String MILLESTONE_REACHED = "MILLESTONE_REACHED";
    /**
     *
     */
    public static final String UNLOAD = "UNLOAD";

    private Map<String, Set<EventListener>> eventListeners = new HashMap<String, Set<EventListener>>();

    /**
     *
     * @param eventListener
     * @param events
     */
    public void registerListener(EventListener eventListener, String[] events) {

        for (String currentEvent : events) {
            Set<EventListener> listeners = eventListeners.get(currentEvent);
            if (listeners == null) {
                listeners = new HashSet<EventListener>();
                eventListeners.put(currentEvent, listeners);
            }
            listeners.add(eventListener);
        }

    }

    /**
     *
     * @param event
     */
    public void fireEvent(String event) {
        Set<EventListener> listeners = eventListeners.get(event);
        if (listeners != null) {
            for (EventListener currentListener : listeners) {
                Logger.getLogger(EventManager.class.getName()).log(Level.FINE, "Firing event {0} to listener: {1}", new Object[]{event, currentListener.toString()});

                currentListener.onEvent(event);
            }
        }

        if (event.equals(UNLOAD)) {
            eventListeners = new HashMap<String, Set<EventListener>>();
        }

        Logger.getLogger(EventManager.class.getName()).log(Level.FINE, "Finished firing event: {0}", event);

    }
}

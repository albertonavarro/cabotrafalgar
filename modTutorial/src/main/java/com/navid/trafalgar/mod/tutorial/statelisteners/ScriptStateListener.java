package com.navid.trafalgar.mod.tutorial.statelisteners;

import com.google.common.base.Optional;
import com.navid.trafalgar.manager.*;
import com.navid.trafalgar.mod.tutorial.NavigationScreenController;
import com.navid.trafalgar.mod.tutorial.script.ScriptEvent;
import com.navid.trafalgar.model.GameConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by alberto on 17/04/16.
 */
public class ScriptStateListener implements InitState, PrestartState, StartedState {

    private ScriptEvent scriptEvent = null;

    private List<ScriptEvent> script = newArrayList();

    private int scriptIndex = 0;

    ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

    @Autowired
    private NavigationScreenController navigationScreenController;

    @Autowired
    private EventManager eventManager;

    @Autowired
    private LoadCameraStateListener loadCameraStateListener;

    @Autowired
    private GameConfiguration gameConfiguration;

    @Override
    public void onInit(float tpf) {
        threadPoolTaskScheduler.initialize();
        script = new ScriptBuilder()
                .withScript(gameConfiguration.getPreGameModel().getSingleByTypeAndName(List.class, "script"))
                .withScriptInterpreter(navigationScreenController)
                .withEventManager(eventManager)
                .withLoadCameraStateListener(loadCameraStateListener).getScript();
    }

    @Override
    public void onUnload() {
        threadPoolTaskScheduler.shutdown();
    }

    public void setNavigationScreenController(NavigationScreenController navigationScreenController) {
        this.navigationScreenController = navigationScreenController;
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void setLoadCameraStateListener(LoadCameraStateListener loadCameraStateListener) {
        this.loadCameraStateListener = loadCameraStateListener;
    }

    public void setGameConfiguration(GameConfiguration gameConfiguration) {
        this.gameConfiguration = gameConfiguration;
    }

    Optional<ScriptEvent> nextScript = getNextScript();

    @Override
    public void onPrestart(float tpf) {
        nextScript = getNextScript();

        if(!nextScript.isPresent()) {
            eventManager.fireEvent(EventManager.SUCCESSFUL);
            return;
        }

        processNextScript(nextScript.get());
    }

    private void processNextScript(final ScriptEvent scriptEvent) {
        if (scriptEvent == null) {
            eventManager.fireEvent("SUCCESS");
            return;
        }

        //time to load a new event
        scriptEvent.getTrigger().register(scriptEvent.getAction());

        eventManager.registerListener(new EventListener() {
            @Override
            public void onEvent(String event) {
                synchronized (scriptEvent) {
                    if(!scriptEvent.isSuccessful().isPresent()) {
                        scriptEvent.setSuccessful(true);
                        scriptEvent.getAction().cleanUpAction();
                        scriptEvent.getTrigger().unregister();
                        eventManager.unregister(this);
                        processNextScript(getNextScript().orNull());
                    }
                }
            }
        }, scriptEvent.getSuccessEvent());

        if(scriptEvent.getTimeoutMillis().isPresent()){
            System.out.println(Instant.now() + "***");
            threadPoolTaskScheduler.schedule(() -> {
                synchronized (scriptEvent) {
                    if(!scriptEvent.isSuccessful().isPresent()) {
                        System.out.println(Instant.now() + "***");
                        scriptEvent.setSuccessful(false);
                        scriptEvent.getAction().cleanUpAction();
                        scriptEvent.getTrigger().unregister();
                        navigationScreenController.printMessageNotSkippeable(new String[]{"TIMEOUT!! Be faster next time. Click ::command::system - show menu:: to see menu."});
                    }
                }
            }, Date.from(Instant.now().plus(scriptEvent.getTimeoutMillis().get(), ChronoUnit.MILLIS)));
        }

    }

    private Optional<ScriptEvent> getNextScript() {
        if(script.size() <= scriptIndex) {
            return Optional.absent();
        }

        return Optional.of(script.get(scriptIndex++));
    }

    @Override
    public void onStarted(float tpf) {
        eventManager.fireEvent("FRAME_STEP");
    }
}

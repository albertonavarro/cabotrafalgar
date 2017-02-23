package com.navid.trafalgar.gamemanager;

import com.navid.gamemanager.rest.RestGame;
import com.navid.gamemanager.rest.RestScope;
import com.navid.gamemanager.rest.impl.GameManagerClient;
import com.navid.trafalgar.lazylogin.LazyLoginService;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.squareup.okhttp.*;
import org.apache.commons.io.IOUtils;
import org.bushe.swing.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import javax.annotation.PostConstruct;

/**
 * Created by anavarro on 12/02/17.
 */
public class GameManagerService {


    @Autowired
    private TaskScheduler executor;

    @Autowired
    private EventService eventService;

    final GameManagerClient gameManagerClient;

    public Status getStatus() {
        return currentStatus;
    }

    public enum Status { UP, BUSY, DOWN, UNKNOWN }

    private volatile Status currentStatus = Status.UNKNOWN;
    private final OkHttpClient client;
    private final Request request ;


    public GameManagerService(String gameManagerUrl) {
        client = new OkHttpClient();
        request = new Request.Builder()
                .url(gameManagerUrl)
                .get()
                .build();
        gameManagerClient = new GameManagerClient(gameManagerUrl);
    }

    @PostConstruct
    public void init(){
        executor.scheduleAtFixedRate(new StatusChecker(), 3000);
    }


    public RestGame createNewGame() {
        return new HystrixCommand<RestGame>(HystrixCommandGroupKey.Factory.asKey("Gameserver"), 5000) {
            @Override
            public final RestGame run() throws Exception {
                return gameManagerClient.createNewGame(RestScope.PUBLIC, "mode1", "map01");
            }
        }.execute();
    }

    private class StatusChecker implements Runnable {

        @Override
        public void run() {
            ResponseBody body = null;
            try {
                Call call = client.newCall(request);
                final Response resp = call.execute();
                final int code = resp.code();
                currentStatus = code == 200? Status.UP : Status.DOWN;
                body = resp.body();
            } catch (Exception e) {
                currentStatus = Status.DOWN;
            } finally {
                if (body != null) {
                    IOUtils.closeQuietly(body);
                }
            }

            eventService.publish(new GameManagerServerStatusChange(currentStatus));
        }
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    public void setExecutor(TaskScheduler executor) {
        this.executor = executor;
    }
}

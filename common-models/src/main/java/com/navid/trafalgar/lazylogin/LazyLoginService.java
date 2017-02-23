package com.navid.trafalgar.lazylogin;

import com.lazylogin.client.user.v0.*;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.squareup.okhttp.*;
import org.apache.commons.io.IOUtils;
import org.bushe.swing.event.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;

import javax.annotation.PostConstruct;
import javax.jws.WebParam;

/**
 * Created by anavarro on 12/02/17.
 */
public class LazyLoginService implements UserCommands {

    //manually wired
    private UserCommands userCommands;

    private int TIMEOUT = 3000;

    public enum Status {
        UP, BUSY, DOWN, UNKNOWN
    }

    @Autowired
    private TaskScheduler executor;

    @Autowired
    private EventService eventService;

    @PostConstruct
    public void init(){
        executor.scheduleAtFixedRate(new StatusChecker(), 3000);
    }

    private volatile Status currentStatus = Status.UNKNOWN;
    private final OkHttpClient client;
    private final Request request ;

    public LazyLoginService(String lazyloginUrl) {
        client = new OkHttpClient();
        request = new Request.Builder()
                .url(lazyloginUrl)
                .get()
                .build();
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

            eventService.publish(new LazyLoginServerStatusChange(currentStatus));
        }
    }


    public Status getStatus() {
        return currentStatus;
    }

    @Override
    public CreateTokenResponse createToken(@WebParam(partName = "parameters", name = "createTokenRequest", targetNamespace = "http://lazylogin.navid.com/") final CreateTokenRequest createTokenRequest) {
        return new HystrixCommand<CreateTokenResponse>(HystrixCommandGroupKey.Factory.asKey("UserCommands"), TIMEOUT) {
            @Override
            protected CreateTokenResponse run() throws Exception {
                return userCommands.createToken(createTokenRequest);
            }
        }.execute();
    }

    @Override
    public GetInfoResponse getInfo(@WebParam(partName = "parameters", name = "getInfoRequest", targetNamespace = "http://lazylogin.navid.com/") final GetInfoRequest getInfoRequest) {
        return new HystrixCommand<GetInfoResponse>(HystrixCommandGroupKey.Factory.asKey("UserCommands"), TIMEOUT) {
            @Override
            protected GetInfoResponse run() throws Exception {
                return userCommands.getInfo(getInfoRequest);
            }
        }.execute();
    }

    @Override
    public LoginWithTokenResponse loginWithToken(@WebParam(partName = "parameters", name = "loginWithTokenRequest", targetNamespace = "http://lazylogin.navid.com/") final LoginWithTokenRequest loginWithTokenRequest) {
        return  new HystrixCommand<LoginWithTokenResponse>(HystrixCommandGroupKey.Factory.asKey("UserCommands"), TIMEOUT) {

            @Override
            protected LoginWithTokenResponse run() throws Exception {
                return userCommands.loginWithToken(loginWithTokenRequest);
            }

        }.execute();
    }

    public void setUserCommands(UserCommands userCommands) {
        this.userCommands = userCommands;
    }

    public void setExecutor(TaskScheduler executor) {
        this.executor = executor;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

}

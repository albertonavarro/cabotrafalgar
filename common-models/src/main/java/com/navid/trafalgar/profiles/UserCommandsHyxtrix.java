package com.navid.trafalgar.profiles;

import com.lazylogin.client.user.v0.*;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

import javax.annotation.Resource;
import javax.jws.WebParam;

/**
 * Created by alberto on 28/03/16.
 */
public class UserCommandsHyxtrix implements UserCommands {

    private UserCommands userCommands;

    private int TIMEOUT = 3000;

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
        return new HystrixCommand<LoginWithTokenResponse>(HystrixCommandGroupKey.Factory.asKey("UserCommands"), TIMEOUT) {
            @Override
            protected LoginWithTokenResponse run() throws Exception {
                return userCommands.loginWithToken(loginWithTokenRequest);
            }
        }.execute();
    }

    public void setUserCommands(UserCommands userCommands) {
        this.userCommands = userCommands;
    }
}

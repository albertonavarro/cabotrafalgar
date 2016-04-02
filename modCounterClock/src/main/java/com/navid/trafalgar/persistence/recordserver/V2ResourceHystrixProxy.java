package com.navid.trafalgar.persistence.recordserver;

import com.navid.recordserver.v2.*;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import javax.annotation.Resource;
import javax.ws.rs.PathParam;

public class V2ResourceHystrixProxy implements V2Resource {

    private V2Resource v2Resource;

    @Override
    public AddRecordResponse postRanking(final AddRecordRequest addRecordRequest) {
        return new HystrixCommand<AddRecordResponse>(HystrixCommandGroupKey.Factory.asKey("V2Resource")){

            @Override
            protected AddRecordResponse run() throws Exception {
                return v2Resource.postRanking(addRecordRequest);
            }
        }.execute();
    }

    @Override
    public GetMapRecordsResponse getRankingshipshipmapsmap(@PathParam("map") final String s, @PathParam("ship") final String s1) {
        return new HystrixCommand<GetMapRecordsResponse>(HystrixCommandGroupKey.Factory.asKey("V2Resource")){

            @Override
            protected GetMapRecordsResponse run() throws Exception {
                return v2Resource.getRankingshipshipmapsmap(s, s1);
            }
        }.execute();
    }

    @Override
    public GetRecordResponse getRankingidid(@PathParam("id") final String s) {
        return new HystrixCommand<GetRecordResponse>(HystrixCommandGroupKey.Factory.asKey("V2Resource")){

            @Override
            protected GetRecordResponse run() throws Exception {
                return v2Resource.getRankingidid(s);
            }
        }.execute();
    }

    public void setV2Resource(V2Resource v2Resource) {
        this.v2Resource = v2Resource;
    }
}

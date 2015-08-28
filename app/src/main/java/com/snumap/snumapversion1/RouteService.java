package com.snumap.snumapversion1;

import com.snumap.snumapversion1.API.RouteApi;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;

/**
 * Created by rukeon01 on 2015-08-28.
 */
public class RouteService {
    private RestAdapter restAdapter;
    private RouteApi routeApi;
    private final String BASE_URL = "http://snumap.com/cmap/selectize/examples";

    public RouteService() {
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("YOUR_LOG_TAG")).build();

        routeApi = restAdapter.create(RouteApi.class);
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

    public RouteApi getRouteApi() {
        return routeApi;
    }
}

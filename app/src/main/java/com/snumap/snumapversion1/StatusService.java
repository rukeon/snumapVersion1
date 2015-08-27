package com.snumap.snumapversion1;

import com.snumap.snumapversion1.API.StatusCheckApi;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;

/**
 * Created by rukeon01 on 2015-08-19.
 */
public class StatusService {
    private RestAdapter restAdapter;
    private StatusCheckApi statusCheckApi;
    private final String BASE_URL = "http://snulive.kr/snumap";

    public StatusService() {
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL).setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("YOUR_LOG_TAG")).build();

        statusCheckApi = restAdapter.create(StatusCheckApi.class);
    }

    public RestAdapter getRestAdapter() {
        return restAdapter;
    }

    public StatusCheckApi getStatusCheckApi() {
        return statusCheckApi;
    }
}

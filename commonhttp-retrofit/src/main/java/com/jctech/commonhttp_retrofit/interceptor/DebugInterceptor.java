package com.jctech.commonhttp_retrofit.interceptor;

import android.support.annotation.RawRes;


import com.jctech.commonhttp_retrofit.FileUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by CHENQIAO on 2017/12/1.
 */

public class DebugInterceptor extends BaseInterceptor {

    private final String DEBUG_URL;
    private final String DEBUG_String;

    public DebugInterceptor(String debugUrl, String s) {
        this.DEBUG_URL = debugUrl;
        this.DEBUG_String = s;
    }

    private Response getResponse(Interceptor.Chain chain, String json) {
        return new Response.Builder()
                .code(200)
                .addHeader("Content-Type", "application/json")
                .body(ResponseBody.create(MediaType.parse("application/json"), json))
                .message("OK")
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .build();
    }

    private Response debugResponse(Interceptor.Chain chain, String s) {
        final String json = "{key:value}";
        return getResponse(chain, json);
    }

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        final String url = chain.request().url().toString();
        if (url.contains(DEBUG_URL)) {
            return debugResponse(chain, DEBUG_String);
        }
        return chain.proceed(chain.request());
    }
}

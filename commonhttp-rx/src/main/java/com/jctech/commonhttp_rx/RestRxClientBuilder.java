package com.jctech.commonhttp_rx;

import android.content.Context;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by CHENQIAO on 2017/12/10.
 */

public class RestRxClientBuilder {

    private String mUrl;
    private static final Map<String, Object> PARAMS = RestRxCreator.getParams();
    private RequestBody mRequestbody;
    private Context mContext;
    private File mFile;

    RestRxClientBuilder() {
    }

    public final RestRxClientBuilder url(String mUrl) {
        this.mUrl = mUrl;
        return this;
    }

    public final RestRxClientBuilder params(Map<String, Object> mParams) {
        PARAMS.putAll(mParams);
        return this;
    }

    public final RestRxClientBuilder params(String key, Object value) {
        PARAMS.put(key, value);
        return this;
    }


    public final RestRxClientBuilder raw(String raw) {
        this.mRequestbody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final Map<String, Object> checkParams() {
        if (PARAMS == null) {
            return new WeakHashMap<>();
        }
        return PARAMS;
    }


    public final RestRxClientBuilder file(File file) {
        this.mFile = file;
        return this;
    }

    public final RestRxClientBuilder file(String filePath) {
        this.mFile = new File(filePath);
        return this;
    }


    public RestRxClient build() {
        return new RestRxClient(mUrl, PARAMS, mRequestbody, mContext, mFile);
    }


}

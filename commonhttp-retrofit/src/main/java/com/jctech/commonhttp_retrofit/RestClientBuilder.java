package com.jctech.commonhttp_retrofit;

import android.content.Context;

import com.jctech.commonhttp_retrofit.callback.IError;
import com.jctech.commonhttp_retrofit.callback.IFailure;
import com.jctech.commonhttp_retrofit.callback.IRequest;
import com.jctech.commonhttp_retrofit.callback.ISuccess;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by CHENQIAO on 2017/9/10.
 */

public class RestClientBuilder {

    private String mUrl;
    private static final Map<String, Object> PARAMS = RestCreator.getParams();
    private IRequest mRequet;
    private ISuccess mSuccess;
    private IFailure mFailure;
    private IError mError;
    private RequestBody mRequestbody;
    private Context mContext;
    private File mFile;

    RestClientBuilder() {
    }

    public final RestClientBuilder url(String mUrl) {
        this.mUrl = mUrl;
        return this;
    }

    public final RestClientBuilder params(Map<String, Object> mParams) {
        PARAMS.putAll(mParams);
        return this;
    }

    public final RestClientBuilder params(String key, Object value) {
        PARAMS.put(key, value);
        return this;
    }

    public final RestClientBuilder request(IRequest mRequet) {
        this.mRequet = mRequet;
        return this;
    }

    public final RestClientBuilder success(ISuccess mSuccess) {
        this.mSuccess = mSuccess;
        return this;
    }

    public final RestClientBuilder failure(IFailure mFailure) {
        this.mFailure = mFailure;
        return this;
    }

    public final RestClientBuilder error(IError mError) {
        this.mError = mError;
        return this;
    }

    public final RestClientBuilder raw(String raw) {
        this.mRequestbody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), raw);
        return this;
    }

    public final Map<String, Object> checkParams() {
        if (PARAMS == null) {
            return new WeakHashMap<>();
        }
        return PARAMS;
    }


    public final RestClientBuilder file(File file) {
        this.mFile = file;
        return this;
    }

    public final RestClientBuilder file(String filePath) {
        this.mFile = new File(filePath);
        return this;
    }


    public RestClient build() {
        return new RestClient(mUrl, PARAMS, mRequet, mSuccess, mFailure, mError, mRequestbody, mContext, mFile);
    }


}

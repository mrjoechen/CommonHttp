package com.jctech.commonhttp_rx;

import android.content.Context;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * Created by CHENQIAO on 2017/9/3.
 */

public class RestRxClient {

    private final String URL;
//    private final Map<String, Object> PARAMS;

    private static final WeakHashMap<String, Object> PARAMS = RestRxCreator.getParams();
    private final RequestBody BODY;
    private final File FILE;
    private final Context CONTEXT;


    public RestRxClient(String mUrl,
                        Map<String, Object> mParams,
                        RequestBody mRequestbody,
                        Context context,
                        File file) {
        this.URL = mUrl;
        PARAMS.putAll(mParams);
        this.BODY = mRequestbody;
        this.CONTEXT = context;
        this.FILE = file;
    }

    public static RestRxClientBuilder builder() {
        return new RestRxClientBuilder();
    }

    private Observable request(HTTP_METHOD http_method) {

        RestRxService restRxService = RestRxCreator.getRestRxService();
        Observable<String> observable = null;

        switch (http_method) {

            case GET:
                observable = restRxService.get(URL, PARAMS);
                break;
            case POST:
                observable = restRxService.post(URL, PARAMS);
                break;
            case POST_RAW:
                observable = restRxService.postRaw(URL, BODY);
                break;
            case PUT:
                observable = restRxService.put(URL, PARAMS);
                break;
            case PUT_RAW:
                observable = restRxService.putRaw(URL, BODY);
                break;
            case DELETE:
                observable = restRxService.delete(URL, PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody = RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);

                final MultipartBody.Part body = MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);

                observable = restRxService.upload(URL, body);
                break;
            default:
                break;

        }

        return observable;

    }


    public final Observable<String> get() {
        return request(HTTP_METHOD.GET);
    }

    public final Observable<String> post() {
        if (BODY == null) {
            return request(HTTP_METHOD.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("request params must be null");
            }
            return request(HTTP_METHOD.POST_RAW);
        }
    }

    public final Observable<String> put() {

        if (BODY == null) {
            return request(HTTP_METHOD.PUT);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("request params must be null");
            }
            return request(HTTP_METHOD.PUT_RAW);

        }
    }

    public final Observable<String> delete() {
        return request(HTTP_METHOD.DELETE);
    }

    public final Observable<String> upload() {
        return request(HTTP_METHOD.UPLOAD);
    }

    public final Observable<ResponseBody> download() {
        Observable<ResponseBody> download = RestRxCreator.getRestRxService().download(URL, PARAMS);
        return download;
    }


}

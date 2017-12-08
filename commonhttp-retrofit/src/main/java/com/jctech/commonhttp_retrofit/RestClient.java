package com.jctech.commonhttp_retrofit;

import android.content.Context;

import com.jctech.commonhttp_retrofit.callback.IError;
import com.jctech.commonhttp_retrofit.callback.IFailure;
import com.jctech.commonhttp_retrofit.callback.IRequest;
import com.jctech.commonhttp_retrofit.callback.ISuccess;
import com.jctech.commonhttp_retrofit.callback.RequestCallbacks;
import com.jctech.commonhttp_retrofit.download.DownloadHandler;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by CHENQIAO on 2017/9/3.
 */
public class RestClient {

    private final String URL;
//    private final Map<String, Object> PARAMS;

    private static final WeakHashMap<String, Object> PARAMS = RestCreator.getParams();
    private final IRequest IREQUEST;
    private final ISuccess ISUCCESS;
    private final IFailure IFAILURE;
    private final IError IERROR;
    private final RequestBody BODY;
    private final File FILE;
    private final String DOWNLOAD_DIR;
    private final String NAME;
    private final Context CONTEXT;
    private final String EXTENSION;


    public RestClient(String mUrl,
                      Map<String, Object> mParams,
                      IRequest iRequet,
                      ISuccess iSuccess,
                      IFailure iFailure,
                      IError iError,
                      RequestBody mRequestbody,
                      Context context,
                      String downloadDir,
                      String name,
                      File file,
                      String extension) {
        this.URL = mUrl;
        PARAMS.putAll(mParams);
        this.IREQUEST = iRequet;
        this.ISUCCESS = iSuccess;
        this.IFAILURE = iFailure;
        this.IERROR = iError;
        this.BODY = mRequestbody;
        this.CONTEXT = context;
        this.DOWNLOAD_DIR = downloadDir;
        this.NAME = name;
        this.FILE = file;
        this.EXTENSION = extension;
    }

    public static RestClientBuilder builder() {
        return new RestClientBuilder();
    }

    private void request(HTTP_METHOD http_method) {

        RestService restService = RestCreator.getRestService();
        Call<String> call = null;
        if (IREQUEST != null) {
            IREQUEST.onRequestStart();
        }
        switch (http_method) {

            case GET:
                call = restService.get(URL, PARAMS);
                break;
            case POST:
                call = restService.post(URL, PARAMS);
                break;
            case POST_RAW:
                call = restService.postRaw(URL, BODY);
                break;
            case PUT:
                call = restService.put(URL, PARAMS);
                break;
            case PUT_RAW:
                call = restService.putRaw(URL, BODY);
                break;
            case DELETE:
                call = restService.delete(URL, PARAMS);
                break;
            case UPLOAD:
                final RequestBody requestBody = RequestBody.create(MediaType.parse(MultipartBody.FORM.toString()), FILE);
                final MultipartBody.Part body = MultipartBody.Part.createFormData("file", FILE.getName(), requestBody);
                call = restService.upload(URL, body);
                break;
            default:
                break;

        }

        if (call != null) {
            call.enqueue(getRequestCallbacks());
        }

    }

    private RequestCallbacks getRequestCallbacks() {
        return new RequestCallbacks(IREQUEST, ISUCCESS, IFAILURE, IERROR);
    }

    public final void get() {
        request(HTTP_METHOD.GET);
    }

    public final void post() {
        if (BODY == null) {
            request(HTTP_METHOD.POST);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("request params must be null");
            }
            request(HTTP_METHOD.POST_RAW);
        }
    }

    public final void put() {

        if (BODY == null) {
            request(HTTP_METHOD.PUT);
        } else {
            if (!PARAMS.isEmpty()) {
                throw new RuntimeException("request params must be null");
            }
            request(HTTP_METHOD.PUT_RAW);

        }
    }

    public final void delete() {
        request(HTTP_METHOD.DELETE);
    }

    public final void upload() {
        request(HTTP_METHOD.UPLOAD);
    }

    public final void download() {
        new DownloadHandler(URL, IREQUEST, DOWNLOAD_DIR, EXTENSION, NAME,
                ISUCCESS, IFAILURE, IERROR)
                .handleDownload();
    }

}


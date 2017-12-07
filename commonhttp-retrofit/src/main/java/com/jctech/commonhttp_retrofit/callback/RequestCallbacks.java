package com.jctech.commonhttp_retrofit.callback;

import android.os.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by CHENQIAO on 2017/9/11.
 */

public class RequestCallbacks implements Callback<String> {

    private final IRequest IREQUET;
    private final ISuccess iSuccess;
    private final IFailure iFailure;
    private final IError iError;
    private final Handler handler = new Handler();


    public RequestCallbacks(IRequest IREQUET, ISuccess iSuccess, IFailure iFailure, IError iError) {
        this.IREQUET = IREQUET;
        this.iSuccess = iSuccess;
        this.iFailure = iFailure;
        this.iError = iError;
    }

    @Override
    public void onResponse(Call<String> call, Response<String> response) {

        if (response.isSuccessful()) {
            if (call.isExecuted()) {
                if (iSuccess != null) {
                    iSuccess.onSuccess(response.body());
                }
            }
        } else {
            if (iError != null) {
                iError.onError(response.code(), response.message());
            }
        }

    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {

        if (iFailure != null) {
            iFailure.onFailure();
        }

        if (IREQUET != null) {
            IREQUET.onRequestEnd();
        }

    }
}

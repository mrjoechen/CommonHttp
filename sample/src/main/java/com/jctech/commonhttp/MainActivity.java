package com.jctech.commonhttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.jctech.commonhttp_rx.FileUtil;
import com.jctech.commonhttp_rx.RestRxClient;
import com.jctech.commonhttp_rx.RestRxCreator;

import java.io.File;
import java.io.InputStream;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private String url = "http://www.baidu.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void doRxGet() {


        WeakHashMap<String, Object> params = new WeakHashMap<>();
        Observable<String> observable = RestRxCreator.getRestRxService().get(url, params);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Toast.makeText(getApplication(), "test....", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void doRestRxClient() {
        RestRxClient.builder()
                .url(url)
                .build()
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        Toast.makeText(getApplication(), "test....", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void doRxDownload(){
        RestRxClient.builder()
                  .url("http://")
                  .build()
                  .download()
                  .map(new Function<ResponseBody, File>() {
                      @Override
                      public File apply(ResponseBody responseBody) throws Exception {
                          final InputStream is = responseBody.byteStream();
                          String downloadDir = "RxJavaDemo";
                          String extension = "chenqiao";
                          String name = "test.chenqiao";

                          if (downloadDir == null || downloadDir.equals("")) {
                              downloadDir = "down_loads";
                          }
                          if (extension == null || extension.equals("")) {
                              extension = "";
                          }
                          if (name == null) {
                              return FileUtil.writeToDisk(is, downloadDir, extension.toUpperCase(), extension);
                          } else {
                              return FileUtil.writeToDisk(is, downloadDir, name);
                          }
                      }
                  })
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Consumer<File>() {
                      @Override
                      public void accept(File s) throws Exception {
                          Toast.makeText(getApplicationContext(), s.getAbsolutePath(), Toast.LENGTH_LONG).show();
                      }
                  }, new Consumer<Throwable>() {
                      @Override
                      public void accept(Throwable throwable) throws Exception {
                          Toast.makeText(getApplicationContext(), "错误", Toast.LENGTH_LONG).show();

                      }
                  });


    }
}

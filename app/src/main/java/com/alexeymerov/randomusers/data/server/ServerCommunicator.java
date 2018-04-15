package com.alexeymerov.randomusers.data.server;

import com.alexeymerov.randomusers.data.db.entity.UserEntity;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.CompletableTransformer;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.SingleTransformer;
import io.reactivex.schedulers.Schedulers;

public class ServerCommunicator {

    private static final int DEFAULT_TIMEOUT = 10; // seconds
    private static final long DEFAULT_RETRY_ATTEMPTS = 4;
    private static final String TAG = ServerCommunicator.class.getSimpleName();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault());
    private ApiService mApiService;

    @Inject
    public ServerCommunicator(ApiService service) {
        mApiService = service;
    }

    private static <T> ObservableTransformer<T, T> observableTransformer() {
        return observable -> observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(Throwable::printStackTrace)
                .timeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retry(DEFAULT_RETRY_ATTEMPTS);
    }

    private static <T> SingleTransformer<T, T> singleTransformer() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(Throwable::printStackTrace)
                .timeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retry(DEFAULT_RETRY_ATTEMPTS);
    }

    private static CompletableTransformer completableTransformer() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .doOnError(Throwable::printStackTrace)
                .timeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retry(DEFAULT_RETRY_ATTEMPTS);
    }

    public Single<UserEntity> getUser() {
        UserEntity user = new UserEntity();
        user.setId(-1);
        return Single.just(user).compose(singleTransformer());
//        return mApiService.getUser().compose(singleTransformer());
    }

    public Single<List<UserEntity>> loadUsers() {
        return mApiService.getAllUsers().compose(singleTransformer());
    }
}

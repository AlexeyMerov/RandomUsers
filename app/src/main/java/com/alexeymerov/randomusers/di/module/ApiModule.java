package com.alexeymerov.randomusers.di.module;


import com.alexeymerov.randomusers.BuildConfig;
import com.alexeymerov.randomusers.data.server.ApiService;
import com.alexeymerov.randomusers.data.server.ServerCommunicator;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

@Module
public class ApiModule {

    private static final String API_URL = "https://jsonplaceholder.typicode.com";

    @Provides
    public ServerCommunicator provideServerCommunicator(ApiService apiService) {
        return new ServerCommunicator(apiService);
    }

    @Provides
    public ApiService provideApiService(Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

    @Provides
    public Retrofit provideRetrofit(Retrofit.Builder builder) {
        return builder.baseUrl(API_URL).build();
    }

    @Provides
    public Retrofit.Builder provideRetrofitBuilder() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool(5, 30, TimeUnit.SECONDS))
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(httpLoggingInterceptor)
                    .addNetworkInterceptor(new StethoInterceptor());
        }

        return new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(MoshiConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    }

}

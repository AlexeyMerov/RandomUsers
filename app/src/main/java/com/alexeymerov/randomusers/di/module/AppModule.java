package com.alexeymerov.randomusers.di.module;

import android.app.Application;
import android.content.Context;

import com.alexeymerov.randomusers.App;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private App mApp;

    public AppModule(App app) {
        mApp = app;
    }

    @Provides
    public App provideApp() {
        return mApp;
    }

    @Provides
    public Application provideApplication() {
        return mApp;
    }

    @Provides
    public Context provideApplicationContext() {
        return mApp.getApplicationContext();
    }

//    @Provides
//    @Singleton
//    public MySharedPreferences provideMySharedPreferences() {
//        return new MySharedPreferences(mApp.getApplicationContext());
//    }

}

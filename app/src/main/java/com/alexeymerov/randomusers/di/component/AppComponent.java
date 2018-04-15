package com.alexeymerov.randomusers.di.component;

import android.content.Context;

import com.alexeymerov.randomusers.App;
import com.alexeymerov.randomusers.di.module.AppModule;

import dagger.Component;

@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(App app);

    App getApp();

    Context getContext();

//    MySharedPreferences getMySharedPreferences();

}

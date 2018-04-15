package com.alexeymerov.randomusers;

import android.app.Application;

import com.alexeymerov.randomusers.di.component.AppComponent;
import com.alexeymerov.randomusers.di.component.DaggerAppComponent;
import com.alexeymerov.randomusers.di.component.DaggerRepositoryComponent;
import com.alexeymerov.randomusers.di.component.DaggerViewModuleComponent;
import com.alexeymerov.randomusers.di.component.RepositoryComponent;
import com.alexeymerov.randomusers.di.component.ViewModuleComponent;
import com.alexeymerov.randomusers.di.module.ApiModule;
import com.alexeymerov.randomusers.di.module.AppModule;
import com.alexeymerov.randomusers.di.module.RepositoryModule;
import com.facebook.stetho.Stetho;

public class App extends Application {

    private ViewModuleComponent mViewModelComponent;

    public ViewModuleComponent getPresentersComponent() {
        return mViewModelComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initializeDagger();

//        Fabric.with(this, new Crashlytics());

        Stetho.initializeWithDefaults(this);


//        RxJavaPlugins.setErrorHandler(e -> DLog.d("Application", "RxJavaPlugins error: ", e));

        Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
//            DLog.e("TaDa", "", e);
//            Crashlytics.logException(e);
            defaultUncaughtExceptionHandler.uncaughtException(t, e);
        });
    }

    private void initializeDagger() {
        AppComponent appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        RepositoryComponent repositoryComponent = DaggerRepositoryComponent.builder()
                .appComponent(appComponent)
                .apiModule(new ApiModule())
                .repositoryModule(new RepositoryModule())
                .build();

        mViewModelComponent = DaggerViewModuleComponent.builder()
                .repositoryComponent(repositoryComponent)
                .appComponent(appComponent)
                .build();
    }

}

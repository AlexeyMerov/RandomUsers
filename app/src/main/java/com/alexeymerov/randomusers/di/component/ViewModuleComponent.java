package com.alexeymerov.randomusers.di.component;

import com.alexeymerov.randomusers.di.module.ViewModelFactoryModule;
import com.alexeymerov.randomusers.di.module.ViewModelModule;
import com.alexeymerov.randomusers.di.scope.ViewModelScope;
import com.alexeymerov.randomusers.presentation.activity.AuthActivity;
import com.alexeymerov.randomusers.presentation.activity.UserInfoActivity;
import com.alexeymerov.randomusers.presentation.activity.UsersListActivity;

import dagger.Component;

@ViewModelScope
@Component(
        modules = {ViewModelModule.class, ViewModelFactoryModule.class},
        dependencies = {AppComponent.class, RepositoryComponent.class})
public interface ViewModuleComponent {

    //Activity

    void inject(AuthActivity activity);

    void inject(UsersListActivity activity);

    void inject(UserInfoActivity userInfoActivity);

    // Fragments


}

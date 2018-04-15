package com.alexeymerov.randomusers.di.module;

import com.alexeymerov.randomusers.di.scope.ViewModelScope;
import com.alexeymerov.randomusers.viewmodel.UserViewModel;
import com.alexeymerov.randomusers.viewmodel.factory.UserViewModelFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModelFactoryModule {

//    @Provides
//    @ViewModelScope
//    public UserViewModelFactory provideUserViewModelFactory(UserManager manager) {
//        return new UserViewModelFactory(manager);
//    }

    @Provides
    @ViewModelScope
    public UserViewModelFactory provideUserViewModelFactory(UserViewModel viewModel) {
        return new UserViewModelFactory(viewModel);
    }


}

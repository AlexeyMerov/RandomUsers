package com.alexeymerov.randomusers.di.module;

import com.alexeymerov.randomusers.data.repository.UserRepository;
import com.alexeymerov.randomusers.di.scope.ViewModelScope;
import com.alexeymerov.randomusers.viewmodel.UserViewModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ViewModelModule {

//    @Provides
//    @ViewModelScope
//    public UserViewModelFactory provideUserViewModelFactory(UserManager manager) {
//        return new UserViewModelFactory(manager);
//    }


    @Provides
    @ViewModelScope
    public UserViewModel provideUserViewModel(UserRepository repository) {
        return new UserViewModel(repository);
    }


}

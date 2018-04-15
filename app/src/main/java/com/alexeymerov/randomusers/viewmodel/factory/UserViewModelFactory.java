package com.alexeymerov.randomusers.viewmodel.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.alexeymerov.randomusers.viewmodel.UserViewModel;

import javax.inject.Inject;

public class UserViewModelFactory implements ViewModelProvider.Factory {

    //    private UserManager mUserManager;
    private UserViewModel mUserViewModel;

//    @Inject
//    public UserViewModelFactory(UserManager userManager) {
//        mUserManager = userManager;
//    }

    @Inject
    public UserViewModelFactory(UserViewModel userViewModel) {
        mUserViewModel = userViewModel;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) mUserViewModel;
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
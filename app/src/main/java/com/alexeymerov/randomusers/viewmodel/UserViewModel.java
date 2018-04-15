package com.alexeymerov.randomusers.viewmodel;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.alexeymerov.randomusers.data.db.entity.UserEntity;
import com.alexeymerov.randomusers.data.repository.UserRepository;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class UserViewModel extends ViewModel {

    private UserRepository mUserRepository;

    private LiveData<List<UserEntity>> mUserList;

    @Inject
    public UserViewModel(UserRepository userRepository) {
        mUserRepository = userRepository;
    }

    public Single<UserEntity> getUser() {
        return mUserRepository.getUser().observeOn(AndroidSchedulers.mainThread());
    }

    public void saveUser(UserEntity user) {
        mUserRepository.saveUser(user);
    }

    public void saveUser(GoogleSignInAccount account) {
        mUserRepository.saveUser(account.getDisplayName(),
                account.getEmail(),
                account.getFamilyName(),
                account.getGivenName(),
                account.getPhotoUrl());
    }

    public LiveData<List<UserEntity>> getUserList() {
        if (mUserList == null) mUserList = mUserRepository.getAllUsers();
        return mUserList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mUserList = null;
    }

    public Single<UserEntity> getUser(long userId) {
        return mUserRepository.getUser(userId).observeOn(AndroidSchedulers.mainThread());
    }

    public void clearAllData() {
        mUserRepository.clearAllData();
    }
}

package com.alexeymerov.randomusers.data.repository;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.graphics.Color;
import android.net.Uri;

import com.alexeymerov.randomusers.data.db.ApplicationDatabase;
import com.alexeymerov.randomusers.data.db.dao.UserDAO;
import com.alexeymerov.randomusers.data.db.entity.UserEntity;
import com.alexeymerov.randomusers.data.server.ServerCommunicator;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

@SuppressLint("CheckResult")
public class UserRepository {

    private ServerCommunicator mServerCommunicator;
    private UserDAO mUserDAO;
    private ApplicationDatabase mDatabase;

    @Inject
    public UserRepository(ServerCommunicator serverCommunicator, ApplicationDatabase database) {
        mServerCommunicator = serverCommunicator;
        mUserDAO = database.userDao();
        mDatabase = database;
    }


    public Single<UserEntity> getUser() {
        final long id = -1;
        return Single.just(true)
                .flatMap(aBoolean -> {
                    UserEntity localUser = mUserDAO.getById(id);
                    return localUser != null
                            ? Single.just(localUser)
                            : getUserFromServer(id);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    private Single<UserEntity> getUserFromServer(long id) {
        return mServerCommunicator.getUser()
                .doOnSuccess(user -> mUserDAO.add(user))
                .flatMap(user -> Single.just(mUserDAO.getById(id)));
    }

    public void saveUser(UserEntity user) {
        mUserDAO.add(user);
    }

    public void saveUser(String displayName, String email, String familyName, String givenName, Uri photoUrl) {
        final long id = -1;
        Completable.fromRunnable(() -> {
            UserEntity localUser = mUserDAO.getById(id);
            if (localUser == null) localUser = new UserEntity();
            String name = givenName;
            if (name == null) name = familyName;
            if (name == null) name = displayName;

            localUser.setId(id);
            localUser.setName(name);
            localUser.setEmail(email);
            localUser.setPhotoUrl(photoUrl.toString());

            mUserDAO.add(localUser);
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }

    public LiveData<List<UserEntity>> getAllUsers() {
        loadUsersIfNeed();
        return mUserDAO.getAllLive();
    }

    private void loadUsersIfNeed() {
        Completable.fromRunnable(() -> {
            long usersCount = mUserDAO.getUsersCount();
            if (usersCount <= 0) {
                mServerCommunicator.loadUsers()
                        .subscribe(userEntities -> {
                                    generateColors(userEntities);
                                    mUserDAO.addAll(userEntities);
                                },
                                Throwable::printStackTrace);
            }
        }).doOnError(Throwable::printStackTrace)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }

    private void generateColors(List<UserEntity> userEntities) {
        Random rnd = new Random();
        for (UserEntity userEntity : userEntities) {
            int color = Color.argb(255,
                    rnd.nextInt(256),
                    rnd.nextInt(256),
                    rnd.nextInt(256));
            userEntity.setColor(color);
        }
    }

    public Single<UserEntity> getUser(long userId) {
        return Single.fromCallable(() -> mUserDAO.getById(userId))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public void clearAllData() {
        Completable.fromRunnable(() -> mDatabase.clearAllTables())
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }
}

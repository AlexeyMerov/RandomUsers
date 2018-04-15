package com.alexeymerov.randomusers.data.server;

import com.alexeymerov.randomusers.data.db.entity.UserEntity;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ApiService {

    @GET("users")
    Single<List<UserEntity>> getAllUsers();

}

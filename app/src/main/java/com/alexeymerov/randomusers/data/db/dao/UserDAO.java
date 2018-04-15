package com.alexeymerov.randomusers.data.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.alexeymerov.randomusers.data.db.entity.UserEntity;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDAO {

    // get

    @Query("SELECT * FROM user_entity WHERE mId > -1")
    LiveData<List<UserEntity>> getAllLive();

    @Query("SELECT * FROM user_entity WHERE mId > -1")
    List<UserEntity> getAll();

    @Query("SELECT * FROM user_entity WHERE mId = :id")
    LiveData<UserEntity> getByIdLive(Long id);

    @Query("SELECT * FROM user_entity WHERE mId = :id")
    UserEntity getById(Long id);

    @Query("SELECT COUNT(mId) FROM user_entity WHERE mId > -1")
    long getUsersCount();

    //add

    @Insert(onConflict = REPLACE)
    void addAll(List<UserEntity> all);

    @Insert(onConflict = REPLACE)
    void add(UserEntity userEntity);

    //update

    @Update(onConflict = REPLACE)
    void updateAll(List<UserEntity> all);

    @Update(onConflict = REPLACE)
    void update(UserEntity userEntity);

    //delete

    @Delete
    void deleteAll(List<UserEntity> all);

}

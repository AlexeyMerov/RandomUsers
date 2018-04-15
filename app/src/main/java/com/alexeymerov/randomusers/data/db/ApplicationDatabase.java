package com.alexeymerov.randomusers.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.alexeymerov.randomusers.data.db.dao.UserDAO;
import com.alexeymerov.randomusers.data.db.entity.UserEntity;

@Database(entities = {
        UserEntity.class
},
        version = 1,
        exportSchema = false)
public abstract class ApplicationDatabase extends RoomDatabase {

    public abstract UserDAO userDao();

}

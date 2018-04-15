package com.alexeymerov.randomusers.di.module;


import android.arch.persistence.room.Room;
import android.content.Context;

import com.alexeymerov.randomusers.data.db.ApplicationDatabase;
import com.alexeymerov.randomusers.data.repository.UserRepository;
import com.alexeymerov.randomusers.data.server.ServerCommunicator;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    public ApplicationDatabase provideDatabase(Context context) {
        return Room.databaseBuilder(context, ApplicationDatabase.class, "randomusers_database")
                .fallbackToDestructiveMigration()
                .build();
    }

//    private fun provideSharedPrefs(context: Context) = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    @Provides
    public UserRepository provideUserManager(ServerCommunicator serverCommunicator, ApplicationDatabase database) {
        return new UserRepository(serverCommunicator, database);
    }

}

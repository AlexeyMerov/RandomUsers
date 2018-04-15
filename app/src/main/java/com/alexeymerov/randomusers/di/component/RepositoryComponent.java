package com.alexeymerov.randomusers.di.component;


import com.alexeymerov.randomusers.data.db.ApplicationDatabase;
import com.alexeymerov.randomusers.data.repository.UserRepository;
import com.alexeymerov.randomusers.data.server.ServerCommunicator;
import com.alexeymerov.randomusers.di.module.ApiModule;
import com.alexeymerov.randomusers.di.module.RepositoryModule;

import dagger.Component;

@Component(modules = {ApiModule.class, RepositoryModule.class}, dependencies = {AppComponent.class})
public interface RepositoryComponent {

    ServerCommunicator getServerCommunicator();

    ApplicationDatabase getDatabase();

    UserRepository getUserRepository();


}

package com.elegion.tracktor.di;

import com.elegion.tracktor.App;
import com.elegion.tracktor.data.IRepository;
import com.elegion.tracktor.data.RealmRepository;

import toothpick.config.Module;

public class AppModule extends Module {
    private final App mApp;

    public AppModule(App app) {
        this.mApp = app;
        bind(App.class).toInstance(mApp);
    }
}

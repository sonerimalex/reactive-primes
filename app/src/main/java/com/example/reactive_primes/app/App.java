package com.example.reactive_primes.app;

import android.app.Application;

public class App extends Application {

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

    }

    public static App getContext() {
        return instance;
    }

}

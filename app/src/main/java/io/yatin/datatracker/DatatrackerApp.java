package io.yatin.datatracker;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by yatin on 04/02/18.
 */

public class DatatrackerApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}

package com.nickteck.teacherapp.service;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;

import com.nickteck.teacherapp.network.ConnectivityReceiver;
import com.nickteck.teacherapp.utilclass.Constants;
import com.nickteck.teacherapp.utilclass.FontsOverride;


public class MyApplication extends Application {

    private static MyApplication mInstance;
    ConnectivityReceiver networkChangeReceiver;
    String isConnection = null;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
//        FontsOverride.setDefaultFont(this, "MONOSPACE", "Cabin-SemiBold.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "OpenSans-Regular.ttf");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkChangeReceiver = new ConnectivityReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent) {
                    super.onReceive(context, intent);

                    if (isConnection == null) {
                        Bundle b = intent.getExtras();
                        isConnection = b.getString(Constants.MESSAGE);
                    }
                }
            };

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            intentFilter.addAction(Constants.BROADCAST);
            this.registerReceiver(networkChangeReceiver,
                    intentFilter);
        }
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(NetworkChangeReceiver.ConnectivityReceiverListener listener) {
        NetworkChangeReceiver.connectivityReceiverListener = listener;
    }
}

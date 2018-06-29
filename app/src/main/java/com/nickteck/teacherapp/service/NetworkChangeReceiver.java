package com.nickteck.teacherapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.nickteck.teacherapp.utilclass.Constants;

/**
 * Created by admin on 5/16/2018.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {


    public static ConnectivityReceiverListener connectivityReceiverListener;
    public static boolean checkConnection = false;

    public NetworkChangeReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String networkConnection = null;

        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            boolean b = isOnline(context);

            // do subroutines here

            if (currentNetworkInfo.isConnected()) {
                networkConnection = Constants.NETWORK_CONNECTED;
                Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
                checkConnection = true;
            } else {
                networkConnection = Constants.NETWORK_NOT_CONNECTED;
                Toast.makeText(context, "Please Check your Network connection", Toast.LENGTH_SHORT).show();
                checkConnection = false;
            }

            if (connectivityReceiverListener != null)
                connectivityReceiverListener.onNetworkConnectionChanged(checkConnection);
            Intent i = new Intent(Constants.BROADCAST);
            // Data you need to pass to activity
            i.putExtra(Constants.MESSAGE, networkConnection);
            Log.e("action", networkConnection);
            context.sendBroadcast(i);
        }
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

}

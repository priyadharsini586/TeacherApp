package com.nickteck.teacherapp.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.nickteck.teacherapp.service.MyApplication;
import com.nickteck.teacherapp.utilclass.Constants;


public class ConnectivityReceiver
        extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;
   public static boolean checkConnection = false;
    String networkConnection = null;
    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {

        String action = arg1.getAction();


        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetworkInfo currentNetworkInfo = (NetworkInfo) arg1.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            boolean b = isOnline(context);

            // do subroutines here

            if (currentNetworkInfo.isConnected()) {
                networkConnection = Constants.NETWORK_CONNECTED;

                checkConnection = true;
            } else if (!currentNetworkInfo.isConnected()){
//                if (networkConnection != Constants.NETWORK_CONNECTED) {
                    networkConnection = Constants.NETWORK_NOT_CONNECTED;
                    checkConnection = false;

//                }

            }
            if (connectivityReceiverListener != null)
                connectivityReceiverListener.onNetworkConnectionChanged(checkConnection);
            Intent i = new Intent(Constants.BROADCAST);
            // Data you need to pass to activity
            i.putExtra(Constants.MESSAGE, networkConnection);
            context.sendBroadcast(i);
        }
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in airplane mode it will be null
        return (netInfo != null && netInfo.isConnected());
    }
    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) MyApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork.isConnected();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }
}

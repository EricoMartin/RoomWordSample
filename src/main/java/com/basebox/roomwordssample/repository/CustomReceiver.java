package com.basebox.roomwordssample.repository;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.basebox.roomwordssample.BuildConfig;
import com.basebox.roomwordssample.R;

public class CustomReceiver extends BroadcastReceiver {


    private static final String TAG = "CustomReceiver";

    private static final String ACTION_CUSTOM_BROADCAST = BuildConfig.APPLICATION_ID + ".ACTION_CUSTOM_BROADCAST";
    @Override
    public void onReceive(Context context, Intent intent) {
//        // This method is called when the BroadcastReceiver is receiving
//        // an Intent broadcast.
//        throw new UnsupportedOperationException("Not yet implemented");
        String intentAction = intent.getAction();

        if(intentAction != null){
            String toastMsg = context.getString(R.string.wrong_intent);
            switch (intentAction){
                case Intent.ACTION_POWER_CONNECTED:
                    toastMsg = context.getString(R.string.power_connected);
                    Log.d(TAG, context.getString(R.string.receiver_connected) + toastMsg);
                    break;
                case Intent.ACTION_POWER_DISCONNECTED:
                    toastMsg = context.getString(R.string.power_disconnected);
                    Log.d(TAG, context.getString(R.string.receiver_disconnected) + toastMsg);
                    break;
                case ACTION_CUSTOM_BROADCAST:
                    toastMsg = context.getString(R.string.custom_broadcast_received);
                    break;
            }
            Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show();
            Log.d(TAG, context.getString(R.string.receiver_error) + toastMsg);
        }
    }
}
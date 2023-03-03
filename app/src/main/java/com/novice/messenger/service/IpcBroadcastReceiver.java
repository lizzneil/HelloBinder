package com.novice.messenger.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class IpcBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(null!= intent){

            Client tClient = new Client();
            tClient.clientPkgName = intent.getStringExtra(Constants.PACKAGE_NAME);
            tClient.clientData = intent.getStringExtra(Constants.DATA);
            tClient.clientProcessId=  intent.getStringExtra(Constants.PID);
            tClient.ipcMethod = "Broadcast";

            IpcMsgService.recentClient = tClient;
        }

    }
}

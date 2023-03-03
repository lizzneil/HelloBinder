package com.novice.messenger.service;


import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;

import com.novice.ipc.IIPcMessenger;

public  class IpcMsgService extends Service {


    public IpcMsgService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        connectionCnt++;
        String intentAction  = intent.getAction();
        if(intentAction.equals("aidlExample")){
            return aidlBinder;
        } else  if(intentAction.equals("messengerExample")){
            return mMessenger.getBinder();
        } else {
            return null;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        recentClient = null;
        return super.onUnbind(intent);
    }

    private int connectionCnt  = 0;
    private static final String NOT_SENT = "not sent!";

    public static Client recentClient  = null ;

    private Messenger mMessenger  = new Messenger(new IncomingHandler());


    public class IncomingHandler extends Handler{

        @Override
        public void handleMessage(  Message msg) {

            super.handleMessage(msg);
            Bundle receivedBundle = msg.getData();

            Client tClient = new Client();
            tClient.clientPkgName = receivedBundle.getString(Constants.PACKAGE_NAME);
            tClient.clientData = receivedBundle.getString(Constants.DATA);
            tClient.clientProcessId=  Integer.toString(receivedBundle.getInt(Constants.PID));
            tClient.ipcMethod = "Messenger";

            recentClient = tClient;

            Message message = Message.obtain(IncomingHandler.this,0);
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.CONNECTION_COUNT, connectionCnt);
            bundle.putInt(Constants.PID,android.os.Process.myPid());
            message.setData(bundle);

            try {
                message.replyTo.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

    }

    IIPcMessenger.Stub aidlBinder = new IIPcMessenger.Stub() {
        @Override
        public int getPid() throws RemoteException {
            return android.os.Process.myPid();
        }

        @Override
        public int getConnectionCount() throws RemoteException {
            return  IpcMsgService.this.connectionCnt;
        }

        @Override
        public void setDisplayedValue(String pkgName, int pid, String data) throws RemoteException {
            String clientData = TextUtils.isEmpty(data)?NOT_SENT:data;
            String tPkgName = TextUtils.isEmpty(pkgName)?NOT_SENT:pkgName;
            Client tClient = new Client();
            tClient.clientPkgName = tPkgName;
            tClient.clientData = clientData;
            tClient.clientProcessId=  Integer.toString(pid);
            tClient.ipcMethod = "AIDL";

            recentClient = tClient;
        }
    };
}
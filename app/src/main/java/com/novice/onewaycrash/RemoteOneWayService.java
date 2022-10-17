package com.novice.onewaycrash;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import com.novice.ipc.IRemoteOneWayService;

public class RemoteOneWayService extends Service {
    private static final String TAG = "RemoteService";

    private BinderService mService;
    private volatile int mCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mService = new BinderService();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mService;
    }

    private class BinderService extends IRemoteOneWayService.Stub {

        @Override
        public void remoteCallbackTest() throws RemoteException {
            Log.d(TAG, "remoteCallbackTest before wait " + (mCount++));
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "remoteCallbackTest after wait " + (--mCount));
        }
    }
}
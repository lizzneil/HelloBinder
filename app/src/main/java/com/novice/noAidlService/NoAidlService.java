package com.novice.noAidlService;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NoAidlService extends Service {
    public NoAidlService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        //返回server 端的IBinder 。在写AIDL 时，这个IBinder 同时会实现 AIDL的接口。
        return new NoAidlBinder();
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
    }
}
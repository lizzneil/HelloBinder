package com.novice.ipc.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.novice.ipc.NoAidlBookData;
import com.novice.ipc.util.DebugUtil;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class RemoteService extends Service {

    private List<NoAidlBookData> noAidlBookData = new ArrayList<>();

    public RemoteService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        NoAidlBookData noAidlBookData = new NoAidlBookData();
        noAidlBookData.setName("三体");
        noAidlBookData.setPrice(88);
        this.noAidlBookData.add(noAidlBookData);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Timber.i("in service onBind");
        DebugUtil.dumpIntent(intent);
        return bookManagerStub;
    }

    @Override
    public boolean onUnbind(Intent intent) {
//        boolean answer = super.onUnbind(intent);
//        String log = String.format("in service onUnbind:[ %b ]",false);
        Timber.i("in service onUnbind:");
        DebugUtil.dumpIntent(intent);
        return true;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Timber.i("in service onRebind");
        DebugUtil.dumpIntent(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.i("in service onDestroy");
    }

    private final Stub bookManagerStub = new Stub() {
        @Override
        public List<NoAidlBookData> getBooks() throws RemoteException {
            synchronized (this) {
                if (noAidlBookData != null) {
                    return noAidlBookData;
                }
                return new ArrayList<>();
            }
        }

        @Override
        public void addBook(NoAidlBookData noAidlBookData) throws RemoteException {
            synchronized (this) {
                if (RemoteService.this.noAidlBookData == null) {
                    RemoteService.this.noAidlBookData = new ArrayList<>();
                }

                if (noAidlBookData == null)
                    return;

                noAidlBookData.setPrice(noAidlBookData.getPrice() * 2);
                RemoteService.this.noAidlBookData.add(noAidlBookData);

               Timber.i( "books: " + noAidlBookData.toString());
            }
        }
    };
}

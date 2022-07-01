package com.novice.music.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.novice.ipc.IRemoteMusicService;
import com.novice.ipc.IRemoteMusicServiceCallback;

import timber.log.Timber;

public class AidlMusicService extends Service {

    /**
     * This is a list of callbacks that have been registered with the
     * service.  Note that this is package scoped (instead of private) so
     * that it can be accessed more efficiently from inner classes.
     */
    final RemoteCallbackList<IRemoteMusicServiceCallback> mCallbacks
            = new RemoteCallbackList<>();

    public AidlMusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binderStub;
    }

    @Override
    public void onCreate() {
        Timber.i("Creating AidlMusicService: " + this);
    }

    @Override
    public void onDestroy() {
        Timber.i("Destroying AidlMusicService: " + this);
        // Unregister all callbacks.
        mCallbacks.kill();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Timber.i("Task removed in " + this + ": " + rootIntent);
        stopSelf();
    }

    private void broadcastValue(int value) {
        // Broadcast to all clients the new value.
        final int N = mCallbacks.beginBroadcast();
        for (int i=0; i<N; i++) {
            try {
                mCallbacks.getBroadcastItem(i).playProcess(value);
            } catch (RemoteException e) {
                // The RemoteCallbackList will take care of removing
                // the dead object for us.
            }
        }
        mCallbacks.finishBroadcast();
    }

    IRemoteMusicService.Stub binderStub = new IRemoteMusicService.Stub() {

        PlayThread mPlayThread = null;
        @Override
        public void registerCallback(IRemoteMusicServiceCallback cb) throws RemoteException {
            if (null != cb) {
                mCallbacks.register(cb);
            }

        }

        @Override
        public void unregisterCallback(IRemoteMusicServiceCallback cb) throws RemoteException {
            if (null != cb) {
                mCallbacks.unregister(cb);
            }

        }

        @Override
        public void pause() throws RemoteException {
            Timber.i("pause in service ");
            synchronized (this){
                if(null!=mPlayThread){
                    mPlayThread.cancel();;
                }
            }
        }


        @Override
        public void play() throws RemoteException {
            Timber.i("play in service ");
            synchronized (this){
                if(null!=mPlayThread){
                    mPlayThread.cancel();
                }
                mPlayThread= new PlayThread();
                mPlayThread.start();
            }

        }

        class PlayThread extends Thread{
            private boolean isCancel  = false;

            public void cancel(){
                isCancel = true;
            }
            public void run(){
                for(int i =0 ;i<=100;i++){
                    if(isCancel){
                        return;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    broadcastValue(i);
                }
            }
        }
    };

}


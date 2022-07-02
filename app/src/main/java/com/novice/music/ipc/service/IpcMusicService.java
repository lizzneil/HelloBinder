package com.novice.music.ipc.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.novice.util.ProcessUtil;
import com.novice.util.ThreadPoolManager;

import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;



public class IpcMusicService extends Service{


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Runnable playRunnable ;

    public IpcMusicService() {
    }

    public IBinder onBind(Intent intent) {
        return new IpcMusicBinder();
    }

    public class IpcMusicBinder extends Binder {
        public IpcMusicService getMyService() {
            return IpcMusicService.this;
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case 0:
                    String inMsg = data.readString();

                    Timber.i(ProcessUtil.getProcessName() + " play :: "+ inMsg);
                    play();
                    reply.writeString("Service-reply-play-at-" + sdf.format(new Date()));
                    break;
                case 1:
                    String inPauseMsg = data.readString();
                    Timber.i(ProcessUtil.getProcessName() + " pause :: "+inPauseMsg);
                    pause();
                    reply.writeString("Service-reply-pause-at-" + sdf.format(new Date()));

                    break;
                default:
                    break;
            }

            return true;
        }
    }

    public void play(){
        Timber.i(ProcessUtil.getProcessName() + " play");
//        synchronized (this){
//            if(null==playRunnable){
//                playRunnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//
//                        playRunnable = null;
//                    }
//                };
//                ThreadPoolManager.newInstance().addExecuteTask(playRunnable);
//            }
//        }

    }
    public void pause(){
        Timber.i(ProcessUtil.getProcessName() + " pause");
//        synchronized (this){
//            if(null!=playRunnable){
//                ThreadPoolManager.newInstance().shutdown();
//                playRunnable = null;
//            }
//        }
    }
}
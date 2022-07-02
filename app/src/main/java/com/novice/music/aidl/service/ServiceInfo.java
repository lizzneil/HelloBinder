package com.novice.music.aidl.service;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.novice.ipc.IRemoteMusicService;
import com.novice.ipc.IRemoteMusicServiceCallback;
import com.novice.ipc.R;

public class ServiceInfo {
    final Activity mActivity;
    //对应service
    final Class<?> mClz;
    //绑定状态
    final TextView mStatus;
    //进度
    final TextView musicStatus;
    boolean mServiceBound;
    IRemoteMusicService mServiceProxy;

    IRemoteMusicServiceCallback musicProcessCallback= new IRemoteMusicServiceCallback.Stub() {
        @Override
        public void playProcess(int value) throws RemoteException {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    musicStatus.setText(String.format( "play [%d] service callback",value));
                }
            });

        }
    };


public ServiceInfo(Activity activity, Class<?> clz,
         int bind, int status, int playMusic, int pauseMusic, int idMusicStatus) {
        mActivity = activity;
        mClz = clz;

        CheckBox cb = (CheckBox)mActivity.findViewById(bind);
        cb.setOnClickListener(mBindListener);
        mStatus = (TextView)mActivity.findViewById(status);

        Button button = (Button)mActivity.findViewById(playMusic);
        button.setOnClickListener(mPlayMusicListener);
        button = (Button)mActivity.findViewById(pauseMusic);
        button.setOnClickListener(mPauseMusicListener);

        musicStatus = (TextView)mActivity.findViewById(idMusicStatus);

    }

    public void destroy() {
        if (mServiceBound) {
            mActivity.unbindService(mConnection);
            try {
                mServiceProxy.unregisterCallback(musicProcessCallback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private View.OnClickListener mBindListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (((CheckBox)v).isChecked()) {
                if (!mServiceBound) {
                    if (mActivity.bindService(new Intent(mActivity, mClz),
                            mConnection, Context.BIND_AUTO_CREATE)) {
                        mServiceBound = true;
                        mStatus.setText("BOUND");
                    }
                }
            } else {
                if (mServiceBound) {
                    mActivity.unbindService(mConnection);
                    mServiceBound = false;
                    mStatus.setText("");
                }
            }
        }
    };

    private View.OnClickListener mPlayMusicListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (mServiceBound) {
                try {
                    mServiceProxy.play();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private View.OnClickListener mPauseMusicListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (mServiceBound) {
                try {
                    mServiceProxy.pause();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            mServiceProxy = IRemoteMusicService.Stub.asInterface(service);
            if (mServiceBound) {
                mStatus.setText("CONNECTED");
            }
            try {
                mServiceProxy.registerCallback(musicProcessCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }

        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mServiceProxy = null;
            if (mServiceBound) {
                mStatus.setText("DISCONNECTED");
            }
            try {
                mServiceProxy.unregisterCallback(musicProcessCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
}
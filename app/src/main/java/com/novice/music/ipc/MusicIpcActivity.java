package com.novice.music.ipc;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.novice.ipc.R;
import com.novice.music.ipc.service.IpcMusicService;
import java.text.SimpleDateFormat;
import java.util.Date;
import timber.log.Timber;

public class MusicIpcActivity extends AppCompatActivity {

    private IBinder mBinder = null;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBinder = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            mBinder = service;
        }
    };
    private Parcel data = Parcel.obtain();
    private Parcel reply = Parcel.obtain();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_ipc);

    }


    public void bind(View v) {
        if (isBinded()) {
            return;
        }
        /**
         * 通过ComponentName去定位意图
         * 第一个参数是应用包名
         * 第二个参数是Service或者Activity所在的包名+类名
         */
        Intent serviceIntent = new Intent(MusicIpcActivity.this, IpcMusicService.class);
        bindService( serviceIntent,
                mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    public void unbind(View v) {
        if (!isBinded()) {
            return;
        }
        unbindService(mServiceConnection);
        mBinder = null;
    }

    public void play(View v) {
        if (!isBinded()) {
            return;
        }
        try {
            data = Parcel.obtain();
            data.writeString("Activity request to play music at " + sdf.format(new Date()));
            mBinder.transact(0, data, reply, 0);
            Timber.i(reply.readString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void pause(View v) {
        try {
            data = Parcel.obtain();
            data.writeString("Activity request to pause music at " + sdf.format(new Date()));
            mBinder.transact(1, data, reply, 0);
            Timber.i(reply.readString());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private boolean isBinded() {
        return mBinder != null;
    }

}
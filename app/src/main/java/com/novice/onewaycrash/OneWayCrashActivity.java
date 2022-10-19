package com.novice.onewaycrash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import com.novice.ipc.IAidlRemoteService;
import com.novice.ipc.IRemoteOneWayService;
import com.novice.ipc.R;
import com.novice.ipc.server.RemoteService;

import timber.log.Timber;

public class OneWayCrashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_way_crash);
    }

    com.novice.ipc.IRemoteOneWayService  mIRemoteOneWayService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        // Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Following the example above for an AIDL interface,
            // this gets an instance of the IRemoteInterface, which we can use to call on the service
            mIRemoteOneWayService = IRemoteOneWayService.Stub.asInterface(service);
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
//            Timber.i("Service has unexpectedly disconnected");
            mIRemoteOneWayService = null;
        }
    };


    private void attemptToBindService() {
        if (null == mIRemoteOneWayService) {
            //   if (!isConnection) {
            Intent intent = new Intent(this, RemoteOneWayService.class);
            intent.setAction("action.test.remote.call");
            bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        }
        Timber.i("attemptToBindService");

    }

    private void disAttachService(){
        unbindService(serviceConnection);
        mIRemoteOneWayService = null;
    }

    @Override
    protected void onStart() {
        super.onStart();
        attemptToBindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.i("onStop");
        if (null != mIRemoteOneWayService) {
            //  if (isConnection) {
            /***
             * 解释有 isConnection 成员变量的情况
             * isConnection = false; 必需在这儿也改一下。
             * 只放到 {@link ServiceConnection#onServiceDisconnected(ComponentName)} 会有漏掉的情况
             *  如主动调用{@link #unbindService(ServiceConnection)} 时，不会走{@link ServiceConnection#onServiceDisconnected(ComponentName)} ，
             *  导致运行时空指针
             */
//            isConnection = false;
            disAttachService();
        }
    }

    public void bind(View view) {
        attemptToBindService();
    }

    public void unbind(View view) {
        disAttachService();
    }

    public void callServiceMethod(View view) {
        if (null != mIRemoteOneWayService) {
            try {
                mIRemoteOneWayService.callRemoteTest();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
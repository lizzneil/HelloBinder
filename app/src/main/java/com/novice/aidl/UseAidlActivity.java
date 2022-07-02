package com.novice.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.novice.aidl.server.AidlRemoteService;
import com.novice.ipc.INoviceRemoteService;
import com.novice.ipc.R;

import timber.log.Timber;

public class UseAidlActivity extends AppCompatActivity {

    /***
     *  常见有示例用标志位 确定是否binding上了service。这会带来同步问题，状态不统一问题。
     *  因有意外条件会导致service中止，导致binding失效。因此直接看 service stub最好。在这里就是{@link #mIAidlRemoteServiceProxy}
     *  因 意外条件会走 到意外的处理。会走 {@link ServiceConnection#onServiceDisconnected(ComponentName)}
     *  主动断掉会走 {@link #unbindService(ServiceConnection)}
     *
     *  细看https://developer.android.google.cn//guide/components/aidl.html#Expose  谷歌就是这样写的。加标志位的是那位师傅呢。
     *
     *  在谷歌apiDemo 里发现了有用标志位的情况，用法是多处改，处理所有情况。不熟的人易错。
     */
//    private boolean isConnection = false;

    int price = 100;
    /**
     *
     * Proxy是Stub在本地Activity的代理。
     * Proxy与Stub依靠transact和onTransact通信，Proxy与Stub的封装设计最终很方便地完成了Activity与RemoteService跨进程通信。
     *
     * RemoteService具体实现了Stub
     */
    INoviceRemoteService mIAidlRemoteServiceProxy;

    private ServiceConnection mAidlConnection = new ServiceConnection() {
        // Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Following the example above for an AIDL interface,
            // this gets an instance of the IRemoteInterface, which we can use to call on the service
            mIAidlRemoteServiceProxy = INoviceRemoteService.Stub.asInterface(service);
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
//            Timber.i("Service has unexpectedly disconnected");
            mIAidlRemoteServiceProxy = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_aidl);

        Button btn = findViewById(R.id.aidl_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIAidlRemoteServiceProxy == null) {
                    attemptToBindService();
                    return;
                }

                if (mIAidlRemoteServiceProxy == null)
                    return;

                try {
                    AidlBookData book = new AidlBookData(price, "aidl书籍" + price);
                    mIAidlRemoteServiceProxy.addBook(book);
                    for(AidlBookData aAidlBookData : mIAidlRemoteServiceProxy.getBooks()){
                        Timber.i(aAidlBookData.toString());
                    }
                    price++;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void attemptToBindService() {
        Intent intent = new Intent(this, AidlRemoteService.class);
        intent.setAction("com.novice.aidl.server");
        bindService(intent, mAidlConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mIAidlRemoteServiceProxy == null) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != mIAidlRemoteServiceProxy) {
            unbindService(mAidlConnection);
            mIAidlRemoteServiceProxy = null;
        }
    }
}
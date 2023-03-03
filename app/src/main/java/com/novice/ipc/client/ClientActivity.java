package com.novice.ipc.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.novice.aidl.UseAidlActivity;
import com.novice.ipc.NoAidlBookData;
import com.novice.ipc.R;
import com.novice.ipc.proxy.Proxy;
import com.novice.ipc.server.IBookManagerService;
import com.novice.ipc.server.RemoteService;
import com.novice.ipc.server.Stub;
import com.novice.messenger.IpcMsgServerActivity;
import com.novice.music.aidl.MusicAidlActivity;
import com.novice.music.ipc.MusicIpcActivity;
import com.novice.noAidlService.ConstValue;
import com.novice.noAidlService.NoAidlActivity;
import com.novice.onewaycrash.OneWayCrashActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

import timber.log.Timber;

public class ClientActivity extends AppCompatActivity {

    private IBookManagerService IBookManagerServiceServiceProxy;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

// 调用方与 RemoteService 不同进程时 这里的 service 为android.os.BinderProxy的实例
// 调用方与 RemoteService 同进程时 这里的 service 为com.novice.ipc.server.RemoteService$1 成员变量   类型为的stub.内部类。
            Log.e(ConstValue.TAG, "onServiceConnected : service type:  " + service);
            Timber.i("onServiceConnected ： ComponentName：[" + name.getPackageName() + "\t" + name.getClassName() + "]");
//用AIDL时，实现如下：
//            IBookManagerServiceServiceProxy = Stub.asInterface(service);
//同进程替代写法：  （com.novice.ipc.server.IBookManagerService）service.queryLocalInterface(DESCRIPTOR); //其中DESCRIPTOR = "com.novice.ipc.server.IBookManagerService"

//实际上：不同进程时 可以直接写成下面的这个，省点步聚。在发起对service调用方，Stub里还要queryLocalInterface最终是new.
            IBookManagerServiceServiceProxy = new Proxy(service);
            if (IBookManagerServiceServiceProxy != null) {
                try {
                    List<NoAidlBookData> noAidlBookData = IBookManagerServiceServiceProxy.getBooks();

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            IBookManagerServiceServiceProxy = null;
            Timber.i("onServiceDisconnected  ::  ComponentName：[" + name.getPackageName() + "\t" + name.getClassName() + "]");
        }

    };

    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     *  常见有示例用标志位 确定是否binding上了service。这会带来同步问题，状态不统一问题。
     *  因有意外条件会导致service中止，导致binding失效。因此直接看 service stub最好。在这里就是{@link #IBookManagerServiceServiceProxy}
     *  因 意外条件会走 到意外的处理。会走 {@link ServiceConnection#onServiceDisconnected(ComponentName)}
     *  主动断掉会走 {@link #unbindService(ServiceConnection)}
     *
     *  细看https://developer.android.google.cn//guide/components/aidl.html#Expose 谷歌就是这样写的。加标志位的是那位师傅呢。
     *
     * 在谷歌apiDemo 里发现了有用标志位的情况，用法是多处改，处理所有情况。不熟的人易错。
     */
    // private boolean isConnection = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);




        Button message_service_btn = findViewById(R.id.message_service_btn);
        message_service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientActivity.this, IpcMsgServerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ClientActivity.this.startActivity(intent);
            }
        });
        //use_aidl_btn
        Button aidl_btn = findViewById(R.id.use_aidl_btn);
        aidl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientActivity.this, UseAidlActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ClientActivity.this.startActivity(intent);
            }
        });


        Button aidl_music_btn = findViewById(R.id.aidl_music_btn);
        aidl_music_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientActivity.this, MusicAidlActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ClientActivity.this.startActivity(intent);
            }
        });


        Button ipc_music_btn = findViewById(R.id.ipc_music_btn);
        ipc_music_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientActivity.this, MusicIpcActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ClientActivity.this.startActivity(intent);
            }
        });

        Button no_aidl_btn = findViewById(R.id.no_aidl_btn);
        no_aidl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientActivity.this, NoAidlActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ClientActivity.this.startActivity(intent);
            }
        });


        Button one_way_btn= findViewById(R.id.one_way_btn);
        one_way_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientActivity.this, OneWayCrashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ClientActivity.this.startActivity(intent);
            }
        });

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IBookManagerServiceServiceProxy == null) {
                    //if (!isConnection) {
                    attemptToBindService();
                    return;
                }

                if (IBookManagerServiceServiceProxy == null)
                    return;

                try {
                    NoAidlBookData noAidlBookData = new NoAidlBookData();
                    noAidlBookData.setPrice(101);
                    noAidlBookData.setName("编码");
                    IBookManagerServiceServiceProxy.addBook(noAidlBookData);

                    Timber.i(ClientActivity.this.getProcessName() + " getBooks: " + IBookManagerServiceServiceProxy.getBooks().toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void attemptToBindService() {
        Timber.i("attemptToBindService");
        Intent intent = new Intent(this, RemoteService.class);
        intent.setAction("com.novice.ipc.server");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null == IBookManagerServiceServiceProxy) {
            //   if (!isConnection) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.i("onStop");
        if (null != IBookManagerServiceServiceProxy) {
            //  if (isConnection) {
            /***
             * 解释有 isConnection 成员变量的情况
             * isConnection = false; 必需在这儿也改一下。
             * 只放到 {@link ServiceConnection#onServiceDisconnected(ComponentName)} 会有漏掉的情况
             *  如主动调用{@link #unbindService(ServiceConnection)} 时，不会走{@link ServiceConnection#onServiceDisconnected(ComponentName)} ，
             *  导致运行时空指针
             */
//            isConnection = false;
            unbindService(serviceConnection);
            IBookManagerServiceServiceProxy = null;
        }
    }
}

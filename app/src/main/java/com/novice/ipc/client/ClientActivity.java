package com.novice.ipc.client;

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

import com.novice.aidl.UseAidlActivity;
import com.novice.ipc.Book;
import com.novice.ipc.R;
import com.novice.ipc.server.BookManager;
import com.novice.ipc.server.RemoteService;
import com.novice.ipc.server.Stub;
import com.novice.music.aidl.MusicAidlActivity;
import com.novice.music.ipc.MusicIpcActivity;

import java.util.List;

import timber.log.Timber;

public class ClientActivity extends AppCompatActivity {

    private BookManager bookManagerServiceProxy;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Timber.i("onServiceConnected ： ComponentName：[" + name.getPackageName() + "\t" + name.getClassName() + "]");
            bookManagerServiceProxy = Stub.asInterface(service);
            if (bookManagerServiceProxy != null) {
                try {
                    List<Book> books = bookManagerServiceProxy.getBooks();

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bookManagerServiceProxy = null;
            Timber.i("onServiceDisconnected  ::  ComponentName：[" + name.getPackageName() + "\t" + name.getClassName() + "]");
        }

    };

    /***
     *  常见有示例用标志位 确定是否binding上了service。这会带来同步问题，状态不统一问题。
     *  因有意外条件会导致service中止，导致binding失效。因此直接看 service stub最好。在这里就是{@link #bookManagerServiceProxy}
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
        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookManagerServiceProxy == null) {
                    //if (!isConnection) {
                    attemptToBindService();
                    return;
                }

                if (bookManagerServiceProxy == null)
                    return;

                try {
                    Book book = new Book();
                    book.setPrice(101);
                    book.setName("编码");
                    bookManagerServiceProxy.addBook(book);

                    Timber.i(bookManagerServiceProxy.getBooks().toString());
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
        if (null == bookManagerServiceProxy) {
            //   if (!isConnection) {
            attemptToBindService();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.i("onStop");
        if (null != bookManagerServiceProxy) {
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
            bookManagerServiceProxy = null;
        }
    }
}

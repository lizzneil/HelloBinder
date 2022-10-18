package com.novice.noAidlService;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import com.novice.ipc.R;

public class NoAidlActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_aidl);


        Intent intent = new Intent(this, NoAidlService.class);

        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

                //调用与service 不同进程时，这里的 service 为android.os.BinderProxy的实例
                //调用与service 同进程时  ，这里的 service 为 com.novice.noAidlService.NoAidlBinder实例
                //AIDL实现里 （这里没有用AIDL，全部自己写的）调用方与service同进程时，  这里的 service 为com.novice.noAidlService.NoAidlService 的成员变量   类型为的stub.内部类。
                Log.e(ConstValue.TAG,"onServiceConnected " + service);
                long token = Binder.clearCallingIdentity();

                String processInfo = String.format("\t||myPid[%d]  Binder getCallingPid[%d] callToke[%d]",android.os.Process.myPid(),Binder.getCallingPid(),token);
                //仅供演示，后面无须调用，所以不保存 IBinder 实例  IBinder mBinder = null; AIDL中的IBinder 为stub
                //没有通过 AIDL.Stub.asInterface 转成对应的AIDL 接口。而是 binder  直接 transact实现stub.proxy的功能。
                //这时的 service 对应 NoAidlBinder 的实例，而不是NoAidlService的实例
                //写AIDL的意义是 封装一下，不用写 每个接口的transact
                //IBinder的 Transact 发起调用 这个是aidl proxy 的成员变量 mRemote（IBinder实例）Transact过去
                // stub 的onTransact方法 ，在service 端回应调用 (int code,通过code 调对应的接口返回结果
                //1. 從物件池拿到可複用的物件（享元模式）
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();

                Log.e(ConstValue.TAG, "在client端\tclient方 transact start---- NoAidlActivity 调用 NoAidlService, pid = "
                        + android.os.Process.myPid() + ", thread = "
                        + Thread.currentThread().getName()+processInfo);

                String str = "****client invoke server :msg";
                Log.e(ConstValue.TAG, "在client端\tclient方 调用 server 传了个String ： " + str+processInfo);
                //2.请求的data  ,向请求包写数据，作为参数。
                data.writeString(str);

                //3. 通过 service 的成员变量IBinder的方法transact 同步调用 service
                //第一个参数code ，约定的调用接口：这里是12，所以在 binder  onTransact(int code 方法里，case code =12时响应。
                //最末一个参数flag :0表示非oneway调用，需要阻塞返回结结。
                try {
                    service.transact(12, data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }

                Log.e(ConstValue.TAG, "在client端\tNoAidlActivity , pid = "
                        + android.os.Process.myPid() + ", thread = "
                        + Thread.currentThread().getName()+processInfo);

                //4. 從reply讀取服務端的返回值
                Log.e(ConstValue.TAG, "在client端\tclient方 transact end---- 收到 server 的返回值 ：" + reply.readString()+processInfo);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, Context.BIND_AUTO_CREATE);
    }
}
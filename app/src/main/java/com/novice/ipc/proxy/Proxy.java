package com.novice.ipc.proxy;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import com.novice.ipc.NoAidlBookData;
import com.novice.ipc.server.IBookManagerService;
import com.novice.ipc.server.Stub;
import com.novice.noAidlService.ConstValue;

import java.util.List;

/**
 * 模拟AIDL里的stub.proxy 在调用service 方使用
 *
 * 实际调用binder的transact 把调用参数写入mmap
 */
public class Proxy implements IBookManagerService {

    private static final String DESCRIPTOR = Stub.DESCRIPTOR;
    private IBinder remote;

    public Proxy(IBinder remote) {

//       调用方与service不同进程时 这里的 remote 为android.os.BinderProxy的实例
//       调用方与service同进程时 这里的 remote 为com.novice.ipc.server.RemoteService$1 成员变量   类型为的stub.内部类。

        Log.e(ConstValue.TAG,"new proxy : IBinder type:  " + remote);
        this.remote = remote;
    }

    public String getInterfaceDescriptor() {
        return DESCRIPTOR;
    }

    @Override
    public List<NoAidlBookData> getBooks() throws RemoteException {
        Parcel data = Parcel.obtain();
        Parcel replay = Parcel.obtain();
        List<NoAidlBookData> result;

        try {
            data.writeInterfaceToken(DESCRIPTOR);
            remote.transact(Stub.TRANSAVTION_getBooks, data, replay, 0);
            replay.readException();
            result = replay.createTypedArrayList(NoAidlBookData.CREATOR);
        } finally {
            replay.recycle();
            data.recycle();
        }
        return result;
    }

    @Override
    public void addBook(NoAidlBookData noAidlBookData) throws RemoteException {

        Parcel data = Parcel.obtain();
        Parcel replay = Parcel.obtain();

        try {
            data.writeInterfaceToken(DESCRIPTOR);
            if (noAidlBookData != null) {
                data.writeInt(1);
                noAidlBookData.writeToParcel(data, 0);
            } else {
                data.writeInt(0);
            }
            remote.transact(Stub.TRANSAVTION_addBook, data, replay, 0);
            replay.readException();
        } finally {
            replay.recycle();
            data.recycle();
        }
    }

    @Override
    public IBinder asBinder() {
        return remote;
    }
}

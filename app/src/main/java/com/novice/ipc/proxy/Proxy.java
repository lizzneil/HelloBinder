package com.novice.ipc.proxy;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.novice.ipc.NoAidlBookData;
import com.novice.ipc.server.IBookManagerService;
import com.novice.ipc.server.Stub;

import java.util.List;

/**
 * 模拟AIDL里的stub.proxy 在调用service 方使用
 */
public class Proxy implements IBookManagerService {

    private static final String DESCRIPTOR = Stub.DESCRIPTOR;
    private IBinder remote;

    public Proxy(IBinder remote) {

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

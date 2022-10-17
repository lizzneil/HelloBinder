package com.novice.ipc.server;

import android.os.IInterface;
import android.os.RemoteException;

import com.novice.ipc.NoAidlBookData;

import java.util.List;

/**
 * 这个类用来模拟AIDL里的 service 接口。
 *
 * 调用方，执行方都用这个接口完成service的动作。
 */
public interface IBookManagerService extends IInterface {

    List<NoAidlBookData> getBooks() throws RemoteException;

    void addBook(NoAidlBookData noAidlBookData) throws RemoteException;
}

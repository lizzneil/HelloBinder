package com.novice.ipc.server;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.novice.ipc.NoAidlBookData;
import com.novice.ipc.proxy.Proxy;

import java.util.List;

/**
 * 这个模拟AIDL 自动生成的stub。在被调用的service 里使用。
 *
 * stub 是实际提供service的地方。android.os.service是stub的容器。是stub安身立命的地方。
 *
 * stub在AIDL里为抽像类 原因是 它是在AIDL里自动生成的。其只知道接口 和传输数据的类型，而不能知道具体实现。
 * 具体实现得使用方定义。所以AIDL实现中，把 stub 放到service 里去被继承，被定义，被实例化，定义传输数据以外的业务逻辑。
 */
public abstract class Stub extends Binder implements IBookManagerService {

    public static final String DESCRIPTOR = "com.novice.ipc.server.IBookManagerService";

    public Stub() {

        this.attachInterface(this, DESCRIPTOR);
    }

    /**
     * asInterface的作用是根据调用是否属于同进程而返回不同的实例对象
     * 在service 里返回的是binder本身，也是stub, 调用的是onTransact,把响应数据写进MMAP.
     * 调用方返回的是proxy 调用的是transact 把请求数据写进MMAP。
     *
     * 实际上stub proxy 所有方法是可以写在一个类里的。分开的好处是： 逻辑清晰，码农好懂。调用方不必看onTransact.使用方不必看transact
     * @param binder
     * @return
     */
    public static IBookManagerService asInterface(IBinder binder) {
        if (binder == null)
            return null;
        IInterface iin = binder.queryLocalInterface(DESCRIPTOR);
        if (iin != null && iin instanceof IBookManagerService)
            return (IBookManagerService) iin;
        return new Proxy(binder);
    }

    @Override
    public IBinder asBinder() {
        return this;
    }

    /**
     * 这里会把数据（执行完的的结果 写的binder的 mmap里，供调用方取结果。）
     * 最后一句 super.onTransact 完成写入mmap.
     * @param code
     * @param data
     * @param reply
     * @param flags
     * @return
     * @throws RemoteException
     */
    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        switch (code) {

            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;

            case TRANSAVTION_getBooks:
                data.enforceInterface(DESCRIPTOR);
                List<NoAidlBookData> result = this.getBooks();
                reply.writeNoException();
                reply.writeTypedList(result);
                return true;

            case TRANSAVTION_addBook:
                data.enforceInterface(DESCRIPTOR);
                NoAidlBookData arg0 = null;
                if (data.readInt() != 0) {
                    arg0 = NoAidlBookData.CREATOR.createFromParcel(data);
                }
                this.addBook(arg0);
                reply.writeNoException();
                return true;

        }
        return super.onTransact(code, data, reply, flags);
    }

    public static final int TRANSAVTION_getBooks = IBinder.FIRST_CALL_TRANSACTION;
    public static final int TRANSAVTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;
}

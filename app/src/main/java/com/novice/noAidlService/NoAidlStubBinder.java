package com.novice.noAidlService;

import android.os.Binder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

/***
 * 直接返回调用，省了 常见的stub 及 proxy 包装，直接看下层的逻辑
 *
 */
public class NoAidlStubBinder extends Binder {

    @Override
    protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {

//        assertEquals(android.os.Process.myPid(), Binder.getCallingPid());
        long token = Binder.clearCallingIdentity();

        String processInfo = String.format("\t||myPid[%d]  Binder getCallingPid[%d] callToke[%d]",android.os.Process.myPid(),Binder.getCallingPid(),token);
        if (code == 12) {//这里响应code为 12的请求。  Binder transact 时的code 为12。
            Log.e(ConstValue.TAG, "在server端\t\tNoAidlBinder onTransact start-----, pid = "
                    + android.os.Process.myPid() + ", thread = "
                    + Thread.currentThread().getName()+processInfo);
            //1.data 是client 传入的数据，读取一下。
            Log.e(ConstValue.TAG, "在server端\t\t收到：" + data.readString()+processInfo);

            String str = "****server response client msg:   result ";
            Log.e(ConstValue.TAG, "在server端\t\t返回：" + str+processInfo);
            //2.  reply是响应的数据包，写入对client的 返回值
            reply.writeString(str);
            Log.e(ConstValue.TAG, "在server端\t\tNoAidlBinder onTransact end----- "+processInfo );
            //3. 完成调用。
            return true;
        }
        return super.onTransact(code, data, reply, flags);
    }
}
